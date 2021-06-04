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
Running with docker is highly recommended, in case you want to run this project
without its help, you will need a JDK 16 installed within your system.

### Option 1: Using docker-compose (recommended)
Before running any other command, keep in mind three docker images will get
downloaded into your system in case you don't have them already
* adoptopenjdk:16
* redis:6.2.3-alpine
* postgres:13.3-alpine

If you're ok with that, then all you need to do is run the following command
```
docker-compose up -d
```

By default this project runs in port `8082`, in any case you want to change defaults
you must head to `application-docker.yml` and `docker-compose.yml` and change
the configuration values related to ports.

### Option 2: Building a docker image
By chosing this method only one docker image will be downloaded to your system
`adoptopenjdk:16`, keep in mind you will need to change configuration in 
`application-docker.yml` for `redis` and `postgresql`, still you can change 
`postgresql` for any sql database of your choice, with configurations done run
```
docker build --tag connect-backend:latest .
```
Then just run a brand new container
```
docker run --name connect -d -p 8082:8082 connect-backend:latest
```

### Option 3: Running with Java
You will need a JDK 16 installed your system in order to compile this code, 
since it will run development mode, you will need both a `redis` and a `postgresql`
database in your system as well as modifing the contents of `application-dev.yml`
contents in order to match your configurations, with that done make use of the
maven gradle wrapper to compile this project.
```
./mvnw package spring-boot:repackage
```
Once compile just run
```
java -jar /target/connect-0.0.1-SNAPSHOT.jar 
```

### Option 4: Running in development mode
You can achieve this easily with the integrated tools provided by modern ides
or you can run this command, as for previous options having a redis and postgres
servers are required aswell, as for `option 3` you will need to make the respective changes
on `application-dev.yml`
```
./mvnw spring-boot:run
```