FROM gradle:8.1.1-jdk11 as BUILD
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build

FROM openjdk:11-jdk-slim

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/ /app/

# Setup env vars
ARG DISCORD_AUTH_TOKEN
ARG HUGGING_FACE_AUTH_TOKEN
RUN touch .env
RUN echo DISCORD_AUTH_TOKEN=${DISCORD_AUTH_TOKEN} >> .env
RUN echo HUGGING_FACE_AUTH_TOKEN=${HUGGING_FACE_AUTH_TOKEN} >> .env

ENTRYPOINT ["java","-jar","-Duser.country=BR", "-Duser.language=pt","/app/kgnome.jar"]