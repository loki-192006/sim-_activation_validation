# 📡 SIM Activation Portal

A **production-grade Spring Boot REST API** for automating the end-to-end SIM card activation process in a telecom environment.

---

## 🏗️ Architecture Overview

```
HTTP Request
    │
    ▼
┌─────────────────────────────────────────┐
│           Controller Layer              │  ← Validates input (@Valid), delegates to service
│  CustomerController | SimController     │
│  ActivationController | OfferController │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│            Service Layer                │  ← Business logic, transactions, exception throwing
│  CustomerService | SimService           │
│  ActivationService | OfferService       │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│          Repository Layer               │  ← Spring Data JPA — auto-implemented CRUD
│  CustomerRepository | SimRepository     │
│  ActivationRepository | OfferRepository │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│           MySQL Database                │
│  customers | sim_cards | activations    │
│  offers                                 │
└─────────────────────────────────────────┘
```

---

## 📦 Package Structure

```
com.project.simactivation
│
├── SimActivationPortalApplication.java      ← Main entry point
│
├── controller/
│   ├── CustomerController.java              ← POST /register, /validate | PUT /address
│   ├── SimController.java                   ← GET /validate, /{simId}
│   ├── ActivationController.java            ← POST /start | GET /customer/{id}
│   └── OfferController.java                 ← GET /{customerId}, /
│
├── service/
│   ├── CustomerService.java                 ← Interface
│   ├── SimService.java                      ← Interface
│   ├── ActivationService.java               ← Interface
│   ├── OfferService.java                    ← Interface
│   └── impl/
│       ├── CustomerServiceImpl.java
│       ├── SimServiceImpl.java
│       ├── ActivationServiceImpl.java
│       └── OfferServiceImpl.java
│
├── repository/
│   ├── CustomerRepository.java
│   ├── SimRepository.java
│   ├── ActivationRepository.java
│   └── OfferRepository.java
│
├── entity/
│   ├── Customer.java
│   ├── Sim.java
│   ├── Activation.java
│   └── Offer.java
│
├── dto/
│   ├── request/
│   │   ├── CustomerRegisterRequest.java
│   │   ├── CustomerValidateRequest.java
│   │   ├── AddressUpdateRequest.java
│   │   └── ActivationRequest.java
│   └── response/
│       ├── ApiResponse.java                 ← Generic wrapper for ALL responses
│       ├── CustomerResponse.java
│       ├── SimResponse.java
│       ├── ActivationResponse.java
│       └── OfferResponse.java
│
├── exception/
│   ├── GlobalExceptionHandler.java          ← @RestControllerAdvice — centralised error handling
│   ├── ErrorResponse.java
│   ├── ResourceNotFoundException.java
│   ├── CustomerNotFoundException.java
│   ├── SimNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── CustomerValidationException.java
│   └── SimActivationException.java
│
└── config/
    ├── SwaggerConfig.java                   ← OpenAPI 3.0 setup
    └── DataSeeder.java                      ← Seeds SIMs & Offers on startup
```

---

## ⚙️ Prerequisites

| Tool        | Version   |
|-------------|-----------|
| Java        | 17+       |
| Maven       | 3.8+      |
| MySQL       | 8.0+      |
| Postman     | Any       |

---

## 🚀 Running Locally — Step by Step

### Step 1: Clone or unzip the project

```bash
cd sim-activation-portal
```

### Step 2: Create MySQL database

```sql
CREATE DATABASE sim_activation_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### Step 3: Configure database credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sim_activation_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 4: Build and run

```bash
# Build
mvn clean install -DskipTests

# Run
mvn spring-boot:run
```

### Step 5: Verify startup

Visit: **http://localhost:8080/swagger-ui.html**

On first startup, the `DataSeeder` automatically inserts 4 sample SIM cards and 4 offers.

---

## 🔗 Full API Reference

### Base URL: `http://localhost:8080/api/v1`

---

### 👤 Customer Module

#### `POST /customers/register`
Register a new customer.

**Request:**
```json
{
  "firstName": "Raj",
  "lastName": "Patel",
  "email": "raj.patel@example.com",
  "dob": "1995-06-15",
  "address": "123 MG Road, Surat, Gujarat 395001",
  "phoneNumber": "9876543210"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Customer registered successfully.",
  "data": {
    "customerId": 1,
    "firstName": "Raj",
    "lastName": "Patel",
    "fullName": "Raj Patel",
    "email": "raj.patel@example.com",
    "dob": "1995-06-15",
    "address": "123 MG Road, Surat, Gujarat 395001",
    "phoneNumber": "9876543210",
    "kycStatus": "PENDING",
    "createdAt": "2024-03-15 10:30:00"
  },
  "timestamp": "2024-03-15T10:30:00"
}
```

---

#### `POST /customers/validate`
Verify customer identity using email + DOB before activation.

**Request:**
```json
{
  "email": "raj.patel@example.com",
  "dob": "1995-06-15"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Customer identity validated successfully.",
  "data": { "customerId": 1, "fullName": "Raj Patel", ... }
}
```

**Response (401 — wrong DOB):**
```json
{
  "success": false,
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials. Email and date-of-birth do not match.",
  "path": "/api/v1/customers/validate",
  "timestamp": "2024-03-15T10:31:00"
}
```

---

#### `PUT /customers/address`
Update a customer's address.

**Request:**
```json
{
  "customerId": 1,
  "newAddress": "456 Ring Road, Surat, Gujarat 395007"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Address updated successfully.",
  "data": { "customerId": 1, "address": "456 Ring Road, Surat, Gujarat 395007", ... }
}
```

---

#### `GET /customers/{id}`
Fetch customer profile by ID.

**Response (200 OK):** `{ "success": true, "data": { ... } }`

---

### 📶 SIM Module

