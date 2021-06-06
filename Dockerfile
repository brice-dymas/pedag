FROM gradle:7.0-jdk11 AS development
WORKDIR /opt/app
RUN apt update
RUN apt install -y nodejs
ENTRYPOINT gradle bootRun & \
           gradle build --continuous --exclude-task testClasses
COPY gradle/*.gradle ./gradle/
COPY build.gradle settings.gradle gradle.properties sonar-project.properties ./
RUN gradle dependencies
COPY . ./
RUN gradle build \
    --no-daemon \
    --no-build-cache \
    --no-configuration-cache \
    --exclude-task testClasses
