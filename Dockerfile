# Stage 0 - "build-stage"
FROM openjdk:11-jdk-slim as build-stage

ARG BUILD_OPTS=""
ENV ENV_BUILD_OPTS=$BUILD_OPTS

COPY . /usr/src/app

WORKDIR /usr/src/app
RUN ./gradlew ${BUILD_OPTS} clean build bootJar

# Stage 1 - Put everything in nginx and run it
FROM openjdk:11.0.6-jre-slim
COPY --from=build-stage /usr/src/app/build/libs/*.jar /app.jar
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0

# add timezone
ENV TZ=Europe/Paris
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

VOLUME /tmp
EXPOSE 8080
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
