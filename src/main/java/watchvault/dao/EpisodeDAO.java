package watchvault.dao;

import watchvault.db.DBConnection;
import watchvault.model.Models.Episode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EpisodeDAO – Application Layer (DAO)
 */
public class EpisodeDAO {

    public List<Episode> getEpisodesForTitle(int titleId) throws SQLException {
        String sql = "SELECT EpisodeID, TitleID, EpisodeTitle, SeasonNumber, EpisodeNumber, Duration " +
                     "FROM Episodes WHERE TitleID=? ORDER BY SeasonNumber, EpisodeNumber";
        List<Episode> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, titleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Episode(rs.getInt("EpisodeID"), rs.getInt("TitleID"),
                            rs.getString("EpisodeTitle"), rs.getInt("SeasonNumber"),
                            rs.getInt("EpisodeNumber"),   rs.getInt("Duration")));
                }
            }
        }
        return list;
    }

    public boolean addEpisode(int titleId, String episodeTitle, int season, int episodeNum, int duration)
            throws SQLException {
        String sql = "INSERT INTO Episodes (TitleID, EpisodeTitle, SeasonNumber, EpisodeNumber, Duration) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, titleId);
            ps.setString(2, episodeTitle);
            ps.setInt(3, season);
            ps.setInt(4, episodeNum);
            ps.setInt(5, duration);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteEpisode(int episodeId) throws SQLException {
        String sql = "DELETE FROM Episodes WHERE EpisodeID=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, episodeId);
            return ps.executeUpdate() > 0;
        }
    }
}
