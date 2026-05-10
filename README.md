# WatchVault — Movie & Streaming Management System
**CS157A Database Systems Project** | SJSU | Prof. Tahereh Arabghalizi

---

## Team Members
| Name | Student ID |
|------|-----------|
| Hoang Khang Pham | 016994471 |
| Jongha Kim | 017774822 |

---

## Project Overview

WatchVault is a three-tier Java/JDBC/MySQL application that models an online movie and streaming platform. It lets users browse a catalog of movies and series, manage personal watchlists, write reviews, and track watch history. Administrators can manage all content and monitor platform usage.

---

## Architecture

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │
│  WatchVaultCLI.java  (CLI menus/input)  │
└──────────────────┬──────────────────────┘
                   │ method calls
┌──────────────────▼──────────────────────┐
│         APPLICATION LAYER               │
│  WatchVaultService.java  (business      │
│  logic & validation)                    │
│  ┌──────────────────────────────────┐   │
│  │  DAOs (one per table):           │   │
│  │  TitleDAO  UserDAO  ReviewDAO    │   │
│  │  WatchlistDAO  WatchHistoryDAO   │   │
│  │  EpisodeDAO  GenreDAO            │   │
│  └──────────────────────────────────┘   │
└──────────────────┬──────────────────────┘
                   │ JDBC PreparedStatement
┌──────────────────▼──────────────────────┐
│            DATA LAYER                   │
│  MySQL — watchvault database            │
│  10 tables, BCNF normalized             │
└─────────────────────────────────────────┘
```

---

## Database Schema (10 Tables)

| Table | Description | Rows |
|-------|-------------|------|
| `Genres` | 15 content genres | 15 |
| `StreamingServices` | Platforms (Netflix, Hulu…) | 15 |
| `Titles` | Movies and TV series | 20 |
| `TitleAvailability` | Which service hosts each title (junction) | 20 |
| `Episodes` | Individual series episodes | 20 |
| `CastCrew` | Actors, directors per title | 20 |
| `Users` | Registered users + admin | 16 |
| `Watchlist` | User saved-to-watch entries | 16 |
| `Reviews` | User ratings + comments | 16 |
| `WatchHistory` | Watch session logs | 20 |

**All columns are NOT NULL. All relations are in BCNF.**

### Key Design Decisions
- `Titles` stores both Movies and Series (unified with a `TitleType` enum) — clean, no NULL columns
- `TitleAvailability` is a junction table resolving the many-to-many Title ↔ StreamingService relationship (fixes a BCNF violation vs. having `ServiceID` on `Titles` directly)
- `Users.Password` stored as `SHA2(password, 256)` hash — never plaintext
- Proper foreign keys with `ON DELETE CASCADE` where child records should follow parent deletion

---

## SQL Operations Covered

| Operation | Examples in app |
|-----------|----------------|
| **SELECT** | Browse all titles, search by keyword, filter by genre/type, top-rated, title details, watchlist, history, reviews |
| **INSERT** | Register user, add title (admin), add to watchlist, submit review, log watch, add episode |
| **UPDATE** | Update title (admin), update review, update user profile, change password |
| **DELETE** | Delete title (admin), remove from watchlist, delete review, delete history entry, delete user (admin) |

All operations use `PreparedStatement` via JDBC — no raw string concatenation, no ORM.

---

## Technologies

| Component | Technology |
|-----------|-----------|
| Language | Java 14+ |
| Database | MySQL 8+ |
| Connectivity | JDBC (mysql-connector-j 9.1.0) |
| IDE | VS Code / IntelliJ IDEA |
| Version control | Git / GitHub |

---

## Setup Instructions

### Prerequisites
- Java JDK 14 or newer (`java -version`)
- MySQL 8.0 or newer (running locally)
- `mysql-connector-j-9.1.0.jar` in the `lib/` folder

### Step 1 — Download MySQL Connector/J
Download from: https://dev.mysql.com/downloads/connector/j/
Place the `.jar` file at: `lib/mysql-connector-j-9.1.0.jar`

### Step 2 — Configure database credentials
Open `src/main/java/watchvault/db/DBConnection.java` and set:
```java
private static final String PASSWORD = "your_mysql_root_password";
```
If your MySQL root has no password, leave it as `""`.

### Step 3 — Create and populate the database
```bash
mysql -u root -p < sql/01_schema.sql
mysql -u root -p < sql/02_seed_data.sql
```

### Step 4 — Compile and run

**macOS / Linux:**
```bash
chmod +x compile.sh
./compile.sh
java -jar WatchVault.jar
```
or
```bash
mvn compile exec:java -Dexec.mainClass=watchvault.ui.watchvaultcli.java
```

**Windows:**
```
compile.bat
java -jar WatchVault.jar
```

**Or manually:**
```bash
javac -cp lib/mysql-connector-j-9.1.0.jar -d out --release 14 \
  $(find src -name "*.java")
