import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        try {
            Connection conn = db.getConnection();
            System.out.println("Connected successfully: " + conn.getCatalog());
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }
}