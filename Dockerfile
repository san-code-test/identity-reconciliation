# ---- Stage 1: Build ----
FROM gradle:8.10.0-jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the built jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Render sets $PORT automatically, so bind to it
ENV PORT=8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
