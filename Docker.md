# Docker


Docker pull <IMage> // pulls the image frm hub.docker.com
docker run <IMG> // if image is not there then it will pull and run it. It will create new container
docker run -d  <IMG> // this will run in detach mode
docker start CONT_NAME or CONT_ID // it will start the exisiting container , it will not create new
docker run -it <IMG> // it will start docker in interactive mode
docker image ls // it will show all images of docker
docker ps // show all running containers
docker ps -a // show all container wether its running or not
docker rmi <IMG_NAME> // remove image  - NOte : To delete image , you need to delete container first
docker rm <CONT_NAME> // remove container
docker -p8080:3360 ..... /// this -p is port binding to container. So host having 8080 is bind to container 3360 port
docker logs <Cont_Name> // give you logs 
docker exec -it <Cont_name or id> // you will be in container shell and you can run anything in running container
env // give you environment variables
-e // it is used to set environment variable when using command
docker network ls // give list of networks
docker network rm <networkname> // delte netwtowrk


Difference between Docker and vm 

there are 3 layers in OS 

```mermaid
flowchart TD
    A[Application Layer] --> B(Host OS Kernel)
    B --> C(Hardware)
```

Docker virtualize only `Application layer` and uses `Host OS kernel` of host but VM virtualize the `Host OS kernel ` and `Application layer` both. 

that's the reason docker is light weight


How two container interact with each other?
- Docker network helps in this.
to check : `docker network ls`
To create network : `docker network create <ANY NAME>`


--- Example

we have application which need mongo db so we will setup mongo and mongo-express

Application : https://github.com/shradha-khapra/docker-testapp

mongo : https://hub.docker.com/_/mongo
mongo-express : https://hub.docker.com/_/mongo-express


What we need : 
we need that this db will work in background, in some network so that interative with mongo-express, port binding to our host machine(container port is mentinoed in docs) , some name also, we need to set username(admin), password(qwerty)

for express we need to define url also which has format like : `ME_CONFIG_MONGODB_URL="mongodb://USERNAME:PASSWORD@MongocontainerName:27017"`

<details>
  <summary>Program.</summary>

```
docker run -d \
-p27017:27017 \
--name mongo
--network mongo-network \
-e MONGO_INITDB_ROOT_USERNAME=admin \
-e MONGO_INITDB_ROOT_PASSWORD=qwerty \
mongo

docker ps

docker run -d \
-p8081:8081 \
--name mongo-express \ 
--network mongo-network \
-e ME_CONFIG_MONGODB_ADMINUSERNAME=admin \
-e ME_CONFIG_MONGODB_ADMINPASSWORD=qwerty \
-e ME_CONFIG_MONGODB_URL="mongodb://admin:qwerty@mongo:27017" \
mongo-express

docker ps
```

</details>






now you can access using : localhost:8081
but creds are different here. use `admin` and `pass` to login

Note : if container is stoped then all db will be gone. so to solve this docker volumes are in place.

### Docker Compose

we are writing commands to run containers and associate mutliple variables. To make this easy docker compose comes into picture. it makes life easy by putting all comands in file . which we call YAML file(yet another markup language). 

Defination : it is a tool for defining and running multi-container applications.

it looks like this 
```yaml

services:

    mongo:
        image: mongo
        restart: always
        ports:
        - 27017:27017
        environment:
            MONGO_INITDB_ROOT_USERNAME: admin
            MONGO_INITDB_ROOT_PASSWORD: qwerty

    mongo-express:
        image: mongo-express
        restart: always
        ports:
        - 8081:8081
        environment:
            ME_CONFIG_MONGODB_ADMINUSERNAME: admin
            ME_CONFIG_MONGODB_ADMINPASSWORD: qwerty
            ME_CONFIG_MONGODB_URL: mongodb://admin:qwerty@mongo:27017/
            ME_CONFIG_BASICAUTH: false

```

To run containers 
`docker compose -f filenanem.yaml up -d`
to delete containers
`docker compose -f filenanem.yaml down`
this will delete network also with containers

When we use docker compose we don't need to create network because whatever containers defined in yamal file, compose default put them in network which is created by itself.




## Dockerizing App
https://docs.docker.com/get-started/docker-concepts/building-images/writing-a-dockerfile/


This is set of instructions
BELOW ARE IMPORTANT INSTRCTURIONS

