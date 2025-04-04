FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "-Dspring.profiles.active=local", "app.jar"]
