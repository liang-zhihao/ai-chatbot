from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity

# Add additional imports as needed
from app.utils.common import error_out, get_time
import os, json

from app.utils.database import db
from app.utils.chatbot import Chatbot
from app.utils.common import get_roles, dir_path

chatbot_bp = Blueprint("chatbot_bp", __name__)
CHAT_LENGTH_LIMIT = 100
chatbot = Chatbot()


@chatbot_bp.route("/api/chatbot/send_message", methods=["POST"])
@jwt_required()
def send_message():
    # Send message to chatbot logic here
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


@chatbot_bp.route("/api/chatbot/create_chatbot", methods=["POST"])
@jwt_required()
def create_chatbot():
    # Create chatbot logic here
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


@chatbot_bp.route("/api/chatbot/delete_chatbot", methods=["POST"])
@jwt_required()
def delete_chatbot():
    # Delete chatbot logic here
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
