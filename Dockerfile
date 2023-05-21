FROM gradle:6.6.1-jdk14 as BUILD

COPY . .
RUN gradle build

FROM openjdk:15-jdk-slim

COPY --from=BUILD /home/gradle/build/libs/kgnome*.jar /usr/local/lib/kgnome-bot.jar

# Setup env vars
# ARG DISCORD_AUTH_TOKEN
# RUN touch .env
# RUN echo "DISCORD_AUTH_TOKEN=${DISCORD_AUTH_TOKEN}" >> .env


ENTRYPOINT ["java", "-jar", "/usr/local/lib/kgnome-bot.jar"]