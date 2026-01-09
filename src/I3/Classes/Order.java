package I3.Classes;

/**
 * Represents a food order linked to a booking.
 *
 * Fields such as total are derived from price * quantity to keep data consistent.
 *
 * @author Faysal
 */
public class Order {

    private int orderId;
    private int bookingId;
    private String foodItem;
    private int price;
    private int quantity;

    // Keep total as derived value (not stored) to avoid inconsistency
    public Order() {
        this.orderId = -1;
        this.bookingId = -1;
        this.foodItem = "";
        this.price = 0;
        this.quantity = 0;
    }

    public Order(int bookingId, String foodItem, int price, int quantity) {
        this();
        this.bookingId = bookingId;
        setFoodItem(foodItem);
        setPrice(price);
        setQuantity(quantity);
    }

    /**
     * Backward-compatible constructor (matches the old signature).
     * The last parameter 't' is ignored and total is recalculated.
     */
    public Order(int b, String f, int p, int q, int t) {
        this(b, f, p, q);
        // total is derived; ignore 't' to prevent inconsistent data
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = (foodItem == null) ? "" : foodItem.trim();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = Math.max(0, price);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    /**
     * Derived total = price * quantity.
     */
    public int getTotal() {
        return price * quantity;
    }

    /**
     * Kept for compatibility with existing code (e.g., TableModel/UI),
     * but total is derived so we do not store it.
     */
    public void setTotal(int total) {
        // Intentionally ignored to keep total consistent with price*quantity
        // If you want to respect stored totals, remove this and add a total field.
    }

    @Override
    public String toString() {
        return "Order{bookingId=" + bookingId +
               ", foodItem='" + foodItem + '\'' +
               ", price=" + price +
               ", quantity=" + quantity +
               ", total=" + getTotal() + '}';
    }
}
