# Secure Medical API

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation and Running](#installation-and-running)
- [Usage](#usage)
    - [Authentication](#authentication)
    - [Protected Endpoints](#protected-endpoints)
- [Testing Strategy](#testing-strategy)

---

## Overview

**Secure Medical API** is a project demonstrating a robust, secure, and tested **Layered Monolithic Application** built with Java and Spring Boot. It is designed to function as a centralized API gateway for a medical or diagnostic environment.

The application's core purpose is to provide a single entry point for all API requests. It enforces authentication using stateless **JSON Web Tokens (JWTs)** and authorizes access to sensitive endpoints based on user roles.

---

## Features

-   **Stateless JWT-based Security:** Authentication is handled via secure, time-limited JSON Web Tokens, eliminating the need for server-side sessions.
-   **Role-Based Access Control:** Access to different API endpoints is controlled with permissions using Spring Security's `@PreAuthorize` annotations.
-   **Layered Architecture:** A clear separation of concerns between the API/Web Layer, the Business Logic/Service Layer, and the Security Layer.
-   **Centralized Configuration:** All security rules and application settings are managed centrally.
-   **Testing:** The project includes both unit tests for isolated logic and integration tests to validate the full security flow.
-   **Built for Maintainability:** The codebase is organized into logical packages, making it easy to extend, and maintain.

---

## Tech Stack

-   **Language:** Java 17
-   **Framework:** Spring Boot 3.x
-   **Security:** Spring Security 6, JSON Web Tokens (JWT) with the `java-jwt` library from Auth0
-   **Build Tool:** Apache Maven
-   **Testing:** JUnit 5, Spring Boot Test, MockMvc

---

## Project Structure

The project follows a standard layout for a layered Spring Boot application.
```
secure-medical-monolith/
├── src/
│ ├── main/
│ │ ├── java/com/gendx/showcase/securemedicalmonolith/
│ │ │ ├── SecureMedicalMonolithApplication.java             # Main entry point
│ │ │ ├── config/
│ │ │ │ └── SecurityConfig.java                             # Define the rules for Spring Security
│ │ │ ├── model/
│ │ │ │ └── LoginRequest.java                               # DTO for the login JSON body
│ │ │ ├── security/
│ │ │ │ ├── JwtAuthenticationFilter.java                    # To check every request
│ │ │ │ └── JwtService.java                                 # Creates and validates JWTs
│ │ │ ├── service/
│ │ │ │ ├── PatientService.java                             # Business logic for patients
│ │ │ │ └── ResultService.java                              # Business logic for results
│ │ │ └── web/
│ │ │ ├── ApiController.java                                # Protected API endpoints
│ │ │ └── AuthController.java                               # Public login endpoint
│ │ └── resources/
│ │ └── application.properties                              # Configuration (server port, JWT secret)
│ └── test/
│ └── java/com/gendx/showcase/securemedicalmonolith/
│ ├── security/
│ │ └── JwtServiceTest.java                                 # Unit tests for JWT logic
│ └── web/
│ └── ApiControllerIntegrationTest.java                     # Integration tests for security flow
├── .gitignore
├── mvnw                                                    # Maven wrapper script (Linux/macOS)
├── mvnw.cmd                                                # Maven wrapper script (Windows)
├── pom.xml                                                 # Maven project configuration (dependencies)
└── README.md                                               # This documentation file
```
---

## Installation and Running

### Prerequisites
- Java JDK 17 or higher. Please see this [link](https://docs.oracle.com/en/java/javase/17/install/installation-jdk-microsoft-windows-platforms.html#GUID-A7E27B90-A28D-4237-9383-A58B416071CA) to download JDK 17 for Windows.
- Recommended IDE - [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows)
- Recommended Tool for testing - [Postman](https://www.postman.com/downloads/)


### Steps
1.  Clone the repository to your local machine:
    ```sh
    git clone https://github.com/Bansari2025/secure-medical-monolith-application.git
    cd secure-medical-monolith-application
    ```

2.  Build and run the application using the provided Maven wrapper. This will download dependencies, compile the code, and start the embedded Tomcat server. The application will start and listen on **`http://localhost:8080`**.
    ```sh
    ./mvnw spring-boot:run
    ```

---

## Usage

All interactions with the API should be performed using an API client like Postman, Insomnia, or `curl`.

### Authentication

Before accessing any protected resources, you must authenticate to receive a JWT. This project includes two mock users: `doctor` and `researcher`.

#### Login Endpoint
-   **Request:** `POST /auth/login`
-   **Request Body (`Content-Type: application/json`):**
    ```json
    {
      "username": "doctor",
      "password": "password123"
    }
    ```
-   **Successful Response (200 OK):**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
    ```
**Copy the received token.** It is valid for 15 minutes.

### Protected Endpoints

All requests to the following endpoints must include an `Authorization` header with the JWT you received.

-   **Header Format:** `Authorization: Bearer <your_copied_token>`

#### 1. Get Patient Data
-   **Permissions:** Requires `DOCTOR` role.
-   **Request:** `GET /api/patients/{id}`
-   **Example:** `GET http://localhost:8080/api/patients/123`
-   **Success (as Doctor):** Returns patient data with a `200 OK` status.
-   **Failure (as Researcher):** Returns a `403 Forbidden` error.

#### 2. Get Result Data
-   **Permissions:** Requires `DOCTOR` or `RESEARCHER` role.
-   **Request:** `GET /api/results/{id}`
-   **Example:** `GET http://localhost:8080/api/results/456`
-   **Success (as Doctor or Researcher):** Returns result data with a `200 OK` status.
-   **Failure (unauthenticated):** Returns a `403 Forbidden` error.

---

## Testing Strategy

The project includes test cases to ensure correctness and maintainability.

-   **Unit Tests (`JwtServiceTest.java`):** These tests validate the core logic of the `JwtService` in isolation, ensuring that token generation and validation work as expected under various conditions (e.g., tampered signature).
-   **Integration Tests (`ApiControllerIntegrationTest.java`):** These tests run in-memory version of the application to validate the entire security flow. They simulate real HTTP requests and assert that the security rules defined in `SecurityConfig` and the `@PreAuthorize` annotations are correctly enforced.

To run all tests from the command line:
```
./mvnw clean test
```