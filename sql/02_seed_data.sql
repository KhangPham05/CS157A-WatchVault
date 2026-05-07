-- =============================================================
--  WatchVault – Seed Data  (15+ rows per table)
-- =============================================================
USE watchvault;

-- ── Genres (15 rows) ──────────────────────────────────────────
INSERT INTO Genres (GenreName, Description) VALUES
('Action',      'High-energy films featuring fights, chases, and stunts'),
('Drama',       'Character-driven stories exploring human emotions'),
('Comedy',      'Humorous content designed to entertain and amuse'),
('Thriller',    'Suspenseful stories with tension and surprise'),
('Horror',      'Content designed to frighten and disturb'),
('Sci-Fi',      'Speculative fiction rooted in science and technology'),
('Fantasy',     'Stories set in magical or supernatural worlds'),
('Romance',     'Stories centered on love and relationships'),
('Documentary', 'Non-fiction content exploring real events and people'),
('Animation',   'Content produced using animated visuals'),
('Crime',       'Stories involving criminal acts and investigations'),
('Adventure',   'Stories featuring exciting journeys and exploration'),
('Mystery',     'Stories centered on solving puzzles or crimes'),
('Biography',   'Real-life stories about notable individuals'),
('History',     'Content set in or exploring historical periods');

-- ── StreamingServices (15 rows) ───────────────────────────────
INSERT INTO StreamingServices (ServiceName, SubscriptionType, CountryAvailability, MonthlyPriceUSD) VALUES
('Netflix',          'Premium',  'Worldwide',        15.99),
('Hulu',             'Standard', 'USA',              7.99),
('Disney+',          'Standard', 'Worldwide',        10.99),
('HBO Max',          'Premium',  'USA, Europe',      15.99),
('Amazon Prime',     'Standard', 'Worldwide',        8.99),
('Apple TV+',        'Standard', 'Worldwide',        9.99),
('Peacock',          'Free',     'USA',              0.00),
('Paramount+',       'Standard', 'USA, Canada',      5.99),
('Crunchyroll',      'Standard', 'Worldwide',        7.99),
('Shudder',          'Standard', 'USA, UK, Canada',  5.99),
('MUBI',             'Premium',  'Worldwide',        14.99),
('Discovery+',       'Standard', 'Worldwide',        4.99),
('ESPN+',            'Standard', 'USA',              9.99),
('Tubi',             'Free',     'USA, Canada',      0.00),
('Criterion Channel','Premium',  'USA, Canada',      10.99);

-- ── Titles (20 rows: mix of Movie and Series) ─────────────────
INSERT INTO Titles (Title, TitleType, ReleaseYear, Duration, Rating, GenreID) VALUES
('Inception',                 'Movie',  2010, 148, 8.8,  6),   -- 1  Sci-Fi
('Breaking Bad',              'Series', 2008, 47,  9.5,  11),  -- 2  Crime
('The Dark Knight',           'Movie',  2008, 152, 9.0,  1),   -- 3  Action
('Stranger Things',           'Series', 2016, 51,  8.7,  6),   -- 4  Sci-Fi
('Parasite',                  'Movie',  2019, 132, 8.6,  4),   -- 5  Thriller
('The Crown',                 'Series', 2016, 58,  8.6,  14),  -- 6  Biography
('Interstellar',              'Movie',  2014, 169, 8.6,  6),   -- 7  Sci-Fi
('Game of Thrones',           'Series', 2011, 57,  9.2,  7),   -- 8  Fantasy
('Get Out',                   'Movie',  2017, 104, 7.7,  5),   -- 9  Horror
('The Witcher',               'Series', 2019, 60,  8.2,  7),   -- 10 Fantasy
('Dune',                      'Movie',  2021, 155, 8.0,  6),   -- 11 Sci-Fi
('Squid Game',                'Series', 2021, 32,  8.0,  4),   -- 12 Thriller
('Whiplash',                  'Movie',  2014, 107, 8.5,  2),   -- 13 Drama
('The Office',                'Series', 2005, 22,  9.0,  3),   -- 14 Comedy
('Everything Everywhere',     'Movie',  2022, 139, 7.8,  6),   -- 15 Sci-Fi
('Wednesday',                 'Series', 2022, 45,  8.1,  5),   -- 16 Horror
('Oppenheimer',               'Movie',  2023, 180, 8.6,  14),  -- 17 Biography
('The Last of Us',            'Series', 2023, 60,  8.8,  6),   -- 18 Sci-Fi
('Mad Max: Fury Road',        'Movie',  2015, 120, 8.1,  1),   -- 19 Action
('Severance',                 'Series', 2022, 52,  8.7,  4);   -- 20 Thriller

