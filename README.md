# Agrimart - Monolithic E-commerce Application

## Overview

Agrimart is a monolithic e-commerce application built with Spring Boot. It provides a complete backend solution for an online marketplace, including user management, product catalog, shopping cart, and order processing.

This project serves as a practical example of building a robust, feature-complete monolithic application using modern Java development practices.

## Technology Stack

*   **Framework:** Spring Boot
*   **Language:** Java 17
*   **Database:** PostgreSQL
*   **Data Access:** Spring Data JPA / Hibernate
*   **Security:** Spring Security (to be implemented)
*   **Build Tool:** Maven

## Project Setup

1.  **Prerequisites:**
    *   Java 17 or higher
    *   Maven 3.x
    *   PostgreSQL database

2.  **Database Configuration:**
    *   Create a new PostgreSQL database named `Agrimart_db`.
    *   Update the `src/main/resources/application.properties` file with your PostgreSQL username and password.

3.  **Run the Application:**
    *   You can run the application from your IDE by running the `AgrimartApplication.java` file.
    *   Alternatively, you can run it from the terminal using Maven:
        ```sh
        mvn spring-boot:run
        ```

4.  **Data Seeding:**
    *   On the first run, the application will automatically populate the database with test data (5 users, 2 sellers, and 5 products) using the `DataLoader` component.

## API Endpoints

The application exposes a RESTful API. The base URL is `http://localhost:9093`.

### Users (`/api/users`)

*   `POST /register`: Register a new user.
*   `GET /{id}`: Get user details by ID.

### Products (`/api/products`)

*   `POST ?sellerId={id}`: Create a new product (requires a seller ID).
*   `GET`: Get all products (with optional `category` or `name` filters).
*   `GET /{id}`: Get product details by ID.

### Health Check (`/api/test`)

*   `GET /health`: A simple endpoint to check if the API is running.
