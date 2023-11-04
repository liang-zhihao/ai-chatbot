import pymongo
import configparser
import os
from app.utils.common import get_config

# uri = "mongodb://{}:{}@{}:{}/{}?authSource=admin".format(MONGO_USER, MONGO_PASS, MONGO_HOST, MONGO_PORT, MONGO_DB)

# load configuration file
config = get_config()
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
        self.uri = real_uri

        self.client = pymongo.MongoClient(self.uri)

    def create_user(self, user_id, username, password):
        # TODO Hash password
        record1 = {
            "user_id": user_id,
            "username": username,
            "password": password,
            "friends": [],
            "avatar": "https://i.imgur.com/removed.png",
        }
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        # validation check
        query = {"$or": [{"user_id": user_id}, {"username": username}]}
        for user in collection.find(query):
            if user["user_id"] == user_id:
                return {"status": "error", "message": "user_id already exists"}
            if user["username"] == username:
                return {"status": "error", "message": "username already exists"}
        if user_id == "" or username == "" or password == "":
            return {
                "status": "error",
                "message": "user_id, username, password cannot be empty",
            }
        if len(password) < 8:
            return {
                "status": "error",
                "message": "password length should be at least 8",
            }
        db_status = collection.insert_one(record1).acknowledged
        if db_status:
            return {"status": "success", "message": "user created successfully"}
        return {"status": "error", "message": "user created failed"}

    def get_user_info(self, user_id):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        # validation check
        for user in collection.find({"user_id": user_id}):
            if user["user_id"] == user_id:
                return {"user_id": user["user_id"], "username": user["username"]}
        return None

    def login(self, user_id, password):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        # validation check
        for user in collection.find({"user_id": user_id}):
            if user["user_id"] == user_id and user["password"] == password:
                return {
                    "status": "success",
                    "message": "login successfully",
                    "user_info": {
                        "user_id": user["user_id"],
                        "username": user["username"],
                    },
                }
            if user["user_id"] == user_id and user["password"] != password:
                return {"status": "error", "message": "password incorrect"}
        return {"status": "error", "message": "user_id not exists??"}

    def user_exists(self, user_id):
        query = {"user_id": user_id}
        db = self.get_databse("user_db")
        for user in db["users"].find(query):
            if user["user_id"] == user_id:
                return True
        return False

    def show_databases(self):
        return self.client.list_database_names()

    def show_collections(self, database):
        return self.client[database].list_collection_names()

    def show_records(self, database, collection):
        return self.client[database][collection].find()

    def get_databse(self, database):
        return self.client[database]

    def new_user_chatbot(self, record) -> bool:
        db = self.get_databse(self.CHAT_DB)
        collection = db[self.CHAT_COLLECTION]
        # validation check
        query = {
            "$and": [
                {"user_id": record["user_id"]},
                {"chatbot_id": record["chatbot_id"]},
            ]
        }
        for user in collection.find(query):
            if (
                    user["user_id"] == record["user_id"]
                    and user["chatbot_id"] == record["chatbot_id"]
            ):
                return False
        return collection.insert_one(record).acknowledged

    def get_user_roles(self, user_id):
        db = self.get_databse(self.CHAT_DB)
        collection = db[self.CHAT_COLLECTION]
        user_roles = []
        for record in collection.find({"user_id": user_id}):
            user_roles.append(record["chatbot_id"])
        return user_roles

    # def get_chat_history(self, user_id, chatbot_id):
    #     query = {"$and": [{"user_id": user_id}, {"chatbot_id": chatbot_id}]}
    #     database = self.CHAT_DB
    #     collection = self.CHAT_COLLECTION
    #     db = self.get_databse(database)
    #     record = db[collection].find_one(query)
    #     if record == None:
    #         return None
    #     chat = record["messages"]
    #     return chat

    def get_chat_history(self, room_id):
        query = {"id": room_id}
        database = self.CHAT_DB
        collection = self.CHAT_COLLECTION
        db = self.get_databse(database)
        record = db[collection].find_one(query)
        if record == None:
            return None
        chat = record["messages"]
        return chat

    def update_chat_history(self, room_id, message) -> bool:
        db = self.get_databse(self.CHAT_DB)
        collection = db[self.CHAT_COLLECTION]
        query = {"id": room_id}
        return collection.update_one(
            query, {"$set": {"messages": message}}
        ).acknowledged

    # delete target user_id all chat history
    def delete_user_all_chatbot(self, user_id) -> bool:
        db = self.get_databse(self.CHAT_DB)
        return db[self.CHAT_COLLECTION].delete_many({"user_id": user_id}).acknowledged

    def delete_user_chatbot(self, user_id, chatbot_id) -> bool:
        db = self.get_databse(self.CHAT_DB)
        return (
            db[self.CHAT_COLLECTION]
            .delete_many({"user_id": user_id, "chatbot_id": chatbot_id})
            .acknowledged
        )

    def delete_all(self, database, collection) -> bool:
        db = self.get_databse(database)
        return db[collection].delete_many({}).acknowledged

    def change_password(self, user_id, old_password, new_password):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        query = {"$and": [{"user_id": user_id}, {"password": old_password}]}
        if collection.find_one(query) == None:
            return False
        return collection.update_one(
            query, {"$set": {"password": new_password}}
        ).acknowledged

    def change_username(self, user_id, new_username):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        query = {"user_id": user_id}
        return collection.update_one(
            query, {"$set": {"username": new_username}}
        ).acknowledged

    def get_chat_by_room_id(self, room_id):
        db = self.get_databse(self.CHAT_DB)
        collection = db[self.CHAT_COLLECTION]
        query = {"id": room_id}
        return collection.find_one(query, {"_id": 0})

    def get_collection(self, database, collection):
        db = self.get_databse(database)
        return db[collection]

    def get_chat_collection(self):
        return self.get_collection(self.CHAT_DB, self.CHAT_COLLECTION)

    def get_participants(self, room_id):
        chat = self.get_chat_by_room_id(room_id)
        return chat["participants"]


db = MongoDB()
# test cases
if __name__ == "__main__":
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
    # print(db.update_chat_history("Alice@gmail.com", "LIBAI", new_messages))

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
