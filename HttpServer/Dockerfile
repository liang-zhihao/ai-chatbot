FROM python:3.11

EXPOSE 8000

WORKDIR /app
COPY . /app
RUN pip install -r requirements.txt

# Deployment
CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0","Backend:create_app()"]