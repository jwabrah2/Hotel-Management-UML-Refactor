package I3.Classes;

/**
 * Represents a food item that can be ordered.
 *
 * @author Faysal Ahmed
 */
public class Food {

    private int food_id;
    private String name;
    private int price;

    public Food() {
        this.food_id = -1;
        this.name = "";
        this.price = 0;
    }

    public Food(int food_id, String name, int price) {
        this.food_id = food_id;
        this.name = name;
        this.price = price;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name == null) ? "" : name.trim();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = Math.max(0, price);
    }

    @Override
    public String toString() {
        return name + " (" + price + ")";
    }
}
