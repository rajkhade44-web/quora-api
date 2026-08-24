# 🚀 QueryHub — Production-Grade Q&A Platform

> A production-oriented, Quora-like knowledge-sharing REST API built with **Spring Boot**, designed to demonstrate real-world backend engineering concepts such as **JWT authentication, refresh-token rotation, Redis caching, distributed rate limiting, event-driven notifications, the Outbox Pattern, audit logging, observability, and containerized deployment**.

---

## 📌 Overview

**QueryHub** is a backend platform where users can:

* Create and discover questions
* Organize questions using topics
* Post answers and nested comments
* Like questions, answers, and comments
* Follow other users
* Receive notifications for important activities
* Search and paginate through questions
* Securely authenticate using JWT
* Maintain sessions using rotating refresh tokens

The project goes beyond basic CRUD by addressing problems commonly encountered in **production backend systems**, including:

* Token theft and refresh-token reuse
* Brute-force login attempts
* Database performance and repeated reads
* Distributed API rate limiting
* Reliable delivery of asynchronous side effects
* Auditability of user actions
* Transaction consistency
* Application performance monitoring

---

# 🎯 Why I Built This Project

A basic CRUD-based Q&A application is relatively straightforward.

The goal of QueryHub was to explore what happens when the same application needs to operate more like a **real production backend**.

For example:

> What happens if a user's refresh token is stolen?

> What happens if thousands of users repeatedly request the same question?

> What happens if a notification must be generated after a database transaction?

> What happens if two application instances need to share rate-limit information?

> What happens if an event is successfully stored in the database but notification processing fails?

QueryHub addresses these problems using established backend engineering patterns.

---

# ✨ Key Features

| Feature                   | Implementation                 | Real-World Problem Solved      |
| ------------------------- | ------------------------------ | ------------------------------ |
| 👤 User Management        | Spring Data JPA                | User lifecycle management      |
| ❓ Questions & Topics      | JPA + MySQL                    | Content organization           |
| 💬 Answers & Comments     | Polymorphic relationships      | Nested discussions             |
| ❤️ Likes                  | Polymorphic likes              | Reactions across content types |
| 👥 Following              | Follow relationships           | Social graph                   |
| 🔐 Authentication         | Spring Security + JWT          | Stateless authentication       |
| 🔄 Refresh Token Rotation | Token family + reuse detection | Token theft prevention         |
| 🔒 Account Lockout        | Failed-attempt tracking        | Brute-force protection         |
| ⚡ Redis Caching           | Spring Cache + Redis           | Reduce database load           |
| 🚦 Rate Limiting          | Redis + AOP                    | Abuse prevention               |
| 🔔 Notifications          | Domain events                  | Decouple side effects          |
| 📦 Outbox Pattern         | Persistent event storage       | Reliable event delivery        |
| 📝 Audit Logging          | JPA auditing                   | Traceability                   |
| 📊 Performance Monitoring | AOP + Micrometer               | API observability              |
| 🐳 Docker                 | Docker Compose                 | Reproducible environment       |
| 🧪 Integration Testing    | Testcontainers                 | Production-like testing        |

---

# 🏗️ Architecture

QueryHub follows a **layered modular architecture** with event-driven components.

```text
                         ┌──────────────────────┐
                         │   React / Postman    │
                         │       Client         │
                         └──────────┬───────────┘
                                    │ HTTP
                                    ▼
                    ┌──────────────────────────────────────────┐
                    │       Spring Boot API                    │
                    │                                          │
                    │  ┌────────────────────────┐              │
                    │  │     Security Layer      │             │
                    │  │ JWT + Refresh Tokens    │             │
                    │  └────────────┬───────────┘              │
                    │               │                          │
                    │  ┌────────────▼───────────┐              │
                    │  │      Controllers       │              │
                    │  └────────────┬───────────┘              │
                    │               │                          │
                    │  ┌────────────▼───────────┐              │
                    │  │       Services         │              │
                    │  │ Business Logic         │              │
                    │  └──────┬─────────┬────────┘             │
                    │         │         │                      │
                    │         │         └────────┐             │
                    │         ▼                  ▼             │
                    │  ┌─────────────┐     ┌─────────────┐     │
                    │  │ Repository  │     │    AOP      │     │
                    │  │    Layer    │     │ Monitoring  │     │
                    │  └──────┬──────┘     │ Rate Limit  │     │
                    │         │            └─────────────┘     │
                    │         ▼                                │
                    │  ┌──────────────────────┐                │
                    │  │   Domain Events      │                │
                    │  └──────────┬───────────┘                │
                    │             │                            │
                    │             ▼                            │
                    │  ┌──────────────────────┐                │
                    │  │ Notification Service │                │
                    │  └──────────────────────┘                │
                    └─────────────┬────────────────────────────┘
                                  │
              ┌───────────────────┼───────────────────┐
              ▼                   ▼                   ▼
       ┌────────────┐      ┌────────────┐      ┌────────────┐
       │   MySQL    │      │   Redis    │      │   Outbox   │
       │ Primary DB │      │ Cache / RL  │      │   Events   │
       └────────────┘      └────────────┘      └────────────┘
```

