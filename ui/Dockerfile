FROM eclipse-temurin:19-jre
WORKDIR /app
COPY target/coworking_space_reservation-1.0.0.jar .
ENTRYPOINT ["java", "-jar", "coworking_space_reservation-1.0.0.jar"]
CMD ["--server.port=8080", "--debug"]

