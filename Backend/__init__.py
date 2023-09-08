import os
from flask import Flask, render_template
from flask import request
from .chatbot import Chatbot
from .database import MongoDB

file_path = os.path.abspath(__file__)
dir_path = os.path.dirname(file_path)
chatbot = Chatbot()
db = MongoDB()

def create_app(test_config=None):
    # create and configure the app
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        SECRET_KEY='dev',
        DATABASE=os.path.join(app.instance_path, 'flaskr.sqlite'),
    )

    if test_config is None:
        # load the instance config, if it exists, when not testing
        app.config.from_pyfile('config.py', silent=True)
    else:
        # load the test config if passed in
        app.config.from_mapping(test_config)

    # ensure the instance folder exists
    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass
    
    @app.route('/api/login', methods=['POST'])
    def login():
        print(request.json)
        return request.json
    
    @app.route('/api/send_img', methods=['GET', 'POST'])
    def upload_file():
        if request.method == 'POST':
            f = request.files['the_file']
            f.save('/var/www/uploads/uploaded_file.txt')

    # get current available chatbots 
    @app.route('/api/chatbot/roles', methods=['GET'])
    def get_chatbot_roles():
        print(request.json)
        return request.json

    # user send message to a chatbot
    @app.route('/api/chatbot/send_message', methods=['POST'])
    def send_message():
        body = request.json
        pass
        return ''

    # user create a new chatbot
    @app.route('/api/chatbot/create_chatbot', methods=['POST'])
    def create_chatbot():
        body = request.json
        pass

    # user delete a chatbot
    @app.route('/api/chatbot/delete_chatbot', methods=['POST'])
    def delete_chatbot():
        body = request.json
        pass

    # get user chat history
    @app.route('/api/chatbot/get_chat_history', methods=['GET'])
    def get_chat_history():
        pass

    # user delete chat history
    @app.route('/api/chatbot/delete_chat_history', methods=['POST'])
    def delete_chat_history():
        pass

    

    return app