---

# 🧱 Architecture Layers

### Controller Layer

Responsible for:

* HTTP requests/responses
* Request validation
* Authentication context
* API endpoint definitions

Controllers do **not** contain business logic.

### Service Layer

Contains application/business logic such as:

* Creating questions
* Validating ownership
* Processing likes
* Following users
* Authentication
* Refresh-token rotation
* Publishing domain events

### Repository Layer

Uses **Spring Data JPA** to interact with MySQL.

Responsibilities include:

* CRUD operations
* Custom queries
* Pagination
* Search
* Entity relationships

### Security Layer

Handles:

* JWT authentication
* Authorization
* Refresh tokens
* Account lockout
* Password encoding
* Security filters

### AOP Layer

Cross-cutting concerns are implemented using Spring AOP.

Examples:

```text
@LogExecutionTime
@RateLimited
@Retryable
```

This keeps concerns such as monitoring and rate limiting outside core business logic.

---

# 🗄️ Database Design

## Core Entities

```text
User
 │
 ├── Question
 │      │
 │      ├── Topic
 │      ├── Answer
 │      │      └── Comment
 │      │
 │      └── Like
 │
 ├── Follow
 │
 ├── Notification
 │
 └── Audit Log
```

---

## Entity Relationships

```text
                    ┌─────────────┐
                    │    User     │
                    └──────┬──────┘
                           │
             ┌─────────────┼──────────────┐
             │             │              │
             ▼             ▼              ▼
        ┌─────────┐   ┌──────────┐   ┌────────────┐
        │Question │   │  Follow  │   │Notification│
        └────┬────┘   └──────────┘   └────────────┘
             │
       ┌─────┼─────────────┐
       │     │             │
       ▼     ▼             ▼
    Topic   Answer        Like
              │
              ▼
           Comment
              │
              ▼
             Like
```

### Important Relationships

**User → Question**

```text
One User → Many Questions
```

**Question ↔ Topic**

```text
Many Questions ↔ Many Topics
```

Implemented using a join table.

**Question → Answer**

```text
One Question → Many Answers
```

**Answer → Comment**

```text
One Answer → Many Comments
```

**Comment → Comment**

Comments can reference another comment, enabling nested discussions.

**Like**

Likes use a polymorphic target model supporting:

```text
Question
Answer
Comment
```

---

# 🔐 Authentication & Security

QueryHub uses:

```text
Spring Security
       +
JWT Access Token
       +
Rotating Refresh Token
       +
BCrypt Password Hashing
```

---

## Authentication Flow

```text
             Login
               │
               ▼
       Validate Credentials
               │
        ┌──────┴──────┐
        │             │
     Success        Failure
        │             │
        ▼             ▼
 Generate Tokens   Increment
        │          Failed Count
        │             │
        │       5 failures?
        │             │
        │             ▼
        │       Lock 15 minutes
        │
        ▼
Access Token + Refresh Token
```

---

## Access Token

The access token is returned to the client and sent with protected API requests:

```http
Authorization: Bearer <access-token>
```

---

## Refresh Token Security

Refresh tokens are stored in an:

```text
HttpOnly
Secure
Cookie
```

This prevents JavaScript from directly accessing the refresh token.

---

# 🔄 Refresh Token Rotation

Instead of reusing the same refresh token indefinitely, QueryHub rotates the refresh token whenever it is used.

```text
Refresh Token A
      │
      ▼
Refresh Request
      │
      ▼
Validate A
      │
      ▼
Invalidate A
      │
      ▼
Generate Token B
```

The next refresh uses:

```text
Token B → Token C
```

---

## 🛡️ Refresh Token Reuse Detection

