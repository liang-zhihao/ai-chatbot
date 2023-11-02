from datetime import datetime
import os, json
from flask import Flask, render_template, abort, jsonify
from flask import request
from chatbot import Chatbot
from database import MongoDB
import configparser
from flask_jwt_extended import (
    create_access_token,
    jwt_required,
    get_jwt_identity,
    JWTManager,
)

# from flask_socketio import SocketIO, send, emit


file_path = os.path.abspath(__file__)
dir_path = os.path.dirname(file_path)
chatbot = Chatbot()
db = MongoDB()

# get configuration
config = configparser.ConfigParser()
config.read(os.path.join(dir_path, "config.ini"))
JWT_SECRET_KEY = config.get("secret", "JWT_SECRET_KEY")
CHAT_LENGTH_LIMIT = config.getint("openai", "CHAT_LENGTH_LIMIT")


def error_out(msg, code):
    return jsonify({"status": code, "message": msg, "success": False, "data": {}}), code


def get_roles():
    role_dir = os.path.join(dir_path, "roles")
    roles = os.listdir(role_dir)
    roles = [r[:-5] for r in roles]
    return roles


def get_time():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


from flask import jsonify


def standard_response(status, message, success=True, data={}, http_status=200):
    response = {"status": status, "message": message, "success": success, "data": data}
    return jsonify(response), http_status


app = Flask(__name__, instance_relative_config=True)


# create the app

# configure the app
app.config["JWT_SECRET_KEY"] = JWT_SECRET_KEY

jwt = JWTManager(app)
# socketio = SocketIO(app, cors_allowed_origins="*")

# Mapping of username to session ID
users = {}


# HTTP
@jwt.expired_token_loader
def expired_token_callback(jwt_header, jwt_payload):
    return error_out("Token has expired", 401)


@jwt.unauthorized_loader
def unauthorized_loader_callback(msg):
    return error_out("Missing Authorization Header", 401)


@jwt.invalid_token_loader
def invalid_token_callback(msg):
    return error_out("Invalid JWT", 422)


# test server status
@app.route("/", methods=["GET"])
def server_status():
    return (
        jsonify(
            {
                "status": 200,
                "message": "server is running",
                "success": True,
                "data": {},
            }
        ),
        200,
    )


@app.route("/chat", methods=["GET"])
def index():
    return render_template("chat.html")


# user register
@app.route("/api/register", methods=["POST"])
def register():
    try:
        user_id = request.json["user_id"]
        username = request.json["username"]
        password = request.json["password"]
        status = db.create_user(user_id, username, password)
        if status["status"] == "success":
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": status["message"],
                        "success": True,
                        "data": {},
                    }
                ),
                200,
            )
    except Exception as e:
        return error_out(str(e), 401)
    return error_out(status["message"], 401)


@app.route("/api/login", methods=["POST"])
def login():
    try:
        user_id = request.json["user_id"]
        password = request.json["password"]
        status = db.login(user_id, password)
        print(status)
        if status["status"] == "success":
            access_token = create_access_token(identity=user_id)
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": status["message"],
                        "success": True,
                        "data": {"access_token": access_token},
                    }
                ),
                200,
            )
    except Exception as e:
        return error_out(str(e), 401)
    return error_out(status["message"], 401)


@app.route("/api/send_img", methods=["GET", "POST"])
@jwt_required()
def upload_file():
    if request.method == "POST":
        f = request.files["the_file"]
        f.save("/var/www/uploads/uploaded_file.txt")


# get current available chatbots
@app.route("/api/chatbot/roles", methods=["GET", "POST"])
@jwt_required()
def get_chatbot_roles():
    current_user = get_jwt_identity()
    if request.method == "POST":
        try:
            user_id = request.json["user_id"]
            if user_id != current_user:
                return error_out("user_id not match with auth token", 401)
            user_roles = db.get_user_roles(user_id)
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": "get user roles successfully",
                        "success": True,
                        "data": {"roles": user_roles},
                    }
                ),
                200,
            )
        except Exception as e:
            return error_out(str(e), 401)
    else:
        roles = get_roles()
    return (
        jsonify(
            {
                "status": 200,
                "message": "get roles successfully",
                "success": True,
                "data": {"roles": roles},
            }
        ),
        200,
    )


# user send message to a chatbot
@app.route("/api/chatbot/send_message", methods=["POST"])
@jwt_required()
def send_message():
    try:
        user_id = request.json["user_id"]
        # match user_id with auth token
        if user_id != get_jwt_identity():
            return error_out("user_id not match with auth token", 401)
        chatbot_id = request.json["chatbot_id"]
        message = {
            "role": "user",
            "content": request.json["message"],
            "time": get_time(),
        }

        # check message length
        if request.json["message"] == "":
            return error_out("message should not be empty", 401)
        print(request.json["message"].split())
        if len(request.json["message"].split()) > CHAT_LENGTH_LIMIT:
            return error_out(
                "message length should be less than " + str(CHAT_LENGTH_LIMIT), 401
            )

        # check if user exists
        if db.user_exists(user_id):
            chat_history = db.get_chat_history(user_id, chatbot_id)
            if chat_history == None:
                return error_out(
                    "user dont have this chatbot, chat history not exists", 401
                )
            chat_history.append(message)
            reply = chatbot.send_message(chat_history)
            time = get_time()
            chat_history.append({"role": "assistant", "content": reply, "time": time})
            db.update_chat_history(user_id, chatbot_id, chat_history)
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": "send message successfully",
                        "success": True,
                        "data": {"time": time, "reply": reply},
                    }
                ),
                200,
            )
        else:
            return error_out("user not exists", 401)
    except Exception as e:
        return error_out(str(e), 401)


