package watchvault.dao;

import watchvault.db.DBConnection;
import watchvault.model.Models.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GenreDAO – Application Layer (DAO)
 */
public class GenreDAO {

    public List<Genre> getAllGenres() throws SQLException {
        String sql = "SELECT GenreID, GenreName, Description FROM Genres ORDER BY GenreName";
        List<Genre> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Genre(rs.getInt("GenreID"),
                        rs.getString("GenreName"), rs.getString("Description")));
            }
        }
        return list;
    }
}
