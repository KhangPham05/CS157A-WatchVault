CREATE DATABASE IF NOT EXISTS watchvault;
USE watchvault;

-- Genres
CREATE TABLE Genres (
    GenreID     INT AUTO_INCREMENT PRIMARY KEY,
    GenreName   VARCHAR(100) NOT NULL
);

-- StreamingServices
CREATE TABLE StreamingServices (
    ServiceID            INT AUTO_INCREMENT PRIMARY KEY,
    ServiceName          VARCHAR(100) NOT NULL,
    SubscriptionType     VARCHAR(50),
    CountryAvailability  VARCHAR(100)
);

-- Movies
CREATE TABLE Movies (
    MovieID          INT AUTO_INCREMENT PRIMARY KEY,
    Title            VARCHAR(255) NOT NULL,
    Type             ENUM('Movie', 'Series') NOT NULL,
    ReleaseYear      YEAR,
    Duration         INT,  -- in minutes
    GenreID          INT,
    StreamingServiceID INT,
    FOREIGN KEY (GenreID)           REFERENCES Genres(GenreID),
    FOREIGN KEY (StreamingServiceID) REFERENCES StreamingServices(ServiceID)
);

-- Episodes
CREATE TABLE Episodes (
    EpisodeID      INT AUTO_INCREMENT PRIMARY KEY,
    MovieID        INT NOT NULL,
    EpisodeTitle   VARCHAR(255),
    SeasonNumber   INT,
    EpisodeNumber  INT,
    Duration       INT,  -- in minutes
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE CASCADE
);

-- CastCrew
CREATE TABLE CastCrew (
    PersonID   INT AUTO_INCREMENT PRIMARY KEY,
    FullName   VARCHAR(150) NOT NULL,
    Role       VARCHAR(100),
    MovieID    INT,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE SET NULL
);

-- Users
CREATE TABLE Users (
    UserID      INT AUTO_INCREMENT PRIMARY KEY,
    FullName    VARCHAR(150) NOT NULL,
    Email       VARCHAR(150) NOT NULL UNIQUE,
    Password    VARCHAR(255) NOT NULL,  -- store hashed
    DateJoined  DATE DEFAULT (CURRENT_DATE)
);

-- Watchlist
CREATE TABLE Watchlist (
    WatchlistID  INT AUTO_INCREMENT PRIMARY KEY,
    UserID       INT NOT NULL,
    MovieID      INT NOT NULL,
    DateAdded    DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (UserID)  REFERENCES Users(UserID)  ON DELETE CASCADE,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE CASCADE
);

-- Reviews
CREATE TABLE Reviews (
    ReviewID    INT AUTO_INCREMENT PRIMARY KEY,
    UserID      INT NOT NULL,
    MovieID     INT NOT NULL,
    Rating      TINYINT CHECK (Rating BETWEEN 1 AND 10),
    Comment     TEXT,
    ReviewDate  DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (UserID)  REFERENCES Users(UserID)  ON DELETE CASCADE,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE CASCADE
);

-- WatchHistory
CREATE TABLE WatchHistory (
    WatchHistoryID  INT AUTO_INCREMENT PRIMARY KEY,
    UserID          INT NOT NULL,
    MovieID         INT NOT NULL,
    WatchDate       DATE DEFAULT (CURRENT_DATE),
    WatchDuration   INT,  -- in minutes
    FOREIGN KEY (UserID)  REFERENCES Users(UserID)  ON DELETE CASCADE,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE CASCADE
);

-- Indexes for performance (as noted in your requirements)
CREATE INDEX idx_movies_title       ON Movies(Title);
CREATE INDEX idx_movies_year        ON Movies(ReleaseYear);
CREATE INDEX idx_movies_genre       ON Movies(GenreID);
CREATE INDEX idx_movies_service     ON Movies(StreamingServiceID);