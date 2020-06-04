FROM openjdk
EXPOSE 8081
COPY target/samp_send-1.0-jar-with-dependencies.jar /opt/samp_send-1.0-jar-with-dependencies.jar
WORKDIR /opt
CMD [ "java", "-cp", "samp_send-1.0-jar-with-dependencies.jar", "sampleSender.App" ] 
