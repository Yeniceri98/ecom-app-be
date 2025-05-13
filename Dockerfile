FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/ecom-app-be-0.0.1.jar /app/ecom-app-be.jar
ENTRYPOINT ["java", "-jar", "ecom-app-be.jar"]
