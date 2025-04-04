FROM openjdk:17-jdk-slim

# 로케일 패키지 설치
RUN apt-get update && \
    apt-get install -y locales && \
    sed -i '/en_US.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen en_US.UTF-8

# 환경 변수로 로케일 지정
ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8

COPY build/libs/*.jar app.jar

CMD ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=local", "-jar", "app.jar"]