-- ── TitleAvailability (20 rows) ───────────────────────────────
INSERT INTO TitleAvailability (TitleID, ServiceID, DateAdded) VALUES
(1,  1, '2020-01-15'),  -- Inception     → Netflix
(1,  5, '2021-03-10'),  -- Inception     → Amazon Prime
(2,  1, '2019-06-01'),  -- Breaking Bad  → Netflix
(3,  4, '2020-08-20'),  -- Dark Knight   → HBO Max
(3,  5, '2021-01-05'),  -- Dark Knight   → Amazon Prime
(4,  1, '2016-07-15'),  -- Stranger Things → Netflix
(5,  1, '2020-05-25'),  -- Parasite      → Netflix
(6,  1, '2016-11-04'),  -- The Crown     → Netflix
(7,  5, '2020-03-01'),  -- Interstellar  → Amazon Prime
(8,  4, '2019-09-01'),  -- Game of Thrones → HBO Max
(9,  2, '2018-02-14'),  -- Get Out       → Hulu
(10, 1, '2019-12-20'),  -- The Witcher   → Netflix
(11, 5, '2022-01-01'),  -- Dune          → Amazon Prime
(12, 1, '2021-09-17'),  -- Squid Game    → Netflix
(13, 2, '2020-04-01'),  -- Whiplash      → Hulu
(14, 7, '2021-01-15'),  -- The Office    → Peacock
(15, 1, '2023-03-13'),  -- Everything Everywhere → Netflix
(16, 1, '2022-11-23'),  -- Wednesday     → Netflix
(17, 5, '2024-01-01'),  -- Oppenheimer   → Amazon Prime
(18, 4, '2023-01-15');  -- The Last of Us → HBO Max

-- ── Episodes (20 rows – for Series titles) ────────────────────
INSERT INTO Episodes (TitleID, EpisodeTitle, SeasonNumber, EpisodeNumber, Duration) VALUES
-- Breaking Bad (TitleID=2)
(2, 'Pilot',                     1, 1, 58),
(2, 'The Cat''s in the Bag',     1, 2, 48),
(2, 'Crawl Space',               4, 11, 45),
(2, 'Ozymandias',                5, 14, 47),
-- Stranger Things (TitleID=4)
(4, 'The Vanishing of Will Byers',1, 1, 48),
(4, 'The Weirdo on Maple Street', 1, 2, 55),
(4, 'Holly, Jolly',               1, 3, 50),
-- Game of Thrones (TitleID=8)
(8, 'Winter Is Coming',          1, 1, 62),
(8, 'The Lion and the Rose',     4, 2, 52),
(8, 'The Winds of Winter',       6, 10, 69),
-- Squid Game (TitleID=12)
(12,'Red Light, Green Light',    1, 1, 32),
(12,'Hell',                      1, 2, 41),
(12,'The Man with the Umbrella', 1, 3, 40),
-- The Last of Us (TitleID=18)
(18,'When You''re Lost in the Darkness', 1, 1, 81),
(18,'Infected',                          1, 2, 54),
(18,'Long Long Time',                    1, 3, 76),
-- Severance (TitleID=20)
(20,'Good News About Hell',      1, 1, 52),
(20,'Half Loop',                 1, 2, 43),
(20,'In Perpetuity',             1, 3, 44),
(20,'The You You Are',           1, 4, 39);

