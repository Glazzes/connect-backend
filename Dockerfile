FROM adoptopenjdk:16 as build
WORKDIR /app
COPY . /app
ENTRYPOINT ["./mvnw", "package", "spring-boot:repckage"]

FROM adoptopenjdk:16
WORKDIR /connect
COPY --from=build app/target/connect-0.0.1-SNAPSHOT.jar /connect/connect-backend.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/connect/connect-backend.jar"]