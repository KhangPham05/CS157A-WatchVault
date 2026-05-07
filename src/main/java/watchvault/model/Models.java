package watchvault.model;

// ─────────────────────────────────────────────
//  Plain model (POJO) classes – one per table
// ─────────────────────────────────────────────

public class Models {

    // ── Genre ──────────────────────────────────
    public static class Genre {
        public int    genreId;
        public String genreName;
        public String description;

        public Genre(int genreId, String genreName, String description) {
            this.genreId     = genreId;
            this.genreName   = genreName;
            this.description = description;
        }

        @Override public String toString() {
            return String.format("[%d] %-15s – %s", genreId, genreName, description);
        }
    }

    // ── StreamingService ───────────────────────
    public static class StreamingService {
        public int    serviceId;
        public String serviceName;
        public String subscriptionType;
        public String countryAvailability;
        public double monthlyPrice;

        public StreamingService(int serviceId, String serviceName, String subscriptionType,
                                String countryAvailability, double monthlyPrice) {
            this.serviceId           = serviceId;
            this.serviceName         = serviceName;
            this.subscriptionType    = subscriptionType;
            this.countryAvailability = countryAvailability;
            this.monthlyPrice        = monthlyPrice;
        }

        @Override public String toString() {
            return String.format("[%d] %-20s %-10s %-20s $%.2f/mo",
                    serviceId, serviceName, subscriptionType, countryAvailability, monthlyPrice);
        }
    }

    // ── Title ──────────────────────────────────
    public static class Title {
        public int    titleId;
        public String title;
        public String titleType;   // Movie | Series
        public int    releaseYear;
        public int    duration;
        public double rating;
        public int    genreId;
        public String genreName;   // joined from Genres

        public Title(int titleId, String title, String titleType, int releaseYear,
                     int duration, double rating, int genreId, String genreName) {
            this.titleId     = titleId;
            this.title       = title;
            this.titleType   = titleType;
            this.releaseYear = releaseYear;
            this.duration    = duration;
            this.rating      = rating;
            this.genreId     = genreId;
            this.genreName   = genreName;
        }

        @Override public String toString() {
            return String.format("[%2d] %-40s %-7s %d  %3dmin  ★%.1f  %s",
                    titleId, title, titleType, releaseYear, duration, rating, genreName);
        }
    }

    // ── Episode ────────────────────────────────
    public static class Episode {
        public int    episodeId;
        public int    titleId;
        public String episodeTitle;
        public int    seasonNumber;
        public int    episodeNumber;
        public int    duration;

        public Episode(int episodeId, int titleId, String episodeTitle,
                       int seasonNumber, int episodeNumber, int duration) {
            this.episodeId     = episodeId;
            this.titleId       = titleId;
            this.episodeTitle  = episodeTitle;
            this.seasonNumber  = seasonNumber;
            this.episodeNumber = episodeNumber;
            this.duration      = duration;
        }

        @Override public String toString() {
            return String.format("[%d] S%02dE%02d – %-45s (%dmin)",
                    episodeId, seasonNumber, episodeNumber, episodeTitle, duration);
        }
    }

    // ── CastCrew ───────────────────────────────
    public static class CastCrew {
        public int    personId;
        public String fullName;
        public String role;
        public int    titleId;
        public String characterName;

        public CastCrew(int personId, String fullName, String role, int titleId, String characterName) {
            this.personId      = personId;
            this.fullName      = fullName;
            this.role          = role;
            this.titleId       = titleId;
            this.characterName = characterName;
        }

        @Override public String toString() {
            return String.format("[%d] %-25s %-12s as %s", personId, fullName, role, characterName);
        }
    }

    // ── User ───────────────────────────────────
    public static class User {
        public int    userId;
        public String fullName;
        public String email;
        public String password;   // SHA2 hash
        public String dateJoined;
        public String userRole;

        public User(int userId, String fullName, String email, String password,
                    String dateJoined, String userRole) {
            this.userId     = userId;
            this.fullName   = fullName;
            this.email      = email;
            this.password   = password;
            this.dateJoined = dateJoined;
            this.userRole   = userRole;
        }

        @Override public String toString() {
            return String.format("[%d] %-25s %-30s %-5s joined:%s",
                    userId, fullName, email, userRole, dateJoined);
        }
    }

    // ── Watchlist ──────────────────────────────
    public static class WatchlistEntry {
        public int    watchlistId;
        public int    userId;
        public int    titleId;
        public String title;       // joined
        public String dateAdded;

        public WatchlistEntry(int watchlistId, int userId, int titleId, String title, String dateAdded) {
            this.watchlistId = watchlistId;
            this.userId      = userId;
            this.titleId     = titleId;
            this.title       = title;
            this.dateAdded   = dateAdded;
        }

        @Override public String toString() {
            return String.format("[%d] %-40s added:%s", watchlistId, title, dateAdded);
        }
    }

    // ── Review ─────────────────────────────────
    public static class Review {
        public int    reviewId;
        public int    userId;
        public int    titleId;
        public String titleName;   // joined
        public String userName;    // joined
        public int    rating;
        public String comment;
        public String reviewDate;

        public Review(int reviewId, int userId, int titleId, String titleName,
                      String userName, int rating, String comment, String reviewDate) {
            this.reviewId   = reviewId;
            this.userId     = userId;
            this.titleId    = titleId;
            this.titleName  = titleName;
            this.userName   = userName;
            this.rating     = rating;
            this.comment    = comment;
            this.reviewDate = reviewDate;
        }

        @Override public String toString() {
            return String.format("[%d] ★%d/10 by %-20s on %-35s – %s",
                    reviewId, rating, userName, titleName, comment);
        }
    }

    // ── WatchHistory ───────────────────────────
    public static class WatchHistoryEntry {
        public int    watchHistoryId;
        public int    userId;
        public int    titleId;
        public String titleName;   // joined
        public String watchDate;
        public int    watchDuration;

        public WatchHistoryEntry(int watchHistoryId, int userId, int titleId,
                                 String titleName, String watchDate, int watchDuration) {
            this.watchHistoryId = watchHistoryId;
            this.userId         = userId;
            this.titleId        = titleId;
            this.titleName      = titleName;
            this.watchDate      = watchDate;
            this.watchDuration  = watchDuration;
        }

        @Override public String toString() {
            return String.format("[%d] %-40s on %s (%dmin watched)",
                    watchHistoryId, titleName, watchDate, watchDuration);
        }
    }
}
