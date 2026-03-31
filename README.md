# 🌐 SIM Activation Portal — Complete Setup Guide

## 📁 Project Structure

```
sim-activation-portal/
├── backend/                          # Spring Boot Application
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/simportal/
│       │   ├── SimActivationPortalApplication.java
│       │   ├── config/
│       │   │   ├── CorsConfig.java
│       │   │   ├── DataSeeder.java
│       │   │   └── SwaggerConfig.java
│       │   ├── controller/
│       │   │   ├── CustomerController.java
│       │   │   ├── OfferController.java
│       │   │   └── SimController.java
│       │   ├── dto/
│       │   │   ├── AddressUpdateDTO.java
│       │   │   ├── ApiResponse.java
│       │   │   ├── CustomerRequestDTO.java
│       │   │   ├── CustomerResponseDTO.java
│       │   │   ├── OfferResponseDTO.java
│       │   │   ├── SimActivationRequestDTO.java
│       │   │   └── SimResponseDTO.java
│       │   ├── entity/
│       │   │   ├── Customer.java
│       │   │   ├── Offer.java
│       │   │   └── Sim.java
│       │   ├── exception/
│       │   │   ├── BusinessException.java
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── ResourceNotFoundException.java
│       │   ├── repository/
│       │   │   ├── CustomerRepository.java
│       │   │   ├── OfferRepository.java
│       │   │   └── SimRepository.java
│       │   └── service/
│       │       ├── CustomerService.java
│       │       ├── OfferService.java
│       │       └── SimService.java
│       └── resources/
│           ├── application.properties
│           └── schema.sql
│
└── frontend/                         # React + Vite Application
    ├── index.html
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── main.jsx
        ├── App.jsx
        ├── services/
        │   └── api.js
        ├── styles/
        │   └── global.css
        ├── components/
        │   ├── Alert.jsx
        │   ├── Button.jsx
        │   ├── Footer.jsx
        │   ├── Input.jsx
        │   ├── Navbar.jsx
        │   └── PageWrapper.jsx
        └── pages/
            ├── LandingPage.jsx
            ├── SimValidationPage.jsx
            ├── CustomerVerificationPage.jsx
            ├── PlanSelectionPage.jsx
            ├── AddressPage.jsx
            └── SuccessPage.jsx
```

---

## ⚙️ Prerequisites

| Tool        | Version    | Download |
|-------------|------------|----------|
| Java        | 17+        | https://adoptium.net |
| Maven       | 3.8+       | https://maven.apache.org |
| MySQL       | 8.0+       | https://dev.mysql.com |
| Node.js     | 18+        | https://nodejs.org |
| npm         | 9+         | Bundled with Node.js |

---

## 🗄️ Step 1: Database Setup

1. Open MySQL Workbench or CLI:
```sql
mysql -u root -p
```

2. Run the schema:
```sql
source /path/to/sim-activation-portal/backend/src/main/resources/schema.sql
```

Or manually:
```sql
CREATE DATABASE sim_portal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 🔧 Step 2: Backend Setup

### 2.1 Configure Database

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sim_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

### 2.2 Build and Run

```bash
cd sim-activation-portal/backend

# Build
mvn clean install -DskipTests

# Run
mvn spring-boot:run
```

✅ Backend starts at: **http://localhost:8080**

### 2.3 Verify Backend

- Health: http://localhost:8080/api/offers
- Swagger: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

---

## 🎨 Step 3: Frontend Setup

```bash
cd sim-activation-portal/frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

✅ Frontend starts at: **http://localhost:3000**

### Build for production:
```bash
npm run build
npm run preview
```

---

## 🔗 API Reference

### Base URL: `http://localhost:8080/api`

---

### 1. GET /sim/validate/{simNumber}

**Validate a SIM card**

```
GET /api/sim/validate/8901260123456789012
```

**Response (200):**
```json
{
  "success": true,
  "message": "SIM is valid and available.",
  "data": {
    "simId": 1,
    "simNumber": "8901260123456789012",
    "status": "AVAILABLE",
    "customerId": null
  }
}
```

**Error (404):**
```json
{
  "success": false,
  "message": "SIM not found with number: XXXX",
  "data": null
}
```

---

### 2. POST /customer/validate

**Register or retrieve a customer**

```
POST /api/customer/validate
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dob": "1995-06-15",
  "address": "12 MG Road, Bangalore, Karnataka - 560001",
  "idProof": "AADHAR"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Customer validated successfully.",
  "data": {
    "customerId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dob": "1995-06-15",
    "address": "12 MG Road, Bangalore, Karnataka - 560001",
    "idProof": "AADHAR"
  }
}
```

---

### 3. GET /offers

**Get all available plans**

```
GET /api/offers
```

