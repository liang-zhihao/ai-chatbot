FROM python:3.11

EXPOSE 8000

WORKDIR /app
COPY . /app
RUN pip install -r requirements.txt

# Deployment
CMD ["gunicorn", "--worker-class", "eventlet", "-w", "1", "-b", "0.0.0.0:8001","--capture-output","--log-level", "debug","src.app:app"]