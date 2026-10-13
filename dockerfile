FROM eclipse-temurin:21-jre-jammy


COPY target/shop-mvc-spring.war app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]