FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
CMD ["java", "-jar", "-Dspring.profiles.active=local", "app.jar"]