FROM java:8 
VOLUME /tmp
ADD ./picture-control-api-0.0.1-SNAPSHOT.jar www.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/www.jar"]
