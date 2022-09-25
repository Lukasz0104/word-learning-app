FROM eclipse-temurin:17-alpine

COPY ./target/wordapp*.jar wordapp.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=""

ENTRYPOINT ["java", "-jar", "wordapp.jar"]
