# Order System - Microservices Project

## 📌 Overview
This project is a microservices-based order processing system using **Java 21, Spring Boot, and Docker**. It consists of three independent services that communicate via REST API:

- **Order Service** - Manages customer orders.
- **Notification Service** - Sends notifications based on order status.
- **Payment Service** - Handles payment transactions.

Each service uses an **H2 in-memory database** for simplicity.

---

## 🏗 Project Structure
```
order-system/
│── order-service/           # Handles order processing
│── notification-service/    # Sends notifications
│── payment-service/         # Manages payments
│── docker-compose.yml       # Defines service containers
│── README.md                # Project documentation
```

Each service is an independent Spring Boot application that can be run separately.

---

## 🚀 Tech Stack
- **Java 21**
- **Spring Boot 3.2**
- **Spring Web, Spring Data JPA**
- **H2 Database**
- **Lombok**
- **Docker & Docker Compose**
- **Feign (Optional)** for inter-service communication

---

## ⚙️ Setup & Run
### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/order-system.git
cd order-system
```

### 2️⃣ Run Each Service Individually
Go to each service directory (`order-service`, `notification-service`, `payment-service`) and start the Spring Boot application:
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
Each service exposes REST APIs:

### **Order Service** (`http://localhost:8080`)
- `POST /orders` → Create an order
- `GET /orders/{id}` → Get order details

### **Notification Service** (`http://localhost:8081`)
- `POST /notifications` → Send a notification

### **Payment Service** (`http://localhost:8082`)
- `POST /payments` → Process a payment

---

## 📌 Future Enhancements
- Implement **RabbitMQ** for async event-driven communication
- Add **JWT authentication**
- Replace H2 with a **real database (PostgreSQL)**

---

## 📜 License
MIT License. See `LICENSE` for details.

---

## 🤝 Contributing
Feel free to submit issues and pull requests!

---

🚀 **Happy coding!** 🎯
