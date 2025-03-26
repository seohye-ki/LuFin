# pull 했던 이미지 이름
FROM jenkins/jenkins:jdk17

# 추후 ec2에 재접근해서 도커를 build 하기 위해, 컨테이너 빌드시 openssh-client 를 미리 설치합니다.
USER root
RUN apt-get update &&\
    apt-get upgrade -y &&\
    apt-get install -y openssh-client