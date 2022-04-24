import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class TikaTest {
    @Test
    void checkTika() {
        try {
            Tika tika = new Tika();
            assertEquals("text/html; charset=ISO-8859-1", tika.detect(new URL("https://google.com")));
        }
        catch (Exception e) {
            System.out.println("Execption occured: " + e.toString());
        }
    }
}
