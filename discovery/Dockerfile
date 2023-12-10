FROM bellsoft/liberica-openjdk-alpine-musl:17

WORKDIR /opt

COPY build/libs/*.jar app.jar

CMD ["java","-jar","app.jar"]