package discount.lib;

import com.eaio.uuid.UUID;
import java.util.Random;


/**
 * This class contains some helper methods
 */
public class Utils {
    private Random rand;

    public Utils() {
        rand = new Random();
    }

    public String generateUUID() {
        UUID uuid = new UUID();
        return uuid.toString();
    }

    public String generateString() {
        String characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        char[] text = new char[10];
        for (int i = 0; i < 10; i++)
        {
            text[i] = characters.charAt(rand.nextInt(characters.length()));
        }
        return new String(text);
    }

    public int generateDigits() {
        int randomNum = rand.nextInt((9999 - 111) + 1) + 111;
        return randomNum;
    }

}
