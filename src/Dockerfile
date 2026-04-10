# Use lightweight Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar file into container
COPY target/DocumentationTool-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Spring Boot will use PORT env variable)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]