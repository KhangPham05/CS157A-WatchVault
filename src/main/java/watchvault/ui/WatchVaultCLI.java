package watchvault.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import watchvault.db.DBConnection;
import watchvault.model.Models.Episode;
import watchvault.model.Models.Genre;
import watchvault.model.Models.Review;
import watchvault.model.Models.Title;
import watchvault.model.Models.User;
import watchvault.model.Models.WatchHistoryEntry;
import watchvault.model.Models.WatchlistEntry;
import watchvault.service.WatchVaultService;

/**
 * WatchVaultCLI – Presentation Layer
 * Full command-line interface for the WatchVault application.
 * Three-tier: CLI → Service → DAO → MySQL (JDBC)
 */
public class WatchVaultCLI {

    private static final WatchVaultService service = new WatchVaultService();
    private static final Scanner           scanner = new Scanner(System.in);
    private static       User              currentUser = null;

    // ══════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        printBanner();
            // test connection
            DBConnection.getConnection();

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = guestMenu();
            } else if (currentUser.userRole.equals("admin")) {
                running = adminMenu();
            } else {
                running = userMenu();
            }
        }

        DBConnection.closeConnection();
        System.out.println("\n  Goodbye! Thanks for using WatchVault.");
    }

    // ══════════════════════════════════════════════════════════
    //  GUEST MENU
    // ══════════════════════════════════════════════════════════
    private static boolean guestMenu() {
        printSeparator("MAIN MENU");
        System.out.println("  1. Login");
        System.out.println("  2. Register");
        System.out.println("  3. Browse titles (guest)");
        System.out.println("  4. Search titles (guest)");
        System.out.println("  0. Exit");
        int choice = promptInt("Choice");
        switch (choice) {
            case 1 -> doLogin();
            case 2 -> doRegister();
            case 3 -> doBrowseTitles();
            case 4 -> doSearchTitles();
            case 0 -> { return false; }
            default -> System.out.println("  Invalid option.");
        }
        return true;
    }

    // ══════════════════════════════════════════════════════════
    //  USER MENU
    // ══════════════════════════════════════════════════════════
    private static boolean userMenu() {
        printSeparator("USER MENU  –  " + currentUser.fullName);
        System.out.println("  BROWSE");
        System.out.println("   1. Browse all titles");
        System.out.println("   2. Search titles");
        System.out.println("   3. Browse by genre");
        System.out.println("   4. Browse movies only");
        System.out.println("   5. Browse series only");
        System.out.println("   6. Top-rated titles");
        System.out.println("   7. View title details & reviews");
        System.out.println("  WATCHLIST");
        System.out.println("   8. My watchlist");
        System.out.println("   9. Add title to watchlist");
        System.out.println("  10. Remove from watchlist");
        System.out.println("  REVIEWS");
        System.out.println("  11. Submit a review");
        System.out.println("  12. My reviews");
        System.out.println("  13. Update my review");
        System.out.println("  14. Delete my review");
        System.out.println("  HISTORY");
        System.out.println("  15. My watch history");
        System.out.println("  16. Log a watch session");
        System.out.println("  17. Delete history entry");
        System.out.println("  ACCOUNT");
        System.out.println("  18. Update profile");
        System.out.println("  19. Change password");
        System.out.println("   0. Logout");
        int choice = promptInt("Choice");
        try {
            switch (choice) {
                case 1  -> doBrowseTitles();
                case 2  -> doSearchTitles();
                case 3  -> doBrowseByGenre();
                case 4  -> doBrowseMovies();
                case 5  -> doBrowseSeries();
                case 6  -> doTopRated();
                case 7  -> doTitleDetails();
                case 8  -> doMyWatchlist();
                case 9  -> doAddToWatchlist();
                case 10 -> doRemoveFromWatchlist();
                case 11 -> doSubmitReview();
                case 12 -> doMyReviews();
                case 13 -> doUpdateReview();
                case 14 -> doDeleteReview();
                case 15 -> doMyHistory();
                case 16 -> doLogWatch();
                case 17 -> doDeleteHistory();
                case 18 -> doUpdateProfile();
                case 19 -> doChangePassword();
                case 0  -> { currentUser = null; System.out.println("  Logged out."); }
                default -> System.out.println("  Invalid option.");
            }
        } catch (SQLException e) {
            System.out.println("  DB Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        return true;
    }

    // ══════════════════════════════════════════════════════════
    //  ADMIN MENU
    // ══════════════════════════════════════════════════════════
    private static boolean adminMenu() {
        printSeparator("ADMIN MENU  –  " + currentUser.fullName);
        System.out.println("  CONTENT MANAGEMENT");
        System.out.println("   1. Browse all titles");
        System.out.println("   2. Add new title");
        System.out.println("   3. Update title");
        System.out.println("   4. Delete title");
        System.out.println("   5. Add episode to series");
        System.out.println("   6. Delete episode");
        System.out.println("   7. View title details");
        System.out.println("  USER MANAGEMENT");
        System.out.println("   8. List all users");
        System.out.println("   9. Delete user");
        System.out.println("  REVIEWS");
        System.out.println("  10. View all reviews");
        System.out.println("  11. Delete a review");
        System.out.println("  ANALYTICS");
        System.out.println("  12. Watch statistics (views per title)");
        System.out.println("   0. Logout");
        int choice = promptInt("Choice");
        try {
            switch (choice) {
                case 1  -> doBrowseTitles();
                case 2  -> doAdminAddTitle();
                case 3  -> doAdminUpdateTitle();
                case 4  -> doAdminDeleteTitle();
                case 5  -> doAdminAddEpisode();
                case 6  -> doAdminDeleteEpisode();
                case 7  -> doTitleDetails();
                case 8  -> doListUsers();
                case 9  -> doAdminDeleteUser();
                case 10 -> doAdminViewAllReviews();
                case 11 -> doAdminDeleteReview();
                case 12 -> service.printWatchStats();
                case 0  -> { currentUser = null; System.out.println("  Logged out."); }
                default -> System.out.println("  Invalid option.");
            }
        } catch (SQLException e) {
            System.out.println("  DB Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        return true;
    }

    // ══════════════════════════════════════════════════════════
    //  AUTH ACTIONS
    // ══════════════════════════════════════════════════════════
    private static void doLogin() {
        System.out.print("  Email: ");    String email = scanner.nextLine().trim();
        System.out.print("  Password: "); String pw    = scanner.nextLine().trim();
        try {
            currentUser = service.login(email, pw);
            System.out.println("  Welcome back, " + currentUser.fullName + "! (Role: " + currentUser.userRole + ")");
        } catch (SQLException e)           { System.out.println("  DB Error: " + e.getMessage()); }
          catch (IllegalArgumentException e){ System.out.println("  " + e.getMessage()); }
    }

    private static void doRegister() {
        System.out.print("  Full Name: "); String name = scanner.nextLine().trim();
        System.out.print("  Email: ");     String email = scanner.nextLine().trim();
        System.out.print("  Password: "); String pw   = scanner.nextLine().trim();
        try {
            service.register(name, email, pw);
            System.out.println("  Registration successful! You can now log in.");
        } catch (SQLException e)           { System.out.println("  DB Error: " + e.getMessage()); }
          catch (IllegalArgumentException e){ System.out.println("  " + e.getMessage()); }
    }

    // ══════════════════════════════════════════════════════════
    //  BROWSE / SEARCH
    // ══════════════════════════════════════════════════════════
    private static void doBrowseTitles() {
        try {
            List<Title> titles = service.browseAllTitles();
            printSeparator("ALL TITLES (" + titles.size() + ")");
            titles.forEach(t -> System.out.println("  " + t));
        } catch (SQLException e) { System.out.println("  DB Error: " + e.getMessage()); }
    }

    private static void doSearchTitles() {
        System.out.print("  Keyword: ");
        String kw = scanner.nextLine().trim();
        try {
            List<Title> results = service.searchTitles(kw);
            printSeparator("SEARCH RESULTS (" + results.size() + ")");
            if (results.isEmpty()) System.out.println("  No titles found.");
            else results.forEach(t -> System.out.println("  " + t));
        } catch (SQLException | IllegalArgumentException e) { System.out.println("  " + e.getMessage()); }
    }

    private static void doBrowseByGenre() throws SQLException {
        List<Genre> genres = service.getAllGenres();
        printSeparator("GENRES");
        genres.forEach(g -> System.out.println("  " + g));
        int genreId = promptInt("Enter Genre ID");
        List<Title> results = service.browseByGenre(genreId);
        printSeparator("TITLES IN GENRE " + genreId + " (" + results.size() + ")");
        if (results.isEmpty()) System.out.println("  No titles in this genre.");
        else results.forEach(t -> System.out.println("  " + t));
    }

    private static void doBrowseMovies() throws SQLException {
        List<Title> movies = service.browseMovies();
        printSeparator("MOVIES (" + movies.size() + ")");
        movies.forEach(t -> System.out.println("  " + t));
    }

    private static void doBrowseSeries() throws SQLException {
        List<Title> series = service.browseSeries();
        printSeparator("SERIES (" + series.size() + ")");
        series.forEach(t -> System.out.println("  " + t));
    }

    private static void doTopRated() throws SQLException {
        int n = promptInt("How many top titles? (e.g. 10)");
        List<Title> top = service.getTopRated(n);
        printSeparator("TOP " + n + " RATED");
        int rank = 1;
        for (Title t : top) System.out.printf("  #%d  %s%n", rank++, t);
    }

    private static void doTitleDetails() throws SQLException {
        int id = promptInt("Enter Title ID");
        Title t = service.getTitleDetails(id);
        printSeparator("TITLE DETAILS");
        System.out.println("  ID:       " + t.titleId);
        System.out.println("  Title:    " + t.title);
        System.out.println("  Type:     " + t.titleType);
        System.out.println("  Year:     " + t.releaseYear);
        System.out.println("  Duration: " + t.duration + " min");
        System.out.println("  Rating:   ★" + t.rating + "/10");
        System.out.println("  Genre:    " + t.genreName);
        System.out.println("  Where to watch: " + service.getServicesForTitle(id));

        if (t.titleType.equals("Series")) {
            List<Episode> eps = service.getEpisodes(id);
            System.out.println("\n  EPISODES (" + eps.size() + "):");
            eps.forEach(e -> System.out.println("    " + e));
        }

        List<Review> reviews = service.getReviews(id);
        System.out.println("\n  REVIEWS (" + reviews.size() + "):");
        if (reviews.isEmpty()) System.out.println("    No reviews yet.");
        else reviews.forEach(r -> System.out.println("    " + r));
    }

    // ══════════════════════════════════════════════════════════
    //  WATCHLIST
    // ══════════════════════════════════════════════════════════
    private static void doMyWatchlist() throws SQLException {
        List<WatchlistEntry> wl = service.getWatchlist(currentUser.userId);
        printSeparator("MY WATCHLIST (" + wl.size() + ")");
        if (wl.isEmpty()) System.out.println("  Your watchlist is empty.");
        else wl.forEach(w -> System.out.println("  " + w));
    }

    private static void doAddToWatchlist() throws SQLException {
        int titleId = promptInt("Enter Title ID to add");
        service.addToWatchlist(currentUser.userId, titleId);
        System.out.println("  Added to your watchlist!");
    }

    private static void doRemoveFromWatchlist() throws SQLException {
        doMyWatchlist();
        int wlId = promptInt("Enter Watchlist ID to remove");
        service.removeFromWatchlist(wlId, currentUser.userId);
        System.out.println("  Removed from watchlist.");
    }

    // ══════════════════════════════════════════════════════════
    //  REVIEWS
    // ══════════════════════════════════════════════════════════
    private static void doSubmitReview() throws SQLException {
        int    titleId = promptInt("Title ID to review");
        int    rating  = promptInt("Rating (1-10)");
        System.out.print("  Comment: ");
        String comment = scanner.nextLine().trim();
        service.submitReview(currentUser.userId, titleId, rating, comment);
        System.out.println("  Review submitted!");
    }

    private static void doMyReviews() throws SQLException {
        List<Review> reviews = service.getMyReviews(currentUser.userId);
        printSeparator("MY REVIEWS (" + reviews.size() + ")");
        if (reviews.isEmpty()) System.out.println("  You haven't reviewed anything yet.");
        else reviews.forEach(r -> System.out.println("  " + r));
    }

    private static void doUpdateReview() throws SQLException {
        doMyReviews();
        int    reviewId = promptInt("Review ID to update");
        int    rating   = promptInt("New rating (1-10)");
        System.out.print("  New comment: ");
        String comment = scanner.nextLine().trim();
        service.updateReview(reviewId, rating, comment);
        System.out.println("  Review updated.");
    }

    private static void doDeleteReview() throws SQLException {
        doMyReviews();
        int reviewId = promptInt("Review ID to delete");
        service.deleteReview(reviewId);
        System.out.println("  Review deleted.");
    }

    // ══════════════════════════════════════════════════════════
    //  WATCH HISTORY
    // ══════════════════════════════════════════════════════════
    private static void doMyHistory() throws SQLException {
        List<WatchHistoryEntry> history = service.getHistory(currentUser.userId);
        printSeparator("MY WATCH HISTORY (" + history.size() + ")");
        if (history.isEmpty()) System.out.println("  No watch history yet.");
        else history.forEach(h -> System.out.println("  " + h));
    }

    private static void doLogWatch() throws SQLException {
        int titleId  = promptInt("Title ID you watched");
        int duration = promptInt("Minutes watched");
        service.logWatch(currentUser.userId, titleId, duration);
        System.out.println("  Watch session logged!");
    }

    private static void doDeleteHistory() throws SQLException {
        doMyHistory();
        int histId = promptInt("History entry ID to delete");
        service.deleteHistoryEntry(histId, currentUser.userId);
        System.out.println("  History entry removed.");
    }

    // ══════════════════════════════════════════════════════════
    //  ACCOUNT
    // ══════════════════════════════════════════════════════════
    private static void doUpdateProfile() throws SQLException {
        System.out.println("  Current name:  " + currentUser.fullName);
        System.out.println("  Current email: " + currentUser.email);
        System.out.print("  New name (Enter to keep): ");  String name  = scanner.nextLine().trim();
        System.out.print("  New email (Enter to keep): "); String email = scanner.nextLine().trim();
        if (name.isEmpty())  name  = currentUser.fullName;
        if (email.isEmpty()) email = currentUser.email;
        service.updateUserProfile(currentUser.userId, name, email);
        currentUser.fullName = name;
        currentUser.email    = email;
        System.out.println("  Profile updated.");
    }

    private static void doChangePassword() throws SQLException {
        System.out.print("  New password: ");
        String pw = scanner.nextLine().trim();
        service.changePassword(currentUser.userId, pw);
        System.out.println("  Password changed.");
    }

    // ══════════════════════════════════════════════════════════
    //  ADMIN ACTIONS
    // ══════════════════════════════════════════════════════════
    private static void doAdminAddTitle() throws SQLException {
        System.out.print("  Title name: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Type (Movie/Series): ");
        String type = scanner.nextLine().trim();
        int year     = promptInt("Release year");
        int duration = promptInt("Duration (minutes)");
        double rating = promptDouble("Rating (0.0-10.0)");
        List<Genre> genres = service.getAllGenres();
        genres.forEach(g -> System.out.println("    " + g));
        int genreId = promptInt("Genre ID");
        service.addTitle(title, type, year, duration, rating, genreId);
        System.out.println("  Title added successfully!");
    }

    private static void doAdminUpdateTitle() throws SQLException {
        int id = promptInt("Title ID to update");
        Title t = service.getTitleDetails(id);
        System.out.println("  Current: " + t);
        System.out.print("  New title (Enter to keep): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) title = t.title;
        int year      = promptIntOrDefault("New year (0=keep): ", t.releaseYear);
        int duration  = promptIntOrDefault("New duration (0=keep): ", t.duration);
        double rating = promptDouble("New rating (0=keep): ");
        if (rating == 0) rating = t.rating;
        List<Genre> genres = service.getAllGenres();
        genres.forEach(g -> System.out.println("    " + g));
        int genreId   = promptIntOrDefault("New genre ID (0=keep): ", t.genreId);
        service.updateTitle(id, title, year, duration, rating, genreId);
        System.out.println("  Title updated.");
    }

    private static void doAdminDeleteTitle() throws SQLException {
        int id = promptInt("Title ID to delete");
        Title t = service.getTitleDetails(id);
        System.out.println("  You are about to delete: " + t.title);
        System.out.print("  Confirm? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            service.deleteTitle(id);
            System.out.println("  Title deleted.");
        } else {
            System.out.println("  Cancelled.");
        }
    }

    private static void doAdminAddEpisode() throws SQLException {
        int titleId = promptInt("Series Title ID");
        System.out.print("  Episode title: ");
        String epTitle = scanner.nextLine().trim();
        int season   = promptInt("Season number");
        int epNum    = promptInt("Episode number");
        int duration = promptInt("Duration (minutes)");
        service.addEpisode(titleId, epTitle, season, epNum, duration);
        System.out.println("  Episode added.");
    }

    private static void doAdminDeleteEpisode() throws SQLException {
        int titleId = promptInt("Series Title ID to view episodes");
        service.getEpisodes(titleId).forEach(e -> System.out.println("  " + e));
        int epId = promptInt("Episode ID to delete");
        service.deleteEpisode(epId);
        System.out.println("  Episode deleted.");
    }

    private static void doListUsers() throws SQLException {
        List<User> users = service.getAllUsers();
        printSeparator("ALL USERS (" + users.size() + ")");
        users.forEach(u -> System.out.println("  " + u));
    }

    private static void doAdminDeleteUser() throws SQLException {
        doListUsers();
        int userId = promptInt("User ID to delete");
        if (userId == currentUser.userId) {
            System.out.println("  Cannot delete yourself.");
            return;
        }
        service.deleteUser(userId);
        System.out.println("  User deleted.");
    }

    private static void doAdminViewAllReviews() throws SQLException {
        List<Review> reviews = service.getAllReviews();
        printSeparator("ALL REVIEWS (" + reviews.size() + ")");
        reviews.forEach(r -> System.out.println("  " + r));
    }

    private static void doAdminDeleteReview() throws SQLException {
        doAdminViewAllReviews();
        int reviewId = promptInt("Review ID to delete");
        service.deleteReview(reviewId);
        System.out.println("  Review deleted.");
    }

    // ══════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════
    private static int promptInt(String label) {
        System.out.print("  " + label + ": ");
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            System.out.println("  Invalid number. Defaulting to 0.");
            return 0;
        }
    }

    private static int promptIntOrDefault(String label, int defaultVal) {
        System.out.print("  " + label);
        try {
            String line = scanner.nextLine().trim();
            int val = Integer.parseInt(line);
            return val == 0 ? defaultVal : val;
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private static double promptDouble(String label) {
        System.out.print("  " + label + ": ");
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║          W A T C H V A U L T             ║");
        System.out.println("  ║    Movie & Streaming Management System   ║");
        System.out.println("  ║      CS157A Database Systems Project     ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printSeparator(String title) {
        System.out.println("\n  ── " + title + " " + "─".repeat(Math.max(0, 48 - title.length())));
    }
}
