FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./target/products-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "products-service-0.0.1-SNAPSHOT.jar"]