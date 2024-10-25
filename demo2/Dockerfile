# Dockerfile for demo2
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/demo2-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
