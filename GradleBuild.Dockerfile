FROM adoptopenjdk:16
WORKDIR /connect
COPY jars/connect*.jar /connect/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", '/connect/app.jar']