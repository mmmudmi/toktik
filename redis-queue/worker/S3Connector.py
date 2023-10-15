from typing import Final

from botocore.client import BaseClient

from setting import get_s3_config
import boto3

ACCESS_KEY: Final[str] = get_s3_config("access_key")
SECRET_KEY: Final[str] = get_s3_config("secret_key")
ENDPOINT: Final[str] = get_s3_config("endpoint")
BUCKET: Final[str] = get_s3_config("bucket")
config: dict[str, str] = {
    "aws_access_key_id": ACCESS_KEY,
    "aws_secret_access_key": SECRET_KEY,
    "endpoint_url": ENDPOINT,
}


def get_s3_client() -> BaseClient:
    return boto3.client("s3", **config)


def upload_s3_file(client: BaseClient, local_path: str, s3_path: str):
    # client.upload_file(Filename='/Users/user/logo.jpg', Bucket='example-bucket', Key='images/logo.jpg')
    client.upload_file(Filename=local_path, Bucket=BUCKET, Key=s3_path)


def download_s3_file(client: BaseClient, local_path: str, s3_path: str):
    client.download_file(Bucket=BUCKET, Key=s3_path, Filename=local_path)


def delete_s3_file(client: BaseClient, s3_path: str) -> dict:
    """
    :return:
    {
    'DeleteMarker': True|False,
    'VersionId': 'string',
    'RequestCharged': 'requester'
    }
    """
    return client.delete_object(Bucket=BUCKET, Key=s3_path)
