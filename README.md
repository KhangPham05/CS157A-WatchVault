WatchVault 🎬
A relational database system that models an online movie and streaming platform, built with Java and MySQL.
About
WatchVault allows users to browse movies and TV shows, manage personal watchlists, and submit reviews. Administrators can manage content and monitor platform usage.
Tech Stack

Language: Java
Database: MySQL
Connectivity: JDBC
Build Tool: Maven
IDE: VS Code / IntelliJ
Version Control: Git & GitHub

Database Schema

Users — account info
Movies — movies and TV series
Episodes — episodes linked to series
Genres — genre categories
StreamingServices — available platforms
CastCrew — cast and crew per title
Watchlist — user saved titles
Reviews — user ratings and comments
WatchHistory — user watch activity

Getting Started
Prerequisites

Java 17+
MySQL 8+
Maven

Setup

Clone the repository

bash   git clone https://github.com/yourusername/CS157A-Project.git
   cd CS157A-Project

Create the database

bash   mysql -u root -p < src/main/resources/schema.sql

Configure your credentials — create src/main/resources/db.properties:

properties   db.url=jdbc:mysql://localhost:3306/watchvault?useSSL=false&serverTimezone=UTC
   db.user=your_username
   db.password=your_password

Run the project

bash   mvn compile exec:java

Status
🚧 In Progress