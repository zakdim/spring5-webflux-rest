[//]: # ([![CircleCI]&#40;https://dl.circleci.com/status-badge/img/gh/zakdim/spring5-webflux-rest/tree/master.svg?style=svg&#41;]&#40;https://dl.circleci.com/status-badge/redirect/gh/zakdim/spring5-webflux-rest/tree/master&#41;)

# RESTFul WebService with Spring WebFlux

An example application for `Section 26: RESTFul WebService with Spring WebFlux` of the Udemy course:
[Spring Framework 5 - Beginner to Guru](https://www.udemy.com/course/spring-framework-5-beginner-to-guru/)

* [Spring Boot Gradle Reference project](https://github.com/zakdim/spring5-reactive-mongo-recipe-app)

## Section 26: RESTFul WebService with Spring WebFlux

### Lecture 410: Assignment Review

* Install mongo docker image :

```shell
docker login
...
# For better security, log in with a limited-privilege personal access token. 
# Learn more at https://docs.docker.com/go/access-tokens/
docker pull mongo:6.0.3
```

* How to run mongo docker container

See https://hub.docker.com/_/mongo

```shell
$ docker run --name mongo-sfg -d -p 27017:27017 mongo:6.0.3

 $ docker ps
CONTAINER ID   IMAGE         COMMAND                  CREATED         STATUS         PORTS                      NAMES
0cc6f5736a0b   mongo:6.0.3   "docker-entrypoint.s…"   4 seconds ago   Up 2 seconds   0.0.0.0:27017->27017/tcp   mongo-sfg

# Useful commands:
docker stop mongo-sfg
docker rm mongo-sfg
```

* To access MongoDB use [Roboto 3T](https://robomongo.org/)

### Lecture 411: Create Category Controller

* To test CategoryController with curl :

```shell
# Get all categories
curl -X 'GET' 'http://localhost:8080/api/v1/categories' -H 'accept: */*'
curl -X 'GET' 'http://localhost:8080/api/v1/categories' -H 'Accept: application/json'

# Get category by ID
curl -X 'GET' 'http://localhost:8080/api/v1/categories/637ad990ce9b462f15e82923' -H 'Accept: */*'

```
