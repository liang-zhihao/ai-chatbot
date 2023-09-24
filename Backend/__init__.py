from datetime import datetime
import os, json
from flask import Flask, render_template, abort
from flask import request
from .chatbot import Chatbot
from .database import MongoDB
import configparser
from flask_jwt_extended import create_access_token, jwt_required, get_jwt_identity, JWTManager
from flask_socketio import SocketIO, send


file_path = os.path.abspath(__file__)
dir_path = os.path.dirname(file_path)
chatbot = Chatbot()
db = MongoDB()

# get configuration
config = configparser.ConfigParser()
config.read(os.path.join(dir_path,'config.ini'))
JWT_SECRET_KEY = config.get('secret', 'JWT_SECRET_KEY')
CHAT_LENGTH_LIMIT = config.getint('openai', 'CHAT_LENGTH_LIMIT')


def create_app(test_config=None):
    # create and configure the app
    app = Flask(__name__, instance_relative_config=True)
    app.config['JWT_SECRET_KEY']  = JWT_SECRET_KEY
    jwt = JWTManager(app)
    socketio = SocketIO(app, cors_allowed_origins="*")

    # test server status
    @app.route('/', methods=['GET'])
    def server_status():
        return {"status": "success", "message": "server is running"}, 200

    @app.route('/chat', methods=['GET'])
    def index():
        return render_template('chat.html')

    @socketio.on('message')
    def handleMessage(msg):
        print('Message: ' + msg)
        send(msg, broadcast=True)

    # user register
    @app.route('/api/register', methods=['POST'])
    def register():
        try: 
            user_id = request.json['user_id']
            username = request.json['username']
            password = request.json['password']
            status = db.create_user(user_id, username, password)
            if status["status"] == "success":
                return status, 200
        except Exception as e:
            return {"status": "error", "message": str(e)}, 500
        return status, 400
    
    @app.route('/api/login', methods=['POST'])
    def login():
        try:
            user_id = request.json['user_id']
            password = request.json['password']
            status = db.login(user_id, password)
            print(status)
            if status["status"] == "success":
                access_token = create_access_token(identity=user_id)
                print(access_token)
                status["access_token"] = access_token
                return status, 200
        except Exception as e:
            return {"status": "error", "message": str(e)}, 500  
        return status, 400
    
    @app.route('/api/send_img', methods=['GET', 'POST'])
    @jwt_required()
    def upload_file():
        if request.method == 'POST':
            f = request.files['the_file']
            f.save('/var/www/uploads/uploaded_file.txt')

    # get current available chatbots 
    @app.route('/api/chatbot/roles', methods=['GET', 'POST'])
    @jwt_required()
    def get_chatbot_roles():
        current_user = get_jwt_identity()
        if request.method == 'POST':
            try:
                user_id = request.json['user_id']
                if user_id != current_user:
                    return {"status": "error", "message": "user_id not match with auth token"}, 400
                user_roles = db.get_user_roles(user_id)
                return {"status": "success","roles": user_roles}, 200
            except Exception as e:
                return {"status": "error","roles": [], "message": str(e)}, 500
        else:
            roles = get_roles()
        return {"status": "success", "roles": roles}, 200

    # user send message to a chatbot
    @app.route('/api/chatbot/send_message', methods=['POST'])
    @jwt_required()
    def send_message():
        try: 
            user_id = request.json['user_id']
            # match user_id with auth token
            if user_id != get_jwt_identity():
                return {"status": "error", "message": "user_id not match with auth token"}, 400
            chatbot_id = request.json['chatbot_id']
            message = {"role": "user", "content": request.json['message'], "time": get_time()}

            # check message length
            if request.json['message'] == "":
                return {"status": "error", "message": "message cannot be empty"}, 400
            print(request.json['message'].split())
            if len(request.json['message'].split()) > CHAT_LENGTH_LIMIT :
                return {"status": "error", "message": "message length should be less than"+str(CHAT_LENGTH_LIMIT)}, 400
            
            # check if user exists
            if db.user_exists(user_id):
                chat_history = db.get_chat_history(user_id, chatbot_id)
                if chat_history == None:
                    return {"status": "error", "message": "user dont have this chatbot, chat history not exists"}, 400
                chat_history.append(message)
                reply = chatbot.send_message(chat_history)
                time = get_time()
                chat_history.append({"role": "assistant", "content": reply, "time": time})
                db.update_chat_history(user_id, chatbot_id, chat_history)
                return {"status": "success", "message": reply, "time":time}, 200
            else:
                return {"status": "error", "message": "user not exists or missing parameter"}, 500
        except Exception as e:
            return {"status": "error", "message": str(e)}, 400

    # user create a new chatbot
    @app.route('/api/chatbot/create_chatbot', methods=['POST'])
    @jwt_required()
    def create_chatbot():
        try:
            user_id = request.json['user_id']
            chatbot_id = request.json['chatbot_id']
            available_roles = get_roles()

            # match user_id with auth token
            if user_id != get_jwt_identity():
                return {"status": "error", "message": "user_id not match with auth token"}, 400
            
            if chatbot_id not in available_roles:
                return {"status": "error", "message": "chatbot not exists"}, 400
            init_message = json.load(open(os.path.join(dir_path, 'roles', chatbot_id + '.json'), encoding='utf-8'))
        except Exception as e:
            return {"status": "error", "message": str(e)}, 400
        record = {
            "user_id": user_id,
            "chatbot_id": chatbot_id,
            "messages": [init_message],
        }
        if db.new_user_chatbot(record):
            return {"status": "success", "message": "create chatbot successfully"}, 200
        return {"status": "error", "message": "create chatbot failed, chatbot already exist"}, 400
        

    # user delete a chatbot
    @app.route('/api/chatbot/delete_chatbot', methods=['POST'])
    @jwt_required()
    def delete_chatbot():
        try:
            user_id = request.json['user_id']
            chatbot_id = request.json['chatbot_id']
            # match user_id with auth token
            if user_id != get_jwt_identity():
                return {"status": "error", "message": "user_id not match with auth token"}, 400
            if db.delete_chat_history(user_id, chatbot_id):
                return {"status": "success", "message": "delete chat history successfully"}, 200
            return {"status": "error", "message": "delete chat history failed"}, 400
        except Exception as e:
            return {"status": "error", "message": str(e)}, 400

    # get user chat history
    @app.route('/api/chatbot/get_chat_history', methods=['GET'])
    @jwt_required()
    def get_chat_history():
        try: 
            user_id = request.json['user_id']
            chatbot_id = request.json['chatbot_id']
            # match user_id with auth token
            if user_id != get_jwt_identity():
                return {"status": "error", "message": "user_id not match with auth token"}, 400
            
            if db.user_exists(user_id):
                chat_history = db.get_chat_history(user_id, chatbot_id)
                if chat_history == None:
                    return {"status": "error", "message": "user dont have this chatbot, chat history not exists"}, 400
                return {"status": "success", "chat history": chat_history}, 200
        except Exception as e:
            return {"status": "error", "message": str(e)}, 400
        
    # user delete chat history
    @app.route('/api/chatbot/delete_chat_history', methods=['POST'])
    @jwt_required()
    def delete_chat_history():
        try:
            user_id = request.json['user_id']
            chatbot_id = request.json['chatbot_id']
            init_message = json.load(open(os.path.join(dir_path, 'roles', chatbot_id + '.json'), encoding='utf-8'))
            # match user_id with auth token
            if user_id != get_jwt_identity():
                return {"status": "error", "message": "user_id not match with auth token"}, 400
            if db.update_chat_history(user_id, chatbot_id, [init_message]):
                return {"status": "success", "message": "delete chat history successfully"}, 200
            return {"status": "error", "message": "delete chat history failed"}, 400
        except Exception as e:
            return {"status": "error", "message": str(e)}, 400
        
    # get user information
    @app.route('/api/user/get_user_info', methods=['GET'])
    @jwt_required()
    def get_user_info():
        pass

    return app

def get_roles():
    role_dir = os.path.join(dir_path, 'roles')
    roles = os.listdir(role_dir)
    roles = [r[:-5] for r in roles]
    return roles

def get_time():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")