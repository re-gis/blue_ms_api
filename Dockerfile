from openjdk:8
EXPOSE 8080
ADD target/blue-0.0.1-SNAPSHOT.jar target/blue-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java", "-jar", "/blue-0.0.1-SNAPSHOT.jar" ]