# Instagram-Dummy-backend

A backend clone of Instagram built with Spring Boot, JPA, MySQL, Kafka, and Flyway.  
This project supports core features like user registration, post uploading, likes, follow/unfollow, and feed generation.

---

## 🛠️ Database Setup

> If the project does not automatically create the database upon startup, follow these steps:

### 1. Connect to MySQL

```bash
  mysql -u root -p
```

### 2. Create the Database

```sql
CREATE DATABASE DummyInsta;
```

---

## 📦 Kafka Producer Setup

### 1. Start Kafka using Docker Compose

```bash
  docker-compose up -d
```

### 2. Pull the Kafka Image

```bash
  docker pull apache/kafka:3.8.0@sha256:c9aea96a4813e77e703541b1d8f7d58c9ee05b77353da33684db55c840548791
```

### 3. Get Kafka Container IP

```bash
  docker inspect kafka-server | grep "IPAddress"
```

> Replace the captured IP in `application.properties`:

```properties
 spring.kafka.bootstrap-servers=172.17.0.2:9092
```

---

## 🗃️ Database Migration with Flyway

Run this command to apply database migrations:

```bash
  ./gradlew flywayMigrate
```

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

| Endpoint                            | Method | Description                      |
|-------------------------------------|--------|----------------------------------|
| `/api/posts/upload/{userId}`       | POST   | Upload post via file or URL      |
| `/api/posts/user/{userId}`         | GET    | Get all posts by a specific user |
| `/api/posts/followed`              | GET    | Get feed from followed users     |
| `/api/follow/{targetUserId}`    | POST   | Follow a user                    |
| `/api/follow/{targetUserId}` | DELETE | Unfollow a user                  |
| `/api/follow/counts/{userId}`  | GET    | Get follower/following counts    |

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
- Update `application.properties` for DB and Kafka config as needed.
- Use Postman or any REST client to test the endpoints.

---

## 📄 License

This project is for learning purposes only.
