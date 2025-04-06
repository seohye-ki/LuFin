FROM openjdk:17-jdk-slim

# 타임존 및 로케일 설정
RUN apt-get update && \
    apt-get install -y tzdata locales && \
    ln -fs /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata && \
    sed -i '/en_US.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen en_US.UTF-8

# 환경 변수로 로케일 및 타임존 지정
ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8 \
    TZ=Asia/Seoul

COPY build/libs/*.jar app.jar

CMD ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=local", "-jar", "app.jar"]
