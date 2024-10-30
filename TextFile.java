import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Error;
import java.util.ArrayList;
import java.util.List;

public class TextFile {
    public static void spellCheck(String textFilePath) {
        if (textFilePath == null) {
            throw new Error("Must provide dictionary file path");
        }

        List<String> misspelledWords = new ArrayList<String>();
        List<String> properNouns = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(textFilePath))) {
            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                String[] wordsInTextLine = line.split("[^a-zA-Z0-9]");
                int i;

                for (i = 0; i < wordsInTextLine.length; i++) {
                    String word = wordsInTextLine[i];
                    int columnNumber = line.indexOf(word) + 1;

                    boolean wordIsProperNoun = word != "" && Character.isUpperCase(word.charAt(0));
                    boolean wordIsMisspelled = !Dictionary.get().contains(word);

                    if (wordIsProperNoun) {
                        properNouns.add(word);
                    }

                    if (word != "" && wordIsMisspelled && !wordIsProperNoun) {
                        misspelledWords.add(word);

                        String misspelling = """
                                Misspelled "%s" (line %s, col %s). Suggestions: %s
                                """
                                .formatted(word, lineNumber, columnNumber, Dictionary.suggestSpellings(word));

                        System.out.println(misspelling);
                    }
                }

                lineNumber++;
            }

        } catch (IOException e) {
            System.err.println("Error checking spelling: " + e.getMessage());
        }
    }
}
