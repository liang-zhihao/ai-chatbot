import pymongo
import datetime
#uri = "mongodb://{}:{}@{}:{}/{}?authSource=admin".format(MONGO_USER, MONGO_PASS, MONGO_HOST, MONGO_PORT, MONGO_DB)

class MongoDB():

    def __init__(self, MONGO_HOST= "34.129.73.102" , MONGO_PORT= "27017", MONGO_USER= "root", MONGO_PASS= "root123abc"):
        self.MONGO_HOST = MONGO_HOST
        self.MONGO_PORT = MONGO_PORT
        self.MONGO_USER = MONGO_USER
        self.MONGO_PASS = MONGO_PASS
        self.uri = "mongodb://{}:{}@{}:{}/?authSource=admin".format(MONGO_USER, MONGO_PASS, MONGO_HOST, MONGO_PORT)
        self.client = pymongo.MongoClient(self.uri)

    def create_user(self, user_id, username, password):
        record1 = {
        "user_id": user_id,
        "username": username,
        "password": password,
        }
        db = self.get_databse("user_db")
        # validation check
        query = { "$or": [ {"user_id": user_id}, {"username": username} ] }
        for user in db["users"].find(query):
            if user["user_id"] == user_id:
                return {"status": "error", "message": "user_id already exists"}
            if user["username"] == username:
                return {"status": "error", "message": "username already exists"}
        if user_id == "" or username == "" or password == "":
            return {"status": "error", "message": "user_id, username, password cannot be empty"}
        if len(password) < 8:
            return {"status": "error", "message": "password length should be at least 8"}
        db_status = db["users"].insert_one(record1).acknowledged
        if db_status:
            return {"status": "success", "message": "user created successfully"}
        return {"status": "error", "message": "user created failed"}
    
    def login(self, user_id, password):
        db = self.get_databse("user_db")
        # validation check
        for user in db["users"].find({"user_id": user_id}):
            if user["user_id"] == user_id and user["password"] == password:
                return {"status": "success", "message": "login successfully", "user_info": {"user_id": user["user_id"], "username": user["username"]}}
            if user["user_id"] == user_id and user["password"] != password:
                return {"status": "error", "message": "password incorrect"}
        return {"status": "error", "message": "user_id not exists"}

    def show_databases(self):
        return self.client.list_database_names()
    
    def show_collections(self, database):
        return self.client[database].list_collection_names()
    
    def show_records(self, database, collection):
        return self.client[database][collection].find()

    def get_databse(self, database):
        return self.client[database]

    def insert_chat_history(self, database, collection, message) -> bool:
        db = self.get_databse(database)
        return db[collection].insert_one(message).acknowledged

    def get_chat_history(self, database, collection, sender_id):
        db = self.get_databse(database)
        return db[collection].find({"sender_id": sender_id})
    
    def update_chat_history(self, database, collection, sender_id, message) -> bool:
        db = self.get_databse(database)
        return db[collection].update_one({"sender_id": sender_id}, {"$set": {"messages": message}}).acknowledged
    
    def delete_sender_id(self, database, collection, sender_id) -> bool:
        db = self.get_databse(database)
        return db[collection].delete_many({"sender_id": sender_id}).acknowledged

    def delete_chatbot_id(self, database, collection, sender_id, chatbot_id) -> bool:
        db = self.get_databse(database)
        return db[collection].delete_many({"sender_id": sender_id, "chatbot_id": chatbot_id}).acknowledged
    
# test cases
if __name__ == "__main__":
    db = MongoDB()
    record1 = {
    "sender_id": "Alice@gmail.com",
    "chatbot_id": "LI_BAI",
    "messages": [
        {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
        {"role": "user", "content": "你好，我是Alice"},
      ],
    }

    record2 = {
    "sender_id": "Alice@gmail.com",
    "chatbot_id": "DU_FU",
    "messages": [
        {"role": "system", "content": "你是唐朝著名诗人杜甫，请用杜甫的口吻和用户对话"},
        {"role": "user", "content": "你好，我是Alice"},
      ],
    }
    # insert chat history
    # print(db.insert_chat_history("chat_history_db", "chat_records", record1))
    # print(db.insert_chat_history("chat_history_db", "chat_records", record2))

    # delete target id
    #print(db.delete_sender_id("chat_history_db", "chat_records", "Alice@gmail.com"))

    # delete target chatbot id
    #print(db.delete_chatbot_id("chat_history_db", "chat_records", "Alice@gmail.com", "LI_BAI"))

    # update chat history
    # new_messages = [
    #     {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
    #     {"role": "user", "content": "你好，我是Alice"},
    #   ]
    # print(db.update_chat_history("chat_history_db", "chat_records", "Alice@gmail.com", new_messages))
    
    # create user
    #print(db.create_user(user_id='ml@student.unimelb.edu.au', username='min', password='12345678'))

    # login
    print(db.login(user_id='minl@student.unimelb.edu.au', password='1234568'))

    # print all database
    dbs = db.show_databases()
    for database in ["user_db", "chat_history_db"]:
        print("current database: ", database)
        for coll in db.show_collections(database):
            print("current collection:",coll)
            for record in db.show_records(database, coll):
                print(record)
        print()
    import os
    file_path = os.path.abspath(__file__)
    dir_path = os.path.dirname(file_path)
    role_dir = os.path.join(dir_path, 'roles')
    roles = os.listdir(role_dir)
    
    print([r[:-5] for r in roles])