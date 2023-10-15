from typing import Final
import yaml

with open("config.yaml", mode="r", encoding="utf-8") as config_file:
    config: Final[dict[str, dict]] = yaml.safe_load(config_file)


def get_redis_config(key: str) -> str | int | None:
    return config["redis"].get(key)


def get_s3_config(key: str) -> str | int | None:
    return config["s3"].get(key)


def get_db_config(key:str)-> str|int|None:
    return config["db"].get(key)