package discount.lib;

import java.util.ArrayList;

/**
 * This is a wrapper class of Discount
 */
public class DiscountWrapper {
    private ArrayList<Discount> discounts;

    public DiscountWrapper(ArrayList<Discount> discounts) {
        this.discounts = discounts;
    }

    public ArrayList<Discount> getDiscounts() { return discounts; }
    public void setDiscounts(ArrayList<Discount> discounts) { this.discounts = discounts; }
}