-- ── CastCrew (20 rows) ────────────────────────────────────────
INSERT INTO CastCrew (FullName, Role, TitleID, CHARACTER_NAME) VALUES
('Leonardo DiCaprio', 'Actor',    1,  'Cobb'),
('Christopher Nolan', 'Director', 1,  'N/A'),
('Bryan Cranston',    'Actor',    2,  'Walter White'),
('Vince Gilligan',    'Director', 2,  'N/A'),
('Christian Bale',    'Actor',    3,  'Bruce Wayne'),
('Heath Ledger',      'Actor',    3,  'The Joker'),
('Millie Bobby Brown','Actor',    4,  'Eleven'),
('Song Kang-ho',      'Actor',    5,  'Ki-taek'),
('Bong Joon-ho',      'Director', 5,  'N/A'),
('Olivia Colman',     'Actor',    6,  'Queen Elizabeth II'),
('Matthew McConaughey','Actor',   7,  'Cooper'),
('Emilia Clarke',     'Actor',    8,  'Daenerys Targaryen'),
('Daniel Kaluuya',    'Actor',    9,  'Chris Washington'),
('Henry Cavill',      'Actor',    10, 'Geralt of Rivia'),
('Timothée Chalamet', 'Actor',    11, 'Paul Atreides'),
('Lee Jung-jae',      'Actor',    12, 'Seong Gi-hun'),
('Miles Teller',      'Actor',    13, 'Andrew Neiman'),
('Steve Carell',      'Actor',    14, 'Michael Scott'),
('Cillian Murphy',    'Actor',    17, 'J. Robert Oppenheimer'),
('Pedro Pascal',      'Actor',    18, 'Joel Miller');

-- ── Users (16 rows) ───────────────────────────────────────────
-- passwords are SHA2-256 of the plain-text shown in comments
INSERT INTO Users (FullName, Email, Password, DateJoined, UserRole) VALUES
('Admin User',      'admin@watchvault.com',   SHA2('admin123',256),   '2023-01-01', 'admin'),
('Alice Johnson',   'alice@email.com',        SHA2('alice123',256),   '2023-02-10', 'user'),
('Bob Martinez',    'bob@email.com',          SHA2('bob123',256),     '2023-03-15', 'user'),
('Carol White',     'carol@email.com',        SHA2('carol123',256),   '2023-04-20', 'user'),
('David Lee',       'david@email.com',        SHA2('david123',256),   '2023-05-05', 'user'),
('Emma Davis',      'emma@email.com',         SHA2('emma123',256),    '2023-06-18', 'user'),
('Frank Wilson',    'frank@email.com',        SHA2('frank123',256),   '2023-07-22', 'user'),
('Grace Taylor',    'grace@email.com',        SHA2('grace123',256),   '2023-08-30', 'user'),
('Henry Brown',     'henry@email.com',        SHA2('henry123',256),   '2023-09-12', 'user'),
('Ivy Chen',        'ivy@email.com',          SHA2('ivy123',256),     '2023-10-01', 'user'),
('Jack Thompson',   'jack@email.com',         SHA2('jack123',256),    '2023-10-15', 'user'),
('Karen Anderson',  'karen@email.com',        SHA2('karen123',256),   '2023-11-01', 'user'),
('Liam Garcia',     'liam@email.com',         SHA2('liam123',256),    '2023-11-20', 'user'),
('Maya Patel',      'maya@email.com',         SHA2('maya123',256),    '2023-12-05', 'user'),
('Noah Kim',        'noah@email.com',         SHA2('noah123',256),    '2024-01-10', 'user'),
('Olivia Scott',    'olivia@email.com',       SHA2('olivia123',256),  '2024-01-25', 'user');

