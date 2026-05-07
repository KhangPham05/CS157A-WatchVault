package watchvault.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection – Data Layer
 * Manages the single JDBC connection to the MySQL WatchVault database.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/watchvault?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER     = "root";
    private static final String PASSWORD = "Jk80553053!"; // ← change to your MySQL root password

    private static Connection connection = null;

    /** Returns (and lazily creates) the singleton connection. */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j.jar to /lib", e);
            }
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /** Closes the connection gracefully. */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Warning: could not close DB connection – " + e.getMessage());
            }
        }
    }
}
