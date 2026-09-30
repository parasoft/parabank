FROM tomcat:11.0.26-jre25-temurin-noble

ARG TOMCAT_HOME=/usr/local/tomcat

USER root:root

COPY target/parabank.war ${TOMCAT_HOME}/webapps

# update packages
# unzip is to enable injecting the Virtualize JDBC driver
RUN apt update && \
    apt dist-upgrade -y && \
    apt install -y --no-install-recommends \
        unzip && \
    apt autoremove -y && \
    apt clean -y && \
    rm -rf /var/lib/apt/lists/* && \
    unzip ${TOMCAT_HOME}/webapps/parabank.war -d ${TOMCAT_HOME}/webapps/parabank && \
    rm ${TOMCAT_HOME}/webapps/parabank.war

EXPOSE 8080
EXPOSE 61616
EXPOSE 9001
