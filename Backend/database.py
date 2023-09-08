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
    print(db.delete_chatbot_id("chat_history_db", "chat_records", "Alice@gmail.com", "LI_BAI"))

    # update chat history
    # new_messages = [
    #     {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
    #     {"role": "user", "content": "你好，我是Alice"},
    #   ]
    # print(db.update_chat_history("chat_history_db", "chat_records", "Alice@gmail.com", new_messages))
    
    # print out all record
    all_recor = db.show_records("chat_history_db", "chat_records")
    for record in all_recor:
        print(record)