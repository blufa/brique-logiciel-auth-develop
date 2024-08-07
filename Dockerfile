FROM openjdk:17.0.2-slim

WORKDIR /app

EXPOSE 8080
EXPOSE 8083

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /auth-service.jar

HEALTHCHECK --interval=5s \
            --timeout=3s \
             CMD curl --fail --silent localhost:8080/actuator/health | grep UP || exit 1

CMD ["java", "-jar", "/auth-service.jar"]