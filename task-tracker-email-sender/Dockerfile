FROM eclipse-temurin:23-jre-alpine
RUN adduser --system app-user
COPY ./build/libs/*.jar app.jar
EXPOSE 8080
USER app-user
CMD ["java", "-jar", "app.jar"]
LABEL authors="Vadim"