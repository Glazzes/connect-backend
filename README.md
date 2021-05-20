# Connect backend (In development)
## About
Connect is a social network that consists of three parts, Connect backend (this project),
connect web and connect mobile.

This project is being developed by making use of the following technologies:
* [Java (OpenJDK)]("https://openjdk.java.net/")
* [Spring boot]("https://spring.io/")
* [PostgreSql]("https://www.postgresql.org/")
* [Redis]("https://redis.io/")

Features you will find in this project
* User crud options
* Security features
    * Refresh/Authentication token based authentication 
    * Server side sessions  
    * Username Password based login
    * Qr code scan login

## How to run this project
### Requirements
Independent of which running method you choose all of them require a jdk version
16 installed in your system.

### Option 1: Using docker-compose (recommended)
In order to run this project, first it needs to be packaged into a jar file,
you can achieve this by running
```
./mvnw package spring-boot:repackage
```

Before running any other command, keep in mind three docker images will get
downloaded into your system in case you don't have them already
* adoptopenjdk:16
* redis:6.2.3-alpine
* postgres:13.3-alpine

If you're ok with that, then all you need to do is run the following command,
by default this project runs on port `8082`, you can change that if you want to,
by changing default port configuration on `docker-compose.yml` file
and `application-docker.yml`.
```
docker-compose up -d
```

### Option 2: Building a docker image
First it needs to be packaged into a jar file
```
./mvnw package spring-boot:repacage
```
In case you already have both a redis server and a postgresql server 
already installed and you don't want to add more bloat to 
your system, you can build the image for this project yourself, be aware
you will need to modify contents of `application-docker.yml` file to match your
own configuration in order to run properly
```
docker build --tag connect-backend:latest
```
With that done, start a new docker container
```
docker run --name connect-backend-container -d -p 8082:8082 connect-backend:latest
```

### Option 3: Running with Java
As for `Option 2` you will need both a redis server and a postgres server running
in your system, same as for option `Option 1`, you will need to package the
project into a jar file.
```
./mvnw package spring-boot:repackage
```
By default this project uses `application-dev.yml`, you should change the configuration
in order to match your own.
```
java -jar /target/connect-0.0.1-SNAPSHOT.jar
```
In case you want to make use of your own application file, you can make use of the
flag `-Dspring-profiles-active=yourprofile` by example
```
java -jar -Dspring.profiles.active=custom /target/connect-0.0.1-SNAPSHOT.jar
```

### Option 4: Running in development mode
You can achieve this easily with the integrated tools provided by modern ides
or you can run this command, as for previous options having a redis and postgres
servers are required aswell, as for `option 3` you will need to make the respective changes
on `application-dev.yml`
```
./mvnw spring-boot:run
```