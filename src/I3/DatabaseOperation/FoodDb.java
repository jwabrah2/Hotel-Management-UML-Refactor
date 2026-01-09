package I3.DatabaseOperation;

import I3.Classes.Food;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Data Access Object (DAO) for food table operations.
 *
 * <p><b>Refactoring highlights:</b>
 * <ul>
 *   <li>Uses parameterized PreparedStatements (safer, avoids SQL injection)</li>
 *   <li>Uses try-with-resources for insert/update/delete to close statements safely</li>
 *   <li>Keeps ResultSet-returning method without auto-close to avoid returning closed ResultSet</li>
 * </ul>
 * </p>
 *
 * @author Faysal Ahmed
 */
public class FoodDb {

    /** Shared DB connection. */
    private final Connection conn;

    /**
     * Creates a new {@code FoodDb} and connects to the database.
     */
    public FoodDb() {
        this.conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a new food row.
     *
     * @param food food object (name, price)
     */
    public void insertFood(Food food) {
        if (food == null) {
            JOptionPane.showMessageDialog(null, "Food is null.");
            return;
        }

        final String sql = "INSERT INTO food('name','price') VALUES (?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, food.getName());
            stmt.setInt(2, food.getPrice());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Food");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert food failed.");
        }
    }

    /**
     * Retrieves all foods.
     *
     * <p><b>Important:</b> Caller should close the returned ResultSet/Statement
     * if used directly.</p>
     *
     * @return ResultSet of all food rows, or null on error
     */
    public ResultSet getFoods() {
        try {
            String query = "SELECT * FROM food";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving foods.");
            return null;
        }
    }

    /**
     * Updates an existing food row by food_id.
     *
     * @param food food object containing updated values and food_id
     */
    public void updateFood(Food food) {
        if (food == null) {
            JOptionPane.showMessageDialog(null, "Food is null.");
            return;
        }

        final String sql = "UPDATE food SET name = ?, price = ? WHERE food_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, food.getName());
            stmt.setInt(2, food.getPrice());
            stmt.setInt(3, food.getFood_id());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Food");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate food failed.");
        }
    }

    /**
     * Deletes a food row by food_id.
     *
     * @param foodId food id
     */
    public void deleteFood(int foodId) {
        final String sql = "DELETE FROM food WHERE food_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, foodId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted food");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete food failed.");
        }
    }

    // NOTE:
    // Old flushAll()/flushStatmentOnly() removed due to try-with-resources.
    // If other code calls them, tell me and I will add deprecated empty methods.
}
