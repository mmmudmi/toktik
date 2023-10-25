import os
from typing import Final

import S3Connector

VIDEO_INPUT: Final[str] = r"Video_Input"
VIDEO_OUTPUT: Final[str] = r"Video_Output"
IMAGE_OUTPUT: Final[str] = r"Image_Output"


def extract_name_ext(file: str) -> tuple[str, str]:
    temp = file.index(".")
    return file[:temp], file[temp + 1:]


def clean_dir(filename: str) -> None:
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


def create_dir() -> None:
    if not os.path.exists(VIDEO_INPUT):
        os.mkdir(VIDEO_INPUT)
    if not os.path.exists(VIDEO_OUTPUT):
        os.mkdir(VIDEO_OUTPUT)
    if not os.path.exists(IMAGE_OUTPUT):
        os.mkdir(IMAGE_OUTPUT)


def upload(s3_client, filename: str):
    for i in os.listdir(VIDEO_OUTPUT):
        if not i.startswith(filename):
            continue
        if ".m3u8" in i:
            S3Connector.upload_s3_file(s3_client, f"{VIDEO_OUTPUT}/{i}", i)
        else:
            S3Connector.upload_s3_file(
                s3_client, f"{VIDEO_OUTPUT}/{i}", f"{filename}/{i}"
            )
    S3Connector.upload_s3_file(
        s3_client, f"{IMAGE_OUTPUT}/{filename}.jpg", f"{filename}.jpg"
    )
