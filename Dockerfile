FROM maven:3.6.3 AS maven
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/*.jar /opt/app/doctr-springboot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","doctr-springboot.jar"]