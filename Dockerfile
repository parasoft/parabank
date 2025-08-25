FROM tomcat:10.1.44-jre21-temurin-noble

ARG TOMCAT_HOME=/usr/local/tomcat

USER root:root

COPY target/parabank.war ${TOMCAT_HOME}/webapps

# To enable injecting Virtualize JDBC driver into ParaBank
RUN apt update && \
    apt install unzip && \
    unzip ${TOMCAT_HOME}/webapps/parabank.war -d ${TOMCAT_HOME}/webapps/parabank && \
    rm ${TOMCAT_HOME}/webapps/parabank.war

EXPOSE 8080
EXPOSE 61616
EXPOSE 9001
