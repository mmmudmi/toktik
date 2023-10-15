import subprocess
import os
from typing import Final

import S3Connector
import DatabaseConnector
import RedisConnector

VIDEO_INPUT: Final[str] = r"Video_Input"
VIDEO_OUTPUT: Final[str] = r"Video_Output"
IMAGE_OUTPUT: Final[str] = r"Image_Output"
VIDEO_CHUNK_SEC: Final[str] = "2"


def extract_name_ext(file: str) -> tuple[str, str]:
    temp = file.index(".")
    return file[:temp], file[temp + 1:]


def upload(s3_client, filename: str):
    for i in os.listdir(VIDEO_OUTPUT):
        if not i.startswith(filename):
            continue
        if ".m3u8" in i:
            S3Connector.upload_s3_file(s3_client, f"{VIDEO_OUTPUT}/{i}", i)
        else:
            S3Connector.upload_s3_file(s3_client, f"{VIDEO_OUTPUT}/{i}", f"{filename}/{i}")
    S3Connector.upload_s3_file(s3_client, f"{IMAGE_OUTPUT}/{filename}.jpg", f"{filename}.jpg")


def delete(filename: str) -> None:
    for i in os.listdir(VIDEO_INPUT):
        if i.startswith(filename):
            os.remove(f"{VIDEO_INPUT}/{i}")
            break
    for i in os.listdir(IMAGE_OUTPUT):
        if i.startswith(filename):
            os.remove(f"{IMAGE_OUTPUT}/{i}")
            break
    for i in os.listdir(VIDEO_OUTPUT):
        if i.startswith(filename):
            os.remove(f"{VIDEO_OUTPUT}/{i}")


if __name__ == '__main__':
    print("Worker started")
    s3_client = S3Connector.get_s3_client()
    # db_cursor = DatabaseConnector.getConnection().cursor()
    redis_db = RedisConnector.redis_db()
    if not os.path.exists(VIDEO_INPUT):
        os.mkdir(VIDEO_INPUT)
    if not os.path.exists(VIDEO_OUTPUT):
        os.mkdir(VIDEO_OUTPUT)
    if not os.path.exists(IMAGE_OUTPUT):
        os.mkdir(IMAGE_OUTPUT)
    file = "8f2835e3-3099-4cdf-bcb1-eb6b4b5a5044.mp4"

    while True:
        print("Load queue")
        file = RedisConnector.redis_queue_pop(redis_db)
        print("get ", file)
        filename, extension = extract_name_ext(file)
        S3Connector.download_s3_file(s3_client, f"{VIDEO_INPUT}/{file}", file)
        subprocess.run(["ffmpeg", "-i", f"{VIDEO_INPUT}/{file}", "-ss", "00:00:01.000", "-vframes", "1",
                        f"{IMAGE_OUTPUT}/{filename}.jpg"])
        subprocess.run(["ffmpeg", "-i", f"{VIDEO_INPUT}/{file}", "-codec:", "copy", "-start_number", "0", "-hls_time",
                        VIDEO_CHUNK_SEC, "-hls_list_size", "0", "-f", "hls", f"{VIDEO_OUTPUT}/{filename}.m3u8"])
        upload(s3_client, filename)
        print("Successfully upload file")
        delete(filename)
        S3Connector.delete_s3_file(s3_client, file)
        print("Clear files")

