package I3.Classes;

/**
 * Stores customer/user information used across the system (booking, orders, etc.).
 *
 * <p>This class is a simple data holder (POJO) that contains customer identity
 * and contact details.</p>
 *
 * @author Faysal Ahmed
 */
public class UserInfo {

    /** Unique id of the customer in the database. */
    private int customer_id;

    /** Full name of the customer. */
    private String name;

    /** Address of the customer. */
    private String address;

    /** Phone number of the customer. */
    private String phone_no;

    /** Customer type (e.g., regular, VIP) depending on system usage. */
    private String type;

    /**
     * Creates an empty {@code UserInfo} with safe default values.
     */
    public UserInfo() {
        this.customer_id = -1;
        this.name = "";
        this.address = "";
        this.phone_no = "";
        this.type = "";
    }

    /**
     * Creates a {@code UserInfo} with provided values.
     *
     * @param customer_id customer unique id
     * @param name customer name
     * @param address customer address
     * @param phone_no customer phone number
     * @param type customer type
     */
    public UserInfo(int customer_id, String name, String address, String phone_no, String type) {
        this();
        this.customer_id = customer_id;
        setName(name);
        setAddress(address);
        setPhone_no(phone_no);
        setType(type);
    }

    /**
     * @return customer id
     */
    public int getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id customer id to set
     */
    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * @return customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets customer name. If null, it becomes an empty string.
     *
     * @param name customer name
     */
    public void setName(String name) {
        this.name = (name == null) ? "" : name.trim();
    }

    /**
     * @return customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets customer address. If null, it becomes an empty string.
     *
     * @param address customer address
     */
    public void setAddress(String address) {
        this.address = (address == null) ? "" : address.trim();
    }

    /**
     * @return customer phone number
     */
    public String getPhone_no() {
        return phone_no;
    }

    /**
     * Sets customer phone number. If null, it becomes an empty string.
     *
     * @param phone_no customer phone number
     */
    public void setPhone_no(String phone_no) {
        this.phone_no = (phone_no == null) ? "" : phone_no.trim();
    }

    /**
     * @return customer type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets customer type. If null, it becomes an empty string.
     *
     * @param type customer type
     */
    public void setType(String type) {
        this.type = (type == null) ? "" : type.trim();
    }

    /**
     * Returns a readable representation for UI display/debugging.
     *
     * @return string containing name and id
     */
    @Override
    public String toString() {
        return name + " (" + customer_id + ")";
    }
}
