-- =============================================================
--  WatchVault – Database Schema (CS157A Project)
--  All columns NOT NULL; relations in BCNF
-- =============================================================

DROP DATABASE IF EXISTS watchvault;
CREATE DATABASE watchvault CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE watchvault;

-- ── 1. Genres ────────────────────────────────────────────────
CREATE TABLE Genres (
    GenreID      INT          NOT NULL AUTO_INCREMENT,
    GenreName    VARCHAR(50)  NOT NULL,
    Description  VARCHAR(255) NOT NULL,
    PRIMARY KEY (GenreID),
    UNIQUE KEY uq_genre_name (GenreName)
);

-- ── 2. StreamingServices ─────────────────────────────────────
CREATE TABLE StreamingServices (
    ServiceID          INT          NOT NULL AUTO_INCREMENT,
    ServiceName        VARCHAR(100) NOT NULL,
    SubscriptionType   VARCHAR(50)  NOT NULL,
    CountryAvailability VARCHAR(100) NOT NULL,
    MonthlyPriceUSD    DECIMAL(6,2) NOT NULL,
    PRIMARY KEY (ServiceID),
    UNIQUE KEY uq_service_name (ServiceName)
);

-- ── 3. Titles (Movies + TV Shows unified) ────────────────────
--   A Title belongs to one Genre; multi-service via junction table
CREATE TABLE Titles (
    TitleID      INT          NOT NULL AUTO_INCREMENT,
    Title        VARCHAR(200) NOT NULL,
    TitleType    ENUM('Movie','Series') NOT NULL,
    ReleaseYear  YEAR         NOT NULL,
    Duration     INT          NOT NULL COMMENT 'minutes for Movie; avg episode mins for Series',
    Rating       DECIMAL(3,1) NOT NULL DEFAULT 0.0,
    GenreID      INT          NOT NULL,
    PRIMARY KEY (TitleID),
    CONSTRAINT fk_title_genre FOREIGN KEY (GenreID) REFERENCES Genres(GenreID)
);

-- ── 4. TitleAvailability (Title ↔ Service junction) ──────────
--   Resolves the multi-service problem; keeps Titles in BCNF
CREATE TABLE TitleAvailability (
    AvailabilityID INT  NOT NULL AUTO_INCREMENT,
    TitleID        INT  NOT NULL,
    ServiceID      INT  NOT NULL,
    DateAdded      DATE NOT NULL,
    PRIMARY KEY (AvailabilityID),
    UNIQUE KEY uq_title_service (TitleID, ServiceID),
    CONSTRAINT fk_avail_title   FOREIGN KEY (TitleID)   REFERENCES Titles(TitleID)   ON DELETE CASCADE,
    CONSTRAINT fk_avail_service FOREIGN KEY (ServiceID) REFERENCES StreamingServices(ServiceID)
);

-- ── 5. Episodes ───────────────────────────────────────────────
CREATE TABLE Episodes (
    EpisodeID     INT          NOT NULL AUTO_INCREMENT,
    TitleID       INT          NOT NULL,
    EpisodeTitle  VARCHAR(200) NOT NULL,
    SeasonNumber  TINYINT      NOT NULL,
    EpisodeNumber TINYINT      NOT NULL,
    Duration      INT          NOT NULL COMMENT 'minutes',
    PRIMARY KEY (EpisodeID),
    CONSTRAINT fk_ep_title FOREIGN KEY (TitleID) REFERENCES Titles(TitleID) ON DELETE CASCADE
);

-- ── 6. CastCrew ───────────────────────────────────────────────
CREATE TABLE CastCrew (
    PersonID  INT          NOT NULL AUTO_INCREMENT,
    FullName  VARCHAR(100) NOT NULL,
    Role      VARCHAR(50)  NOT NULL COMMENT 'Actor, Director, Producer, etc.',
    TitleID   INT          NOT NULL,
    CHARACTER_NAME VARCHAR(100) NOT NULL DEFAULT 'N/A',
    PRIMARY KEY (PersonID),
    CONSTRAINT fk_cast_title FOREIGN KEY (TitleID) REFERENCES Titles(TitleID) ON DELETE CASCADE
);

-- ── 7. Users ──────────────────────────────────────────────────
CREATE TABLE Users (
    UserID      INT          NOT NULL AUTO_INCREMENT,
    FullName    VARCHAR(100) NOT NULL,
    Email       VARCHAR(150) NOT NULL,
    Password    VARCHAR(255) NOT NULL COMMENT 'stored as SHA2 hash',
    DateJoined  DATE         NOT NULL,
    UserRole    ENUM('user','admin') NOT NULL DEFAULT 'user',
    PRIMARY KEY (UserID),
    UNIQUE KEY uq_email (Email)
);

-- ── 8. Watchlist ──────────────────────────────────────────────
CREATE TABLE Watchlist (
    WatchlistID INT  NOT NULL AUTO_INCREMENT,
    UserID      INT  NOT NULL,
    TitleID     INT  NOT NULL,
    DateAdded   DATE NOT NULL,
    PRIMARY KEY (WatchlistID),
    UNIQUE KEY uq_user_title (UserID, TitleID),
    CONSTRAINT fk_wl_user  FOREIGN KEY (UserID)  REFERENCES Users(UserID)  ON DELETE CASCADE,
    CONSTRAINT fk_wl_title FOREIGN KEY (TitleID) REFERENCES Titles(TitleID) ON DELETE CASCADE
);

-- ── 9. Reviews ────────────────────────────────────────────────
CREATE TABLE Reviews (
    ReviewID    INT          NOT NULL AUTO_INCREMENT,
    UserID      INT          NOT NULL,
    TitleID     INT          NOT NULL,
    Rating      TINYINT      NOT NULL COMMENT '1-10',
    Comment     VARCHAR(1000) NOT NULL,
    ReviewDate  DATE         NOT NULL,
    PRIMARY KEY (ReviewID),
    UNIQUE KEY uq_user_review (UserID, TitleID),
    CONSTRAINT fk_rev_user  FOREIGN KEY (UserID)  REFERENCES Users(UserID)  ON DELETE CASCADE,
    CONSTRAINT fk_rev_title FOREIGN KEY (TitleID) REFERENCES Titles(TitleID) ON DELETE CASCADE,
    CONSTRAINT chk_rating CHECK (Rating BETWEEN 1 AND 10)
);

-- ── 10. WatchHistory ─────────────────────────────────────────
CREATE TABLE WatchHistory (
    WatchHistoryID INT  NOT NULL AUTO_INCREMENT,
    UserID         INT  NOT NULL,
    TitleID        INT  NOT NULL,
    WatchDate      DATE NOT NULL,
    WatchDuration  INT  NOT NULL COMMENT 'minutes watched',
    PRIMARY KEY (WatchHistoryID),
    CONSTRAINT fk_wh_user  FOREIGN KEY (UserID)  REFERENCES Users(UserID)  ON DELETE CASCADE,
    CONSTRAINT fk_wh_title FOREIGN KEY (TitleID) REFERENCES Titles(TitleID) ON DELETE CASCADE
);

-- Performance indexes
CREATE INDEX idx_titles_year   ON Titles(ReleaseYear);
CREATE INDEX idx_titles_genre  ON Titles(GenreID);
CREATE INDEX idx_wh_user       ON WatchHistory(UserID);
CREATE INDEX idx_wh_date       ON WatchHistory(WatchDate);
CREATE INDEX idx_reviews_title ON Reviews(TitleID);
