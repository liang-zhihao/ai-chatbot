FROM python:3.11
WORKDIR /app
COPY . /app
RUN pip install -r requirements.txt
EXPOSE 5000
ENV FLASK_ENV=production
CMD ["flask", "--app", "Backend", "run","--host=0.0.0.0"]
