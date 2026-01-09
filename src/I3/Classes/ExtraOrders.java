package I3.Classes;

/**
 * Represents extra (additional) orders made by a customer during a stay,
 * such as buying items/services.
 *
 * @author Faysal Ahmed
 */
public class ExtraOrders {

    private int orderId;
    private int customerId;
    private String dateTime; // Keeping as String to avoid breaking existing DB/UI code
    private int quantity;
    private Item item;

    public ExtraOrders() {
        this.orderId = -1;
        this.customerId = -1;
        this.quantity = 0;
        this.item = null;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        // simple guard to prevent negative quantities
        this.quantity = Math.max(0, quantity);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Calculates total price for this extra order.
     * @return item price * quantity, or 0 if item is null.
     */
    public int calculateTotal() {
        if (item == null) return 0;
        return item.getPrice() * quantity;
    }
}