-- ── Watchlist (16 rows) ───────────────────────────────────────
INSERT INTO Watchlist (UserID, TitleID, DateAdded) VALUES
(2,  1,  '2024-01-01'),
(2,  4,  '2024-01-15'),
(3,  2,  '2024-02-01'),
(3,  8,  '2024-02-10'),
(4,  5,  '2024-02-20'),
(4,  11, '2024-03-01'),
(5,  3,  '2024-03-10'),
(5,  7,  '2024-03-15'),
(6,  12, '2024-04-01'),
(6,  18, '2024-04-05'),
(7,  10, '2024-04-10'),
(8,  16, '2024-04-20'),
(9,  20, '2024-05-01'),
(10, 13, '2024-05-10'),
(11, 15, '2024-05-15'),
(12, 17, '2024-05-20');

-- ── Reviews (16 rows) ─────────────────────────────────────────
INSERT INTO Reviews (UserID, TitleID, Rating, Comment, ReviewDate) VALUES
(2,  1,  9, 'Mind-bending masterpiece. Nolan at his best.',                            '2024-01-10'),
(3,  2,  10,'The greatest TV show ever made. Period.',                                  '2024-02-05'),
(4,  5,  9, 'Parasite is a brilliant social commentary disguised as a thriller.',       '2024-02-25'),
(5,  3,  10,'Heath Ledger''s Joker is the greatest villain performance in film.',       '2024-03-12'),
(6,  12, 8, 'Squid Game is intense and thought-provoking. A bit overhyped though.',    '2024-04-03'),
(7,  10, 8, 'The Witcher is a great fantasy series. Cavill carries it effortlessly.',  '2024-04-12'),
(8,  16, 7, 'Wednesday is fun but gets a bit formulaic by mid-season.',                '2024-04-22'),
(9,  20, 9, 'Severance is the most original concept on TV right now.',                 '2024-05-03'),
(10, 13, 9, 'Whiplash is brutal and electrifying. Teller and Simmons are incredible.', '2024-05-12'),
(11, 15, 8, 'Everything Everywhere is chaotic in the best possible way.',              '2024-05-17'),
(12, 17, 9, 'Oppenheimer is a cinematic achievement. 3 hours flew by.',                '2024-05-22'),
(13, 18, 10,'The Last of Us is the best video game adaptation ever made.',             '2024-05-25'),
(14, 7,  9, 'Interstellar made me feel things I cannot fully describe.',               '2024-05-28'),
(15, 8,  8, 'Game of Thrones was incredible until the last season.',                   '2024-06-01'),
(16, 4,  9, 'Stranger Things is nostalgic, scary, and endlessly rewatchable.',         '2024-06-05'),
(2,  11, 8, 'Dune is visually stunning. Part 2 made it a complete story.',             '2024-06-08');

-- ── WatchHistory (20 rows) ────────────────────────────────────
INSERT INTO WatchHistory (UserID, TitleID, WatchDate, WatchDuration) VALUES
(2,  1,  '2024-01-08', 148),
(2,  4,  '2024-01-20', 48),
(3,  2,  '2024-02-03', 47),
(3,  8,  '2024-02-12', 62),
(4,  5,  '2024-02-22', 132),
(5,  3,  '2024-03-11', 152),
(5,  7,  '2024-03-16', 169),
(6,  12, '2024-04-02', 32),
(6,  18, '2024-04-07', 81),
(7,  10, '2024-04-11', 60),
(8,  16, '2024-04-21', 45),
(9,  20, '2024-05-02', 52),
(10, 13, '2024-05-11', 107),
(11, 15, '2024-05-16', 139),
(12, 17, '2024-05-21', 180),
(13, 18, '2024-05-24', 81),
(14, 7,  '2024-05-27', 169),
(15, 8,  '2024-05-31', 62),
(16, 4,  '2024-06-04', 51),
(2,  11, '2024-06-07', 155);
