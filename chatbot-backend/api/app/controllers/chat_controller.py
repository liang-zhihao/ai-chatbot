from datetime import datetime
from uuid import uuid1
from flask import Blueprint, request, jsonify
from app.utils.common import utcnow
from flask_jwt_extended import jwt_required, get_jwt_identity

# Add additional imports as needed
from app.utils.common import error_out, standard_response

from app.utils.database import db

chat_bp = Blueprint("chat_bp", __name__)

"""
chat_db.chat_collection
{
  "_id": ObjectId,
  "id": String,
  "name": String,
  "description": String,
  "participants": [ObjectId],
  "created_at": Date,
  "updated_at": Date,
  "messages": [ObjectId] // References to the messages
}
"""


@chat_bp.route("/api/chat/create_chat_room", methods=["POST"])
def create_chat_room():
    data = request.get_json()
    participants = data.get("participants", [])
    chat_name = data.get("chat_name", "")
    current_user_id = data.get("created_by", "")

    # is private and no duplicate chatroom
    new_chat = {
        "id": str(uuid1()),
        "name": chat_name,
        "participants": participants,
        "created_by": current_user_id,
        "messages": [],
        "is_group_chat": len(participants) > 2,
        "created_at": utcnow()
    }
    # TODO all user in participants should be in the database

    db.get_chat_collection().insert_one(new_chat)
    # Insert logic to create a chat room using received data
    # Return response with appropriate status code
    # print(new_chat)
    return standard_response(201, "Chat room created", data={"room_id": new_chat["id"]})


@chat_bp.route("/api/chat/chat_room_details", methods=["GET"])
def chat_room_details():
    room_id = request.args.get("room_id")
    chat = db.get_chat_by_room_id(room_id)
    # Insert logic to get chat room details
    print(1)
    print(chat)
    return standard_response(200, "Chat room details", data=chat)


@chat_bp.route("/api/chat/list_user_chat_rooms", methods=["GET"])
def list_user_chat_rooms():
    # Optional: Check if the endpoint was reached by an authenticated user
    current_user_id = request.args.get("user_id")
    if current_user_id is None:
        return jsonify({"msg": "Missing or invalid token"}), 401

    # Verify user ID is valid, for example, by checking the format or existence in the user collection

    # Query the database for chats that include the user ID in their participants
    chats = db.get_chat_collection().find({"participants": current_user_id})

    # Format the response into a JSON-friendly format
    chat_list = [
        {
            "room_id": str(chat["id"]),
            "name": chat["name"],
            "participants": chat["participants"],
            "created_at": chat["created_at"],
            "last_message": chat["messages"][-1]["content"] if len(chat["messages"]) > 0 else None,
        }
        for chat in chats
    ]
    # Return the list of chats
    return standard_response(200, "List of chats", data=chat_list)


# get chat room history by room id
@chat_bp.route("/api/chat/get_chat_history", methods=["GET"])
def get_chat_history():
    room_id = request.args.get("room_id")
    print(room_id, flush=True)
    chat = db.get_chat_collection().find_one({"id": room_id})
    if chat is None:
        return standard_response(404, "Chat not found")
    # Insert logic to get chat room history
    # return jsonify({"room_id": room_id, "messages": chat["messages"]}), 200
    return standard_response(200, "Chat history", data={
        "chat_history": chat["messages"]
    })


@chat_bp.route("/api/chat/find_chatroom_by_friend_id", methods=["GET"])
def get_chatroom_by_user_id():
    user_id = request.args.get("user_id")
    friend_id = request.args.get("friend_id")

    chatroom = db.get_chat_collection().find_one({"participants": {"$all": [user_id, friend_id], "$size": 2}})

    # Convert the result to a JSON-friendly format using dumps from bson.json_util if a chatroom is found
    if chatroom:
        return standard_response(200, "Chatroom found", data={
            "id": chatroom["id"],
            "is_group_chat": chatroom["is_group_chat"],
            "name": chatroom["name"],
        })
    else:
        # If no chatroom found, you can return a suitable response
        return error_out("Chatroom not found", 404)

# @chat_bp.route("/api/chat/add_user_to_room", methods=["POST"])
# def add_user_to_room():
#     data = request.get_json()
#     # Insert logic to add user to chat room
#     return jsonify({"room_id": data["room_id"], "user_id": data["user_id"]}), 200


# @chat_bp.route("/api/chat/remove_user_from_room", methods=["DELETE"])
# def remove_user_from_room():
#     room_id = request.args.get("room_id")
#     user_id = request.args.get("user_id")
#     # Insert logic to remove user from chat room
#     return jsonify({"room_id": room_id, "user_id": user_id}), 200
# @chat_bp.route("/update_chat_room", methods=["PATCH"])
# def update_chat_room():
#     data = request.get_json()
#     # Insert logic to update chat room settings
#     return jsonify({"room_id": data["room_id"], "updated": True}), 200
