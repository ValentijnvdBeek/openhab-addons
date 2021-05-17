    FROM maven:3.8.1-jdk-11
    MAINTAINER Valentijn van de Beek <v.d.vandebeek@student.tudelft.nl>

    WORKDIR /work
    COPY . . 

    RUN mvn clean install -pl :org.openhab.binding.mavlinkardupilot -DskipChecks -DskipTests
