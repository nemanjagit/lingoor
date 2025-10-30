# 🪶 Lingoor - Full‑Stack word definition Application

**Lingoor** is a full‑stack web application built with **Spring Boot** and **Angular**.  
Users can post and explore word definitions, follow authors, like posts, and view a daily highlighted word chosen by admins. The app demonstrates a modern SPA architecture with JWT authentication, RESTful APIs, pagination & filtering, and a responsive UI.
IDEs used: IntelliJ and WebStorm

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 20 (standalone components), SCSS, Bootstrap 5 |
| Backend | Spring Boot 3 (Java 17) |
| Database | MySQL |
| Auth | JWT (JSON Web Token) |
| Reports | JasperReports (PDF export) |

---

## 🏗️ Project Structure

```plaintext
lingoor/
├─ backend/        ← Spring Boot project (REST API under /api)
├─ frontend/       ← Angular 20 app
└─ README.md
```

**Backend:** http://localhost:8080  
**Frontend:** http://localhost:4200

---

## 🌐 Backend Overview

### Architecture (packages)

```plaintext
config/          ← JWT + Spring Security
constants/       ← Contant values, magic strings..
controllers/     ← REST API endpoints
dtos/            ← Request/Response objects
exceptions/      ← Custom exceptions + GlobalExceptionHandler
filters/         ← JwtAuthenticationFilter
mappers/         ← to/from Entity/Response/Request
models/          ← JPA entities
repositories/    ← Spring Data JPA
services/        ← Business logic
```

**Key controllers**
- `AuthController`
- `PostController`
- `FollowController`
- `LikeController`
- `AdminController` (reports + Word of the Day)

**Security**
- Public: `/api/auth/**`, `GET /api/posts/**`
- Admin only: `/api/admin/**`
- All other routes require authentication (JWT)

**JWT claims example**
```json
{
  "sub": "user@example.com",
  "userId": 5,
  "email": "user@example.com",
  "role": "ROLE_USER"
}
```

---

## 🧱 Database Schema (MySQL, schema: `lingoor_db`)

| Table | Columns (main) | Notes |
|---|---|---|
| `users` | `id`, `username`, `email`, `password`, `role_id` | Users have roles `ROLE_USER` or `ROLE_ADMIN` |
| `posts` | `id`, `word`, `definition`, `created_at`, `author_id` | Word entries by users |
| `likes` | `id`, `user_id`, `post_id` | Many‑to‑many via likes |
| `follows` | `id`, `follower_id`, `followed_id` | Following relationships |
| `daily_highlight` | `id`, `post_id`, `date` | Word of the Day history (one per date; new on same date replaces old) |

---

## 📡 REST API Reference

### Auth

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/register` | Register (email, username, password) | Public |
| POST | `/api/login` | Authenticate; returns JWT | Public |

**Login response**
```json
{ "jwt": "<token>" }
```

### Posts

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/posts` | Community feed (search/sort/pagination) | Public |
| GET | `/api/posts/personalized` | Feed from followed authors | JWT |
| POST | `/api/posts` | Create a post (word, definition) | JWT |
| PUT | `/api/posts/{id}` | Update if author | JWT |
| DELETE | `/api/posts/{id}` | Delete if author or admin | JWT |
| GET | `/api/posts/word-of-the-day` | Current Word of the Day | Public |

**Query parameters for feeds**
- `query` — search in word/definition  
- `sort` — `newest` (default) or `oldest`  
- `page`, `size` — pagination (default size 10)

### Likes

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/posts/{id}/likes/toggle` | Like/Unlike | JWT |
| GET | `/api/posts/{id}/likes/count` | Like count | Public |
| GET | `/api/posts/{id}/likes/me` | If current user liked | JWT |

### Follows

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/users/{id}/follow` | Whether current user follows target | JWT |
| POST | `/api/users/{id}/follow` | Toggle follow/unfollow | JWT |

**Follow toggle response**
```json
{ "following": true, "followerId": 3, "followedId": 5 }
```

