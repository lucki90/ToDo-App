#!/bin/bash

MYSQL_VERSION=5.6
MYSQL_CONTAINER_NAME=mysql-standalone
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE_NAME=todo_app
MYSQL_USER=root
MYSQL_PASSWORD=root
APP_IMAGE_NAME=todo-app-img
APP_CONTAINER_NAME=todo-app

#apt-get update
echo "Checking maven..."
if [[ "$(dpkg -l | grep maven)" == "" ]]; then
    echo "Installing maven"
    apt-get install -y maven
    echo "OK - maven installation completed."
else
    echo "OK - maven has already been installed."
fi

echo "Checking docker..."
if [[ "$(dpkg -l | grep docker)" = "" ]]; then
    apt-get install -y libltdl7
    export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
    wget https://download.docker.com/linux/debian/dists/buster/pool/stable/amd64/docker-ce_18.06.3~ce~3-0~debian_amd64.deb
    dpkg -i  docker-ce_18.06.3~ce~3-0~debian_amd64.deb
#sudo usermod -aG docker root
    echo "OK - docker installation completed."
else
    echo "OK - docker has already been installed."
fi

echo "Preparing for making todo-app.war..."
mvn clean
echo "Begin making todo-app.war..."
mvn install -Dmaven.test.skip=true


echo "Checking mysql image..."
if [[ "$(docker images | grep mysql)" == "" ]]; then
    echo "Pulling mysql:${MYSQL_VERSION}"
    docker pull mysql:${MYSQL_VERSION}
    echo "OK - Pulling mysql:${MYSQL_VERSION} complete."
else
    echo "OK - Image has already been pulled."
fi

echo "Creating and running container from image mysql:${MYSQL_VERSION}..."
if [[ "$(docker ps -a | grep ${MYSQL_CONTAINER_NAME} | grep up)" == "" ]]; then
    if [[ "$(docker ps -a | grep ${MYSQL_CONTAINER_NAME} )" != "" ]]; then
    echo "Removing unused container: ${MYSQL_CONTAINER_NAME}"
    docker rm -f ${MYSQL_CONTAINER_NAME}
    fi
docker run --name ${MYSQL_CONTAINER_NAME} \
    -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
    -e MYSQL_DATABASE=${MYSQL_DATABASE_NAME} \
    -e MYSQL_USER=${MYSQL_USER} \
    -e MYSQL_PASSWORD=${MYSQL_PASSWORD} \
    -d mysql:${MYSQL_VERSION}
else
    echo "Container already created"
fi

echo "Building todo-app image from Dockerfile..."
docker build -t ${APP_IMAGE_NAME} .

echo "Building todo-app container..."
if [[ "$(docker ps -a | grep ${MYSQL_CONTAINER_NAME} | grep up)" == "" ]]; then
    docker start ${MYSQL_CONTAINER_NAME}
fi
docker run -p 8080:8080 --name ${APP_CONTAINER_NAME} --link ${MYSQL_CONTAINER_NAME}:mysql -d ${APP_IMAGE_NAME}

#/TODO temporary solution - user_role doesn't add to DB from schema.sql when running app on docker
mysql --user="root" --password="root" --database="todo_app" --execute="INSERT INTO todo_app.user_role (role, description) VALUES ('ROLE_ADMIN', 'Access to all data');"
mysql --user="root" --password="root" --database="todo_app" --execute="INSERT INTO todo_app.user_role (role, description) VALUES ('ROLE_USER', 'Access to one user data');"