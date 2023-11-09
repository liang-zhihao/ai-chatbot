from pymongo import MongoClient
from pymongo.errors import ConnectionFailure


def connect_to_mongodb():
    try:
        # Define the MongoDB URI with SSL enabled and X.509 for authentication
        uri = f"mongodb+srv://cluster0.3qzbzga.mongodb.net/?authMechanism=MONGODB-X509&authSource=%24external&tls=true&tlsCertificateKeyFile=./api/db.pem"

        # Connect to MongoDB
        client = MongoClient(uri)

        print(client.list_database_names())
        for database in ["user_db",'chat_history_db']:
            print("current database: ", database)
            for coll in client[database].list_collection_names():
                print("current collection:",coll)
                print(client[database][coll].find_one({"user_id": "loading0"}))
                print(client[database][coll].find_one())
        # Access the 'test' database
        db = client["test"]

        # Test the connection
        client.admin.command("ismaster")

        print("Successfully connected to MongoDB")

        return db

    except ConnectionFailure as e:
        print(f"Failed to connect to MongoDB: {e}")


if __name__ == "__main__":
    pass
    connect_to_mongodb()
    bot_id = "Bot_Li_Bai"
    print(bot_id[4:].replace("_", " "))