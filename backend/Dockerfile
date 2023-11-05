FROM maven:3.8.1-openjdk-17 as builder

WORKDIR /app
COPY ./ ./
RUN mvn clean install -DskipTests
# mvn clean install -DskipTests
# mvn verify -Dskip.it=true -f pom.xml clean package
FROM openjdk:17.0.2-jdk
COPY --from=builder /app/target/toktik-0.0.1-SNAPSHOT.jar toktik-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","/toktik-0.0.1-SNAPSHOT.jar"]


