import subprocess
from typing import Final

import RedisConnector
import S3Connector
import worker_util

INPUT_QUEUE: Final[str] = RedisConnector.get_redis_config("convert_queue")
OUTPUT_QUEUE: Final[str] = RedisConnector.get_redis_config("chunk_queue")

if __name__ == '__main__':
    worker_util.create_dir()
    print("ConvertWorker start..")
    s3_client = S3Connector.get_s3_client()
    redis_db = RedisConnector.redis_db()
    while True:
        print("waiting for queue")
        file = RedisConnector.redis_queue_pop(redis_db, INPUT_QUEUE)
        filename, extension = worker_util.extract_name_ext(file)
        output_file = f"{filename}.mp4"
        S3Connector.download_s3_file(s3_client, f"{worker_util.VIDEO_INPUT}/{file}", file)

        subprocess.run(
            [
                "ffmpeg",
                "-i",
                f"{worker_util.VIDEO_INPUT}/{file}",
                "-vf",
                "scale='min(1080,iw)':-1",
                "-crf",
                "18",
                "-preset",
                "slow",
                f"{worker_util.VIDEO_OUTPUT}/{output_file}",
            ]
        )
        S3Connector.delete_s3_file(s3_client, file)
        S3Connector.upload_s3_file(
            s3_client, f"{worker_util.VIDEO_OUTPUT}/{output_file}", f"{output_file}"
        )
        worker_util.clean_dir(filename)
        RedisConnector.redis_queue_push(redis_db, OUTPUT_QUEUE, output_file)

