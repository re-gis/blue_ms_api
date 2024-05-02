# Use a Maven image as the base image for the build stage
FROM maven:3.8.4-openjdk-8 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package

# Use the OpenJDK image as the base image for the runtime environment
FROM openjdk:8

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file from the build stage to the runtime image
COPY --from=build /app/target/blue-0.0.1-SNAPSHOT.jar /app/blue-0.0.1-SNAPSHOT.jar

# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run the Java application
CMD ["java", "-jar", "blue-0.0.1-SNAPSHOT.jar"]
