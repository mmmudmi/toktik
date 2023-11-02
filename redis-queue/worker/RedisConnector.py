from typing import Final

from redis import Redis
from setting import get_redis_config

FINISH_CHANNEL: Final[str] = get_redis_config("finish_channel")
ERR_CHANNEL: Final[str] = get_redis_config("error_channel")
HOST: Final[str] = get_redis_config("host")
PORT: Final[str] = get_redis_config("port")
PASSWORD: Final[str] = get_redis_config("password")
DB_NUMBER: Final[str] = get_redis_config("db_number")


def redis_db() -> Redis:
    db = Redis(host=HOST, port=PORT, db=DB_NUMBER, password=PASSWORD, decode_responses=True)
    db.ping()
    return db


def redis_queue_push(db: Redis, video_queue: str, message: str):
    return db.lpush(video_queue, message)


def redis_queue_pop(db: Redis, video_queue: str) -> str:
    return db.brpop(video_queue)[1]


def redis_publish(db: Redis, message: str):
    return db.publish(FINISH_CHANNEL, message)


def redis_error(db: Redis, message: str):
    return db.publish(ERR_CHANNEL, message)
