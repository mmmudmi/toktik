# TOKTIK
a short-video platform project🚀

## Frameworks
#### Frontend:
Vuetify
#### Backend:
Spring Boot
#### Workers:
Redis and python


## Components 

TOKTIK root

  +-- docker-compose.yml

  +-- frontend/

  ----+--- Dockerfile

  ----+--- ...

  +-- backend/  
     
  ----+--- Dockerfile

  ----+--- ...

  +-- redis-queue/

  ----+--- worker/

  ---------+--- worker/

  --------------+--- Dockerfile

  --------------+--- ...

  +-- k8s/

  +-- proxy/

## How to run docker

```bash
docker compose build
```

```bash
docker compose up -d
```


