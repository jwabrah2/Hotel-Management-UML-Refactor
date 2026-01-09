package I3.Classes;

/**
 * Represents an item/service that can be ordered as an extra order.
 *
 * @author Faysal Ahmed
 */
public class Item {

    private int item_id;
    private String item_name;
    private String description;
    private int price;

    public Item() {
        this.item_id = -1;
        this.item_name = "";
        this.description = "";
        this.price = 0;
    }

    public Item(int item_id, String item_name, String description, int price) {
        this.item_id = item_id;
        setItem_name(item_name);
        setDescription(description);
        setPrice(price);
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = (item_name == null) ? "" : item_name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = (description == null) ? "" : description.trim();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = Math.max(0, price);
    }

    @Override
    public String toString() {
        return item_name + " (" + price + ")";
    }
}