```
FROM // this is base image from which it start like to start app, we need node so we need node image first.

WORKDIR // where main work will happen
 
COPY // this will copy the code or file , format is like host : IMG

RUN // whatever is written execute in image ,there may be multipel run commands

CMD // this is singel command which tells that how you want to run the applciation

EXPOSE

ENV

```

to build image `docker build -t testapp:1.0 . `
-t means tag
. is used for current directory where docker file is there.


## Publish

you can publish the docker image and you don't need the whole code for that. if you havfe published then only use command of docker and run the application. easy-peasy


## Docker Volumes

simple form a memory is attached to container from host

`-v` is used to tell that this volume is used

`-v <absolute path of host>:<path of container>` 

```
docker run -it -v /Users/joh.doe/Desktop/data:/test/data ubuntu
```

now whatever data docker saves in test/data folder it will be copied to desktop/data folder. and when container restart then it will pick these files from this folder 

and if container get deleted then also data persist in host machine

to check : 
stop the container and start again

```
docker start <CONTID>
docker exec -it <CONTID> /bin/bash
```

To work With compose, you need to define volume in yaml file

```yaml
services:

    mongo:
        image: mongo
        restart: always
        ports:
        - 27017:27017
        environment:
            MONGO_INITDB_ROOT_USERNAME: admin
            MONGO_INITDB_ROOT_PASSWORD: qwerty
        volumes:
        -hostdir:container_dir
```


you can check all volumes , create new volume and delete
```
docker volume ls
docker volume create <NAME> // called named volumes
dockeer volume rm <NAME>
```

you havn't given path so docker default create on below path in system
- Windows : C:\ProgramData\Docker\Volume
- Mac\Linux : var\lib\docker\volumes

Usage of volumes

1. Named Volumes : `docker run -v VOL_name:Cont_Dir` , if volume is not created , docker will create it. and this is popular way to use, its mostly used in production enviornments

2. Anonymous Volumes : `docker run -v Cont_dir`, this is used for temperatory storage for containers

3. Bind Mount : `docker run -v Host_dir:Cont_dir`, we bind host directory to container

There small difference in bind mount and other two.
Bind mount is volume hosted by host OS means managed by OS
but in other two , its manageed by docker

to delete unused volumes by containers 
```
docker volume prune
```




Commands

Docker Commands
IMAGES :
List all Local images
docker images
Delete an image
docker rmi <image_name>
Remove unused images
docker image prune
Build an image from a Dockerfile
docker build -t <image_name>:<version> . //version is optional
docker build -t <image_name>:<version> . -no-cache //build without cache
CONTAINER :
List all Local containers (running & stopped)
docker ps -a
List all running containers
docker ps
Create & run a new container
docker run <image_name>
//if image not available locally, it’ll be downloaded from DockerHub
Run container in background
docker run -d <image_name>
Run container with custom name
docker run - -name <container_name> <image_name>
Port Binding in container
docker run -p<host_port>:<container_port> <image_name>
Set environment variables in a container
docker run -e <var_name>=<var_value> <container_name> (or <container_id)
Start or Stop an existing container
docker start|stop <container_name> (or <container_id)
Inspect a running container
docker inspect <container_name> (or <container_id)
Delete a container
docker rm <container_name> (or <container_id)
TROUBLESHOOT :
Fetch logs of a container
docker logs <container_name> (or <container_id)
Open shell inside running container
docker exec -it <container_name> /bin/bash
docker exec -it <container_name> sh
DOCKER HUB :
Pull an image from DockerHub
docker pull <image_name>
Publish an image to DockerHub
docker push <username>/<image_name>
Login into DockerHub
docker login -u <image_name>
Or
docker login
//also, docker logout to remove credentials
Search for an image on DockerHub
docker search <image_name>
VOLUMES :
List all Volumes
docker volume ls
Create new Named volume
docker volume create <volume_name>
Delete a Named volume
docker volume rm <volume_name>
Mount Named volume with running container
docker run - -volume <volume_name>:<mount_path>
//or using - -mount
docker run - -mount type=volume,src=<volume_name>,dest=<mount_path>
Mount Anonymous volume with running container
docker run - -volume <mount_path>
To create a Bind Mount
docker run - -volume <host_path>:<container_path>
//or using - -mount
docker run - -mount type=bind,src=<host_path>,dest=<container_path>
Remove unused local volumes
docker volume prune //for anonymous volumes
NETWORK :
List all networks
docker network ls
Create a network
docker network create <network_name>
Remove a network
docker network rm <network_name>
Remove all unused networks
docker network prune

ref:
https://www.apnacollege.in/notes