### Admin

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/admin/word-of-the-day/{postId}` | Set Word of the Day | Admin |
| GET | `/api/admin/reports/most-liked` | PDF: most liked posts | Admin |
| GET | `/api/admin/reports/most-followed` | PDF: most followed users | Admin |
| GET | `/api/admin/reports/word-of-the-day-history` | PDF: WOTD history | Admin |

### FeedPostResponse (example)

```json
{
  "id": 1,
  "word": "serendipity",
  "definition": "The occurrence of happy or beneficial events by chance.",
  "authorId": 2,
  "authorUsername": "admin",
  "likeCount": 5,
  "likedByMe": true,
  "followingAuthor": false
}
```

---

## 💡 Frontend Overview (Angular 20, standalone)

### Folder layout

```plaintext
src/app/
├─ core/
│  ├─ guards/             (AdminGuard, AuthGuard)
│  ├─ interceptors/       (AuthInterceptor)
│  ├─ models/             (Post)
│  ├─ services/           (AuthService, PostsService, HttpErrorHandlerService)
│  └─ header/             (header)
├─ features/
│  ├─ auth/               (login, register)
│  ├─ feed/               (community-feed, personalized-feed, post-card, feed-filters, word-of-the-day, create-post)
│  └─ admin/              (reports)
├─ shared/
│  └─ toast/              (Toast)
│  └─ confirm-dialog/
├─ environments/
└─ app.routes.ts
```

**Key components**
- `Header` — logo left; nav center; user/email + logout right
- `CommunityFeed` / `PersonalizedFeed` — list of posts, search/sort, pagination (“Load more”)
- `PostCard` — like/Unlike, follow/unfollow popup, delete (author/admin), set WOTD (admin)
- `WordOfTheDay` — shows latest highlight or hides if none
- `FeedFilters` — input search + sort select
- `Toast` — lightweight global notifications

**Services**
- `AuthService` — JWT storage/decoding (email/userId/role), reactive `loggedIn$`
- `PostsService` — feeds, likes, WOTD, create post
- `HttpErrorHandlerService` — central error → toast

**Routing**
| Path | Component | Access |
|---|---|---|
| `/feed/community` | CommunityFeed | Public |
| `/feed/personalized` | PersonalizedFeed | Auth |
| `/login` | Login | Public |
| `/register` | Register | Public |
| `/admin/reports` | Reports | Admin |

Guards: `AuthGuard`, `AdminGuard`  
Interceptor: adds `Authorization: Bearer <token>` to API calls

**UI**
- Bootstrap 5 grid + Flexbox, responsive 1920×1080 primary target  
- Palette via SCSS variables; component‑scoped styles  
- Toasts for success/error; no blocking alerts  
- Floating “+” button for Create Post (modal/popup form)

---

## 🗄️ Database Setup (MySQL)

### 1️⃣ Create Empty Database & Tables

> Run this SQL script in **MySQL Workbench** or CLI to create an empty schema (no data).  
> Afterwards, proceed to the “Seed Starter Data” section below.

```sql
/* Create database */
CREATE DATABASE IF NOT EXISTS `lingoor_db`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE `lingoor_db`;

-- Roles table
CREATE TABLE IF NOT EXISTS `roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_roles_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Users table
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `member_since` DATETIME NOT NULL,
  `role_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_email` (`email`),
  UNIQUE KEY `uk_users_username` (`username`),
  KEY `fk_users_role` (`role_id`),
  CONSTRAINT `fk_users_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Posts table
CREATE TABLE IF NOT EXISTS `posts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `word` VARCHAR(255) NOT NULL,
  `definition` VARCHAR(512) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `author_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_posts_user` (`author_id`),
  CONSTRAINT `fk_posts_user` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Likes table (unique user_id, post_id)
CREATE TABLE IF NOT EXISTS `likes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `post_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_likes_user_post` (`user_id`,`post_id`),
  KEY `fk_likes_post` (`post_id`),
  KEY `fk_likes_user` (`user_id`),
  CONSTRAINT `fk_likes_post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_likes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Follows table (unique follower_id, followed_id)
CREATE TABLE IF NOT EXISTS `follows` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `follower_id` BIGINT NOT NULL,
  `followed_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follows_pair` (`follower_id`,`followed_id`),
  KEY `fk_follows_followed` (`followed_id`),
  KEY `fk_follows_follower` (`follower_id`),
  CONSTRAINT `fk_follows_followed` FOREIGN KEY (`followed_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_follows_follower` FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Daily highlight table (one per date)
CREATE TABLE IF NOT EXISTS `daily_highlight` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_daily_highlight_date` (`date`),
  KEY `idx_highlight_date` (`date`),
  KEY `fk_highlight_post` (`post_id`),
  CONSTRAINT `fk_highlight_post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
```
---

### 3️⃣ Seed Starter Data (uses BCrypt hashes)

