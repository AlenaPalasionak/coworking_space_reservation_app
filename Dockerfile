FROM maven:3.9-eclipse-temurin-19 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:19-jre
WORKDIR /app
COPY --from=build /app/ui/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
#CMD ["--server.port=8080", "--debug"]

