<div align="center">

# 🔐 Spring Security RBAC Demo

**Role-Based Access Control with Spring Boot 4, Spring Security, JPA and MySQL**

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-7-6DB33F?logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)

</div>

---

## 📖 Overview

This project demonstrates how to secure a Spring Boot application using **database-backed authentication** and **role-based authorization**. Users and their roles are stored in MySQL, passwords are hashed with **BCrypt**, and endpoints are protected both at the URL level and at the method level.

## ✨ Features

- Database-backed authentication through a custom `UserDetailsService`
- Password hashing with `BCryptPasswordEncoder`
- Role-based URL authorization (`ADMIN`, `USER`, `VENDOR`)
- Method-level security using `@EnableMethodSecurity` and `@PreAuthorize`
- Form-based login
- Configurable public URLs via `application.properties`
- Many-to-many mapping between users and roles (`users`, `roles`, `user_roles`)
- Auto-generated join timestamp for users
- File and console logging

## 🧰 Tech Stack

| Category      | Technology                              |
|---------------|-----------------------------------------|
| Language      | Java                                    |
| Framework     | Spring Boot 4 (Web MVC, Data JPA)       |
| Security      | Spring Security                         |
| Database      | MySQL                                   |
| ORM           | Hibernate / Spring Data JPA             |
| Boilerplate   | Lombok                                  |
| Build Tool    | Maven (Maven Wrapper included)          |

## 🏗️ Project Structure

```
learn-security
├── src
│   ├── main
│   │   ├── java/com/training
│   │   │   ├── LearnSecurityApplication.java
│   │   │   ├── config        # SecurityConfig (filter chain, encoder, provider)
│   │   │   ├── constants     # RoleName enum
│   │   │   ├── controller    # MainController, AuthController
│   │   │   ├── entity        # GenericUser, Role
│   │   │   ├── repository    # GenericUserRepository
│   │   │   ├── security      # CustomerUserDetail (UserDetails adapter)
│   │   │   └── service       # GenericUserService + impl (UserDetailsService)
│   │   └── resources
│   │       └── application.properties
│   └── test
├── pom.xml
└── README.md
```

## 🔑 Access Rules

| Endpoint                   | Access                                   | Description              |
|----------------------------|------------------------------------------|--------------------------|
| `GET /api/v1/`             | Public                                   | Health check             |
| `GET /api/v1/auth/login`   | Public                                   | Login placeholder        |
| `GET /api/v1/auth/register`| Public                                   | Register placeholder     |
| `GET /api/v1/admin/greet`  | Role `ADMIN`                             | Admin greeting           |
| `GET /api/v1/user/greet`   | Role `USER`                              | User greeting            |
| `GET /api/v1/vendor/greet` | Role `VENDOR`                            | Vendor greeting          |
| `GET /api/v1/see-admins`   | Role `ADMIN` **and** authority `SEE_ADMINS` | Method-security demo (returns `403` until fine-grained authorities are implemented) |

All other endpoints require authentication. Unauthenticated requests are redirected to the default Spring Security login page at `/login`.

## ✅ Prerequisites

- **JDK 17** or higher
- **MySQL** running on port `3306`
- **Git**
- Maven (optional, the included `mvnw` wrapper can be used)

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/naveenrajput8823/spring-security-rbac-demo.git
cd spring-security-rbac-demo
```

### 2. Configure the database

Set your MySQL credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/security6?createDatabaseIfNotExist=true
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>
```

> The database `security6` and all tables are created automatically on the first run.

### 3. Run the application

**Windows**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS**
```bash
./mvnw spring-boot:run
```

The application starts on **http://localhost:9090**

### 4. Create roles and test users

Registration is not implemented yet, so insert roles and sample users manually after the first run.
The password hash below is the BCrypt hash of `1234`.

```sql
USE security6;

INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_VENDOR');

INSERT INTO users (email, password, joined_at) VALUES
  ('admin@example.com',  '$2a$10$xEUSk750sCaV2za7ETDO7eq6D3hH5zSRxLhuKs3C5w5HYrl2fNGe.', NOW()),
  ('user@example.com',   '$2a$10$xEUSk750sCaV2za7ETDO7eq6D3hH5zSRxLhuKs3C5w5HYrl2fNGe.', NOW()),
  ('vendor@example.com', '$2a$10$xEUSk750sCaV2za7ETDO7eq6D3hH5zSRxLhuKs3C5w5HYrl2fNGe.', NOW());

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE (u.email = 'admin@example.com'  AND r.name = 'ROLE_ADMIN')
   OR (u.email = 'user@example.com'   AND r.name = 'ROLE_USER')
   OR (u.email = 'vendor@example.com' AND r.name = 'ROLE_VENDOR');
```

### 5. Try it out

1. Open `http://localhost:9090/api/v1/` → accessible without login.
2. Open `http://localhost:9090/api/v1/admin/greet` → redirected to the login page.
3. Log in with `admin@example.com` / `1234` → you see **Hello World By Admin**.
4. Log in as `user@example.com` and open `/api/v1/admin/greet` → `403 Forbidden`.

## 🗄️ Database Schema

**`users`**

| Column      | Type         | Notes                       |
|-------------|--------------|-----------------------------|
| `id`        | INT          | Primary key, auto-increment |
| `email`     | VARCHAR(255) | Used as the username        |
| `password`  | VARCHAR(255) | BCrypt hash                 |
| `joined_at` | DATETIME     | Auto-generated              |

**`roles`**

| Column | Type         | Notes                                   |
|--------|--------------|-----------------------------------------|
| `id`   | INT          | Primary key, auto-increment             |
| `name` | VARCHAR(255) | `ROLE_ADMIN`, `ROLE_USER`, `ROLE_VENDOR` |

**`user_roles`** (join table)

| Column    | Type | Notes                |
|-----------|------|----------------------|
| `user_id` | INT  | Foreign key → users  |
| `role_id` | INT  | Foreign key → roles  |

## 🧪 Running Tests

```bash
./mvnw test
```

## 🛣️ Roadmap

- [ ] User registration and login REST APIs
- [ ] Automatic role seeding on startup
- [ ] Fine-grained authorities (e.g. `SEE_ADMINS`) loaded from the database
- [ ] JWT-based stateless authentication
- [ ] Global exception handling
- [ ] Externalized configuration using environment variables
- [ ] Unit and integration tests for security rules
- [ ] Docker and Docker Compose support

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

## 👨‍💻 Author

**Naveen Rajput**

- GitHub: [@naveenrajput8823](https://github.com/naveenrajput8823)

---

<div align="center">

⭐ If you found this project useful, consider giving it a star!

</div>
