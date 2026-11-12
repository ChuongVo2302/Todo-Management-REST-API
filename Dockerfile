# ---- Stage 1: build the jar inside Docker (no JDK/Maven needed on the host) ----
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

# Download dependencies first so they are cached between builds
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
# maven.test.skip skips compiling AND running tests (tests run separately via ./mvnw test)
RUN mvn -B clean package -Dmaven.test.skip=true

# ---- Stage 2: small runtime image ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
