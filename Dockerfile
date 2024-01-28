FROM openjdk:8
COPY target/order-service.jar order-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/stock-order-api.jar"]
