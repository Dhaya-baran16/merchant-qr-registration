# Merchant QR Registration System

A Spring Boot and MySQL based Merchant QR Registration System that validates merchant accounts, generates unique Merchant IDs, creates QR codes, generates downloadable PDFs, exports registration data to Excel, and provides searchable merchant registration history.

## Features

* Merchant account number validation
* Merchant registration form
* Automatic Merchant ID generation
* Unique 8-digit Merchant ID
* QR code generation
* QR payload generation
* QR image download
* PDF generation
* Excel export
* Merchant registration history
* Search by account number / merchant details
* Date-based filtering
* MySQL database persistence
* Input validation
* Spring Boot REST/controller architecture
* Thymeleaf-based web interface

## Technology Stack

### Backend

* Java 17
* Spring Boot 3.5.5
* Spring MVC
* Spring Data JPA
* Maven

### Frontend

* HTML
* CSS
* JavaScript
* Thymeleaf

### Database

* MySQL

### Libraries

* ZXing – QR code generation
* Apache PDFBox – PDF generation
* Apache POI – Excel export

## Project Structure

```text
merchant-qr-registration/
│
├── mysql/
│   └── merchant_tables.sql
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/merchantqr/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── repository/
│       │       └── service/
│       │
│       └── resources/
│           ├── static/
│           │   ├── css/
│           │   └── js/
│           ├── templates/
│           └── application.properties
│
├── .gitignore
├── pom.xml
└── README.md
```

## Application Flow

```text
User
  │
  ▼
Merchant Registration Form
  │
  ▼
Validate Input
  │
  ▼
Validate Account Number
  │
  ▼
Generate Merchant ID
  │
  ▼
Create QR Payload
  │
  ▼
Generate QR Code
  │
  ├──────────────► Generate PDF
  │
  └──────────────► Save Registration
                         │
                         ▼
                   MySQL Database
                         │
                         ▼
                Registration History
```

## Database Setup

1. Create a MySQL database:

```sql
CREATE DATABASE simulator_dev;
```

2. Select the database:

```sql
USE simulator_dev;
```

3. Execute:

```text
mysql/merchant_tables.sql
```

The SQL script creates the merchant-related tables required by the application.

### Existing Account Table

The application validates merchant account numbers against the existing:

```text
account001
```

table.

The account number column expected by the application is:

```text
ACCOUNTNO
```

Make sure this table exists before running the application.

## Configuration

Do not store database passwords or other secrets directly in GitHub.

Configure the following environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Example:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/simulator_dev}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
```

## Running the Application

### Prerequisites

Install:

* Java 17
* Maven
* MySQL
* Git

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

### Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/merchant-qr-registration.git
```

Enter the project:

```bash
cd merchant-qr-registration
```

### Configure Database

Create the database and execute:

```text
mysql/merchant_tables.sql
```

Set your database credentials using environment variables.

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## QR Code Generation

The application generates a QR payload using the merchant registration details and the generated Merchant ID.

The generated QR code can be displayed and downloaded as part of the merchant registration process.

## Export

Registered merchant information can be exported for further processing and reporting.

Supported formats include:

* PDF
* Excel

## Security Note

This repository does not contain production database credentials, API keys, access tokens, or other confidential information.

Always use environment variables or a secure configuration system for sensitive values.

## Future Improvements

* Role-based authentication
* JWT authentication
* REST API documentation using Swagger/OpenAPI
* Production database configuration
* Docker support
* Cloud deployment
* QR payment integration
* Audit logging
* Centralized exception handling
* Automated unit and integration testing

## Author

Dhaya Baran

## License

This project is intended for educational and portfolio purposes.
