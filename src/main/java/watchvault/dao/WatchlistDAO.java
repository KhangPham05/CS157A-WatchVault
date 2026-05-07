package watchvault.dao;

import watchvault.db.DBConnection;
import watchvault.model.Models.WatchlistEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WatchlistDAO – Application Layer (DAO)
 */
public class WatchlistDAO {

    public List<WatchlistEntry> getWatchlist(int userId) throws SQLException {
        String sql = "SELECT w.WatchlistID, w.UserID, w.TitleID, t.Title, w.DateAdded " +
                     "FROM Watchlist w JOIN Titles t ON w.TitleID = t.TitleID " +
                     "WHERE w.UserID = ? ORDER BY w.DateAdded DESC";
        List<WatchlistEntry> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new WatchlistEntry(
                            rs.getInt("WatchlistID"), rs.getInt("UserID"),
                            rs.getInt("TitleID"),     rs.getString("Title"),
                            rs.getString("DateAdded")));
                }
            }
        }
        return list;
    }

    public boolean addToWatchlist(int userId, int titleId) throws SQLException {
        String sql = "INSERT INTO Watchlist (UserID, TitleID, DateAdded) VALUES (?, ?, CURDATE())";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, titleId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeFromWatchlist(int watchlistId, int userId) throws SQLException {
        String sql = "DELETE FROM Watchlist WHERE WatchlistID=? AND UserID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, watchlistId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }
}
