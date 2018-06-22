# server

The Mechabellum server hosts a game and is responsible for enforcing all game rules.

## Docker

A Dockerfile is provided to build a Mechabellum server image based on the local development environment.

### Build

Prior to building the image, the Gradle `build` task must be run to generate the required artifacts. Then build the image using the following command:

```
$ docker build --tag mechabellum/server:latest .
```

### Run

Start a new container using the following command:

```
$ docker run -d --name=mechabellum-server -p 8080:8080 mechabellum/server
```
