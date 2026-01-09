package I3.DatabaseOperation;

import I3.Classes.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Provides miscellaneous database operations used by the application.
 *
 * <p><b>Refactoring highlights:</b>
 * <ul>
 *   <li>Replaced SQL string concatenation with parameterized PreparedStatements</li>
 *   <li>Fixed ResultSet cursor bugs (calling next() before reading)</li>
 *   <li>Removed incorrect resource closing that returned closed ResultSets</li>
 * </ul>
 * </p>
 *
 * <p><b>Important:</b> Methods returning {@link ResultSet} do not use try-with-resources
 * because closing the statement would close the ResultSet. The caller should close
 * the ResultSet/Statement when finished (or refactor to return Lists instead).</p>
 *
 * @author Faysal Ahmed
 */
public class DatabaseOperation {

    /** Shared connection obtained from DB connector. */
    private final Connection conn;

    /**
     * Creates a new {@code DatabaseOperation} and connects to the database.
     */
    public DatabaseOperation() {
        this.conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a new customer row in userInfo.
     *
     * @param user customer data
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
     * Updates an existing customer row in userInfo by user_id.
     *
     * @param user customer data (must include customer_id)
     */
    public void updateCustomer(UserInfo user) {
        if (user == null) {
            JOptionPane.showMessageDialog(null, "Customer is null.");
            return;
        }

        final String sql =
                "UPDATE userInfo SET name = ?, address = ?, phone = ?, type = ? WHERE user_id = ?";

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
     * Deletes a customer row from userInfo by user_id.
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
     * Returns all customers from userInfo table.
     *
     * @return ResultSet of all customers, or null on error
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

    // --------------------------- SEARCH AND OTHERS ---------------------------

    /**
     * Searches users by name (partial match).
     *
     * @param user name fragment
     * @return ResultSet of (user_id, name, address) matching search, or null on error
     */
    public ResultSet searchUser(String user) {
        try {
            String query = "SELECT user_id, name, address FROM userInfo WHERE name LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + (user == null ? "" : user) + "%");
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in searchUser.");
            return null;
        }
    }

    /**
     * Searches a user by id.
     *
     * @param id user_id
     * @return ResultSet of the user row, or null on error
     */
    public ResultSet searchAnUser(int id) {
        try {
            String query = "SELECT * FROM userInfo WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in searchAnUser.");
            return null;
        }
    }

    /**
     * Retrieves available rooms at a given check-in time.
     *
     * @param check_inTime requested check-in time (stored as long)
     * @return ResultSet with room_no values, or null on error
     */
    public ResultSet getAvailableRooms(long check_inTime) {
        try {
            String query =
                    "SELECT room_no " +
                    "FROM room " +
                    "LEFT OUTER JOIN booking ON room.room_no = booking.booking_room " +
                    "WHERE booking.booking_room IS NULL OR ? < booking.check_in OR booking.check_out < ? " +
                    "GROUP BY room.room_no " +
                    "ORDER BY room_no";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, check_inTime);
            stmt.setLong(2, check_inTime);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getAvailableRooms.");
            return null;
        }
    }

    /**
     * Retrieves booking info between two dates for a specific room.
     *
     * @param start_date start time (long)
     * @param end_date end time (long)
     * @param roomNo room number
     * @return ResultSet of matching bookings, or null on error
     */
    public ResultSet getBookingInfo(long start_date, long end_date, String roomNo) {
        try {
            String query =
                    "SELECT * FROM booking WHERE booking_room = ? AND (" +
                    "(check_in <= ? AND (check_out = 0 OR check_out <= ?)) OR " +
                    "(check_in > ? AND check_out < ?) OR " +
                    "(check_in <= ? AND (check_out = 0 OR check_out > ?))" +
                    ")";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomNo);
            stmt.setLong(2, start_date);
            stmt.setLong(3, end_date);
            stmt.setLong(4, start_date);
            stmt.setLong(5, end_date);
            stmt.setLong(6, end_date);
            stmt.setLong(7, end_date);

            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getBookingInfo.");
            return null;
        }
    }

    /**
     * Retrieves user_id for a given user name + phone combination.
     *
     * @param user user info with name and phone filled
     * @return user_id if found, otherwise -1
     */
    public int getCustomerId(UserInfo user) {
        if (user == null) return -1;

        final String query = "SELECT user_id FROM userInfo WHERE name = ? AND phone = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPhone_no());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // ✅ important fix
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getCustomerId.");
        }

        return -1;
    }
}