#### `GET /sim/validate?simIccid=...&mobileNumber=...`
Validate a SIM card.

**Request params:**
- `simIccid` = `8991101200003204510`
- `mobileNumber` = `9876543210`

**Response (200 OK — valid, ready to activate):**
```json
{
  "success": true,
  "message": "SIM is valid and ready for activation.",
  "data": {
    "simId": 1,
    "simIccid": "8991101200003204510",
    "mobileNumber": "9876543210",
    "status": "INACTIVE",
    "simType": "PREPAID",
    "operatorCode": "IND01",
    "isValid": true
  }
}
```

**Response (200 OK — already active, not eligible):**
```json
{
  "success": true,
  "message": "SIM found but not eligible for activation. Status: ACTIVE",
  "data": { ..., "isValid": false }
}
```

---

### ⚡ Activation Module

#### `POST /activation/start`
Activate a SIM card (the core workflow).

**Request:**
```json
{
  "customerId": 1,
  "simIccid": "8991101200003204510",
  "mobileNumber": "9876543210",
  "planSelected": "Starter Pack"
}
```

**Response (201 Created — success):**
```json
{
  "success": true,
  "message": "SIM activated successfully. Welcome to the network!",
  "data": {
    "activationId": 1,
    "customerId": 1,
    "customerName": "Raj Patel",
    "simId": 1,
    "mobileNumber": "9876543210",
    "activationStatus": "SUCCESS",
    "planSelected": "Starter Pack",
    "remarks": "SIM activated successfully via portal.",
    "activatedAt": "2024-03-15 10:35:00",
    "createdAt": "2024-03-15 10:35:00"
  }
}
```

**Response (422 — SIM already active):**
```json
{
  "success": false,
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "SIM cannot be activated. Current status: ACTIVE. Only INACTIVE SIMs can be activated.",
  "path": "/api/v1/activation/start"
}
```

---

#### `GET /activation/customer/{customerId}`
Get activation history for a customer.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Fetched 1 activation record(s).",
  "data": [ { "activationId": 1, ... } ]
}
```

---

### 🎁 Offers Module

#### `GET /offers/{customerId}`
Get personalized offers based on customer's SIM type.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Found 3 offer(s) for customer.",
  "data": [
    {
      "offerId": 1,
      "offerName": "Starter Pack",
      "description": "Entry-level prepaid plan with basic data and calls.",
      "price": 99.00,
      "validityDays": 28,
      "dataGb": 1.00,
      "callingMinutes": 100,
      "smsCount": 100,
      "simTypeEligible": "PREPAID",
      "validFrom": "2024-02-14",
      "validTo": "2024-09-11"
    }
  ]
}
```

---

## 🔄 Complete Activation Flow (Happy Path)

```
1. POST /api/v1/customers/register       → Get customerId = 1
2. POST /api/v1/customers/validate       → Confirm identity
3. GET  /api/v1/sim/validate
          ?simIccid=8991101200003204510
          &mobileNumber=9876543210       → Confirm isValid = true
4. POST /api/v1/activation/start         → Activate SIM → status = SUCCESS
5. GET  /api/v1/offers/1                 → Browse available plans
```

---

## ❌ Error Response Format

All errors follow the same structure:

```json
{
  "success": false,
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with ID: 99",
  "path": "/api/v1/customers/99",
  "timestamp": "2024-03-15T10:30:00"
}
```

For validation errors (`400 Bad Request`), a `fieldErrors` map is also included:

```json
{
  "success": false,
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed. Please fix the following errors.",
  "fieldErrors": {
    "email": "Please provide a valid email address",
    "dob": "Date of birth must be in the past"
  }
}
```

---

## 🧪 Running Tests

```bash
# All tests (uses H2 in-memory DB — no MySQL required)
mvn test

# Single test class
mvn test -Dtest=CustomerServiceTest
mvn test -Dtest=ActivationServiceTest
```

---

## 📚 Swagger UI

After starting the application:

| URL | Purpose |
|-----|---------|
| `http://localhost:8080/swagger-ui.html` | Interactive API documentation |
| `http://localhost:8080/api-docs`        | Raw OpenAPI 3.0 JSON spec      |

---

## 🗃️ Database Tables

| Table         | Description                                   |
|---------------|-----------------------------------------------|
| `customers`   | Customer profiles with KYC status             |
| `sim_cards`   | SIM inventory with ICCID, mobile, status      |
| `activations` | Activation records linking customer ↔ SIM     |
| `offers`      | Available plans with eligibility criteria     |

---

## 🌱 Pre-Seeded Test Data

On first startup, `DataSeeder` inserts:

**SIMs:**
| ICCID                 | Mobile       | Status   | Type     |
|-----------------------|--------------|----------|----------|
| 8991101200003204510   | 9876543210   | INACTIVE | PREPAID  |
| 8991101200003204511   | 9876543211   | INACTIVE | POSTPAID |
| 8991101200003204512   | 9876543212   | INACTIVE | PREPAID  |
| 8991101200003204513   | 9876543213   | ACTIVE   | PREPAID  |

**Offers:** Starter Pack (₹99), Power User (₹299), Business Unlimited (₹799), Weekend Special (₹149)

---

## 🛠️ Tech Stack

| Layer           | Technology                |
|-----------------|---------------------------|
| Language        | Java 17                   |
| Framework       | Spring Boot 3.2.3         |
| Persistence     | Spring Data JPA (Hibernate) |
| Database        | MySQL 8.0                 |
| Validation      | Jakarta Validation API    |
| Documentation   | Springdoc OpenAPI 3 (Swagger) |
| Boilerplate     | Lombok                    |
| Build           | Maven                     |
| Testing         | JUnit 5 + Mockito         |
| Test DB         | H2 (in-memory)            |
