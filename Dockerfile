# Use the official OpenJDK 23 image as a base
FROM openjdk:23-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/amisafe-0.0.1-SNAPSHOT.jar /app/amisafe.jar

# Expose the port your application listens on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "amisafe.jar"]
