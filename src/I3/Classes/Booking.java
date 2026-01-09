package I3.Classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a hotel booking that contains a customer and a list of rooms,
 * along with check-in/check-out times and booking metadata.
 *
 * @author Faysal Ahmed
 */
public class Booking {

    public static final String TYPE_RESERVED = "Reserved";
    public static final String TYPE_CONFIRMED = "Confirmed";

    private UserInfo customer;
    private final List<Room> rooms;

    private int bookingId;
    private long checkInDateTime;
    private long checkOutDateTime;
    private String bookingType;
    private int person;

    public Booking() {
        this.customer = new UserInfo();
        this.rooms = new ArrayList<>();
        this.bookingId = -1;
        this.bookingType = TYPE_RESERVED;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public UserInfo getCustomer() {
        return customer;
    }

    public void setCustomer(UserInfo customer) {
        this.customer = customer;
    }

    public long getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(long checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public long getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(long checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }

    /**
     * Returns a read-only view of rooms list.
     * (Prevents external code from replacing the list reference.)
     */
    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(String roomNo) {
        rooms.add(new Room(roomNo));
    }

    /**
     * Removes a room by room number safely (no ConcurrentModificationException).
     */
    public void removeRoom(String roomNo) {
        if (roomNo == null) return;

        Iterator<Room> iterator = rooms.iterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if (roomNo.equals(room.getRoom_no())) {
                iterator.remove();
                // If room numbers are unique, we can break:
                break;
            }
        }
    }

    /**
     * Calculates total fare for all rooms (sum of pricePerDay).
     */
    public int getRoomsFare() {
        int total = 0;
        for (Room room : rooms) {
            if (room != null && room.getRoom_class() != null) {
                total += room.getRoom_class().getPricePerDay();
            }
        }
        return total;
    }
}
