# Instagram-Dummy-backend

A backend clone of Instagram built with Spring Boot, JPA, MySQL, Kafka, and Flyway.  
This project supports core features like user registration, post uploading, likes, follow/unfollow, and feed generation.

---

## 🛠️ Database Setup

If the project does not automatically create the database upon startup, follow these steps:

### 1. Connect to MySQL

```bash
mysql -u root -p
```

### 2. Create the Database

```sql
CREATE DATABASE DummyInsta;
```

---

## 📦 Kafka Setup

### 1. Start Kafka using Docker Compose

```bash
docker-compose up -d
```

### 2. Pull the Kafka Image (if needed)

```bash
docker pull apache/kafka:3.8.0@sha256:c9aea96a4813e77e703541b1d8f7d58c9ee05b77353da33684db55c840548791
```

### 3. Get Kafka Container IP

```bash
docker inspect kafka-server | grep "IPAddress"
```

> Replace the captured IP in your `application.yml` or `application.properties`:

```yaml
spring:
  kafka:
    bootstrap-servers: 172.17.0.2:9092
```

---

## 🗃️ Database Migration with Flyway

Run this command to apply database migrations:

```bash
  ./gradlew flywayMigrate
```

- Locations: `classpath:db/migration`
- Baseline on Migrate: Enabled to handle existing databases

For multi-tenancy support (e.g., adding tenant table and tenant_id columns), refer to migration scripts like `V1__add_multi_tenancy.sql`.

Ensure your database credentials are set correctly before running migrations.

---

## 🗃️ Database Schema

For multi-tenancy support (e.g., adding tenant table and tenant_id columns), refer to migration scripts like `V1__add_multi_tenancy.sql`.

---

## 🔒 Environment Variables for Secure Configuration

To secure sensitive information like database credentials, server ports, and connection pool settings, use environment variables. These are referenced as placeholders in `application.yml` or profile-specific files (e.g., `application-default.yml`). This prevents hardcoding secrets in your codebase.

**Example Placeholders in YAML**

In `application-default.yml` (or your active profile):

```yaml
server:
  port: ${SERVER_PORT:8081}

spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/DummyInsta?serverTimezone=UTC}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD}
    driver-class-name: ${DB_DRIVER:com.mysql.cj.jdbc.Driver}
  hikari:
    maximum-pool-size: ${HIKARI_MAX_POOL_SIZE:10}
    minimum-idle: ${HIKARI_MIN_IDLE:5}
    idle-timeout: ${HIKARI_IDLE_TIMEOUT:30000}
    max-lifetime: ${HIKARI_MAX_LIFETIME:120000}
    connection-timeout: ${HIKARI_CONNECTION_TIMEOUT:20000}
```

### Setting Environment Variables

- **Local Development:** Set variables in your shell or IDE run configuration:

```bash
export SERVER_PORT=8081
export DB_URL="jdbc:mysql://localhost:3306/DummyInsta?serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="your-secure-password"
# Add HIKARI_* vars as needed
  ./gradlew bootRun
```

- **Docker/Deployment:** Use `ENV` in Dockerfile or `environment` in `docker-compose.yml`.

- **Security Note:** Never commit actual values to version control. Rotate secrets regularly and use tools like AWS Secrets Manager for production.

#### Adding Environment Variables in IntelliJ for Local Development

To securely manage sensitive configurations like database credentials without hardcoding them, set environment variables in your IntelliJ run configuration. This resolves placeholders in `application.yml` or profile files at runtime.

Steps:
1. Go to **Run > Edit Configurations...** and select your app's run configuration.
2. In the **Environment variables** field, paste the following semicolon-separated string (update sensitive values like `DB_PASSWORD`):

```
SERVER_PORT=8081;DB_URL=jdbc:mysql://localhost:3306/DummyInsta?serverTimezone=UTC;DB_USERNAME=root;DB_PASSWORD=WelCome!23$5;DB_DRIVER=com.mysql.cj.jdbc.Driver;HIKARI_MAX_POOL_SIZE=10;HIKARI_MIN_IDLE=5;HIKARI_IDLE_TIMEOUT=30000;HIKARI_MAX_LIFETIME=120000;HIKARI_CONNECTION_TIMEOUT=20000
```

Click **Apply** and **OK**. IntelliJ will parse this into individual variables.

Run your app and verify in the logs that values are resolved correctly.

This keeps secrets local and out of version control. For production, use deployment tools like Docker or cloud secrets managers.

---

## 🧽 Format Code with Spotless

To automatically format your Java code:

```bash
./gradlew spotlessApply
```

---

## 🚀 Run the Application

You can run the application using your IDE or with Gradle:

```bash
./gradlew bootRun
```

---

## 📬 API Endpoints Overview

| Endpoint                          | Method | Description                      |
|------------------------------------|--------|----------------------------------|
| `/api/posts/upload/{userId}`       | POST   | Upload post via file or URL      |
| `/api/posts/user/{userId}`         | GET    | Get all posts by a specific user |
| `/api/posts/followed`              | GET    | Get feed from followed users     |
| `/api/follow/{targetUserId}`       | POST   | Follow a user                    |
| `/api/follow/{targetUserId}`       | DELETE | Unfollow a user                  |
| `/api/follow/counts/{userId}`      | GET    | Get follower/following counts    |

---

## 🧱 Tech Stack

- **Spring Boot**
- **Spring Security (JWT)**
- **MySQL**
- **JPA/Hibernate**
- **Kafka**
- **Flyway**
- **Gradle**
- **Docker (for Kafka setup)**

---

## 📌 Notes

- Make sure your MySQL server is running.
- Update `application.yml` for DB and Kafka config as needed.
- Use Postman or any REST



## keycloak docker image :
```bash
    docker run -d -p 8080:8080 \
    -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
    -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
    --name keycloak \
    quay.io/keycloak/keycloak:26.3.2 start-dev
```