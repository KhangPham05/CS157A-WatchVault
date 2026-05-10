package watchvault.dao;

import watchvault.db.DBConnection;
import watchvault.model.Models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ReviewDAO – Application Layer (DAO)
 */
public class ReviewDAO {

    public List<Review> getReviewsForTitle(int titleId) throws SQLException {
        String sql = "SELECT r.ReviewID, r.UserID, r.TitleID, t.Title, u.FullName, " +
                     "r.Rating, r.Comment, r.ReviewDate " +
                     "FROM Reviews r " +
                     "JOIN Users u  ON r.UserID  = u.UserID " +
                     "JOIN Titles t ON r.TitleID = t.TitleID " +
                     "WHERE r.TitleID = ? ORDER BY r.ReviewDate DESC";
        List<Review> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, titleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Review> getReviewsByUser(int userId) throws SQLException {
        String sql = "SELECT r.ReviewID, r.UserID, r.TitleID, t.Title, u.FullName, " +
                     "r.Rating, r.Comment, r.ReviewDate " +
                     "FROM Reviews r " +
                     "JOIN Users u  ON r.UserID  = u.UserID " +
                     "JOIN Titles t ON r.TitleID = t.TitleID " +
                     "WHERE r.UserID = ? ORDER BY r.ReviewDate DESC";
        List<Review> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Review> getAllReviews() throws SQLException {
        String sql = "SELECT r.ReviewID, r.UserID, r.TitleID, t.Title, u.FullName, " +
                     "r.Rating, r.Comment, r.ReviewDate " +
                     "FROM Reviews r " +
                     "JOIN Users u  ON r.UserID  = u.UserID " +
                     "JOIN Titles t ON r.TitleID = t.TitleID " +
                     "ORDER BY r.ReviewDate DESC";
        List<Review> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean addReview(int userId, int titleId, int rating, String comment) throws SQLException {
        String sql = "INSERT INTO Reviews (UserID, TitleID, Rating, Comment, ReviewDate) " +
                     "VALUES (?, ?, ?, ?, CURDATE())";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, titleId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateReview(int reviewId, int rating, String comment) throws SQLException {
        String sql = "UPDATE Reviews SET Rating=?, Comment=?, ReviewDate=CURDATE() WHERE ReviewID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, rating);
            ps.setString(2, comment);
            ps.setInt(3, reviewId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteReview(int reviewId) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE ReviewID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            return ps.executeUpdate() > 0;
        }
    }

    private Review mapRow(ResultSet rs) throws SQLException {
        return new Review(
                rs.getInt("ReviewID"), rs.getInt("UserID"), rs.getInt("TitleID"),
                rs.getString("Title"),  rs.getString("FullName"),
                rs.getInt("Rating"),    rs.getString("Comment"), rs.getString("ReviewDate")
        );
    }
}
