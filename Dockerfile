FROM openjdk:15
VOLUME /tmp
ADD ./target/retos-subscripciones-0.0.1-SNAPSHOT.jar subscripciones.jar
ENTRYPOINT ["java","-jar","/subscripciones.jar"]