# wb-customer-management-services
**Customer Management Service**

**Overview**

This is a Spring Boot microservice responsible for managing bank customers. It provides REST APIs for customer onboarding, retrieval, listing customers, account creation and listing accounts. The service is designed as part of a microservices ecosystem, communicating events to account services using RabbitMQ for Event-Driven Architecture (EDA).


**Architecture Diagram**

<pre>
   +-------------------+
   |      Client       |
   +-------------------+
             |
             v
   +-------------------------+
   | Customer Management MS  |
   |-------------------------|
   | REST APIs (Swagger)     |
   | Spring Security (Basic) |
   | H2 / Relational DB      |
   | RabbitMQ Publisher      |
   +-------------------------+
             |
             v
   +-------------------+
   | RabbitMQ Exchange |
   +-------------------+
             |
             v
   +-------------------+
   | Account Service   |
   | (Consumer of      |
   | AccountCreateEvent) |
   +-------------------+

</pre>


**Event-Driven Architecture (EDA) Flow**

1. Client sends POST /api/v1/customers/onboard-customer
2. CustomerService validates and saves the customer to DB
3. On Create Account AccountEventPublisher publishes a AccountCreateEvent to RabbitMQ
4. Account Service receives the event and processes it asynchronously
5. Decouples customer onboarding from downstream services

**Database Schema**

**CUSTOMER TABLE**

| Column        | Type       | Constraints                |
| ------------- | ---------- | -------------------------- |
| customer_id   | VARCHAR(7) | PK, Not Null               |
| legal_id      | VARCHAR    | Unique, Not Null           |
| name          | VARCHAR    | Not Null                   |
| customer_type | VARCHAR    | Not Null (RETAIL/CORP/INV) |
| status        | VARCHAR    | Not Null (ACTIVE/BLOCK)    |
| phone         | VARCHAR    |                            |
| email         | VARCHAR    |                            |
| created_at    | TIMESTAMP  |                            |
| address_id    | BIGINT     | FK -> Address.id           |

**Address Table**

| Column        | Type    | Constraints |
| ------------- | ------- | ----------- |
| id            | BIGINT  | PK          |
| address_line1 | VARCHAR | Not Null    |
| address_line2 | VARCHAR |             |
| area          | VARCHAR |             |
| building_no   | VARCHAR |             |
| governorate   | VARCHAR |             |

**Account Table**

| Column       | Type       | Constraints                |
|--------------|------------|----------------------------|
| account_id   | BIGINT     | PK                         |
| customer_id  | BIGINT     | FK -> Customer.customer_id |
| account_type | VARCHAR    |                            |
| currency     | VARCHAR    |                            |
| balance      | BIGDECIMAL |                            |
| status       | VARCHAR    |                            |
| created_at   | TIMESTAMP  |                            |
| updated_at   | TIMESTAMP  |                            |


**Design Decisions**

1. H2 database for development; production-ready for Oracle/MySQL
2. REST APIs follow OpenAPI (Swagger 3)
3. HTTP Basic Authentication with Spring Security
4. Event-Driven Architecture using RabbitMQ
5. Validation enforced via Jakarta Bean Validation (@Valid)
6. UnifiedResponse<T> standardizes all responses

**Technology Stack**

1. Java 21
2. Spring Boot 4.0.1
3. Spring Data JPA
4. Spring Security (HTTP Basic Auth for secured endpoints)
5. RabbitMQ (message broker for EDA)
6. H2 database (in-memory relational database)
7. JUnit 5 + Mockito (unit testing)
8. Swagger (OpenAPI 3) for API documentation


**Database Design**

**Customer Entity:**

1. customerId (7 digits, primary key)
2. legalId (unique)
3. name, email, phone, status, customerType (RETAIL, CORPORATE, INVESTMENT)
4. address as a separate entity for relational mapping

**Address Entity:**

1. id, addressLine1, addressLine2, area, buildingNo, governorate
2. Linked to customer using @OneToOne

**Account Entity:**

1. accountId(10 digits, primary key)
2. Linked to customer using @ManyToOne
3. accountType, currency, balance, status, createdAt, updatedAt

**Validation**

1. customerId must be 7 digits
2. Phone number must be 8 digits
3. Validation enforced via Jakarta Bean Validation (@Valid)
4. Number of accounts will be validated it should not be more than 10
5. At least one Salary account will be created for a particular customer

**REST APIs**

| Endpoint                                         | Method | Description                              |
|--------------------------------------------------|--------|------------------------------------------|
| `/api/v1/customers/onboard-customer`             | POST   | Onboard a new customer                   |
| `/api/v1/customers/{customerId}`                 | GET    | Retrieve a customer by ID                |
| `/api/v1/customers/list-onboard-customers`       | GET    | List all onboarded customers             |
| `/api/v1/customers/create-account `              | POST   | Create New Account                       |
| `/api/v1/customers/fetch-accounts/{customerId}`  | GET    | Fetch all accounts for specific customer |

1. APIs follow OpenAPI Specification (Swagger 3)
 -- Public endpoints: /swagger-ui/**, /v3/api-docs/**, /h2-console/**, /actuator/health


**Exception Handling**

Global exception handler (ServiceExceptionHandler) handles:

1. BusinessException
2. EntityNotFoundException
3. ConstraintViolationException
4. MethodArgumentNotValidException
5. Generic Exception
6. Returns standardized UnifiedResponse<T> with responseCode, httpStatus, errorMessage, validationErrors, and timestamp.

**Testing**

Unit tests with JUnit 5 and Mockito:

1. CustomerControllerTest
2. CustomerServiceTest
3. CustomerEventPublisherTest
4. ServiceExceptionHandlerTest
5. Tests business logic, controller responses, and event publishing without starting Spring Boot or connecting to RabbitMQ.

**Assumptions**

1. Customer status: ACTIVE or BLOCKED
2. Customer type: RETAIL, CORPORATE, INVESTMENT
3. Each customer can have up to 10 accounts
4. One salary account allowed per customer
5. RabbitMQ consumers (account service) exist and process events and save the account in db


**Shortcomings**

1. H2 database only in memory for dev/testing
2. Basic authentication only; no roles/permissions
3. RabbitMQ errors/retries not handled yet
4. GET endpoints do not support pagination/filtering
5. Swagger and H2 console unsecured (dev only)

**Running the Service**

1. Start RabbitMQ (Docker recommended):

docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

2. Run the Spring Boot application:

mvn spring-boot:run

**Future Improvements**

1. Add integration tests with Testcontainers for H2 & RabbitMQ
2. Add JWT/OAuth2 authentication with roles
3. Add retry/confirmation mechanism for RabbitMQ 
4. Add pagination, sorting, and filtering for list endpoints
