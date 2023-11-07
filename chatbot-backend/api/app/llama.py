from llama_index import VectorStoreIndex, SimpleDirectoryReader
import os
import json
import openai, tiktoken
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

deployment_name = "gpt-35-turbo"


def test_api():
    messages=[
          {"role": "system", "content": "你是唐朝著名诗人李白，世人称你为诗仙太白，请用李白的口吻和用户对话"},
        ]

    while True:
        user_input = input()
        if user_input == "bye bye":
            break
        messages.append({"role": "user", "content": user_input})
        response = openai.ChatCompletion.create(
          engine=deployment_name,
          messages=messages,
          temperature=0.7,
          max_tokens=350,
          top_p=0.95,
          frequency_penalty=0,
          presence_penalty=0,
          stop=None)
        reply = response['choices'][0]['message']['content']
        print(reply)
        messages.append({"role": "assistant", "content": reply})
    
    print("Bye bye!")

def test_llama_index():
    # Initialize LLM and Embeddings model (model is the actual model name, e.g., gpt-35-turbo, engine is your custom deployment name, e.g., my-gpt-35-turbo)
    llm = AzureOpenAI(engine=deployment_name, model="gpt-35-turbo", temperature=0.0)
    embeddings = LangchainEmbedding(OpenAIEmbeddings(deployment_id="text-embedding-ada-002", chunk_size=1))

    # Define prompt helper
    prompt_helper = PromptHelper(context_window=3000, 
                                num_output=500, 
                                chunk_overlap_ratio=0.1, 
                                chunk_size_limit=1000)

    service_context = ServiceContext.from_defaults(llm=llm, embed_model=embeddings, prompt_helper=prompt_helper)

    # Load documents
    documents = SimpleDirectoryReader('./data/').load_data()

    # Create index
    index = GPTVectorStoreIndex.from_documents(documents, service_context=service_context, prompt_helper=prompt_helper)
    query_engine = index.as_query_engine(service_context=service_context, verbose=True)

    query = "What is azure openai service? give me back a bullet point list"
    answer = query_engine.query(query)

    print(f"Query: {query}")
    print(f"Answer: {answer}")
    print(f"Sources: {answer.get_formatted_sources()}")

encoding = tiktoken.encoding_for_model("gpt-3.5-turbo")
encoding.encode("tiktoken is great!")