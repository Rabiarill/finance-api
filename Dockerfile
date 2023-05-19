FROM amazoncorretto:17-alpine

WORKDIR /finance-api

ARG JAR_FILE=target/finance-api-1.0.0-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar" ]