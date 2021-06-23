FROM gradle:7.0-jdk11 AS development
WORKDIR /opt/app
#RUN curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.30.1/install.sh | bash
#RUN nvm install v14.16.0
ADD https://nodejs.org/dist/v14.16.0/node-v14.16.0-linux-x64.tar.gz /opt/
RUN tar -xf ../node-v14.16.0-linux-x64.tar.gz -C /opt/
ENV PATH=/opt/node-v14.16.0-linux-x64/bin:$PATH
ARG PATH=/opt/node-v14.16.0-linux-x64/bin:$PATH
ENTRYPOINT gradle bootRun & \
           gradle build --continuous --exclude-task testClasses
COPY gradle/*.gradle ./gradle/
COPY build.gradle settings.gradle gradle.properties sonar-project.properties ./
RUN gradle dependencies
COPY . ./
RUN npm install
RUN gradle build \
    --no-daemon \
    --no-build-cache \
    --no-configuration-cache \
    --exclude-task testClasses

FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine
WORKDIR /opt/app
CMD ["java", "-jar", "pedag-0.0.1-SNAPSHOT.jar"]
COPY --from=development /opt/app/build/libs/pedag-0.0.1-SNAPSHOT.jar .

