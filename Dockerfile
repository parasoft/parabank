FROM tomcat:9.0.65-jre11-openjdk

ARG TOMCAT_HOME=/usr/local/tomcat

USER root:root

COPY target/parabank.war ${TOMCAT_HOME}/webapps

EXPOSE 8080
EXPOSE 61616