# run container with command: docker run -p 8001:8001 -d --name imserver imserver:latest
# build image with command: docker build -t imserver:latest .
FROM python:3.11

EXPOSE 8001

WORKDIR /app
COPY . /app
RUN pip install -r requirements.txt

# Deployment
CMD ["gunicorn", "--worker-class", "eventlet", "-w", "1", "-b", "0.0.0.0:8001","--capture-output","--log-level", "debug","src.app:app"]