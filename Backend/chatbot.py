from llama_index import VectorStoreIndex, SimpleDirectoryReader
import os
import json
import openai
from llama_index.llms import AzureOpenAI
from langchain.llms import AzureOpenAI
from langchain.embeddings import OpenAIEmbeddings
from llama_index import GPTVectorStoreIndex, SimpleDirectoryReader, PromptHelper, LangchainEmbedding, ServiceContext

os.environ["OPENAI_API_KEY"] = "ab5c91747ca0446dad39c2e227067266"
os.environ["OPENAI_API_BASE"] = "https://loading8425-us.openai.azure.com/"
os.environ["OPENAI_API_TYPE"] = "azure"
os.environ["OPENAI_API_VERSION"] = "2023-05-15"

openai.api_type = os.getenv('OPENAI_API_TYPE')
openai.api_base = os.getenv('OPENAI_API_BASE')
openai.api_version = os.getenv('OPENAI_API_VERSION')
openai.api_key = os.getenv("OPENAI_API_KEY")

class Chatbot:
    def __init__(self):

        self.deployment_name = "gpt-35-turbo"
    
    def send_message(self, messages):
        response = openai.ChatCompletion.create(
          engine=self.deployment_name,
          messages=messages,
          temperature=0.7,
          max_tokens=350,
          top_p=0.95,
          frequency_penalty=0,
          presence_penalty=0,
          stop=None)
        
        reply = response['choices'][0]['message']['content']
        return reply

if __name__ == "__main__":
    chatbot = Chatbot()
    messages=[
          {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
        ]

    while True:
        user_input = input()
        if user_input == "bye bye":
            break
        messages.append({"role": "user", "content": user_input})
        reply = chatbot.send_message(messages)
        print(reply)
        messages.append({"role": "assistant", "content": reply})
    
    print("Bye bye!")