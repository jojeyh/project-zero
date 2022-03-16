# syntax=docker/dockerfile:1

FROM gradle:7.4.1-jdk8 AS TEMP_BUILD_IMAGE
WORKDIR /home/george/projectzero

RUN gradlew build

RUN gradle clean build

FROM openjdk:11
WORKDIR /home/george/projectzero

EXPOSE 7070
ENTRYPOINT exec java -jar build/libs/project-zero-0.1.0.jar 
