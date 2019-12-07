#!/bin/bash

MYSQL_VERSION=5.6
MYSQL_CONTAINER_NAME=mysql-standalone
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE_NAME=todo_app
MYSQL_USER=root
MYSQL_PASSWORD=root
APP_IMAGE_NAME=todo-app-img
APP_CONTAINER_NAME=todo-app

#============================INSTALL MAVEN=====================================#
echo "$(tput setaf 3)Checking maven...$(tput sgr0)"
if [[ "$(dpkg -l | grep maven)" == "" ]]; then
    echo "$(tput setaf 3)Installing maven$(tput sgr0)"
    apt-get install -y maven
    echo "$(tput setaf 2)OK - maven installation completed.$(tput sgr0)"
else
    echo "$(tput setaf 2)OK - maven has already been installed.$(tput sgr0)"
fi
#===============================================================================#

#============================INSTALL DOCKER=====================================#
echo "$(tput setaf 3)Checking docker...$(tput sgr0)"
if [[ "$(dpkg -l | grep docker)" = "" ]]; then
    echo "$(tput setaf 3)Installing docker$(tput sgr0)"
    apt-get install -y libltdl7
    export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
    wget https://download.docker.com/linux/debian/dists/buster/pool/stable/amd64/docker-ce_18.06.3~ce~3-0~debian_amd64.deb
    dpkg -i  docker-ce_18.06.3~ce~3-0~debian_amd64.deb
    echo "$(tput setaf 2)OK - docker installation completed.$(tput sgr0)"
else
    echo "$(tput setaf 2)OK - docker has already been installed.$(tput sgr0)"
fi
#======================================================================================#

#============================PULL MSQL IMAGE===========================================#
echo "$(tput setaf 3)Checking mysql image...$(tput sgr0)"
if [[ "$(docker images | grep mysql)" == "" ]]; then
    echo "$(tput setaf 3)Pulling mysql:${MYSQL_VERSION}$(tput sgr0)"
    docker pull mysql:${MYSQL_VERSION}
    echo "$(tput setaf 2)OK - Pulling mysql:${MYSQL_VERSION} complete.$(tput sgr0)"
else
    echo "$(tput setaf 2)OK - Image has already been pulled.$(tput sgr0)"
fi
#======================================================================================#

#============================RUN MSQL CONTAINER========================================#
echo "$(tput setaf 3)Creating and running container from image mysql:${MYSQL_VERSION}...$(tput sgr0)"
if [[ "$(docker ps -a | grep ${MYSQL_CONTAINER_NAME} | grep up)" == "" ]]; then
    if [[ "$(docker ps -a | grep ${MYSQL_CONTAINER_NAME} )" != "" ]]; then
    echo "$(tput setaf 3)Removing unused container: ${MYSQL_CONTAINER_NAME}$(tput sgr0)"
    docker rm -f ${MYSQL_CONTAINER_NAME}
    fi
docker run --name ${MYSQL_CONTAINER_NAME} \
    -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
    -e MYSQL_DATABASE=${MYSQL_DATABASE_NAME} \
    -e MYSQL_USER=${MYSQL_USER} \
    -e MYSQL_PASSWORD=${MYSQL_PASSWORD} \
    -d mysql:${MYSQL_VERSION}
    echo "$(tput setaf 2)OK - has been created.$(tput sgr0)"
else
    echo "$(tput setaf 2)Container already created$(tput sgr0)"
fi
#======================================================================================#

#============================CREATE APP WAR============================================#
echo "$(tput setaf 3)Preparing for making todo-app.war...$(tput sgr0)"
mvn clean
echo "$(tput setaf 3)Begin to make todo-app.war...$(tput sgr0)"
mvn install -Pdocker -DprofileIdEnabled=true -Dmaven.test.skip=true
echo "$(tput setaf 2)OK -todo-app.war has been made.$(tput sgr0)"
#======================================================================================#

#============================BUILD APP IMAGE===========================================#
echo "$(tput setaf 3)Building todo-app image from Dockerfile...$(tput sgr0)"
docker build -t ${APP_IMAGE_NAME} .
echo "$(tput setaf 2)OK - image has been built.$(tput sgr0)"
#======================================================================================#

#============================CHECK MYSQL CONTAINER=====================================#
if [[ "$(docker ps -a | grep ${MYSQL_CONTAINER_NAME} | grep up)" == "" ]]; then
    echo "$(tput setaf 3)Starting mysql container...$(tput sgr0)"
    docker start ${MYSQL_CONTAINER_NAME}
    echo "$(tput setaf 2)OK - mysql container has been started.$(tput sgr0)"
fi
#======================================================================================#

#============================RUN APP CONTAINER=========================================#
echo "$(tput setaf 3)Building todo-app container...$(tput sgr0)"
docker run -p 8080:8080 --name ${APP_CONTAINER_NAME} --link ${MYSQL_CONTAINER_NAME}:mysql -d ${APP_IMAGE_NAME}
echo "$(tput setaf 2)OK - todo-app is up.$(tput sgr0)"
#======================================================================================#

#============================ADD BASIC ROLES TO DB=====================================#
sleep 10s
docker exec mysql-standalone mysql --user="root" --password="root" --database="todo_app" --execute="INSERT INTO todo_app.user_role (role, description) VALUES ('ROLE_ADMIN', 'Access to all data'), ('ROLE_USER', 'Access to one user data');"
#======================================================================================#
