import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFile {
    private String textFilePath;
    private Dictionary dictionary;

    public TextFile(String textFilePath, Dictionary dictionary) {
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
                // Use copy of line to aid in finding indexes of multiple same misspellings
                lineCopy = line;

                String[] wordsInTextLine = line.split("[^a-zA-Z0-9]");
                int i;

                for (i = 0; i < wordsInTextLine.length; i++) {
                    String word = wordsInTextLine[i];
                    int columnNumber = lineCopy.indexOf(word) + 1;

                    boolean wordIsMisspelled = word != "" && !dictionary.entries.contains(word.toLowerCase());
                    boolean wordIsCapitalized = word != "" && Character.isUpperCase(word.charAt(0));
                    boolean wordIsProbablyProperNoun = wordIsMisspelled && wordIsCapitalized
                            && i > 0
                            && wordsInTextLine[i - 1] != ""
                            && Character.isLowerCase((wordsInTextLine[i - 1]).charAt(0));

                    if (wordIsMisspelled && !wordIsCapitalized) {
                        // Determines how "wide" a net to try to cast for surrounding context.
                        int edgeChars = 16;
                        int startIndex = lineCopy.indexOf(word) - edgeChars >= 0 ? lineCopy.indexOf(word) - edgeChars
                                : 0;
                        int endIndex = lineCopy.indexOf(word) + word.length() + edgeChars < line.length() - 1
                                ? lineCopy.indexOf(word) + word.length() + edgeChars
                                : line.length() - 1;

                        // Creates substring, removes incomplete words, adds ellipses
                        String context = line.substring(startIndex, endIndex)
                                .replaceAll("^.*?\\s", "")
                                .replaceAll("\\s[^\\s]*$", "")
                                .replaceAll("^([a-z])", "...$1")
                                .replaceAll("([^\\.])$", "$1...");

                        misspelledWords.add("""
                                "%s" (line %s, col %s: "%s"). Suggestions: %s"""
                                .formatted(word, lineNumber, columnNumber, context, dictionary.suggestFor(word)));
                    }

                    if (wordIsProbablyProperNoun) {
                        properNouns.add("""
                                "%s" (line %s, col %s)""".formatted(word, lineNumber, columnNumber));

                    }

                    lineCopy = lineCopy.replaceFirst(word, "*".repeat(word.length()));
                }

                lineNumber++;
            }

            System.out.println("\nMISSPELLINGS\n");
            for (String misspellingMessage : misspelledWords) {
                System.out.println(misspellingMessage);
            }

            System.out.println(
                    "\nPOSSIBLE MISSPELLINGS (may just be proper nouns at the start of a sentence)\n");
            for (String properNounMessage : properNouns) {
                System.out.println(properNounMessage);
            }

        } catch (IOException e) {
            System.err.println("Error checking spelling: " + e.getMessage());
        }
    }
}
