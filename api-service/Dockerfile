# Stage 1: build — imagem grande, descartada após compilar
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q   # camada cacheada se pom.xml não mudar
COPY src ./src
RUN mvn package -DskipTests -q

# Stage 2: runtime — imagem enxuta (~200MB vs ~500MB)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]