A stolen refresh token should not remain usable forever.

QueryHub tracks the token family.

Example:

```text
Token A
   │
   ▼
Token B
   │
   ▼
Token C
```

If an attacker attempts to reuse **Token A** after it has already been rotated:

```text
Reused Token A detected
          │
          ▼
Compromise suspected
          │
          ▼
Revoke token family
          │
          ▼
User must authenticate again
```

This provides protection against refresh-token theft and replay.

---

# 🔒 Account Lockout

To protect against brute-force login attacks:

```text
Failed Login
     │
     ▼
Increment Counter
     │
     ▼
5 Failed Attempts?
     │
    YES
     │
     ▼
Lock Account
for 15 Minutes
```

After the cooldown period, the account becomes available again.

---

# ⚡ Redis Caching

Redis is used to cache frequently requested data.

Typical flow:

```text
Client
  │
  ▼
API
  │
  ▼
Check Redis
  │
  ├──── Cache Hit ────► Return Data
  │
  └──── Cache Miss
           │
           ▼
        MySQL
           │
           ▼
      Store in Redis
           │
           ▼
       Return Data
```

---

## Why Redis?

Without caching:

```text
Request
   ↓
Application
   ↓
MySQL
   ↓
Response
```

With Redis:

```text
Request
   ↓
Application
   ↓
Redis
   ↓
Response
```

This reduces database reads and improves response time for hot endpoints.

---

# 📊 Performance Improvement

Measured API latency:

| Endpoint                | Without Cache | With Redis | Improvement |
| ----------------------- | ------------: | ---------: | ----------: |
| `GET /questions/search` |        130 ms |      12 ms |        ~90% |
| `GET /users/{id}`       |         45 ms |       8 ms |        ~82% |
| `GET /questions/{id}`   |         50 ms |       6 ms |        ~88% |

> Performance numbers are environment-dependent and represent the measurements observed during project testing.

---

# 🚦 Rate Limiting

Rate limiting protects APIs from:

* Brute-force attacks
* Accidental request storms
* API abuse
* Excessive content creation

Current limits:

| API Category     |              Limit |
| ---------------- | -----------------: |
| Login            |  5 requests/minute |
| Content Creation | 10 requests/minute |

Redis provides shared counters, allowing rate limiting to work even when multiple application instances are running.

```text
Application Instance 1 ─┐
                         │
Application Instance 2 ─┼──► Redis Counter
                         │
Application Instance 3 ─┘
```

---

# 🔔 Event-Driven Notifications

Notifications are generated using domain events.

Example:

```text
User A answers User B's question
              │
              ▼
        Answer Created
              │
              ▼
       Domain Event
              │
              ▼
    Notification Listener
              │
              ▼
       Create Notification
```

Supported notification scenarios include:

* New answer
* New comment
* New like
* New follower

---

# 🔄 Transaction-Aware Events

Notifications should not be generated if the original transaction fails.

QueryHub uses:

```java
@TransactionalEventListener(
    phase = TransactionPhase.AFTER_COMMIT
)
```

Conceptually:

```text
Database Transaction
        │
        ├── Success ──► Publish/Process Event
        │
        └── Rollback ──► No Notification
```

This prevents situations such as:

> "You received a notification for an answer that was never successfully saved."

---

# 📦 Outbox Pattern

For reliable event delivery, QueryHub uses the **Outbox Pattern**.

The core problem is the **dual-write problem**.

Imagine:

```text
Save Answer ─────► MySQL
                       │
                       X
                Event Publish Failed
```

The answer exists, but the notification event is lost.

---

## Outbox Solution

Instead:

```text
┌─────────────────────────────┐
│       Single DB Transaction │
│                             │
│  Save Answer                │
│       +                     │
│  Save Outbox Event          │
│                             │
└──────────────┬──────────────┘
               │
               ▼
         Outbox Table
               │
               ▼
        Event Processor
               │
               ▼
       Notification System
```

Because the business data and event are stored in the same transaction, the event is not lost simply because downstream processing temporarily fails.

### Delivery Semantics

The design targets:

```text
At-Least-Once Delivery
```

Therefore, event consumers should be designed to tolerate duplicate delivery where necessary.

---

# 📝 Audit Logging

Important user actions can be tracked for traceability.

Examples:

```text
LOGIN
CREATE_QUESTION
UPDATE_QUESTION
CREATE_ANSWER
CREATE_COMMENT
LIKE_CONTENT
FOLLOW_USER
LOGOUT
```