jar cfm WatchVault.jar out/META-INF/MANIFEST.MF -C out .
java -jar WatchVault.jar
```

---

## Default Accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@watchvault.com | admin123 |
| User | alice@email.com | alice123 |
| User | bob@email.com | bob123 |

*(All 16 seeded users follow the pattern `<firstname>@email.com` / `<firstname>123`)*

---

## Feature Summary

### Regular User
- Browse all titles / search by keyword / filter by genre, type, or top-rated
- View full title details: type, genre, duration, rating, streaming services, episodes, reviews
- Manage personal watchlist (add / remove)
- Submit, update, and delete reviews (rating 1–10 + comment)
- Log watch sessions and view/delete watch history
- Update profile (name, email) and change password

### Admin
- All user features, plus:
- Add, update, and delete titles
- Add and delete individual episodes
- View and delete all users
- View and delete any review
- View watch statistics (views and total minutes per title)

---

## Project Structure

```
WatchVault/
├── sql/
│   ├── 01_schema.sql          ← DDL: CREATE TABLE statements
│   └── 02_seed_data.sql       ← INSERT seed data (15–20 rows/table)
├── src/main/java/watchvault/
│   ├── db/
│   │   └── DBConnection.java  ← JDBC connection singleton
│   ├── model/
│   │   └── Models.java        ← POJO model classes (one per table)
│   ├── dao/
│   │   ├── TitleDAO.java      ← CRUD for Titles
│   │   ├── UserDAO.java       ← CRUD + auth for Users
│   │   ├── ReviewDAO.java     ← CRUD for Reviews
│   │   ├── WatchlistDAO.java  ← CRUD for Watchlist
│   │   ├── WatchHistoryDAO.java ← CRUD for WatchHistory
│   │   ├── EpisodeDAO.java    ← CRUD for Episodes
│   │   └── GenreDAO.java      ← SELECT for Genres
│   ├── service/
│   │   └── WatchVaultService.java ← Business logic / validation
│   └── ui/
│       └── WatchVaultCLI.java ← CLI presentation layer
├── lib/
│   └── mysql-connector-j-9.1.0.jar  ← (download separately)
├── WatchVault.jar             ← Pre-built runnable JAR
├── compile.sh                 ← Build script (Mac/Linux)
├── compile.bat                ← Build script (Windows)
└── README.md
```

---

## Non-Functional Requirements Implemented

| Requirement | Implementation |
|-------------|---------------|
| **Security** | SHA2-256 password hashing; admin-only routes enforced in CLI; PreparedStatement prevents SQL injection |
| **Performance** | Indexes on `Titles.ReleaseYear`, `Titles.GenreID`, `WatchHistory.UserID`, `WatchHistory.WatchDate`, `Reviews.TitleID` |
| **Scalability** | New streaming service = one new row in `StreamingServices`; new genre = one row in `Genres`; no schema changes required |
| **Maintainability** | Strict three-tier separation; one DAO per table; service layer isolates business rules from SQL |
