import os
import subprocess
from typing import Final

import RedisConnector
import S3Connector
import worker_util

INPUT_QUEUE: Final[str] = RedisConnector.get_redis_config("chunk_queue")
VIDEO_CHUNK_SEC: Final[str] = "5"


def upload_playlist(s3_client, filename: str):
    for i in os.listdir(worker_util.VIDEO_OUTPUT):
        if not i.startswith(filename):
            continue
        if ".m3u8" in i:
            S3Connector.upload_s3_file(s3_client, f"{worker_util.VIDEO_OUTPUT}/{i}", i)
        else:
            S3Connector.upload_s3_file(
                s3_client, f"{worker_util.VIDEO_OUTPUT}/{i}", f"{filename}/{i}"
            )


if __name__ == '__main__':
    worker_util.create_dir()
    print("PreviewWorker start..")
    s3_client = S3Connector.get_s3_client()
    redis_db = RedisConnector.redis_db()
    while True:
        print("waiting for queue")
        file = RedisConnector.redis_queue_pop(redis_db, INPUT_QUEUE)
        filename, extension = worker_util.extract_name_ext(file)
        output_file = f"{filename}.m3u8"
        S3Connector.download_s3_file(s3_client, f"{worker_util.VIDEO_INPUT}/{file}", file)
        subprocess.run(
            [
                "ffmpeg",
                "-i",
                f"{worker_util.VIDEO_INPUT}/{file}",
                "-codec:",
                "copy",
                "-start_number",
                "0",
                "-hls_time",
                VIDEO_CHUNK_SEC,
                "-hls_list_size",
                "0",
                "-f",
                "hls",
                f"{worker_util.VIDEO_OUTPUT}/{output_file}",
            ]
        )
        upload_playlist(s3_client, filename)
        S3Connector.delete_s3_file(s3_client, file)
        RedisConnector.redis_publish(redis_db, filename)
        worker_util.clean_dir(filename)
