FROM maven:3.6.3 AS maven
LABEL MAINTAINER="vitaly.kolesnikov88@gmail.com"
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=doctr-springboot.jar
WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/
ENTRYPOINT ["java","-jar","doctr-springboot.jar"]