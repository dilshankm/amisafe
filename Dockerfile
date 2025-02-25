# Use the official OpenJDK 23 image as a base
FROM openjdk:23-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
# (Assuming your Maven/Gradle build produces crime-alert-service.jar in the target folder)
COPY target/crime-alert-service.jar /app/crime-alert-service.jar

# Expose the port your application listens on (e.g., 8080)
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "crime-alert-service.jar"]