This provides an audit trail that can be useful for:

* Debugging
* Security investigations
* User activity tracking
* Operational analysis

---

# 📈 Observability & Performance Monitoring

QueryHub uses:

```text
Spring Boot Actuator
        +
Micrometer
        +
Prometheus
```

Performance-sensitive methods can use:

```java
@LogExecutionTime
```

Conceptually:

```text
Request
   │
   ▼
Start Timer
   │
   ▼
Execute Method
   │
   ▼
Stop Timer
   │
   ▼
Record Metric
```

This makes it possible to identify slow APIs and monitor application behavior.

---

# 🛠️ Technology Stack

| Layer               | Technology                  |
| ------------------- | --------------------------- |
| Language            | Java 17+                    |
| Framework           | Spring Boot 3.2.x           |
| Security            | Spring Security             |
| Authentication      | JWT                         |
| Database            | MySQL 8.0                   |
| ORM                 | Spring Data JPA / Hibernate |
| Cache               | Redis 7.2                   |
| Database Migration  | Flyway                      |
| AOP                 | Spring AOP                  |
| Monitoring          | Micrometer + Prometheus     |
| Testing             | JUnit 5                     |
| Integration Testing | Testcontainers              |
| Containerization    | Docker                      |
| Local Orchestration | Docker Compose              |
| Build Tool          | Maven                       |
| API Documentation   | Swagger / OpenAPI           |

---

# 📁 Project Structure

```text
src/main/java/com/quora/
│
├── common/
│   ├── annotation/
│   │   ├── LogExecutionTime
│   │   ├── RateLimited
│   │   └── Retryable
│   │
│   ├── aspect/
│   ├── config/
│   ├── dto/
│   ├── entity/
│   ├── enums/
│   └── exception/
│
├── auth/
│   ├── controller/
│   ├── dto/
│   └── service/
│
├── user/
├── question/
├── answer/
├── comment/
├── like/
├── follow/
├── notification/
├── outbox/
├── security/
└── audit/
```

The application is organized by **business modules**, making the codebase easier to maintain and extend than a purely technical package structure.

---

# 🚀 Getting Started

## Prerequisites

Install:

* Java 17+
* Maven
* Docker
* Docker Compose
* Git

Verify:

```bash
java -version
mvn -version
docker --version
docker compose version
```

---

# 1️⃣ Clone the Repository

```bash
git clone <your-repository-url>

cd quora-api
```

---

# 2️⃣ Build the Application

```bash
mvn clean package -DskipTests
```

---

# 3️⃣ Start Infrastructure

```bash
docker compose up -d
```

This starts:

```text
MySQL  → localhost:3306
Redis  → localhost:6379
```

Check running containers:

```bash
docker ps
```

---

# 4️⃣ Run the Application

```bash
java -jar target/quora-api-0.0.1.jar \
  --spring.profiles.active=dev
```

The application will start on:

```text
http://localhost:8080
```

---

# 5️⃣ Open Swagger

```text
http://localhost:8080/swagger-ui.html
```

Swagger can be used to explore and test the REST APIs.

---

# 🔐 API Authentication Example

## Register

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@doe.com",
    "password": "Password123"
  }'
```

---

## Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@doe.com",
    "password": "Password123"
  }'
```

The response contains:

```text
Access Token
```

while the refresh token is stored in an:

```text
HttpOnly Cookie
```

---

## Access Protected API

```bash
curl -X GET http://localhost:8080/me \
  -H "Authorization: Bearer <accessToken>"
```

---

## Refresh Access Token

```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H "Cookie: refresh_token=<refreshToken>"
```

---

## Logout

```bash
curl -X POST http://localhost:8080/auth/logout \
  -H "Cookie: refresh_token=<refreshToken>"
```

---

# 📡 Core API Examples

## Create Question

```bash
curl -X POST http://localhost:8080/questions \
  -H "Authorization: Bearer <accessToken>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "What is Spring Boot?",
    "body": "Explain Spring Boot with examples.",
    "topicTags": ["java", "spring"]
  }'
```

---

## Search Questions

```bash
curl "http://localhost:8080/questions/search?text=spring&tag=java&page=0&size=10"
```

Supports:

* Text search
* Topic filtering
* Pagination

---

## Answer a Question

