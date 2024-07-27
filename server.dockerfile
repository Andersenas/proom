FROM maven:3.9.7-eclipse-temurin-22-alpine as builder

WORKDIR /app

ADD pom.xml .
ADD engine/pom.xml ./engine/pom.xml
ADD server/pom.xml ./server/pom.xml

RUN mvn --batch-mode -pl engine verify --fail-never
ADD engine ./engine
RUN mvn --batch-mode -pl engine install -DskipTests -Dcheckstyle.skip

RUN mvn --batch-mode -pl server verify --fail-never
ADD server ./server
RUN mvn --batch-mode -pl engine,server package -DskipTests -Dcheckstyle.skip

RUN cp server/target/server-1.0-SNAPSHOT.jar /app/application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:22-jre-alpine as runtime

WORKDIR /app

COPY --from=builder /app/dependencies ./
COPY --from=builder /app/snapshot-dependencies ./
COPY --from=builder /app/spring-boot-loader ./
COPY --from=builder /app/application ./

RUN addgroup -S proomusr && adduser -S proomusr -G proomusr
USER proomusr

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
