import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class TextFileTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    Dictionary dictionary = new Dictionary("./dictionary.txt");

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void expectedSuggestions() {
        TextFile textFile = new TextFile("./file-to-check.txt", dictionary);
        textFile.spellCheck();

        String output = outputStream.toString();
        assert output.contains("yoonique");
        assert output.contains("frenship");
        assert output.contains("Squidward");
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }
}
