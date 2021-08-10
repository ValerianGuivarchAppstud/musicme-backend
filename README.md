# swag-backend

![production deploy workflow](https://github.com/appstud/appstud-swag-kotlin-backend/workflows/production%20deploy%20workflow/badge.svg)

## Technology Stack

- Programming language : [Kotlin](https://kotlinlang.org/)
- Framework : [Spring Boot 2.1](https://projects.spring.io/spring-boot/)
- Data sources : [MongoDB](https://www.mongoDB.com)


## Contributing

### Prerequisites

- Java JDK 8
- [IntelliJ Idea (Community Edition)](https://www.jetbrains.com/idea/download/#section=mac)

### Importing project
1. Import the project in IntelliJ :
    - File ; New  Project for existing sources
    - Choose `Gradle` when asked
2. Setup the [Ktlint](https://ktlint.github.io/) linter tools with `./gradlew setupProject`
    -  Configures intelliJ code formatter
    -  Adds a git commit prehook to check code format with ktlint
    -  Note: Needs ktlint to be installed first ```brew install shyiko/ktlint/ktlint```
    -  To ensure sourcetree works with ktlint, add `export PATH=/usr/local/bin:$PATH` in `.git/hooks/pre-commit`
3. Start the application :
    - Right-click on file `IkarBackendApplication.kt`; Run
4. Set an active profile :
    - Application is most likely to crash without a [Spring Boot active profile](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)
    - Run ; Edit Configurations
    - Add `-Dspring.profiles.active=local` in `VM options` configuration

### database and mail server

You can use the docker-compose for local devs : `support/docker-dev/docker-compose.yml`
It will run a mongoDB container running on the default port and smtp4dev (local smtp server with a web UI) : `http://127.0.0.1:8081`

#####################################################################################
### Dependencies updates
#####################################################################################
add this plugin in gradle config :
```
plugins {
    id("com.github.ben-manes.versions") version "0.22.0"
}
```

then generate the report : `./gradlew dependencyUpdates -Drevision=release`


#####################################################################################
### Documentation
#####################################################################################
There is swagger configured in local/dev profiles  http://<server url>/swagger-ui/index.html


#####################################################################################
### CI
#####################################################################################
A bitbucket pipeline is configured on branches `master` and `staging` to execute all tests.
The configuration is managed in `bitbucket-pipelines.yml`
The file `bitbucket-pipelines-known-hosts` contains the ssh signatures of deployment servers, it is used by the pipelines.

- sprint* branches :
deployment is fully automatic at each commit on a sprint* branch

- master/staging branches :
build is fully automatic at each commit on the branch, the deployment has to be manually triggered on bitbucket :
https://bitbucket.org/appstud/ikar_backend/addon/pipelines/home

If you don't want to trigger an automatic build, add `[skip ci]` or `[ci skip]` to your commit message

#####################################################################################
### Deployment
#####################################################################################
#### DEV

The dev deployment is using heroku :

https://dashboard.heroku.com/apps/swagger-backend
https://dashboard.heroku.com/apps/swagger-backoffice

Some env vars must be set (`https://dashboard.heroku.com/apps/swagger-backend/settings`) :
```
PORT
APPSTUD_LIBRARIES_SECURITY_JWT_SECRETKEY
SPRING_MAIL_PASSWORD
SPRING_PROFILES_ACTIVE
SPRING_DATA_MONGODB_URI
```

Local setup : https://dashboard.heroku.com/apps/swagger-backend/deploy/heroku-git

Build & deployment : see the CI section


#### PREPROD

The preprod deployment is using an OVH VPS (VPS Cloud RAM 2) : *******
It is managed with a docker-compose file (`docker/docker-compose.yml`)
A few env vars are required and must be set on the server (see `docker/.env.tpl`)

To access the server, see the shared credential file (gdrive)

Build & deployment : see the CI section


#### PROD

The preprod deployment is using an OVH VPS (VPS Cloud RAM 3) : *******
It is managed with a docker-compose file (`docker/docker-compose.yml`)
A few env vars are required and must be set on the server (see `docker/.env.tpl`)

To access the server, see the shared credential file (gdrive)

Build & deployment : see the CI section


#####################################################################################
## Technical details
#####################################################################################

### Profiles & Configuration

The applications uses [Spring Boot standard profiles configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).
Configuration files are located in the folder [./src/main/resources/config](./src/main/resources/config) and are using [YAML format](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-loading-yaml)

### Linter

This application uses [ktlint](https://ktlint.github.io/) to keep code syntax consistency across project.

In order to automatically fix linter issues, run `./gradlew ktlintFormat`

### Code

Source code is located at [src/main/kotlin/com/ikar/backend](./src/main/kotlin/com/ikar/backend)

Unit & endpoints tests are located at [src/test/kotlin/com/ikar/backend](./src/main/kotlin/com/ikar/backend)

### Local server for mobile testing

#### Install ngrok
```
brew cask install ngrok
```
_Note:_ Or install from https://ngrok.com/download

#### Run ngrok server
```
ngrok http -region eu 8080
```
_Note:_ https url is displayed in the console
```
Forwarding                    https://ac1ff9c5.eu.ngrok.io -> localhost:8080
```
