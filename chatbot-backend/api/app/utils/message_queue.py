import pymongo
from pymongo import MongoClient
from datetime import datetime
import configparser
import os

# load configuration file
dir_path = os.path.dirname(os.path.abspath(__file__))
config = configparser.ConfigParser()
config.read(os.path.join(dir_path,'config.ini'))
host = config.get('mongodb', 'host')
port = config.getint('mongodb', 'port')
username = config.get('mongodb', 'username')
password = config.get('mongodb', 'password')

class MessageQueue:
    def __init__(self, db_name="message_queue", collection_name="messages"):
        # Initialize MongoDB client and select database and collection.
        self.uri = "mongodb://{}:{}@{}:{}/?authSource=admin".format(username, password, host, port)
        self.client = MongoClient(self.uri)
        self.db = self.client[db_name]
        self.collection = self.db[collection_name]
    
    def enqueue_message(self, sender, recipient, message):
        """
        Add a message to the queue.
        
        :param sender: str, sender's username.
        :param recipient: str, recipient's username.
        :param message: str, message text.
        """
        msg = {
            "sender": sender,
            "recipient": recipient,
            "message": message,
            "timestamp": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
        self.collection.insert_one(msg)
    
    def dequeue_messages(self, recipient):
        """
        Get and remove all messages for a recipient from the queue.
        
        :param recipient: str, recipient's username.
        :return: list of dicts, each dict contains a message information.
        """
        messages = list(self.collection.find({"recipient": recipient}))
        self.collection.delete_many({"recipient": recipient})
        if len(messages) == 0:
            return []
        data = {
            "from_user_id":messages[0]["sender"],
            "to_user_id": messages[0]["recipient"],
            "messages":[]
        }
        for msg in messages:
            data["messages"].append({"message":msg["message"],'timestamp':msg["timestamp"]})
        return data

if __name__ == '__main__':
    # Example usage:
    mq = MessageQueue()
    mq.enqueue_message("user1", "user2", "Hello, User2!")
    mq.enqueue_message("user3", "user2", "Are you there, User2?")
    messages = mq.dequeue_messages("user2")
    print(messages)