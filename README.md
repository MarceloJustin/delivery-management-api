# Delivery Management API

REST API for delivery order management inspired by food delivery platforms.

## Technologies

- Java 21
- Spring Boot 4
- PostgreSQL
- Spring Data JPA
- Maven

## Features

- Application health check
- Customer management (coming soon)
- Restaurant management (coming soon)
- Product management (coming soon)
- Order management (coming soon)

## Running the project

1. Create the database:

CREATE DATABASE delivery_management_db;

2. Configure application.properties

3. Run the application

## Health Check

GET /api/health

Response:

{
  "status": "UP",
  "application": "Delivery Management API"
}