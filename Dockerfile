#FROM bellsoft/liberica-openjre-alpine:21
#VOLUME /tmp
#RUN adduser -S jmix-user
#USER jmix-user
#COPY build/libs/*.jar app.jar
#
#ENTRYPOINT ["java", "-jar", "/app.jar"]
FROM openjdk:21-jdk-slim
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