# user create a new chatbot
@app.route("/api/chatbot/create_chatbot", methods=["POST"])
@jwt_required()
def create_chatbot():
    try:
        user_id = request.json["user_id"]
        chatbot_id = request.json["chatbot_id"]
        available_roles = get_roles()

        # match user_id with auth token
        if user_id != get_jwt_identity():
            return error_out("user_id not match with auth token", 401)

        if chatbot_id not in available_roles:
            return error_out("chatbot_id not exists", 401)
        init_message = json.load(
            open(
                os.path.join(dir_path, "roles", chatbot_id + ".json"),
                encoding="utf-8",
            )
        )
    except Exception as e:
        return error_out(str(e), 401)
    record = {
        "user_id": user_id,
        "chatbot_id": chatbot_id,
        "messages": [init_message],
    }
    if db.new_user_chatbot(record):
        return (
            jsonify(
                {
                    "status": 200,
                    "message": "create chatbot successfully",
                    "success": True,
                    "data": {},
                }
            ),
            200,
        )
    return error_out("create chatbot failed, chatbot already exist", 401)


# user delete a chatbot
@app.route("/api/chatbot/delete_chatbot", methods=["POST"])
@jwt_required()
def delete_chatbot():
    try:
        user_id = request.json["user_id"]
        chatbot_id = request.json["chatbot_id"]
        # match user_id with auth token
        if user_id != get_jwt_identity():
            return error_out("user_id not match with auth token", 401)
        if db.delete_chat_history(user_id, chatbot_id):
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": "delete chatbot successfully",
                        "success": True,
                        "data": {},
                    }
                ),
                200,
            )
        return error_out("delete chatbot failed", 401)
    except Exception as e:
        return error_out(str(e), 401)


# get user chat history
@app.route("/api/chatbot/get_chat_history", methods=["POST"])
@jwt_required()
def get_chat_history():
    try:
        user_id = request.json["user_id"]
        chatbot_id = request.json["chatbot_id"]
        # match user_id with auth token
        if user_id != get_jwt_identity():
            return error_out("user_id not match with auth token", 401)

        if db.user_exists(user_id):
            chat_history = db.get_chat_history(user_id, chatbot_id)
            if chat_history == None:
                return error_out(
                    "user dont have this chatbot, chat history not exists", 401
                )
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": "get chat history successfully",
                        "success": True,
                        "data": {"chat_history": chat_history},
                    }
                ),
                200,
            )
    except Exception as e:
        return error_out(str(e), 401)


# user delete chat history
@app.route("/api/chatbot/delete_chat_history", methods=["POST"])
@jwt_required()
def delete_chat_history():
    try:
        user_id = request.json["user_id"]
        chatbot_id = request.json["chatbot_id"]
        init_message = json.load(
            open(
                os.path.join(dir_path, "roles", chatbot_id + ".json"),
                encoding="utf-8",
            )
        )
        # match user_id with auth token
        if user_id != get_jwt_identity():
            return error_out("user_id not match with auth token", 401)
        if db.update_chat_history(user_id, chatbot_id, [init_message]):
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": "delete chat history successfully",
                        "success": True,
                        "data": {},
                    }
                ),
                200,
            )
        return error_out("delete chat history failed", 401)
    except Exception as e:
        return error_out(str(e), 401)


# get user information
@app.route("/api/user/get_user_info", methods=["POST"])
@jwt_required()
def get_user_info():
    try:
        user_id = request.json["user_id"]
        user_info = db.get_user_info(user_id)
        if user_info == None:
            return error_out("user not exists", 401)
        return (
            jsonify(
                {
                    "status": 200,
                    "message": "get user info successfully",
                    "success": True,
                    "data": {"user_info": user_info},
                }
            ),
            200,
        )
    except Exception as e:
        return error_out(str(e), 401)

# change user password
@app.route("/api/user/change_password", methods=["POST"])
@jwt_required()
def change_password():
    try:
        user_id = request.json["user_id"]
        old_password = request.json["old_password"]
        new_password = request.json["new_password"]
        if db.change_password(user_id, old_password, new_password):
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": "change password successfully",
                        "success": True,
                        "data": {},
                    }
                ),
                200,
            )
        return error_out("change password failed", 401)
    except Exception as e:
        return error_out(str(e), 401)

# change user username
@app.route("/api/user/change_username", methods=["POST"])
@jwt_required()
def change_username():
    try:
        user_id = request.json["user_id"]
        new_username = request.json["new_username"]
        if db.change_username(user_id, new_username):
            return (
                jsonify(
                    {
                        "status": 200,
                        "message": "change username successfully",
                        "success": True,
                        "data": {},
                    }
                ),
                200,
            )
        return error_out("change username failed", 401)
    except Exception as e:
        return error_out(str(e), 401)

if __name__ in "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
