#
# Dockerfile for building a Mechabellum server image based on the local development environment.
#

FROM openjdk:8-jre
MAINTAINER steven.soloff@gmail.com

ADD ./app/build/distributions/app-*.tar /opt

EXPOSE 8080

CMD ["/bin/sh", "-c", "/opt/app-*/bin/app"]
