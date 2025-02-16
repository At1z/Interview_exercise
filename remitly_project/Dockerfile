FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -Dspring.profiles.active=test

FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /app/target/remitly_project-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
