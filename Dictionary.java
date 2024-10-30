import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Error;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Dictionary {
    public static void create(String dictionaryFilePath) {
        if (dictionaryFilePath == null) {
            throw new Error("Must provide dictionary file path");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFilePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] wordsInDictLine = line.split("[^a-zA-Z0-9]");
                int i;

                for (i = 0; i < wordsInDictLine.length; i++) {
                    dictHashSet.add(wordsInDictLine[i]);
                }
            }

        } catch (IOException e) {
            System.err.println("Error creating dictionary set: " + e.getMessage());
        }
    }

    public static Set<String> get() {
        return dictHashSet;
    }

    public static String suggestFor(String misspelledString) {
        Set<String> dictionary = get();

        Map<String, Float> suggestions = new LinkedHashMap<String, Float>();
        suggestions.put(" ", -1.00f);

        for (String word : dictionary) {
            suggestions = suggestions.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                            Map::putAll);

            String prevSuggestion = suggestions.keySet().iterator().next();
            Float prevJaroScore = suggestions.values().iterator().next();
            Float jaroScore = Jaro.calc(misspelledString, word);

            if (jaroScore > prevJaroScore && jaroScore > 0.75f) {
                suggestions.put(word, jaroScore);
                if (suggestions.size() >= 4 || prevSuggestion == " ") {
                    suggestions.remove(prevSuggestion);
                }
            }
        }

        Map<String, Float> reversedMap = suggestions.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Float>comparingByValue().reversed())
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(),
                        entry.getValue()),
                        Map::putAll);

        Set<String> rankedSuggestions = reversedMap.keySet();
        return "%s".formatted(rankedSuggestions);
    }

    // PRIVATE

    private static Set<String> dictHashSet = new HashSet<String>();
}