FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/cadastro-escolar-0.0.1-SNAPSHOT.jar /app/cadastro_escolar.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "cadastro_escolar.jar"]

