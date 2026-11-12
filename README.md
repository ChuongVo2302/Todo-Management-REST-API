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
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Compose)

That's it — the jar is built inside Docker and PostgreSQL runs in a container,
so you don't need Java, Maven or PostgreSQL installed.

### 2. Environment Setup
Copy the example env file and fill in your values:
```
cp .env.example .env        # macOS / Linux / Git Bash
copy .env.example .env      # Windows cmd / PowerShell
```
```
DB_URL=jdbc:postgresql://localhost:5432/todo_db
DB_USERNAME=postgres
DB_PASSWORD=change_me
JWT_SECRET=<a long random string, 32+ characters>
```

### 3. Run
```
docker compose up -d --build
```
API will be available at http://localhost:8080

Useful commands:
```
docker compose logs -f todo-api   # view app logs
docker compose down               # stop containers (data is kept)
docker compose down -v            # stop and delete the database data
```

> If you change `DB_USERNAME`/`DB_PASSWORD` after the first run, run
> `docker compose down -v` once so Postgres is re-created with the new credentials.

### Running without Docker (for development)
Requires JDK 17+. Start only the database, then run the app with Maven:
```
docker compose up -d todo-db
./mvnw spring-boot:run       # Windows: .\mvnw.cmd spring-boot:run
```