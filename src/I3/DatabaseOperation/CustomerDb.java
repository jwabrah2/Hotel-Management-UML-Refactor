package I3.DatabaseOperation;

import I3.Classes.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Data Access Object (DAO) for customer (userInfo) table operations.
 *
 * <p>Refactoring highlights:
 * <ul>
 *   <li>Uses parameterized PreparedStatements (avoids SQL injection and quoting bugs)</li>
 *   <li>Uses try-with-resources for safe closing of statements/result sets</li>
 *   <li>Improves readability and adds null-safety where reasonable</li>
 * </ul>
 * </p>
 *
 * <p><b>Note:</b> This DAO still shows messages using {@link JOptionPane}
 * to match the existing UI design.</p>
 *
 * @author Faysal Ahmed
 */
public class CustomerDb {

    /** Shared connection obtained from DB connector. */
    private final Connection conn;

    /**
     * Creates a new {@code CustomerDb} and connects to the database.
     */
    public CustomerDb() {
        this.conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a new customer into userInfo table.
     *
     * @param user customer data to insert
     */
    public void insertCustomer(UserInfo user) {
        if (user == null) {
            JOptionPane.showMessageDialog(null, "Customer is null.");
            return;
        }

        final String sql = "INSERT INTO userInfo('name','address','phone','type') VALUES (?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getAddress());
            stmt.setString(3, user.getPhone_no());
            stmt.setString(4, user.getType());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted new Customer");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert customer failed.");
        }
    }

    /**
     * Updates an existing customer in userInfo table by user_id.
     *
     * @param user customer data (must include customer_id)
     */
    public void updateCustomer(UserInfo user) {
        if (user == null) {
            JOptionPane.showMessageDialog(null, "Customer is null.");
            return;
        }

        final String sql =
                "UPDATE userInfo " +
                "SET name = ?, address = ?, phone = ?, type = ? " +
                "WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getAddress());
            stmt.setString(3, user.getPhone_no());
            stmt.setString(4, user.getType());
            stmt.setInt(5, user.getCustomer_id());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Customer");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate customer failed.");
        }
    }

    /**
     * Deletes a customer from userInfo table.
     *
     * @param userId customer id (user_id)
     */
    public void deleteCustomer(int userId) {
        final String sql = "DELETE FROM userInfo WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted user");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete customer failed.");
        }
    }

    /**
     * Retrieves all customers.
     *
     * <p><b>Important:</b> Caller must close the returned ResultSet/Statement
     * if used directly.</p>
     *
     * @return ResultSet of all rows from userInfo table, or null on error
     */
    public ResultSet getAllCustomer() {
        try {
            String query = "SELECT * FROM userInfo";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving all customers.");
            return null;
        }
    }

    // NOTE:
    // Old flushAll()/flushStatementOnly() removed due to try-with-resources usage.
    // If other code still calls them, tell me and I will add deprecated empty versions.
}
