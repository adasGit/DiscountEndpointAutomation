package discount.lib;

import com.eaio.uuid.UUID;
import java.util.Random;


/**
 * This class contains some helper methods
 */
public class Utils {

    public String generateUUID() {
        UUID uuid = new UUID();
        return uuid.toString();
    }

    public String generateString()
    {
        Random rng = new Random();
        String characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        char[] text = new char[10];
        for (int i = 0; i < 10; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
