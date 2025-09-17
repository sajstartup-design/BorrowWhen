package project.borrowhen.common.util;

import org.springframework.stereotype.Component;

@Component("calculationUtil")
public class CalculationUtil {

    /**
     * Calculates the total price by multiplying quantity and unit price.
     *
     * @param qty   the quantity of items
     * @param price the price per item
     * @return the total price (qty * price)
     */
    public static double getTotalPrice(int qty, double price) {
        return qty * price;
    }
}
