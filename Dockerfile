FROM adoptopenjdk:16
WORKDIR /connect
COPY target/connect-0.0.1-SNAPSHOT.jar /connect/connect-backend.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/connect/connect-backend.jar"]