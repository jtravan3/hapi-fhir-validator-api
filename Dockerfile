# our base build image
FROM maven:3.6-jdk-11 as maven

# Add Maintainer Info
LABEL maintainer="john@jtravan.com"

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for release
RUN mvn package

# our final base image
FROM openjdk:11-jre-slim

# Make port 5000 available to the world outside this container
EXPOSE 5000

# set deployment directory
WORKDIR /hapi-fhir-validator-api

# copy over the built artifacts from the maven image
COPY --from=maven target/hapi-fhir-validator-api-*.jar hapi-fhir-validator-api.jar

# set the startup command to run your binary
CMD ["java", "-Xmx4096m", "-jar", "hapi-fhir-validator-api.jar"]