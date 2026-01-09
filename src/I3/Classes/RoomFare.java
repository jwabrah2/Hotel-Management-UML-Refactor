package I3.Classes;

/**
 * Represents the room fare/class information (room type and price per day).
 *
 * <p>This class is used by {@link Room} and affects booking cost calculations.</p>
 *
 * @author Faysal Ahmed
 */
public class RoomFare {

    /** Room type (e.g., Standard, Deluxe). */
    private String room_type;

    /** Price per day for this room type. */
    private int pricePerDay;

    /**
     * Creates an empty {@code RoomFare} with safe default values.
     */
    public RoomFare() {
        this.room_type = "";
        this.pricePerDay = 0;
    }

    /**
     * Creates a {@code RoomFare} with provided values.
     *
     * @param room_type room type name
     * @param pricePerDay price per day (non-negative)
     */
    public RoomFare(String room_type, int pricePerDay) {
        this();
        setRoom_type(room_type);
        setPricePerDay(pricePerDay);
    }

    /**
     * @return room type
     */
    public String getRoom_type() {
        return room_type;
    }

    /**
     * Sets room type. If null, it becomes an empty string.
     *
     * @param room_type room type
     */
    public void setRoom_type(String room_type) {
        this.room_type = (room_type == null) ? "" : room_type.trim();
    }

    /**
     * @return price per day
     */
    public int getPricePerDay() {
        return pricePerDay;
    }

    /**
     * Sets price per day. Negative values are converted to 0.
     *
     * @param pricePerDay price per day
     */
    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = Math.max(0, pricePerDay);
    }

    /**
     * Returns a readable representation for UI display/debugging.
     *
     * @return string containing room type and price
     */
    @Override
    public String toString() {
        return room_type + " (" + pricePerDay + "/day)";
    }
}
