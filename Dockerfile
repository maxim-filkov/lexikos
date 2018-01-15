FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/translator-1.0.0-exec.jar translator.jar
EXPOSE 8080
RUN sh -c 'touch /translator.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/translator.jar"]