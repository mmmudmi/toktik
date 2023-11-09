# TOKTIK
a short-video platform project🚀

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

## How to run 

```bash
docker compose build
```

```bash
docker up -d
```


