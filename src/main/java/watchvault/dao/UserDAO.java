package watchvault.dao;

import watchvault.db.DBConnection;
import watchvault.model.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO – Application Layer (DAO)
 * All SQL for the Users table executed directly via JDBC PreparedStatement.
 */
public class UserDAO {

    // ── AUTHENTICATE (login) ──────────────────────────────────
    public User authenticate(String email, String plainPassword) throws SQLException {
        // Password stored as SHA2-256 hash in DB; we hash the input and compare
        String sql = "SELECT UserID, FullName, Email, Password, DateJoined, UserRole " +
                     "FROM Users WHERE Email = ? AND Password = SHA2(?, 256)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, plainPassword);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // ── SELECT ALL ────────────────────────────────────────────
    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT UserID, FullName, Email, Password, DateJoined, UserRole " +
                     "FROM Users ORDER BY UserID";
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── SELECT BY ID ──────────────────────────────────────────
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT UserID, FullName, Email, Password, DateJoined, UserRole " +
                     "FROM Users WHERE UserID = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // ── CHECK EMAIL EXISTS ────────────────────────────────────
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // ── INSERT (register) ─────────────────────────────────────
    public boolean registerUser(String fullName, String email, String plainPassword) throws SQLException {
        String sql = "INSERT INTO Users (FullName, Email, Password, DateJoined, UserRole) " +
                     "VALUES (?, ?, SHA2(?, 256), CURDATE(), 'user')";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, plainPassword);
            return ps.executeUpdate() > 0;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────
    public boolean updateUser(int userId, String fullName, String email) throws SQLException {
        String sql = "UPDATE Users SET FullName=?, Email=? WHERE UserID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── UPDATE PASSWORD ───────────────────────────────────────
    public boolean updatePassword(int userId, String newPlainPassword) throws SQLException {
        String sql = "UPDATE Users SET Password=SHA2(?,256) WHERE UserID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, newPlainPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE UserID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── MAPPER ────────────────────────────────────────────────
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("UserID"),
                rs.getString("FullName"),
                rs.getString("Email"),
                rs.getString("Password"),
                rs.getString("DateJoined"),
                rs.getString("UserRole")
        );
    }
}
