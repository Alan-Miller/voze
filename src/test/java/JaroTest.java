import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JaroTest {
    @Test
    void completeMismatches() {
        Jaro jaro = new Jaro();
        assertEquals(0.0f, jaro.calc("cat", "dog"));
    }

    @Test
    void betterMatches() {
        Float easy_peasy = new Jaro().calc("easy", "peasy");
        Float lemon_squeezy = new Jaro().calc("lemon", "squeezy");
        assert (easy_peasy > lemon_squeezy);
        assert (easy_peasy > 0.90f);
    }

    @Test
    void perfectMatches() {
        Jaro jaro = new Jaro();
        assertEquals(1.0f, jaro.calc("cat", "cat"));
    }
}
