import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Error;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextFile {
    private String textFilePath;
    private Dictionary dictionary;

    public TextFile(String textFilePath, Dictionary dictionary) {
        if (dictionary.entries.isEmpty()) {
            throw new Error("No dictionary found!");
        }

        this.textFilePath = textFilePath;
        this.dictionary = dictionary;
    }

    public void spellCheck() {
        System.out.println("Checking " + textFilePath + " for misspellings...");

        List<String> misspelledWords = new ArrayList<String>();
        List<String> properNouns = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(textFilePath))) {
            String line;
            String lineCopy;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                // Skip blank lines
                if (line.length() == 0) {
                    lineNumber++;
                    continue;
                }

                // Line copy to help find all indexes when there are multiple same misspellings
                lineCopy = line;
                String[] wordsInTextLine = line.split("[^a-zA-Z0-9']");

                int i;
                for (i = 0; i < wordsInTextLine.length; i++) {
                    String word = wordsInTextLine[i];
                    int columnNumber = lineCopy.indexOf(word) + 1;

                    // Catch common contractions, checking if an acceptable root word can be found
                    String normalizedWord = word.toLowerCase().replaceAll("'s|n't|'t|'ll|'ve|'re|'d|'m|'", "");

                    // Ignore easy proper nouns that often appear at start of sentence
                    // Note: Here our dictionary could be supplemented with proper nouns, prefixes
                    String[] supplementalEntries = { "I", "I'm", "I've", "I'll", "I'd", "non", "pre", "post" };

                    // Ignore numbers, supplemental entries, words with correct dictionary spelling
                    boolean wordIsMisspelled = word != ""
                            && !word.matches("-?\\d+(\\.\\d+)?")
                            && !(Arrays.asList(supplementalEntries)).contains(word)
                            && !dictionary.entries.contains(normalizedWord);

                    boolean wordIsCapitalized = word != "" && Character.isUpperCase(word.charAt(0));

                    boolean wordMayBeSentenceStartingProperNoun = wordIsMisspelled
                            && wordIsCapitalized
                            && (i == 0 || line.charAt(lineCopy.indexOf(word) - 2) == '.');

                    if (wordIsMisspelled && !wordIsCapitalized) {
                        // Determines how "wide a net" to cast for surrounding context
                        int edgeChars = 16;
                        int startIndex = lineCopy.indexOf(word) - edgeChars >= 0 ? lineCopy.indexOf(word) - edgeChars
                                : 0;
                        int endIndex = lineCopy.indexOf(word) + word.length() + edgeChars < line.length() - 1
                                ? lineCopy.indexOf(word) + word.length() + edgeChars
                                : line.length() - 1;

                        // Creates context substring, removes incomplete words, adds ellipses
                        String context = line.substring(startIndex, endIndex)
                                .replaceAll("^.*?\\s", "")
                                .replaceAll("\\s[^\\s]*$", "")
                                .replaceAll("^([a-z])", "...$1")
                                .replaceAll("([^\\.])$", "$1...");

                        misspelledWords.add("""
                                "%s" (line %s, col %s: "%s"). Suggestions: %s"""
                                .formatted(word, lineNumber, columnNumber, context, dictionary.suggestFor(word)));
                    }

                    if (wordMayBeSentenceStartingProperNoun) {
                        properNouns.add("""
                                "%s" (line %s, col %s)""".formatted(word, lineNumber, columnNumber));
                    }

                    // Removing the word ensures the next identical misspelling has correct index
                    lineCopy = lineCopy.replaceFirst(word, "*".repeat(word.length()));
                }

                lineNumber++;
            }

            // Misspellings
            if (misspelledWords.size() > 0) {
                System.out.println("\nMISSPELLINGS\n");

                for (String misspellingMessage : misspelledWords) {
                    System.out.println(misspellingMessage);
                }
            }

            // Possible Misspellings
            if (properNouns.size() > 0) {
                System.out.println(
                        "\nPOSSIBLE MISSPELLINGS (may be proper nouns at start of sentence)\n");

                for (String properNounMessage : properNouns) {
                    System.out.println(properNounMessage);
                }
            }

            // No misspellings!
            if (misspelledWords.size() == 0 && properNouns.size() == 0) {
                System.out.println("No misspelled words!");
            }

        } catch (IOException e) {
            System.err.println("Error checking spelling: " + e.getMessage());
        }
    }
}
