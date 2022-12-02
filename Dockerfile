FROM tomcat:9.0.65-jre11-openjdk

ARG TOMCAT_HOME=/usr/local/tomcat

USER root:root

COPY target/parabank.war ${TOMCAT_HOME}/webapps

# To enable injecting Virtualize JDBC driver into ParaBank
RUN unzip ${TOMCAT_HOME}/webapps/parabank.war -d ${TOMCAT_HOME}/webapps/parabank

EXPOSE 8080
EXPOSE 61616
EXPOSE 9001