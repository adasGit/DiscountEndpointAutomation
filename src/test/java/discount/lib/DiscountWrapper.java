package discount.lib;

import java.util.ArrayList;

/**
 * This class will allow to create DiscountWrapper entities
 */
public class DiscountWrapper {
    private ArrayList<Discount> discounts;

    public DiscountWrapper(ArrayList<Discount> discounts) {
        this.discounts = discounts;
    }

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(ArrayList<Discount> discounts) {
        this.discounts = discounts;
    }
}
