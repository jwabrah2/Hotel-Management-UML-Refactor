package I3.DatabaseOperation;

import I3.Classes.Booking;
import I3.Classes.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Data Access Object (DAO) for booking and related order operations.
 *
 * <p>This class encapsulates all SQL queries related to bookings and order items.
 * Refactoring highlights:
 * <ul>
 *   <li>Uses PreparedStatement parameters (avoids SQL injection and quoting issues)</li>
 *   <li>Uses try-with-resources (auto-closes statements/result sets)</li>
 *   <li>Fixes ResultSet cursor handling in getRoomPrice()</li>
 * </ul>
 * </p>
 */
public class BookingDb {

    /** Shared connection obtained from application DB connector. */
    private final Connection conn;

    /**
     * Creates a new {@code BookingDb} and connects to the database.
     */
    public BookingDb() {
        this.conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a booking record for each room in the provided booking.
     *
     * @param booking booking to insert (customer, rooms, dates, type)
     */
    public void insertBooking(Booking booking) {
        if (booking == null || booking.getCustomer() == null || booking.getRooms() == null) {
            JOptionPane.showMessageDialog(null, "Booking is null or incomplete.");
            return;
        }

        final String sql =
                "INSERT INTO booking " +
                "('customer_id','booking_room','guests','check_in','check_out','booking_type','has_checked_out') " +
                "VALUES (?,?,?,?,?,?,?)";

        for (int i = 0; i < booking.getRooms().size(); i++) {
            if (booking.getRooms().get(i) == null) continue;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, booking.getCustomer().getCustomer_id());
                stmt.setString(2, booking.getRooms().get(i).getRoom_no());
                stmt.setInt(3, booking.getPerson());
                stmt.setLong(4, booking.getCheckInDateTime());
                stmt.setLong(5, booking.getCheckOutDateTime());
                stmt.setString(6, booking.getBookingType());
                stmt.setInt(7, 0); // 0 = has_checked_out false

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Successfully inserted new Booking");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert booking failed.");
            }
        }
    }

    /**
     * Returns all booking rows.
     *
     * <p><b>Important:</b> Caller is responsible for closing the returned ResultSet
     * (and its statement) if used directly.</p>
     *
     * @return ResultSet for "select * from booking", or null on error
     */
    public ResultSet getBookingInformation() {
        try {
            String query = "SELECT * FROM booking";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving all bookings.");
            return null;
        }
    }

    /**
     * Returns a single booking row by booking_id.
     *
     * @param bookingId booking id
     * @return ResultSet for the booking row, or null on error
     */
    public ResultSet getABooking(int bookingId) {
        try {
            String query = "SELECT * FROM booking WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookingId);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving a booking.");
            return null;
        }
    }

    /**
     * Returns bookings (booking_id, booking_room, name) that match a room name fragment
     * and have not checked out.
     *
     * @param roomName room filter (partial match)
     * @return ResultSet of matching bookings, or null on error
     */
    public ResultSet bookingsReadyForOrder(String roomName) {
        try {
            String query =
                    "SELECT booking_id, booking_room, name " +
                    "FROM booking " +
                    "JOIN userInfo ON booking.customer_id = userInfo.user_id " +
                    "WHERE booking_room LIKE ? AND has_checked_out = 0 " +
                    "ORDER BY booking_id DESC";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + (roomName == null ? "" : roomName) + "%");
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in bookingsReadyForOrder, BookingDb.");
            return null;
        }
    }

    /**
     * Updates a booking checkout status and checkout time.
     *
     * @param bookingId booking id
     * @param checkOutTime new checkout time (epoch milliseconds / stored long)
     */
    public void updateCheckOut(int bookingId, long checkOutTime) {
        final String sql = "UPDATE booking SET has_checked_out = 1, check_out = ? WHERE booking_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, checkOutTime);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Check Out");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nupdateCheckOut failed.");
        }
    }

    /**
     * Retrieves room price for a given booking id.
     *
     * @param bookingId booking id
     * @return price if found, otherwise -1
     */
    public int getRoomPrice(int bookingId) {
        final String sql =
                "SELECT price " +
                "FROM booking " +
                "JOIN room ON booking_room = room_no " +
                "JOIN roomType ON type = room_class " +
                "WHERE booking_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // ✅ important fix
                    return rs.getInt("price");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getRoomPrice, BookingDb.");
        }

        return -1;
    }

    /**
     * Inserts a new order item for a booking.
     *
     * @param order order to insert
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

    /**
     * Retrieves all order items/payment-related information for a booking.
     *
     * @param bookingId booking id
     * @return ResultSet of orderItem rows, or null on error
     */
    public ResultSet getAllPaymentInfo(int bookingId) {
        try {
            String query = "SELECT * FROM orderItem WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookingId);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getAllPaymentInfo, BookingDb.");
            return null;
        }
    }

    // NOTE:
    // Old flushAll()/flushStatementOnly() were removed because we now use try-with-resources.
    // If other code still calls them, tell me and I will keep empty versions for compatibility.
}
