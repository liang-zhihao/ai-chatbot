from pymongo import MongoClient
from pymongo.errors import ConnectionFailure


def connect_to_mongodb():
    try:
        # Define the MongoDB URI with SSL enabled and X.509 for authentication
        uri = f"mongodb+srv://cluster0.3qzbzga.mongodb.net/?authMechanism=MONGODB-X509&authSource=%24external&tls=true&tlsCertificateKeyFile=db.pem"

        # Connect to MongoDB
        client = MongoClient(uri)

        # Access the 'test' database
        db = client["test"]

        # Test the connection
        client.admin.command("ismaster")

        print("Successfully connected to MongoDB")

        return db

    except ConnectionFailure as e:
        print(f"Failed to connect to MongoDB: {e}")


if __name__ == "__main__":
    connect_to_mongodb()
