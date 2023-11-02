import subprocess
from typing import Final

import RedisConnector
import S3Connector
import worker_util

VIDEO_QUEUE: Final[str] = RedisConnector.get_redis_config("preview_queue")

if __name__ == '__main__':
    worker_util.create_dir()
    print("PreviewWorker start..")
    s3_client = S3Connector.get_s3_client()
    redis_db = RedisConnector.redis_db()
    while True:
        print("waiting for queue")
        file = RedisConnector.redis_queue_pop(redis_db, VIDEO_QUEUE)
        filename, extension = worker_util.extract_name_ext(file)
        output_file = f"{filename}.jpg"
        S3Connector.download_s3_file(s3_client, f"{worker_util.VIDEO_INPUT}/{file}", file)
        result = subprocess.run(
            [
                "ffmpeg",
                "-i",
                f"{worker_util.VIDEO_INPUT}/{file}",
                "-ss",
                "00:00:01.000",
                "-vframes",
                "1",
                f"{worker_util.IMAGE_OUTPUT}/{output_file}",
            ]
        )
        if result.returncode != 0:
            RedisConnector.redis_error(redis_db, filename)
        else:
            S3Connector.upload_s3_file(
                s3_client, f"{worker_util.IMAGE_OUTPUT}/{output_file}", f"{output_file}"
            )
        worker_util.clean_dir(filename)
