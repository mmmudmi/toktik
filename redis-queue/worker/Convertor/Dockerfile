FROM python:3.11

WORKDIR /app
ADD ../ /app
RUN apt-get -y update && apt-get -y upgrade && apt-get install -y --no-install-recommends ffmpeg
RUN pip install -r requirements.txt

CMD ["python3", "ConvertWorker.py"]