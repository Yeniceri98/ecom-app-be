# EcomApp

Fullstack e-commerce application built with **Spring Boot** and **React**.  
Provides secure REST APIs for product, cart, order, and payment management.  
Uses **PostgreSQL** as the database and integrates **Stripe** for payment processing.  
Designed as a Dockerized, real-world reference project suitable for demos and interviews.

---

## Requirements

Ensure the following tools are installed on your system:

- Java Development Kit (JDK) 17
- Maven 3.8+
- Docker & Docker Compose
- Git

---

## Technologies Used

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security (JWT authentication)
- Spring Web (REST APIs)
- Spring Data JPA (Hibernate)
- Bean Validation
- MapStruct
- Lombok
- PostgreSQL
- Stripe Java SDK

### Frontend
- React
- Stripe JS

### DevOps
- Docker
- Docker Compose
- Maven

---

## Setup Instructions

### 1) Create environment file

Create a `.env` file in the project root directory.  
**Do NOT commit this file.** Use `.env.example` as a reference.

```env
# PostgreSQL Container
POSTGRES_DB=ecom_app_db_local
POSTGRES_USER=app_user
POSTGRES_PASSWORD=change-me

# Spring Boot App Container
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ecom_app_db_local
SPRING_DATASOURCE_USERNAME=app_user
SPRING_DATASOURCE_PASSWORD=change-me

# JWT
JWT_SECRET=change-me-very-long-random

# Stripe (Backend)
STRIPE_SECRET_KEY=sk_test_change_me
```

### 2) Build & Run
Build Docker images and start all services:
```
docker-compose up --build
```

### 3) Stop Services
Stop all running containers:
```
docker-compose down
```

### 4) Reset Database (Optional)
Remove containers and database volumes:
```
docker-compose down -v
```

### 5) Run Backend as Standalone Docker Image (Optional)
This option runs **only the Spring Boot backend** as a Docker container.  
PostgreSQL must be running separately (local or another container).

#### Build backend image
```bash
docker build -t ecom-app-image:0.0.2 .
```

#### Run container
```bash
docker run -d -p 9090:9090 --name ecom-app-be ecom-app-image:0.0.2
```