> Run once after importing the schema. You can change emails/usernames, but keep the `{bcrypt}...` hashes as‑is for valid logins.

    -- Roles
    INSERT INTO roles (id, name) VALUES
      (1, 'ROLE_USER'),
      (2, 'ROLE_ADMIN')
    ON DUPLICATE KEY UPDATE name = VALUES(name);

    -- Users (BCrypt hashes provided)
    INSERT INTO users (id, email, username, password, member_since, role_id) VALUES
      (1, 'admin@gmail.com', 'admin', '{bcrypt}$2a$10$ifrdpNZfSjxrfr9uOw9XXOdUD8xAR5CNTnW9ni37M3i6Hn7L6kn4m', NOW(), 2),
      (2, 'nemanja@gmail.com',  'nemanja',  '{bcrypt}$2a$10$SqraCXepCHMOKZCo6P./cOv5H4DjFV2lUFtybnniTzdbqzFbPvvpK', NOW(), 1)
    ON DUPLICATE KEY UPDATE email = VALUES(email);

    -- Sample posts
    INSERT INTO posts (id, word, definition, created_at, author_id) VALUES
      (1, 'serendipity', 'The occurrence of happy or beneficial events by chance.', NOW(), 2),
      (2, 'ephemeral',   'Lasts for a very short time.', NOW(), 2)
    ON DUPLICATE KEY UPDATE definition = VALUES(definition);

    -- Sample interactions
    INSERT IGNORE INTO likes   (user_id,    post_id) VALUES (2, 1);
    INSERT IGNORE INTO follows (follower_id, followed_id) VALUES (2, 1);

    -- Word of the Day (re-runnable without duplicate-date errors)
    INSERT INTO daily_highlight (post_id, date)
    VALUES (1, CURDATE())
    ON DUPLICATE KEY UPDATE post_id = VALUES(post_id);

**Seeded credentials**

| Role | Email              | Username | Password (BCrypt) |
|---|---|---|---|
| Admin | `admin@gmail.com` | `admin` | `{bcrypt}$2a$10$ifrdpNZfSjxrfr9uOw9XXOdUD8xAR5CNTnW9ni37M3i6Hn7L6kn4m` |
| User  | `nemanjagmail.com`  | `nemanja`  | `{bcrypt}$2a$10$SqraCXepCHMOKZCo6P./cOv5H4DjFV2lUFtybnniTzdbqzFbPvvpK` |

> In production, generate new BCrypt hashes with Spring Security’s `PasswordEncoder`.

---

### 4️⃣ Configure Spring Boot
`backend/src/main/resources/application.properties`

    spring.datasource.url=jdbc:mysql://localhost:3306/lingoor_db
    spring.datasource.username=YOUR_DB_USER
    spring.datasource.password=YOUR_DB_PASSWORD
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

### 5️⃣ Verify
1. Start the backend: `cd backend && ./mvnw spring-boot:run`
2. Open `http://localhost:8080/api/posts` → should list the seeded posts.
3. Log in with:
   - **Admin** → `admin@gmail.com` / 'Admin123!'
   - **User**  → `nemanja@gmail.com`  / 'Nemanja123!'

---

### 📤 (Optional) Exporting the schema from MySQL Workbench
1. Open **Server → Data Export**.
2. Select schema **lingoor_db** → **Dump Structure Only**.
3. Choose **Export to Self‑Contained File** → save as `lingoor_schema.sql` → **Start Export**.

## ⚙️ Environment & Run

### Backend
1) Set environment variable:
```
JWT_SECRET=<your-secret-key>
```
2) Run:
```
cd backend
./mvnw spring-boot:run
```

### Frontend
```
cd frontend
npm install
ng serve
```

**URLs**
- Frontend: `http://localhost:4200`
- Backend API base: `http://localhost:8080/api`

---

## 📊 Admin Reports

- `/api/admin/reports/most-liked` — PDF of most liked posts  
- `/api/admin/reports/most-followed` — PDF of most followed users  
- `/api/admin/reports/word-of-the-day-history` — PDF history of highlights  
Templates: `resources/reports/*.jrxml` (JasperReports)  
Content‑Type: `application/pdf`

---

## 🧪 Example API Flow

```plaintext
POST /api/auth/login                 → returns JWT
GET  /api/posts?page=0&size=10       → community feed
POST /api/posts/1/likes/toggle       → like/unlike
POST /api/users/2/follow             → follow/unfollow user 2
DELETE /api/posts/5                  → delete (author/admin)
POST /api/admin/word-of-the-day/5    → set WOTD (admin)
```

---


## 🧾 Notes

- Default feed sort: `newest`  
- All endpoints return JSON, except admin reports (PDF)  
- CORS enabled for `http://localhost:4200`

---

## 🧩 License

© 2025 - Lingoor (academic demo project)
