package discount.lib;

import com.eaio.uuid.UUID;


/**
 * This class contains some helper methods
 */
public class Utils {

    public String generateUUID() {
        UUID uuid = new UUID();
        return uuid.toString();
    }

}
