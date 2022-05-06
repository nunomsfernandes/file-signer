#FROM eclipse-temurin:17
#FROM alpine:latest
FROM eclipse-temurin:17-jre-alpine

RUN mkdir /opt/filesigner
COPY target/*.jar /opt/filesigner/app.jar
ENTRYPOINT ["java","-jar","/opt/filesigner/app.jar", "--spring.config.location=/opt/filesigner/config/application.yml"]
