package watchvault.dao;

import watchvault.db.DBConnection;
import watchvault.model.Models.Title;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TitleDAO – Application Layer (DAO)
 * All SQL for the Titles table executed directly via JDBC PreparedStatement.
 */
public class TitleDAO {

    // ── SELECT ALL ────────────────────────────────────────────
    public List<Title> getAllTitles() throws SQLException {
        String sql = "SELECT t.TitleID, t.Title, t.TitleType, t.ReleaseYear, " +
                     "t.Duration, t.Rating, t.GenreID, g.GenreName " +
                     "FROM Titles t JOIN Genres g ON t.GenreID = g.GenreID " +
                     "ORDER BY t.TitleID";
        List<Title> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── SELECT BY ID ──────────────────────────────────────────
    public Title getTitleById(int titleId) throws SQLException {
        String sql = "SELECT t.TitleID, t.Title, t.TitleType, t.ReleaseYear, " +
                     "t.Duration, t.Rating, t.GenreID, g.GenreName " +
                     "FROM Titles t JOIN Genres g ON t.GenreID = g.GenreID " +
                     "WHERE t.TitleID = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, titleId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // ── SEARCH BY KEYWORD ─────────────────────────────────────
    public List<Title> searchByTitle(String keyword) throws SQLException {
        String sql = "SELECT t.TitleID, t.Title, t.TitleType, t.ReleaseYear, " +
                     "t.Duration, t.Rating, t.GenreID, g.GenreName " +
                     "FROM Titles t JOIN Genres g ON t.GenreID = g.GenreID " +
                     "WHERE t.Title LIKE ? ORDER BY t.Rating DESC";
        List<Title> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ── FILTER BY GENRE ───────────────────────────────────────
    public List<Title> getByGenre(int genreId) throws SQLException {
        String sql = "SELECT t.TitleID, t.Title, t.TitleType, t.ReleaseYear, " +
                     "t.Duration, t.Rating, t.GenreID, g.GenreName " +
                     "FROM Titles t JOIN Genres g ON t.GenreID = g.GenreID " +
                     "WHERE t.GenreID = ? ORDER BY t.Rating DESC";
        List<Title> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, genreId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ── SELECT by TYPE ────────────────────────────────────────
    public List<Title> getByType(String type) throws SQLException {
        String sql = "SELECT t.TitleID, t.Title, t.TitleType, t.ReleaseYear, " +
                     "t.Duration, t.Rating, t.GenreID, g.GenreName " +
                     "FROM Titles t JOIN Genres g ON t.GenreID = g.GenreID " +
                     "WHERE t.TitleType = ? ORDER BY t.Rating DESC";
        List<Title> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ── TOP-RATED ─────────────────────────────────────────────
    public List<Title> getTopRated(int limit) throws SQLException {
        String sql = "SELECT t.TitleID, t.Title, t.TitleType, t.ReleaseYear, " +
                     "t.Duration, t.Rating, t.GenreID, g.GenreName " +
                     "FROM Titles t JOIN Genres g ON t.GenreID = g.GenreID " +
                     "ORDER BY t.Rating DESC LIMIT ?";
        List<Title> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ── INSERT ────────────────────────────────────────────────
    public boolean addTitle(String title, String type, int releaseYear,
                            int duration, double rating, int genreId) throws SQLException {
        String sql = "INSERT INTO Titles (Title, TitleType, ReleaseYear, Duration, Rating, GenreID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, type);
            ps.setInt(3, releaseYear);
            ps.setInt(4, duration);
            ps.setDouble(5, rating);
            ps.setInt(6, genreId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────
    public boolean updateTitle(int titleId, String title, int releaseYear,
                               int duration, double rating, int genreId) throws SQLException {
        String sql = "UPDATE Titles SET Title=?, ReleaseYear=?, Duration=?, Rating=?, GenreID=? " +
                     "WHERE TitleID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, releaseYear);
            ps.setInt(3, duration);
            ps.setDouble(4, rating);
            ps.setInt(5, genreId);
            ps.setInt(6, titleId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────
    public boolean deleteTitle(int titleId) throws SQLException {
        String sql = "DELETE FROM Titles WHERE TitleID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, titleId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── GET STREAMING SERVICES FOR A TITLE ───────────────────
    public String getServicesForTitle(int titleId) throws SQLException {
        String sql = "SELECT s.ServiceName FROM StreamingServices s " +
                     "JOIN TitleAvailability ta ON s.ServiceID = ta.ServiceID " +
                     "WHERE ta.TitleID = ?";
        StringBuilder sb = new StringBuilder();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, titleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(rs.getString("ServiceName"));
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : "Not available on any service";
    }

    // ── MAPPER ────────────────────────────────────────────────
    private Title mapRow(ResultSet rs) throws SQLException {
        return new Title(
                rs.getInt("TitleID"),
                rs.getString("Title"),
                rs.getString("TitleType"),
                rs.getInt("ReleaseYear"),
                rs.getInt("Duration"),
                rs.getDouble("Rating"),
                rs.getInt("GenreID"),
                rs.getString("GenreName")
        );
    }
}
