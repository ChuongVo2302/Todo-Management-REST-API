# TODO-Application-API (Spring Boot & PostgreSQL)
A Simple TODO API to manage daily tasks.

## Tech Stack
- Java 25
- Spring Boot(Web, Data JPA, Validation)
- PostgresSQL
- Lombok
- Maven

## Project Structure

```
todo-app/
├── src/
│   ├── main/
│   │   ├── java/com/github/yourusername/todo/
│   │   │   ├── controller/      # REST APIs
│   │   │   ├── service/         # Business Logic
│   │   │   ├── repository/      # Database Queries
│   │   │   ├── model/           # Database Entities
│   │   │   └── TodoApplication.java
│   │   └── resources/
│   │       ├── application.properties  # App Configuration
│   └── test/                           # Unit and Integration Tests
├── pom.xml              # Maven dependencies
└── README.md            # Project documentation

```
## Running the Application
### 1. Prerequisites
- Install PostgresSQL and create a database named `todo_db`.
- Install JDK 17 or higher.

## Environment Setup
Create a `.env` file in the root directory and use your database credentials:
```
DB_URL=jdbc:postgresql://localhost:5432/todo_db
DB_USERNAME=postgres_username
DB_PASSWORD=postgres_password
```

## Run the Application
`docker compose up -d`

`API will be available at http://localhost:8080`