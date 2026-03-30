# ✈️ Airline Ticketing System API

**Course:** SE 4458 – Software Architecture & Design of Modern Large Scale Systems
**Student:** Ayfernaz Baygın
**Project Type:** REST API for Airline Company

---

## 🚀 Project Overview

This project is a Spring Boot–based REST API developed for an airline ticketing system. The system supports:

* Flight creation (single & bulk via CSV)
* Flight search (one-way & round-trip)
* Ticket purchasing
* Passenger check-in
* Passenger listing per flight

The application follows **service-oriented architecture principles** using controllers, services, DTOs, validation, and exception handling.

Key features:

* JWT-based authentication
* Swagger API documentation
* API Gateway routing
* Gateway-level rate limiting
* k6 load testing

---

## 🛠️ Technologies Used

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Security
* JWT Authentication
* H2 Database
* Swagger / OpenAPI
* Spring Cloud Gateway
* k6 (Load Testing Tool)
* Postman
* VS Code

---

## 🌐 System Architecture

The system consists of two main services:

### 1️⃣ Backend Airline API

* Handles all business logic
* Runs on port 8080

### 2️⃣ API Gateway

* Routes all external requests
* Applies rate limiting
* Runs on port 8081

### 🔁 Request Flow

Client → API Gateway → Backend API

This architecture ensures centralized control and scalability.

---

## 🧩 Data Model

The system is based on a **Flight–Ticket relationship**:

* A **Flight** contains schedule and capacity information
* A **Ticket** represents a passenger reservation
* Seat assignment is performed during check-in

Supported operations:

* storing flights
* managing ticket sales
* tracking available seats
* assigning seats
* retrieving passenger lists

---

## 🔐 Authentication (JWT)

Authentication is implemented using JSON Web Tokens.

### 🔹 Login Endpoint

POST /api/v1/auth/login

### 🔹 Demo Credentials

username: admin
password: 1234

### 🔹 Usage

Authorization header:
Authorization: Bearer <token>

### 🔹 Behavior

* Without token → 403 Forbidden
* With token → 200 OK

---

## 🔌 API Endpoints

### Add Flight

POST /api/v1/flights
✔ Authentication required

### Add Flight via CSV

POST /api/v1/flights/upload
✔ Authentication required

### Query Flight

GET /api/v1/flights/search
✔ No authentication
✔ Pagination enabled

### Roundtrip Search

GET /api/v1/flights/search/roundtrip

### Buy Ticket

POST /api/v1/flights/tickets/buy
✔ Authentication required

### Check-in

POST /api/v1/flights/check-in
✔ No authentication

### Passenger List

GET /api/v1/flights/{flightNumber}/passengers
✔ Authentication required
✔ Pagination enabled

---

## 📊 Authentication & Pagination Compliance

| API                | Authentication | Pagination |
| ------------------ | -------------- | ---------- |
| Add Flight         | Yes            | No         |
| Add Flight by File | Yes            | No         |
| Query Flight       | No             | Yes        |
| Buy Ticket         | Yes            | No         |
| Check-in           | No             | No         |
| Passenger List     | Yes            | Yes        |

---

## 📘 Swagger Documentation

Swagger UI is integrated for API documentation and testing.

### 🔗 Swagger UI

http://airline-api-env.eba-daigpjim.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html

### 🔗 API Docs

http://airline-api-env.eba-daigpjim.eu-north-1.elasticbeanstalk.com/api-docs

---

## 🌐 API Gateway

All requests are routed through the API Gateway.

### Responsibilities:

* Request routing
* Centralized entry point
* Rate limiting enforcement

### 🔗 Gateway Base URL

http://airline-gateway-env.eba-by68ht3y.eu-north-1.elasticbeanstalk.com

---

## ⛔ Rate Limiting

Rate limiting is implemented at the API Gateway level using request-based throttling. Each client is allowed a limited number of requests within a defined time window. When the limit is exceeded, the system responds with HTTP 429 (Too Many Requests).

### 🔹 Behavior

* First 3 requests → 200 OK
* 4th request → 429 Too Many Requests

### 🔹 Endpoint

GET /api/v1/flights/search

---

## 🧪 Load Testing

Load testing was conducted using **k6**.

### 🔹 Scenarios

* 20 users / 30 sec
* 50 users / 30 sec
* 100 users / 30 sec



## 🌐 Deployment Links
The system is deployed on AWS Elastic Beanstalk and publicly accessible via the links below.

### 🔹 Backend Base URL

http://airline-api-env.eba-daigpjim.eu-north-1.elasticbeanstalk.com

### 🔹 Swagger UI

http://airline-api-env.eba-daigpjim.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html

### 🔹 API Documentation

http://airline-api-env.eba-daigpjim.eu-north-1.elasticbeanstalk.com/api-docs

---

### 🔹 API Gateway Base URL

http://airline-gateway-env.eba-by68ht3y.eu-north-1.elasticbeanstalk.com

### 🔹 Gateway Health Check

http://airline-gateway-env.eba-by68ht3y.eu-north-1.elasticbeanstalk.com/health

---

### 🔹 Flight Search (Backend)

http://airline-api-env.eba-daigpjim.eu-north-1.elasticbeanstalk.com/api/v1/flights/search?airportFrom=IST&airportTo=ANK&numberOfPeople=1&page=0

### 🔹 Flight Search (Gateway - Rate Limited)

http://airline-gateway-env.eba-by68ht3y.eu-north-1.elasticbeanstalk.com/api/v1/flights/search?airportFrom=IST&airportTo=ANK&numberOfPeople=1&page=0





### 🔹 Notes

Rate limiting was disabled during testing to measure raw performance.

---

# 📈 Load Test Analysis

Load testing was conducted under three different scenarios: 20, 50, and 100 virtual users (VUs), each executed for 30 seconds.

The system demonstrated stable and consistent performance across all load levels. The average response time remained within the range of 100–106 ms, indicating efficient request processing even as the number of users increased.

Although the p95 latency showed a slight increase under the stress scenario (100 VUs), it remained within acceptable limits, with a maximum value of 156.90 ms. This indicates that the system can handle peak load conditions without significant latency degradation.

Throughput increased proportionally with the number of virtual users, reaching up to 160.32 requests per second under the highest load. In addition, the error rate remained 0.00% in all scenarios, showing that no failed HTTP requests occurred during the tests and that the system handled concurrent traffic reliably.

Overall, the results confirm that the system scales effectively and maintains stable performance under increasing load conditions.

---

## ⚙️ Assumptions

* Flights with no seats are excluded from search
* Both one-way and round-trip supported
* Passenger lists are paginated
* JWT is used for protected endpoints
* Seat assigned at check-in
* Rate limiting applied only to search
* H2 used for development

---

## 🔮 Future Improvements

* Use production-grade database
* Distributed rate limiting
* Cloud-native deployment improvements
* Monitoring & observability
* Advanced gateway rules

---

## 📌 Conclusion

This project successfully implements a scalable airline ticketing system using modern software architecture principles.

It includes:

* JWT authentication
* API Gateway routing
* Rate limiting
* Swagger documentation
* Load testing

All functional and technical requirements of the assignment have been satisfied.
The system also demonstrates microservice architecture principles with API Gateway integration.

## 📄 Project Report

You can access the detailed project report here:

[Download Report](report/Airline_Ticketing_System_Report.docx)



