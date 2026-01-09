package I3.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles payment calculations for a booking including rent and extra orders.
 *
 * Note: This refactor keeps legacy getter/setter names to avoid breaking UI/DB code.
 *
 * @author Faysal Ahmed
 */
public class Payment {

    // Required object
    private Booking booking;

    private final List<ExtraOrders> orders;

    private int totalRentPrice;   // e.g., rooms fare * days stayed
    private int daysStayed;

    private String payment_date;
    private String payment_method;

    private boolean hasDiscount;
    private float discount; // percentage (e.g., 10 = 10%) or fraction (0.1) depending on usage

    private int totalBill;

    public Payment(Booking booking) {
        this.booking = booking;
        this.orders = new ArrayList<>();
        this.totalRentPrice = 0;
        this.daysStayed = 0;
        this.totalBill = 0;
        this.hasDiscount = false;
        this.discount = 0f;
    }

    // ---------- Booking ----------
    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    // ---------- Orders helpers ----------
    public List<ExtraOrders> getOrders() {
        return orders;
    }

    public void addOrder(ExtraOrders order) {
        if (order != null) {
            orders.add(order);
        }
    }

    public void clearOrders() {
        orders.clear();
    }

    // ---------- Rent info ----------
    public int getTotalRentPrice() {
        return totalRentPrice;
    }

    public void setTotalRentPrice(int totalRentPrice) {
        this.totalRentPrice = Math.max(0, totalRentPrice);
    }

    public int getDaysStayed() {
        return daysStayed;
    }

    public void setDaysStayed(int daysStayed) {
        this.daysStayed = Math.max(0, daysStayed);
    }

    // ---------- Payment info (legacy names kept) ----------
    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    // ---------- Discount ----------
    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        // keep safe range; allow 0..100 (if percentage) or 0..1 (if fraction)
        this.discount = Math.max(0f, discount);
    }

    public int getTotalBill() {
        return totalBill;
    }

    /**
     * Calculates the total bill:
     * totalBill = totalRentPrice + sum(extraOrders)
     * If discount is enabled, it applies discount to the grand total.
     */
    public int calculateTotalBill() {
        int orderTotal = 0;

        for (ExtraOrders order : orders) {
            if (order == null || order.getItem() == null) continue;

            int qty = Math.max(0, order.getQuantity());
            int price = Math.max(0, order.getItem().getPrice());

            orderTotal += qty * price;
        }

        int subtotal = Math.max(0, totalRentPrice) + orderTotal;

        if (hasDiscount && discount > 0f) {
            // If discount is given as 0..100, convert to fraction.
            float d = (discount > 1f) ? (discount / 100f) : discount;
            subtotal = Math.max(0, Math.round(subtotal * (1f - d)));
        }

        totalBill = subtotal;
        return totalBill;
    }
}
