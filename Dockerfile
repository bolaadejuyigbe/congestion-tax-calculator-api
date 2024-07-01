FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /workspace

# Ensure all project dependencies are downloaded
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src/ /workspace/src/

# Build the application
RUN mvn -X package -DskipTests

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