```bash
curl -X POST \
  http://localhost:8080/questions/{questionId}/answers \
  -H "Authorization: Bearer <accessToken>" \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Spring Boot simplifies Spring application development..."
  }'
```

---

## Comment on an Answer

```bash
curl -X POST \
  http://localhost:8080/answers/{answerId}/comments \
  -H "Authorization: Bearer <accessToken>" \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Great explanation!"
  }'
```

---

## Like a Question

```bash
curl -X POST \
  http://localhost:8080/questions/{questionId}/likes \
  -H "Authorization: Bearer <accessToken>"
```

---

## Follow a User

```bash
curl -X POST \
  http://localhost:8080/users/{userId}/follow/{targetUserId} \
  -H "Authorization: Bearer <accessToken>"
```

---

## Get Notifications

```bash
curl -X GET \
  http://localhost:8080/notifications \
  -H "Authorization: Bearer <accessToken>"
```

---

## Get Unread Notification Count

```bash
curl -X GET \
  http://localhost:8080/notifications/unread-count \
  -H "Authorization: Bearer <accessToken>"
```

---

# 🧪 Testing

## Unit Tests

Run the complete unit-test suite:

```bash
mvn test
```

---

## Integration Tests

Integration testing uses **Testcontainers** to run dependencies in containers rather than relying entirely on locally installed services.

```bash
mvn verify -Dtest=*IntegrationTest
```

Example environment:

```text
Test
 │
 ├── Spring Boot
 │
 ├── MySQL Container
 │
 └── Redis Container
```

This provides a testing environment closer to the actual application infrastructure.

### Coverage Target

```text
Service Layer: 60–70%
```

---

# 🐳 Docker Commands

### Start Services

```bash
docker compose up -d
```

### Stop Services

```bash
docker compose down
```

### View Logs

```bash
docker compose logs -f
```

### Check Containers

```bash
docker ps
```

---

# 🧠 Important Design Decisions

| Design Decision               | Reason                                            |
| ----------------------------- | ------------------------------------------------- |
| **Layered Architecture**      | Separation of responsibilities                    |
| **Modular Package Structure** | Better maintainability and feature ownership      |
| **JWT Access Tokens**         | Stateless API authentication                      |
| **Refresh Token Rotation**    | Limits damage from stolen tokens                  |
| **Reuse Detection**           | Detects refresh-token replay                      |
| **HttpOnly Refresh Cookie**   | Reduces JavaScript-based token exposure           |
| **BCrypt**                    | Secure password hashing                           |
| **Account Lockout**           | Protects against brute-force login                |
| **Redis Cache**               | Reduces repeated database reads                   |
| **Redis Rate Limiting**       | Shared limits across application instances        |
| **Domain Events**             | Decouples business actions from side effects      |
| **Transactional Events**      | Prevents notifications for rolled-back operations |
| **Outbox Pattern**            | Reliable event persistence                        |
| **At-Least-Once Delivery**    | Prevents silent event loss                        |
| **Flyway**                    | Version-controlled database schema                |
| **Testcontainers**            | Production-like integration testing               |
| **AOP Monitoring**            | Separates monitoring from business logic          |

---

# 🔥 Real-World Engineering Problems Solved

## Problem 1 — Database Gets Hit Repeatedly

### Before

```text
1000 requests
      │
      ▼
1000 database queries
```

### Solution

```text
1000 requests
      │
      ▼
Redis Cache
      │
      ├── Cache Hit → Return
      │
      └── Cache Miss → MySQL
```

Result:

**Lower database load + faster response time**

---

## Problem 2 — Brute-Force Login

### Problem

An attacker repeatedly tries passwords.

### Solution

```text
5 failed attempts
       ↓
Account locked
       ↓
15 minute cooldown
```

---

## Problem 3 — Stolen Refresh Token

### Problem

An attacker obtains an old refresh token.

### Solution

```text
Refresh Token Rotation
        +
Reuse Detection
        +
Token Family Revocation
```

This limits the usefulness of stolen tokens.

---

## Problem 4 — Notification Lost After Database Success

### Problem

```text
Save Answer → SUCCESS
Publish Event → FAILURE
```

The answer exists, but the notification event is lost.

### Solution

```text
Save Answer
    +
Save Outbox Event
        │
        ▼
Same DB Transaction
        │
        ▼
Outbox Processor
```

---

## Problem 5 — Rate Limiting Multiple Instances

With a local in-memory counter:

```text
Instance 1 → Counter A
Instance 2 → Counter B
```

