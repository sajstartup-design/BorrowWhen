FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app  
COPY Borrowhen/pom.xml .  
RUN mvn dependency:go-offline  
COPY Borrowhen/. . 
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /app/target/Borrowhen-0.0.1-SNAPSHOT.jar Borrowhen.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Borrowhen.jar"]