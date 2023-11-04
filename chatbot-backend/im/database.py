import pymongo
import configparser
import os

# uri = "mongodb://{}:{}@{}:{}/{}?authSource=admin".format(MONGO_USER, MONGO_PASS, MONGO_HOST, MONGO_PORT, MONGO_DB)

# load configuration file
dir_path = os.path.dirname(os.path.abspath(__file__))
config = configparser.ConfigParser()
config.read(os.path.join(dir_path, "config.ini"))
host = config.get("mongodb", "host")
port = config.getint("mongodb", "port")
username = config.get("mongodb", "username")
password = config.get("mongodb", "password")
chat_db = config.get("mongodb", "chat_db")
user_db = config.get("mongodb", "user_db")
chat_collection = config.get("mongodb", "chat_collection")
user_collection = config.get("mongodb", "user_collection")
real_uri = config.get("mongodb", "uri")


class MongoDB:
    def __init__(
        self, MONGO_HOST=host, MONGO_PORT=port, MONGO_USER=username, MONGO_PASS=password
    ):
        self.MONGO_HOST = MONGO_HOST
        self.MONGO_PORT = MONGO_PORT
        self.MONGO_USER = MONGO_USER
        self.MONGO_PASS = MONGO_PASS
        self.CHAT_DB = chat_db
        self.CHAT_COLLECTION = chat_collection
        self.USER_DB = user_db
        self.USER_COLLECTION = user_collection

        self.uri = "mongodb://{}:{}@{}:{}/?authSource=admin".format(
            MONGO_USER, MONGO_PASS, MONGO_HOST, MONGO_PORT
        )
        # TODO change to altas uri
        self.uri = real_uri
        self.client = pymongo.MongoClient(self.uri)


# test cases
if __name__ == "__main__":
    db = MongoDB()
    record1 = {
        "user_id": "Alice@gmail.com",
        "chatbot_id": "LIBAI",
        "messages": [
            {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
            {"role": "user", "content": "你好，我是Alice"},
        ],
    }

    record2 = {
        "user_id": "Alice@gmail.com",
        "chatbot_id": "DUFU",
        "messages": [
            {"role": "system", "content": "你是唐朝著名诗人杜甫，请用杜甫的口吻和用户对话"},
            {"role": "user", "content": "你好，我是Alice"},
        ],
    }
    # insert chat history
    # print(db.insert_chat_history(record1))
    # print(db.insert_chat_history(record2))

    # get chat history
    # print(db.get_chat_history("Alice@gmail.com", "LIBAI"))

    # delete target id
    # print(db.delete_user_all_chatbot("loading8425@gmail.com"))

    # delete target chatbot id
    # print(db.delete_user_chatbot("Alice@gmail.com", "DUFU"))

    # update chat history
    new_messages = [
        {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
        {"role": "user", "content": "你好，我是Alice"},
        {"role": "user", "content": "XXXXXXXXXXXXXXXXXXXX"},
    ]
    print(db.update_chat_history("Alice@gmail.com", "LIBAI", new_messages))

    # create user
    # print(db.create_user(user_id='ml@student.unimelb.edu.au', username='min', password='12345678'))

    # login
    print(db.login(user_id="minl@student.unimelb.edu.au", password="1234568"))

    # print all database
    dbs = db.show_databases()
    for database in ["user_db", "chat_history_db"]:
        print("current database: ", database)
        for coll in db.show_collections(database):
            print("current collection:", coll)
            for record in db.show_records(database, coll):
                print(record)
        print()
