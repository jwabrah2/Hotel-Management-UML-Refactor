package I3.DatabaseOperation;

import I3.Classes.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Data Access Object (DAO) for item table operations.
 *
 * <p><b>Refactoring highlights:</b>
 * <ul>
 *   <li>Fixed a critical bug: updateItem was updating the wrong table (food)</li>
 *   <li>Replaced SQL string concatenation with parameterized PreparedStatements</li>
 *   <li>Used try-with-resources for insert/update/delete to close statements safely</li>
 * </ul>
 * </p>
 *
 * @author Faysal Ahmed
 */
public class ItemDb {

    /** Shared DB connection. */
    private final Connection conn;

    /**
     * Creates a new {@code ItemDb} and connects to the database.
     */
    public ItemDb() {
        this.conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a new item row.
     *
     * @param item item object (name, description, price)
     */
    public void insertItem(Item item) {
        if (item == null) {
            JOptionPane.showMessageDialog(null, "Item is null.");
            return;
        }

        final String sql = "INSERT INTO item('name','description','price') VALUES (?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItem_name());
            stmt.setString(2, item.getDescription());
            stmt.setInt(3, item.getPrice());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Item");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert item failed.");
        }
    }

    /**
     * Updates an existing item row by item_id.
     *
     * @param item item object containing updated values and item_id
     */
    public void updateItem(Item item) {
        if (item == null) {
            JOptionPane.showMessageDialog(null, "Item is null.");
            return;
        }

        // ✅ Fixed table name + fixed SQL syntax
        final String sql =
                "UPDATE item SET name = ?, description = ?, price = ? WHERE item_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItem_name());
            stmt.setString(2, item.getDescription());
            stmt.setInt(3, item.getPrice());
            stmt.setInt(4, item.getItem_id());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Item");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate item failed.");
        }
    }

    /**
     * Retrieves all items.
     *
     * <p><b>Important:</b> Caller should close the returned ResultSet/Statement
     * if used directly.</p>
     *
     * @return ResultSet of all item rows, or null on error
     */
    public ResultSet getItems() {
        try {
            String query = "SELECT * FROM item";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving items.");
            return null;
        }
    }

    /**
     * Deletes an item row by item_id.
     *
     * @param itemId item id
     */
    public void deleteItem(int itemId) {
        final String sql = "DELETE FROM item WHERE item_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted item");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete item failed.");
        }
    }

    // NOTE:
    // Old flushAll()/flushStatmentOnly() removed due to try-with-resources.
    // If other code calls them, tell me and I will add deprecated empty methods.
}
