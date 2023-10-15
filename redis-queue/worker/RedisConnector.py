from typing import Final

from redis import Redis
from setting import get_redis_config

QUEUE_NAME: Final[str] = get_redis_config("queue_name")
HOST: Final[str] = get_redis_config("host")
PORT: Final[str] = get_redis_config("port")
PASSWORD: Final[str] = get_redis_config("password")
DB_NUMBER: Final[str] = get_redis_config("db_number")


def redis_db() -> Redis:
    db = Redis(host=HOST, port=PORT, db=DB_NUMBER, password=PASSWORD, decode_responses=True)
    db.ping()
    return db


def redis_queue_push(db: Redis, message: str):
    return db.lpush(QUEUE_NAME, message)


def redis_queue_pop(db) -> str:
    return db.brpop(QUEUE_NAME)[1]
