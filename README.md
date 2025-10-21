# 🪶 Lingoor — Full‑Stack Application

**Lingoor** is a full‑stack web application built with **Spring Boot** and **Angular**.  
Users can post and explore word definitions, follow authors, like posts, and view a daily highlighted word chosen by admins. The app demonstrates a modern SPA architecture with JWT authentication, RESTful APIs, pagination & filtering, and a responsive UI.

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
├─ frontend/       ← Angular 17+ app
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

## 💡 Frontend Overview (Angular 17, standalone)

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
