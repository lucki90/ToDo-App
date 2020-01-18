FROM tomcat:8.0.51-jre8-alpine

RUN rm -rf /usr/local/tomcat/webapps/*

COPY ./../target/todo-app.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]