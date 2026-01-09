package I3.DatabaseOperation;

import I3.Classes.Room;
import I3.Classes.RoomFare;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Data Access Object (DAO) for room and roomType tables.
 *
 * <p><b>Refactoring highlights:</b>
 * <ul>
 *   <li>Uses parameterized PreparedStatements (safer, avoids SQL injection)</li>
 *   <li>Uses try-with-resources for insert/update/delete</li>
 *   <li>Fixes broken updateRoom query (added WHERE room_id and removed incomplete SQL)</li>
 * </ul>
 * </p>
 *
 * <p><b>Note:</b> Methods returning {@link ResultSet} do not auto-close
 * statements to avoid returning closed ResultSets.</p>
 *
 * @author Faysal Ahmed
 */
public class RoomDb {

    /** Shared DB connection. */
    private final Connection conn;

    /**
     * Creates a new {@code RoomDb} and connects to the database.
     */
    public RoomDb() {
        this.conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a new room row.
     *
     * @param room room object containing room_no, bed number, facilities and room class type
     */
    public void insertRoom(Room room) {
        if (room == null || room.getRoom_class() == null) {
            JOptionPane.showMessageDialog(null, "Room is null or room_class is missing.");
            return;
        }

        final String sql =
                "INSERT INTO room('room_no','bed_number','tv','wifi','gizer','phone','room_class') " +
                "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoom_no());
            stmt.setInt(2, room.getBed_number());
            stmt.setString(3, Boolean.toString(room.isHasTV()));
            stmt.setString(4, Boolean.toString(room.isHasWIFI()));
            stmt.setString(5, Boolean.toString(room.isHasGizer()));
            stmt.setString(6, Boolean.toString(room.isHasPhone()));
            stmt.setString(7, room.getRoom_class().getRoom_type());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert room failed.");
        }
    }

    /**
     * Retrieves all rooms.
     *
     * @return ResultSet of all room rows, or null on error
     */
    public ResultSet getRooms() {
        try {
            String query = "SELECT * FROM room";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving rooms.");
            return null;
        }
    }

    /**
     * Counts number of rooms.
     *
     * @return number of rooms, or -1 on error
     */
    public int getNoOfRooms() {
        final String query = "SELECT COUNT(room_no) AS noRoom FROM room";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("noRoom");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError counting rooms.");
        }

        return -1;
    }

    /**
     * Retrieves all room numbers.
     *
     * @return ResultSet of room_no values, or null on error
     */
    public ResultSet getAllRoomNames() {
        try {
            String query = "SELECT room_no FROM room";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving room names.");
            return null;
        }
    }

    /**
     * Deletes a room row by room_id.
     *
     * @param roomId room id
     */
    public void deleteRoom(int roomId) {
        final String sql = "DELETE FROM room WHERE room_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete room failed.");
        }
    }

    /**
     * Updates an existing room row by room_id.
     *
     * <p><b>Fix:</b> The original code had an incomplete SQL (meal_id=) and no WHERE clause.
     * This refactor updates only valid columns and applies WHERE room_id.</p>
     *
     * @param room room object with updated values (must include room_id and room_class)
     */
    public void updateRoom(Room room) {
        if (room == null || room.getRoom_class() == null) {
            JOptionPane.showMessageDialog(null, "Room is null or room_class is missing.");
            return;
        }

        final String sql =
                "UPDATE room SET room_no = ?, bed_number = ?, tv = ?, wifi = ?, gizer = ?, phone = ?, room_class = ? " +
                "WHERE room_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoom_no());
            stmt.setInt(2, room.getBed_number());
            stmt.setString(3, Boolean.toString(room.isHasTV()));
            stmt.setString(4, Boolean.toString(room.isHasWIFI()));
            stmt.setString(5, Boolean.toString(room.isHasGizer()));
            stmt.setString(6, Boolean.toString(room.isHasPhone()));
            stmt.setString(7, room.getRoom_class().getRoom_type());
            stmt.setInt(8, room.getRoom_id());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated a room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate room failed.");
        }
    }

    // --------------------------- Room Type (roomType) ---------------------------

    /**
     * Inserts a new room type row in roomType table.
     *
     * @param roomType room fare/type (type, price per day)
     */
    public void insertRoomType(RoomFare roomType) {
        if (roomType == null) {
            JOptionPane.showMessageDialog(null, "Room type is null.");
            return;
        }

        final String sql = "INSERT INTO roomType(type, price) VALUES (?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roomType.getRoom_type());
            stmt.setInt(2, roomType.getPricePerDay());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Room Type");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert room type failed.");
        }
    }

    /**
     * Retrieves all room types.
     *
     * @return ResultSet of all roomType rows, or null on error
     */
    public ResultSet getRoomType() {
        try {
            String query = "SELECT * FROM roomType";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving room types.");
            return null;
        }
    }

    /**
     * Updates room type price by type.
     *
     * @param roomType room fare/type (type, new price per day)
     */
    public void updateRoomType(RoomFare roomType) {
        if (roomType == null) {
            JOptionPane.showMessageDialog(null, "Room type is null.");
            return;
        }

        final String sql = "UPDATE roomType SET price = ? WHERE type = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomType.getPricePerDay());
            stmt.setString(2, roomType.getRoom_type());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Room Type");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate room type failed.");
        }
    }
}
