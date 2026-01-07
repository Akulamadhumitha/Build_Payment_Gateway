# -------- BUILD STAGE --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# -------- RUN STAGE --------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]
