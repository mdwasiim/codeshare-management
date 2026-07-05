FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

COPY common common
COPY services services

RUN chmod +x mvnw

ARG SERVICE_MODULE

RUN ./mvnw clean package -DskipTests -pl ${SERVICE_MODULE} -am

FROM eclipse-temurin:21-jre

WORKDIR /app

ARG SERVICE_MODULE

COPY --from=build /app/${SERVICE_MODULE}/target/*.jar app.jar

RUN apt-get update && apt-get install -y curl netcat-openbsd && rm -rf /var/lib/apt/lists/*

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]