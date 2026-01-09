package I3.DatabaseOperation;

import I3.Classes.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Data Access Object (DAO) for orderItem table operations.
 *
 * <p>This class currently provides only insertion of order items.</p>
 *
 * <p><b>Refactoring highlights:</b>
 * <ul>
 *   <li>Uses parameterized PreparedStatement (safer, avoids SQL injection)</li>
 *   <li>Uses try-with-resources to close statements automatically</li>
 * </ul>
 * </p>
 *
 * @author Faysal
 */
public class OrderDb {

    /** Shared DB connection. */
    private final Connection conn;

    /**
     * Creates a new {@code OrderDb} and connects to the database.
     */
    public OrderDb() {
        this.conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a new order item row.
     *
     * @param order order object containing bookingId, food item, price, quantity, and total
     */
    public void insertOrder(Order order) {
        if (order == null) {
            JOptionPane.showMessageDialog(null, "Order is null.");
            return;
        }

        final String sql =
                "INSERT INTO orderItem('booking_id','item_food','price','quantity','total') " +
                "VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getBookingId());
            stmt.setString(2, order.getFoodItem());
            stmt.setInt(3, order.getPrice());
            stmt.setInt(4, order.getQuantity());
            stmt.setInt(5, order.getTotal());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Order");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert order failed.");
        }
    }
}
