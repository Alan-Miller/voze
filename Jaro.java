import java.lang.Math;
import java.util.ArrayList;

public class Jaro {

    public static Float calc(String textString, String dictString) {
        textWord = textString;
        dictWord = dictString;

        findMatches();
        if (textMatches.size() == 0) {
            return 0.0f;
        }

        calcTranspositions();
        calcScore();

        // Clear values for next use
        textMatches.clear();
        dictMatches.clear();
        numTranspositions = 0;

        return jaroScore;
    }

    // PRIVATE

    private static String textWord;
    private static String dictWord;
    private static ArrayList<Character> textMatches = new ArrayList<Character>();
    private static ArrayList<Character> dictMatches = new ArrayList<Character>();
    private static int numTranspositions = 0;
    private static float jaroScore;

    private static void findMatches() {
        int textLen = textWord.length();
        int dictLen = dictWord.length();

        // Maximum allowed change is half the longest length, rounded down, plus 1
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

        // Remove null values, used in calcTranspositions to compare chars
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

        // Total number of matches, used in calcScore
    }

    private static void calcTranspositions() {
        for (char i = 0; i < textMatches.size(); i++) {
            if (textMatches.get(i) != dictMatches.get(i)) {
                numTranspositions++;
            }
        }
    }

    private static void calcScore() {
        int m = 0;
        int s1 = textWord.length();
        int s2 = dictWord.length();
        int t = numTranspositions;

        for (char ch : textMatches) {
            if (ch != '\u0000') {
                m++;
            }
        }

        // Jaro Score formula
        // 1/3 * (m/s1 + m/s2 + (m-(t/2))/m)
        // where s1 is length1, s2 is length2, m is # matches, and t is # transpositions
        jaroScore = 1.0f / 3.0f *
                (((float) m / (float) s1) + ((float) m / (float) s2) + (((float) m - (float) t / 2.0f) / (float) m));
    }
}
