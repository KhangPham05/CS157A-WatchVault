package watchvault.service;

import watchvault.dao.*;
import watchvault.model.Models.*;

import java.sql.SQLException;
import java.util.List;

public class WatchVaultService {

    private final TitleDAO        titleDAO        = new TitleDAO();
    private final UserDAO         userDAO         = new UserDAO();
    private final ReviewDAO       reviewDAO       = new ReviewDAO();
    private final WatchlistDAO    watchlistDAO    = new WatchlistDAO();
    private final WatchHistoryDAO watchHistoryDAO = new WatchHistoryDAO();
    private final EpisodeDAO      episodeDAO      = new EpisodeDAO();
    private final GenreDAO        genreDAO        = new GenreDAO();

    public User login(String email, String password) throws SQLException {
        if (email == null || email.isBlank() || password == null || password.isBlank())
            throw new IllegalArgumentException("Email and password cannot be empty.");
        User u = userDAO.authenticate(email, password);
        if (u == null) throw new IllegalArgumentException("Invalid email or password.");
        return u;
    }

    public void register(String fullName, String email, String password) throws SQLException {
        if (fullName == null || fullName.isBlank())    throw new IllegalArgumentException("Name cannot be empty.");
        if (email == null || !email.contains("@"))     throw new IllegalArgumentException("Invalid email address.");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password must be at least 4 characters.");
        if (userDAO.emailExists(email))                throw new IllegalArgumentException("Email already registered.");
        userDAO.registerUser(fullName, email, password);
    }

    public List<Title> browseAllTitles() throws SQLException { return titleDAO.getAllTitles(); }
    public List<Title> searchTitles(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) throw new IllegalArgumentException("Search keyword cannot be empty.");
        return titleDAO.searchByTitle(keyword.trim());
    }
    public List<Title> browseByGenre(int genreId) throws SQLException { return titleDAO.getByGenre(genreId); }
    public List<Title> browseMovies()  throws SQLException { return titleDAO.getByType("Movie"); }
    public List<Title> browseSeries()  throws SQLException { return titleDAO.getByType("Series"); }
    public List<Title> getTopRated(int n) throws SQLException { return titleDAO.getTopRated(n); }

    public Title getTitleDetails(int titleId) throws SQLException {
        Title t = titleDAO.getTitleById(titleId);
        if (t == null) throw new IllegalArgumentException("Title not found with ID: " + titleId);
        return t;
    }
    public String getServicesForTitle(int titleId) throws SQLException { return titleDAO.getServicesForTitle(titleId); }

    public void addTitle(String title, String type, int year, int duration, double rating, int genreId) throws SQLException {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be empty.");
        if (!type.equals("Movie") && !type.equals("Series")) throw new IllegalArgumentException("Type must be Movie or Series.");
        if (rating < 0 || rating > 10) throw new IllegalArgumentException("Rating must be 0-10.");
        titleDAO.addTitle(title, type, year, duration, rating, genreId);
    }
    public void updateTitle(int titleId, String title, int year, int duration, double rating, int genreId) throws SQLException {
        getTitleDetails(titleId);
        titleDAO.updateTitle(titleId, title, year, duration, rating, genreId);
    }
    public void deleteTitle(int titleId) throws SQLException {
        getTitleDetails(titleId);
        titleDAO.deleteTitle(titleId);
    }

    public List<Review>       getReviews(int titleId)  throws SQLException { return reviewDAO.getReviewsForTitle(titleId); }
    public List<Review>       getMyReviews(int userId) throws SQLException { return reviewDAO.getReviewsByUser(userId); }
    public List<Review>       getAllReviews()           throws SQLException { return reviewDAO.getAllReviews(); }
    public void submitReview(int userId, int titleId, int rating, String comment) throws SQLException {
        if (rating < 1 || rating > 10) throw new IllegalArgumentException("Rating must be between 1 and 10.");
        if (comment == null || comment.isBlank()) throw new IllegalArgumentException("Comment cannot be empty.");
        getTitleDetails(titleId);
        reviewDAO.addReview(userId, titleId, rating, comment);
    }
    public void updateReview(int reviewId, int rating, String comment) throws SQLException {
        if (rating < 1 || rating > 10) throw new IllegalArgumentException("Rating must be between 1 and 10.");
        reviewDAO.updateReview(reviewId, rating, comment);
    }
    public void deleteReview(int reviewId) throws SQLException { reviewDAO.deleteReview(reviewId); }

    public List<WatchlistEntry> getWatchlist(int userId) throws SQLException { return watchlistDAO.getWatchlist(userId); }
    public void addToWatchlist(int userId, int titleId) throws SQLException {
        getTitleDetails(titleId);
        watchlistDAO.addToWatchlist(userId, titleId);
    }
    public void removeFromWatchlist(int watchlistId, int userId) throws SQLException { watchlistDAO.removeFromWatchlist(watchlistId, userId); }

    public List<WatchHistoryEntry> getHistory(int userId) throws SQLException { return watchHistoryDAO.getHistory(userId); }
    public void logWatch(int userId, int titleId, int watchDuration) throws SQLException {
        if (watchDuration <= 0) throw new IllegalArgumentException("Watch duration must be positive.");
        getTitleDetails(titleId);
        watchHistoryDAO.logWatch(userId, titleId, watchDuration);
    }
    public void deleteHistoryEntry(int watchHistoryId, int userId) throws SQLException { watchHistoryDAO.deleteHistoryEntry(watchHistoryId, userId); }
    public void printWatchStats() throws SQLException { watchHistoryDAO.printWatchStats(); }

    public List<Episode> getEpisodes(int titleId) throws SQLException { return episodeDAO.getEpisodesForTitle(titleId); }
    public void addEpisode(int titleId, String epTitle, int season, int epNum, int duration) throws SQLException { episodeDAO.addEpisode(titleId, epTitle, season, epNum, duration); }
    public void deleteEpisode(int episodeId) throws SQLException { episodeDAO.deleteEpisode(episodeId); }

    public List<Genre> getAllGenres() throws SQLException { return genreDAO.getAllGenres(); }

    public List<User> getAllUsers()                                              throws SQLException { return userDAO.getAllUsers(); }
    public void deleteUser(int userId)                                          throws SQLException { userDAO.deleteUser(userId); }
    public void updateUserProfile(int userId, String fullName, String email)    throws SQLException { userDAO.updateUser(userId, fullName, email); }
    public void changePassword(int userId, String newPassword)                  throws SQLException {
        if (newPassword == null || newPassword.length() < 4) throw new IllegalArgumentException("Password must be at least 4 characters.");
        userDAO.updatePassword(userId, newPassword);
    }
}
