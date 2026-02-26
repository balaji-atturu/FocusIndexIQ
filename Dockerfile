FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/timemanager-0.0.1-SNAPSHOT.jar timemanager-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "timemanager-v1.0.jar"]