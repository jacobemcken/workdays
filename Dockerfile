FROM java:7
COPY ./target/workdays-1.1.0-standalone.jar /
WORKDIR /
CMD [ "java", "-verbose", "-jar", "/workdays-1.1.0-standalone.jar" ]
