FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/*.jar /app/ecom-app-be.jar
ENTRYPOINT ["java", "-jar", "ecom-app-be.jar"]
