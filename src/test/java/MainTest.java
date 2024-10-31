import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void incorrectNumArgsPrintsUsage() {
        String[] args = { "./dictionary.txt" };
        Main.main(args);

        String output = outputStream.toString();
        assert output.contains(
                "Usage: java Main.java <dictionary_file_path> <file_to_check_path>");
    }

    @Test
    void badDictionaryPathThrowsError() {
        assertThrows(java.lang.Error.class, () -> {
            String[] args = { "./ddddictionary.txt", "file-to-check.txt" };
            Main.main(args);
        });
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

}
