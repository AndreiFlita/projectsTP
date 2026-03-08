# 🎨 CulturApp

A Java desktop application built with Swing that serves as an interactive cultural platform, connecting artists, event organizers, and culture enthusiasts in a unified community space.

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Getting Started](#getting-started)
- [User Roles](#user-roles)
- [Screenshots](#screenshots)
- [Contributing](#contributing)

---

## ✨ Features

- **🛍️ Marketplace** — Browse, buy, and sell cultural products (paintings, sculptures, photography, crafts, and more)
- **🎭 Events** — Discover and book tickets for cultural events; artists can create and manage their own events
- **💬 Forum** — Community discussion board organized by categories: Literature, Film, Visual Arts, Theatre, Music, and General Culture
- **📓 Cultural Journal** — Personal diary for logging cultural experiences (books, films, exhibitions) with ratings
- **👤 User Profiles** — Customizable profiles with bio, interests, and role-based access
- **👨‍💼 Admin Panel** — Full user management including role assignment and moderation tools

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 11+ |
| UI Framework | Java Swing |
| Database | MySQL 8.x |
| JDBC Driver | MySQL Connector/J (`com.mysql.cj.jdbc.Driver`) |
| Build Tool | Maven / IntelliJ IDEA |

---

## 📁 Project Structure

```
CulturApp/
├── src/
│   ├── connection/
│   │   └── ConnectionFactory.java       # Singleton DB connection manager
│   ├── model/
│   │   ├── User.java
│   │   ├── Event.java
│   │   ├── Product.java
│   │   ├── ForumTopic.java
│   │   ├── ForumPost.java
│   │   └── JournalEntry.java
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── EventDAO.java
│   │   ├── ProductDAO.java
│   │   ├── ForumTopicDAO.java
│   │   ├── ForumPostDAO.java
│   │   └── JournalEntryDAO.java
│   ├── gui/
│   │   ├── LoginFrame.java              # Entry point / login screen
│   │   ├── RegisterFrame.java
│   │   ├── MainFrame.java               # Main window with navigation
│   │   ├── MarketplacePanel.java
│   │   ├── EventsPanel.java
│   │   ├── EventOrganizerPanel.java     # Artist-only event management
│   │   ├── ForumPanel.java
│   │   ├── JournalPanel.java
│   │   ├── ProfilePanel.java
│   │   └── AdminPanel.java
│   └── utils/
│       └── ModernUI.java                # Shared UI components and styling
```

---

## 🗄️ Database Setup

1. Make sure MySQL is running locally on port `3306`.

2. Create the database:

```sql
CREATE DATABASE cultural_app;
```

3. Create the required tables:

```sql
USE cultural_app;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    parola_hash VARCHAR(255) NOT NULL,
    rol ENUM('user', 'artist', 'admin') DEFAULT 'user',
    bio TEXT,
    interese TEXT,
    state ENUM('active', 'banned') DEFAULT 'active',
    data_inregistrarii TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE artists (
    artist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    artist_id INT NOT NULL,
    titlu VARCHAR(200) NOT NULL,
    descriere TEXT,
    pret DECIMAL(10,2) NOT NULL,
    categorie VARCHAR(100),
    imagine VARCHAR(500),
    stoc INT DEFAULT 0,
    data_postarii TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE
);

CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    organizer_id INT NOT NULL,
    titlu VARCHAR(200) NOT NULL,
    descriere TEXT,
    locatie VARCHAR(200),
    data_start DATETIME NOT NULL,
    data_end DATETIME,
    numar_locuri INT DEFAULT 0,
    pret_bilet DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (organizer_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    status ENUM('rezervat', 'anulat') DEFAULT 'rezervat',
    data_rezervarii TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE forum_topics (
    topic_id INT AUTO_INCREMENT PRIMARY KEY,
    titlu VARCHAR(300) NOT NULL,
    categorie VARCHAR(100),
    creat_de INT NOT NULL,
    data_crearii TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creat_de) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE forum_posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    topic_id INT NOT NULL,
    user_id INT NOT NULL,
    continut TEXT NOT NULL,
    data_postarii TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (topic_id) REFERENCES forum_topics(topic_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE journal_entries (
    entry_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    tip_activitate VARCHAR(100),
    titlu VARCHAR(200) NOT NULL,
    descriere TEXT,
    data_experientei DATE,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

4. Update the credentials in `ConnectionFactory.java` if needed:

```java
private static final String DBURL = "jdbc:mysql://localhost:3306/cultural_app";
private static final String USER = "root";
private static final String PASS = "your_password";
```

---

## 🚀 Getting Started

### Prerequisites

- Java 11 or higher
- MySQL 8.x
- MySQL Connector/J JAR (add to classpath)

### Running the App

1. Clone the repository:
```bash
git clone https://github.com/your-username/CulturApp.git
cd CulturApp
```

2. Add the MySQL JDBC driver to your project dependencies.

3. Set up the database as described above.

4. Run the application starting from:
```
src/gui/LoginFrame.java (contains main())
```

---

## 👥 User Roles

| Role | Capabilities |
|---|---|
| **User** | Browse marketplace, attend events, use journal, participate in forum |
| **Artist** | Everything a User can do, plus: list products for sale, create and manage events |
| **Admin** | Full access: manage all users (ban/unban, change roles, delete), moderate forum topics |

> Banned users are blocked from posting on the forum but can still browse the platform.

---

## 🗂️ Forum Categories

- 📚 Literatură (Literature)
- 🎬 Film
- 🎨 Arte Vizuale (Visual Arts)
- 🎭 Teatru (Theatre)
- 🎵 Muzică (Music)
- 🏛️ Cultură Generală (General Culture)

---

## 📝 Notes

- Passwords are stored as plain text in the current implementation. For production use, replace with a hashing algorithm such as BCrypt.
- The `ConnectionFactory` uses a singleton pattern and creates a new connection per query — suitable for a desktop app with low concurrency.
- The `bookEvent` feature in `EventsPanel` is partially implemented (UI confirmation only; full booking persistence is ready via the `bookings` table).

---

## 📄 License

This project is for educational purposes. Feel free to fork and extend it.