A user could bypass limits by hitting another instance.

Redis provides:

```text
Instance 1 ─┐
Instance 2 ─┼──► Shared Redis Counter
Instance 3 ─┘
```

---

# 📈 Scalability Considerations

The architecture can be extended horizontally.

```text
                    Load Balancer
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
      App Instance   App Instance   App Instance
          │              │              │
          └──────────────┼──────────────┘
                         │
              ┌──────────┴──────────┐
              ▼                     ▼
            Redis                 MySQL
```

Important design choices supporting horizontal scaling:

* Stateless JWT authentication
* Shared Redis cache
* Redis-based rate limiting
* Externalized infrastructure
* Transactional persistence
* Event-driven side effects

---

# 🗺️ Development Roadmap

```text
Phase 0  ── Foundation
   │
   ├── Spring Boot
   ├── MySQL
   ├── Flyway
   ├── Exception Handling
   └── Swagger
   │
Phase 1  ── User Management
   │
Phase 2  ── Questions & Topics
   │
Phase 3  ── Answers & Comments
   │
Phase 4  ── Likes & Follows
   │
Phase 5  ── Authentication
   │
Phase 5.5 ─ Redis Foundation
   │
Phase 6A ── Caching & Performance
   │
Phase 6B ── Rate Limiting
   │
Phase 6C ── Notifications
   │
Phase 6D ── Outbox Pattern
   │
Phase 7  ── Testing
   │
Phase 8  ── DevOps & Documentation
   │
Phase 9  ── AI Duplicate Detection
```

---

# 🚧 Future Improvements

Potential next steps include:

* [ ] Production deployment
* [ ] GitHub Actions CI/CD pipeline
* [ ] Centralized logging
* [ ] Distributed tracing
* [ ] Kafka-based event streaming
* [ ] Elasticsearch/OpenSearch for advanced search
* [ ] Object storage for user media
* [ ] WebSocket/SSE real-time notifications
* [ ] AI-powered duplicate-question detection
* [ ] Recommendation/feed ranking
* [ ] Read replicas for high-volume queries
* [ ] API versioning
* [ ] Advanced moderation and abuse detection

---

# 🎤 Interview Talking Points

This project demonstrates practical knowledge of:

### Backend Development

```text
Java
Spring Boot
REST APIs
Spring Data JPA
Hibernate
MySQL
```

### Security

```text
Spring Security
JWT
BCrypt
Refresh Token Rotation
Token Reuse Detection
HttpOnly Cookies
Account Lockout
```

### Performance

```text
Redis
Caching
Rate Limiting
AOP
Latency Measurement
Prometheus
Micrometer
```

### Distributed Systems

```text
Domain Events
Transactional Events
Outbox Pattern
At-Least-Once Delivery
Idempotency Considerations
```

### DevOps

```text
Docker
Docker Compose
CI/CD
Testcontainers
```

---

# 🏆 Elevator Pitch

> **"QueryHub is a production-oriented Quora-like Q&A backend built with Spring Boot. I implemented the core Q&A functionality along with JWT authentication, rotating refresh tokens with reuse detection, Redis caching and rate limiting, event-driven notifications, audit logging, and the Outbox Pattern for reliable event delivery. I also added performance monitoring with Micrometer and Prometheus, integration testing with Testcontainers, and Docker-based infrastructure. Redis caching reduced latency on selected hot endpoints by roughly 82–90% in my test environment."**

---

# 📊 Project at a Glance

| Category          | Implementation                   |
| ----------------- | -------------------------------- |
| Architecture      | Layered + Modular + Event-Driven |
| Backend           | Spring Boot                      |
| Language          | Java                             |
| Database          | MySQL                            |
| Cache             | Redis                            |
| Security          | Spring Security + JWT            |
| ORM               | Hibernate / JPA                  |
| Migration         | Flyway                           |
| Events            | Domain Events                    |
| Reliability       | Outbox Pattern                   |
| Rate Limiting     | Redis                            |
| Monitoring        | Micrometer + Prometheus          |
| Testing           | JUnit + Testcontainers           |
| Infrastructure    | Docker Compose                   |
| API Documentation | Swagger / OpenAPI                |

---

# 📄 License

This project is primarily intended as a **backend engineering and interview demonstration project**.

---

<div align="center">

### Built with ☕ Java + Spring Boot + MySQL + Redis

**QueryHub — Building a production-oriented Q&A backend**

</div>
