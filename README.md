# Order System - Microservices Project

## 📌 Overview

This project is a microservices-based order processing system using **Java 21, Spring Boot, and Docker**. It consists of four independent services that communicate via **RabbitMQ**:

- **Order Service** - Manages customer orders.
- **Notification Service** - Sends notifications based on order status.
- **Payment Service** - Handles payment transactions.
- **Inventory Service** - Manages product stock levels and availability.

Each service uses an **H2 in-memory database** for simplicity.

---

## 🏠 Project Structure

```
order-system/
│── order-service/           # Handles order processing
│── notification-service/    # Sends notifications
│── payment-service/         # Manages payments
│── inventory-service/       # Manages product inventory
│── docker-compose.yml       # Defines service containers
│── README.md                # Project documentation
```

Each service is an independent Spring Boot application that can be run separately.

---

## 🚀 Tech Stack

- **Java 21**
- **Spring Boot 3.2**
- **RabbitMQ**
- **Docker**
- **H2 Database**
- **Lombok**
- **Mapstruct**

---

## ⚙️ Setup & Run

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/order-system.git
cd order-system
```

### 2️⃣ Run Each Service Individually

Go to each service directory (`order-service`, `notification-service`, `payment-service`, `inventory-service`) and start the Spring Boot application:

```bash
mvn spring-boot:run
```

or, using **IntelliJ IDEA**, run the main class of each service.

### 3️⃣ Run with Docker

If you have **Docker** installed, you can run all services together using:

```bash
docker-compose up --build
```

---

## 🛠 API Endpoints

Order Service exposes REST APIs:

### **Order Service** (`http://localhost:8081`)

This repository contains the Postman API documentation that can be viewed [here](https://documenter.getpostman.com/view/42993524/2sAYk7S4Fv).

---

## 📌 Future Enhancements

- Add **JWT authentication**
- Replace H2 with a **real database (PostgreSQL)**
- Implement real notification services in notification-service strategies

---

## 🐜 License

MIT License. See `LICENSE` for details.

