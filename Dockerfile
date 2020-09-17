FROM openjdk:8-alpine

EXPOSE 8080

WORKDIR /c

COPY build/libs/*.jar guiabolso.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","guiabolso.jar"]
