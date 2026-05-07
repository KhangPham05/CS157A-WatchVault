package watchvault.dao;

import watchvault.db.DBConnection;
import watchvault.model.Models.WatchHistoryEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WatchHistoryDAO – Application Layer (DAO)
 */
public class WatchHistoryDAO {

    public List<WatchHistoryEntry> getHistory(int userId) throws SQLException {
        String sql = "SELECT wh.WatchHistoryID, wh.UserID, wh.TitleID, t.Title, " +
                     "wh.WatchDate, wh.WatchDuration " +
                     "FROM WatchHistory wh JOIN Titles t ON wh.TitleID = t.TitleID " +
                     "WHERE wh.UserID = ? ORDER BY wh.WatchDate DESC";
        List<WatchHistoryEntry> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new WatchHistoryEntry(
                            rs.getInt("WatchHistoryID"), rs.getInt("UserID"),
                            rs.getInt("TitleID"),         rs.getString("Title"),
                            rs.getString("WatchDate"),    rs.getInt("WatchDuration")));
                }
            }
        }
        return list;
    }

    public boolean logWatch(int userId, int titleId, int watchDuration) throws SQLException {
        String sql = "INSERT INTO WatchHistory (UserID, TitleID, WatchDate, WatchDuration) " +
                     "VALUES (?, ?, CURDATE(), ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, titleId);
            ps.setInt(3, watchDuration);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteHistoryEntry(int watchHistoryId, int userId) throws SQLException {
        String sql = "DELETE FROM WatchHistory WHERE WatchHistoryID=? AND UserID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, watchHistoryId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public void printWatchStats() throws SQLException {
        String sql = "SELECT t.Title, COUNT(*) AS Views, SUM(wh.WatchDuration) AS TotalMinutes " +
                     "FROM WatchHistory wh JOIN Titles t ON wh.TitleID = t.TitleID " +
                     "GROUP BY wh.TitleID, t.Title ORDER BY Views DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("\n  ── Watch Statistics ─────────────────────────────────");
            System.out.printf("  %-40s %5s %12s%n", "Title", "Views", "TotalMinutes");
            System.out.println("  " + "─".repeat(60));
            while (rs.next()) {
                System.out.printf("  %-40s %5d %12d%n",
                        rs.getString("Title"), rs.getInt("Views"), rs.getInt("TotalMinutes"));
            }
        }
    }
}
