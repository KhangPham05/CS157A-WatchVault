import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;  
import java.util.Properties;        

public class DBConnection {

    private final Properties props;
    private Connection connection;

    // Constructor — loads db.properties when the object is created
    public DBConnection() {
        props = new Properties();
        try {
            props.load(new FileInputStream("db.properties"));
        } catch (IOException e) {
            System.err.println("Could not load db.properties: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
            );
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}