**Response (200):**
```json
{
  "success": true,
  "message": "Offers retrieved successfully.",
  "data": [
    {
      "offerId": 1,
      "planName": "Basic",
      "price": 99.00,
      "validity": "28 Days",
      "description": "Great for light users",
      "dataLimit": "1 GB/day",
      "calls": "100 min/day",
      "sms": "100 SMS/day",
      "popular": false
    },
    {
      "offerId": 2,
      "planName": "Standard",
      "price": 199.00,
      "validity": "28 Days",
      "description": "Best value for daily users",
      "dataLimit": "2 GB/day",
      "calls": "Unlimited",
      "sms": "100 SMS/day",
      "popular": true
    },
    {
      "offerId": 3,
      "planName": "Premium",
      "price": 399.00,
      "validity": "56 Days",
      "description": "Ultimate plan for power users",
      "dataLimit": "3 GB/day",
      "calls": "Unlimited",
      "sms": "Unlimited",
      "popular": false
    }
  ]
}
```

---

### 4. POST /sim/activate

**Activate a SIM with plan**

```
POST /api/sim/activate
Content-Type: application/json

{
  "simNumber": "8901260123456789012",
  "customerId": 1,
  "offerId": 2
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "SIM activated successfully!",
  "data": {
    "simId": 1,
    "simNumber": "8901260123456789012",
    "status": "ACTIVE",
    "customerId": 1
  }
}
```

---

### 5. PUT /customer/update-address

**Update customer address**

```
PUT /api/customer/update-address
Content-Type: application/json

{
  "customerId": 1,
  "address": "45 New Colony, Chennai, Tamil Nadu - 600001"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Address updated successfully.",
  "data": {
    "customerId": 1,
    "firstName": "John",
    "lastName": "Doe",
    ...
    "address": "45 New Colony, Chennai, Tamil Nadu - 600001"
  }
}
```

---

## 📬 Postman Testing Guide

### Import Collection

Create a new Postman Collection named **"SIM Portal"** and add these requests:

#### Environment Variables:
- `BASE_URL` = `http://localhost:8080/api`
- `SIM_NUMBER` = `8901260123456789012`

#### Test Flow (run in order):

1. **Validate SIM**
   - Method: GET
   - URL: `{{BASE_URL}}/sim/validate/{{SIM_NUMBER}}`

2. **Register Customer**
   - Method: POST
   - URL: `{{BASE_URL}}/customer/validate`
   - Body: (JSON as shown above)
   - Test Script:
   ```javascript
   var res = pm.response.json();
   pm.environment.set("CUSTOMER_ID", res.data.customerId);
   ```

3. **Get Offers**
   - Method: GET
   - URL: `{{BASE_URL}}/offers`
   - Test Script:
   ```javascript
   var res = pm.response.json();
   pm.environment.set("OFFER_ID", res.data[1].offerId);
   ```

4. **Activate SIM**
   - Method: POST
   - URL: `{{BASE_URL}}/sim/activate`
   - Body:
   ```json
   {
     "simNumber": "{{SIM_NUMBER}}",
     "customerId": {{CUSTOMER_ID}},
     "offerId": {{OFFER_ID}}
   }
   ```

5. **Update Address**
   - Method: PUT
   - URL: `{{BASE_URL}}/customer/update-address`
   - Body:
   ```json
   {
     "customerId": {{CUSTOMER_ID}},
     "address": "New address here"
   }
   ```

---

## 🎓 Key Concepts for Viva

### Architecture
- **Layered Architecture**: Controller → Service → Repository → Entity
- **REST**: Stateless, resource-based HTTP APIs
- **JPA/Hibernate**: ORM for database interaction
- **Spring Boot**: Auto-configuration, embedded Tomcat

### Design Patterns
- **DTO Pattern**: Separates API layer from persistence layer
- **Repository Pattern**: Abstracts data access
- **Service Layer**: Contains all business logic
- **Global Exception Handling**: Centralized error management

### Frontend
- **React Context**: Global state shared across all pages
- **Framer Motion**: Declarative animations
- **React Router**: Client-side routing with protected flow
- **Axios Interceptors**: Centralized API error handling

### Security Concepts
- **CORS**: Cross-Origin Resource Sharing for frontend-backend communication
- **Input Validation**: Both client-side (React) and server-side (@Valid)
- **HTTP Status Codes**: 200 OK, 400 Bad Request, 404 Not Found, 422 Validation Error

---

## 🚀 Production Deployment Notes

### Backend
```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/sim-activation-portal-1.0.0.jar \
  --spring.datasource.password=PROD_PASSWORD
```

### Frontend
```bash
npm run build
# Deploy /dist folder to Nginx/Apache/Vercel
```

### Nginx Config (sample)
```nginx
server {
    listen 80;
    root /var/www/sim-portal/dist;
    index index.html;

    location / { try_files $uri $uri/ /index.html; }
    location /api { proxy_pass http://localhost:8080; }
}
```

---

## ✅ Feature Checklist

- [x] 6-step guided user flow
- [x] SIM validation with status check
- [x] Customer registration/lookup by email
- [x] 3 pricing plans with feature comparison
- [x] Address confirmation with order summary
- [x] Animated success page with activation reference
- [x] Global exception handling
- [x] Input validation (client + server)
- [x] Swagger API documentation
- [x] CORS configuration
- [x] Data seeding on startup
- [x] Layered Spring Boot architecture
- [x] DTO pattern
- [x] Premium dark-mode UI
- [x] Framer Motion animations
- [x] Responsive design
- [x] Step progress indicator in Navbar
