package I3.DatabaseOperation;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Provides a shared method to create a SQLite database connection.
 *
 * <p>The application uses a local SQLite file (hotel.sqlite). This class attempts
 * to connect to the database file using the JDBC driver.</p>
 *
 * <p><b>Refactor notes:</b>
 * <ul>
 *   <li>Removed unused imports</li>
 *   <li>Centralized JDBC URL creation</li>
 *   <li>Added a compatibility wrapper method (connectTODB)</li>
 * </ul>
 * </p>
 *
 * @author Faysal Ahmed
 */
public class DataBaseConnection {

    /** Default database file name. */
    private static final String DB_FILE_NAME = "hotel.sqlite";

    /**
     * Compatibility method (keeps old method name used across the project).
     *
     * @return Connection to the SQLite DB, or null if connection fails
     */
    public static Connection connectTODB() {
        return connectToDB();
    }

    /**
     * Creates a connection to the SQLite database.
     *
     * @return Connection to the SQLite DB, or null if connection fails
     */
    public static Connection connectToDB() {
        try {
            Class.forName("org.sqlite.JDBC");

            // Prefer an absolute path if the file exists in the current working directory.
            // This makes behavior more predictable when running from NetBeans.
            String url = buildJdbcUrl(DB_FILE_NAME);

            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "SQLite JDBC driver not found.\n" + e);
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to database.\n" + e);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Unexpected error while connecting to database.\n" + e);
            return null;
        }
    }

    /**
     * Builds the JDBC URL for a SQLite database file.
     *
     * @param fileName database file name (e.g., hotel.sqlite)
     * @return JDBC url string (jdbc:sqlite:...)
     */
    private static String buildJdbcUrl(String fileName) {
        File dbFile = new File(fileName);
        if (dbFile.exists()) {
            // Use absolute path when possible
            return "jdbc:sqlite:" + dbFile.getAbsolutePath();
        }
        // Fallback: relative path
        return "jdbc:sqlite:" + fileName;
    }
}
