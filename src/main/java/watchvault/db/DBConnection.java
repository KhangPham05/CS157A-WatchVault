package watchvault.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;  

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                InputStream input = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");
                props.load(input);
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to database successfully!");
            } catch (Exception e) {
                System.out.println("Could not connect: " + e.getMessage());
                System.out.println("Check MySQL is running and db.properties credentials are correct.");
            }
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
