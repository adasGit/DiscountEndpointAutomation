package discount.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will allow to create Discount entities
 */
public class Discount {
    private String uuid;
    private String name;
    private String description;
    private List<String> imageLookupKeys = new ArrayList<>();
    private Amount amount;
    private String percentage;
    private String externalReference;

    public String getUuid() { return this.uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getImageLookupKeys() { return this.imageLookupKeys; }
    public void setImageLookupKeys(List<String> imageLookupKeys) { this.imageLookupKeys = imageLookupKeys; }
    public Amount getAmount() { return this.amount; }
    public void setAmount(int _amount, String _currencyId) { this.amount = new Amount(_amount, _currencyId); }
}

class Amount {
    private int amount;
    private String currencyId;

    public Amount(int amount, String currencyId) {
        this.amount = amount;
        this.currencyId = currencyId;
    }
}
