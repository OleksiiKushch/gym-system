FROM maven:3.8.5-openjdk-17 as builder
ARG version=1.0-SNAPSHOT
ARG SKIP_TESTS=false
WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src/

RUN mvn clean package -DskipTests=$SKIP_TESTS
RUN cp target/trainer-manager-${version}.jar target/app.jar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app/

COPY --from=builder /build/target/app.jar /app/
ENTRYPOINT ["java", "-jar", "/app/app.jar"]