FROM openjdk:18
ARG JAR_FILE=target/MueblesStgoApp.jar
COPY ${JAR_FILE} MueblesStgoApp.jar
ENTRYPOINT ["java","-jar","/MueblesStgoApp.jar"]