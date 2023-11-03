FROM maven:3.8.1-openjdk-17 as build

WORKDIR /app

COPY /pom.xml ./
COPY ./ ./
RUN mvn package

FROM openjdk:latest
COPY target/toktik-0.0.1-SNAPSHOT.jar toktik-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","/toktik-0.0.1-SNAPSHOT.jar"]


