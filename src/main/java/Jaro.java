import java.lang.Math;
import java.util.ArrayList;

public class Jaro {
    private String textWord;
    private String dictWord;
    private ArrayList<Character> textMatches = new ArrayList<Character>();
    private ArrayList<Character> dictMatches = new ArrayList<Character>();
    private int numTranspositions = 0;
    private float jaroScore;

    private void findMatches() {
        int textLen = textWord.length();
        int dictLen = dictWord.length();

        // Maximum allowed change is half the longest length, rounded down, plus 1
        // Any character farther away is not considered a match for the compared word
        int maxDelta = (int) (Math.floor((Math.max(textLen, dictLen) / 2))) + 1;

        // Remaining unmatched chars, used to prevent double-counting matches
        char[] remainingTextChars = textWord.toCharArray();
        char[] remainingDictChars = dictWord.toCharArray();

        // Already-matched chars, used to track total matches
        char[] matchingTextChars = new char[textLen];
        char[] matchingDictChars = new char[dictLen];

        for (int t = 0; t < textLen; t++) {
            char textChar = textWord.charAt(t);
            for (int d = 0; d < dictLen; d++) {
                char dictChar = dictWord.charAt(d);

                // Characters must match and not be beyond max change length
                boolean charsAreNotTooDistant = t < d + maxDelta && d < t + maxDelta;
                boolean charsMatch = textChar == dictChar && remainingTextChars[t] != '_'
                        && remainingDictChars[d] != '_';

                if (charsMatch && charsAreNotTooDistant) {
                    // Update matching chars
                    matchingTextChars[t] = textWord.charAt(t);
                    matchingDictChars[d] = dictWord.charAt(d);
                    // Update remaining unmatched chars
                    remainingTextChars[t] = '_';
                    remainingDictChars[d] = '_';
                }
            }
        }

        // Remove null values; remaining chars compared in calcTranspositions
        for (char ch : matchingTextChars) {
            if (ch != '\u0000') {
                textMatches.add(ch);
            }
        }
        for (char ch : matchingDictChars) {
            if (ch != '\u0000') {
                dictMatches.add(ch);
            }
        }
    }

    private void calcTranspositions() {
        for (char i = 0; i < textMatches.size(); i++) {
            if (textMatches.get(i) != dictMatches.get(i)) {
                numTranspositions++;
            }
        }
    }

    private void calcScore() {
        int m = 0;
        int s1 = textWord.length();
        int s2 = dictWord.length();
        int t = numTranspositions;
        // Where s1 is length1, s2 is length2, m is # matches, and t is # transpositions

        for (char ch : textMatches) {
            if (ch != '\u0000') {
                m++;
            }
        }

        // JARO SCORE FORMULA
        // 1/3 * (m/s1 + m/s2 + (m-(t/2))/m)
        jaroScore = 1.0f / 3.0f
                * (((float) m / (float) s1) + ((float) m / (float) s2) + (((float) m - (float) t / 2.0f) / (float) m));
    }

    // PUBLIC

    public Float calc(String textWord, String dictWord) {
        this.textWord = textWord;
        this.dictWord = dictWord;

        findMatches();
        // If no matches found, exit early with a zero score
        if (textMatches.size() == 0) {
            return 0.0f;
        }

        calcTranspositions();
        calcScore();

        return jaroScore;
    }
}
