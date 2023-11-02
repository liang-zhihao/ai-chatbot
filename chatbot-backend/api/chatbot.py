from llama_index import VectorStoreIndex, SimpleDirectoryReader
import os
import json
import openai
from llama_index.llms import AzureOpenAI
from langchain.llms import AzureOpenAI
from langchain.embeddings import OpenAIEmbeddings
from llama_index import GPTVectorStoreIndex, SimpleDirectoryReader, PromptHelper, LangchainEmbedding, ServiceContext
import configparser
from datetime import datetime

# load configuration file
dir_path = os.path.dirname(os.path.abspath(__file__))
config = configparser.ConfigParser()
config.read(os.path.join(dir_path,'config.ini'))
OPENAI_API_KEY = config.get('openai', 'OPENAI_API_KEY')
OPENAI_API_BASE = config.get('openai', 'OPENAI_API_BASE')
OPENAI_API_TYPE = config.get('openai', 'OPENAI_API_TYPE')
OPENAI_API_VERSION = config.get('openai', 'OPENAI_API_VERSION')
MAX_CHAT_LENGTH = config.getint('openai', 'MAX_CHAT_LENGTH')

os.environ["OPENAI_API_KEY"] = OPENAI_API_KEY
os.environ["OPENAI_API_BASE"] = OPENAI_API_BASE
os.environ["OPENAI_API_TYPE"] = OPENAI_API_TYPE
os.environ["OPENAI_API_VERSION"] = OPENAI_API_VERSION

openai.api_type = os.getenv('OPENAI_API_TYPE')
openai.api_base = os.getenv('OPENAI_API_BASE')
openai.api_version = os.getenv('OPENAI_API_VERSION')
openai.api_key = os.getenv("OPENAI_API_KEY")

class Chatbot:
    def __init__(self):

        self.deployment_name = "gpt-35-turbo"
    
    def send_message(self, messages):
        chat = []
        print(messages, flush=True)
        # get latest messages
        if len(messages) > MAX_CHAT_LENGTH:
            chat.append({"role": messages[0]["role"], "content": messages[0]["content"]})
        for message in messages[-MAX_CHAT_LENGTH:]:
            chat.append({"role": message["role"], "content": message["content"]})
        print(chat, flush=True)
        response = openai.ChatCompletion.create(
          engine=self.deployment_name,
          messages=chat,
          temperature=0.7,
          max_tokens=350,
          top_p=0.95,
          frequency_penalty=0,
          presence_penalty=0,
          stop=None)
        
        reply = response['choices'][0]['message']['content']
        usage = response['usage']
        print(usage,flush=True)
        return reply

if __name__ == "__main__":
    chatbot = Chatbot()
    messages=[
          {"role": "system", "content": "You are Albert Einstein, Please use Albert Einstein's tone to talk to the user"},
        ]
    while True:
        user_input = input()
        if user_input == "bye bye":
            break
        messages.append({"role": "user", "content": user_input, "time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")})
        reply = chatbot.send_message(messages)
        print(reply)
        messages.append({"role": "assistant", "content": reply, "time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")})

    print({"time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")})
    print(messages)