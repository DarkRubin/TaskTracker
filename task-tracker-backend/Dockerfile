FROM gradle:jdk23-alpine AS builder
WORKDIR /opt/app
COPY build.gradle ./
COPY /src ./src
RUN gradle clean build -x test --no-daemon


FROM eclipse-temurin:23-jre-alpine
RUN adduser --system app-user
COPY --from=builder --chown=app-user:app-user /opt/app/build/libs/*.jar app.jar
EXPOSE 8080
USER app-user
CMD ["java", "-jar", "app.jar"]
LABEL authors="Vadim"
