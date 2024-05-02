FROM openjdk:17
COPY ./target /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar","IDFLab-1.0-SNAPSHOT.jar"]