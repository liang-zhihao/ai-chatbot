from typing import List

import pymongo, re

from app.utils.common import get_config, get_roles

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
        # self.uri = real_uri
        self.uri = "mongodb+srv://cluster0.3qzbzga.mongodb.net/?authMechanism=MONGODB-X509&authSource=$external&tls=true&tlsCertificateKeyFile=db.pem"
        self.client = pymongo.MongoClient(self.uri)

    def create_user(self, user_id, username, password):
        # TODO Hash password
        record1 = {
            "user_id": user_id,
            "username": username,
            "password": password,
            "friends": ["bot_Li_Bai", "bot_Einstein", "bot_Gordon_Ramsay"],
            "avatar": user_id,
        }
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        # validation check
        query = {"$or": [{"user_id": user_id}, {"username": username}]}
        for user in collection.find(query):
            if user["user_id"] == user_id:
                return {"status": "error",
                        "message": "This email is already registered. Would you like to log in?"}
            if user["username"] == username:
                return {"status": "error", "message": "This username is taken. Please choose a different one."}
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
        return {"status": "error",
                "message": "We couldn’t create your account. Please check your information and try again."}

    def get_user_info(self, user_id):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        # validation check
        for user in collection.find({"user_id": user_id}):
            if user["user_id"] == user_id:
                return {
                    "user_id": user["user_id"],
                    "username": user["username"],
                    "friends": user["friends"],
                    "avatar": user["avatar"],
                }
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
                return {"status": "error", "message": "Email or password is wrong"}
        return {"status": "error", "message": "Email or password is wrong"}

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

    def get_chat_history(self, room_id) -> List[dict]:
        query = {"id": room_id}

        record = self.get_chat_collection().find_one(query)
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

    def add_friend(self, user_id, friend_id):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        query = {"user_id": user_id}
        return collection.update_one(
            query, {"$push": {"friends": friend_id}}
        ).acknowledged

    def delete_friend(self, user_id, friend_id):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        query = {"user_id": user_id}
        return collection.update_one(
            query, {"$pull": {"friends": friend_id}}
        ).acknowledged

    def check_friend(self, user_id, friend_id):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        query = {"user_id": user_id}
        return friend_id in collection.find_one(query)["friends"]

    def get_friends(self, user_id):
        # check user_id exists
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        # validation check

        if collection.find_one({"user_id": user_id}) == None:
            return None
        query = {"user_id": user_id}
        friends = collection.find_one(query)["friends"]
        for i in range(len(friends)):
            friends[i] = self.get_user_info(friends[i])
        return friends

    def update_avatar(self, user_id, avatar):
        db = self.get_databse(self.USER_DB)
        collection = db[self.USER_COLLECTION]
        query = {"user_id": user_id}
        return collection.update_one(query, {"$set": {"avatar": avatar}}).acknowledged

    def get_user_collection(self):
        return self.get_collection(self.USER_DB, self.USER_COLLECTION)

    def search_username_by_prefix(self, prefix):
        """
        Search for usernames that match a given prefix.
        :param prefix: The prefix string to match the username
        :return: A list of matching usernames
        """
        # Compile a regular expression pattern for case-insensitive prefix match
        regex_pattern = f"^{re.escape(prefix)}"
        regex = re.compile(regex_pattern, re.IGNORECASE)

        # Perform the search query
        matching_users = self.get_user_collection().find(
            {"username": regex}, {"_id": 0}
        )

        # Convert the cursor to a list of users and return
        return list(matching_users)

    def get_user_name(self, from_user_id):
        collection = self.get_user_collection()
        query = {"user_id": from_user_id}
        return collection.find_one(query)["username"]

    def delete_user(self, user_id):
        # delete user from user_db
        db.get_user_collection().delete_one({"user_id": user_id})
        # delete chat from chat_history_db when participants has the user_id
        return (
            db.get_chat_collection()
            .delete_many({"participants": {"$in": [user_id]}})
            .acknowledged
        )

    # random recommend a user
    def recommend_user(self, user_id):
        query = {"user_id": user_id}
        collection = self.get_user_collection()
        friends = collection.find_one(query)["friends"]
        random_user = collection.aggregate([
            {'$match': {'user_id': {'$nin': friends}}},
            {'$sample': {'size': 1}}
        ])
        user = random_user.next()
        # remove objectid
        user.pop("_id")
        user.pop("password")

        return user


db = MongoDB()


def seed_bot():
    # read roles folder

    import os

    names = os.listdir("roles")
    for name in names:
        # remove .json
        name = name[:-5]
        id = "bot_" + name.replace(" ", "_")
        db.create_user(user_id=id, username=name, password="1234567890")


# test cases
if __name__ == "__main__":
    # record1 = {
    #     "user_id": "Alice@gmail.com",
    #     "chatbot_id": "LIBAI",
    #     "messages": [
    #         {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
    #         {"role": "user", "content": "你好，我是Alice"},
    #     ],
    # }
    seed_bot()
    # record2 = {
    #     "user_id": "Alice@gmail.com",
    #     "chatbot_id": "DUFU",
    #     "messages": [
    #         {"role": "system", "content": "你是唐朝著名诗人杜甫，请用杜甫的口吻和用户对话"},
    #         {"role": "user", "content": "你好，我是Alice"},
    #     ],
    # }
    # # insert chat history
    # # print(db.insert_chat_history(record1))
    # # print(db.insert_chat_history(record2))

    # # get chat history
    # # print(db.get_chat_history("Alice@gmail.com", "LIBAI"))

    # # delete target id
    # # print(db.delete_user_all_chatbot("loading8425@gmail.com"))

    # # delete target chatbot id
    # # print(db.delete_user_chatbot("Alice@gmail.com", "DUFU"))

    # # update chat history
    # new_messages = [
    #     {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
    #     {"role": "user", "content": "你好，我是Alice"},
    #     {"role": "user", "content": "XXXXXXXXXXXXXXXXXXXX"},
    # ]
    # # print(db.update_chat_history("Alice@gmail.com", "LIBAI", new_messages))

    # # create user
    # # print(db.create_user(user_id='ml@student.unimelb.edu.au', username='min', password='12345678'))

    # # login
    # print(db.login(user_id="minl@student.unimelb.edu.au", password="1234568"))

    # print all database
    dbs = db.show_databases()
    for database in ["user_db", "chat_history_db"]:
        print("current database: ", database)
        for coll in db.show_collections(database):
            print("current collection:", coll)
            for record in db.show_records(database, coll):
                print(record)
        print()
