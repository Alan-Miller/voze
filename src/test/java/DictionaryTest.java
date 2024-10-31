import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DictionaryTest {
    @Test
    void goodSuggestions() {
        Dictionary dictionary = new Dictionary("./dictionary.txt");

        String songbardSuggestions = dictionary.suggestFor("songbard");
        assert songbardSuggestions.contains("songbird");

        String magpizeSuggestions = dictionary.suggestFor("magpize");
        assert magpizeSuggestions.contains("magpies");
    }

    @Test
    void noSuggestionsGivenForLowMatches() {
        Dictionary dictionary = new Dictionary("./dictionary.txt");

        String whatIsThisTerribleWord = dictionary.suggestFor("xxyttttrrxx");
        assertEquals(whatIsThisTerribleWord, "[ ]");
    }

    @Test
    void badDictionaryPath() {
        Dictionary dictionary = new Dictionary("./not-a-dictionary.txt");
        assert dictionary.entries.isEmpty();
    }

    @Test
    void goodDictionaryPath() {
        Dictionary dictionary = new Dictionary("./dictionary.txt");
        assert !dictionary.entries.isEmpty();
    }
}
