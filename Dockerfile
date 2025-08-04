FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml -DskipTests clean package

FROM eclipse-temurin:21-ubi9-minimal
COPY --from=build /home/app/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
LABEL authors="juanliz"