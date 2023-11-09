from datetime import datetime

from flask import request, current_app
from flask_socketio import send, emit, join_room, leave_room
import logging
from app.factory import socketio
from app.utils.database import MongoDB
from app.utils.message_queue import MessageQueue
from app.utils.common import intersection, get_roles, utcnow, get_role
from app.utils.chatbot import Chatbot

chatbot = Chatbot()
db = MongoDB()
mq = MessageQueue()
online_users = {}
connected_users = {}


def get_sid():
    return request.sid


def ack(message=None):
    if message is None:
        message = "Message was received!"
    # print("message was received!", flush=True)
    return message


def get_online_user_by_sid(sid):
    return online_users.get(sid, None)


# socketio
@socketio.on("connect")
def handle_connect():
    # user_id = request.args.get('user_id')
    # if not user_id or user_id in connected_users:
    #     return False
    # connected_users[user_id] = get_sid()
    # print("User Connected", get_sid(), flush=True)
    return_message = {
        "status": 200,
        "message": "connect successfully?",
        "sid": get_sid(),
        "success": True,
        "data": {},
    }

    emit("connect", return_message, room=request.sid)


@socketio.event
def initialize_connection(data):
    # TODO contain jwt token

    data = data['data']
    user_id = data["user_id"]
    sid = get_sid()
    online_users[sid] = user_id

    print("User Connected", user_id, "Online users: ", online_users, flush=True)

    # callback
    return ack("initialize_connection: " + user_id)


@socketio.on("disconnect")
def handle_disconnect():
    # sid is key
    if get_sid() in online_users:
        user_id = online_users[get_sid()]
        online_users.pop(get_sid(), None)
        print("User Disconnected", user_id, flush=True)
        return_message = {
            "status": 200,
            "message": "disconnect successfully",
            "success": True,
            "data": {},
        }
        emit("disconnect", return_message, room=get_sid())


# @socketio.event
# def private_message(data):
#     to_user_id = data["to_user_id"]
#     message = data["message"]
#     from_user_id = online_users[get_sid()]
#     # if to_user_id not in online_users:
#     #     # save message to queue
#     #     mq.enqueue_message(from_user_id, to_user_id, message)
#     #     print("private_message", {"from": from_user_id, "message": message}, to_user_id)
#     #     return
#     # find sid by userid
#     to_sid = next(sid for sid, user_id in online_users.items() if user_id == to_user_id)
#     data = {
#         "timestamp": get_time(),
#         "from_user_id": from_user_id,
#         "message": message,
#     }
#     return_message = data
#     print("private_message", data, to_user_id, flush=True)
#     emit("private_message", return_message, to=to_sid)


@socketio.on("message_to_server")
def handle_message(data):
    print("message_to_server", data, flush=True)
    sid = get_sid()
    data = data['data']
    # get data
    room_id = data["room_id"]
    message = data["message"]

    # boardcast
    from_user_id = online_users[sid]
    # get room chat details

    participants = db.get_participants(room_id)
    online_participants = intersection(participants, get_online_users())
    offline_participants = [x for x in participants if x not in online_participants]
    message_json = {
        "role": "user",
        "content": message,
        "room_id": room_id,
        "sender_id": from_user_id,

        # json cannot serialize datetime.datetime.now()
        "timestamp": utcnow(),
        "read_by": [],
        "attachments": [
            # "url": String,
            # "type": String
        ]
    }
    messages = db.get_chat_history(room_id)
    messages.append(message_json)
    db.update_chat_history(room_id, messages)

    message_json["sender_name"] = db.get_user_name(from_user_id)
    data = standard_socket_message("message_to_client", message_json)
    emit("message_to_client", data, room=room_id)

    all_roles = get_roles()
    # if user is aibot
    bots = intersection(participants, all_roles.keys())
    if len(bots) > 0:
        bot_id = bots[0]
        # get bot response
        # get bot name from "bot_Li_Bai" to "Li Bai"
        bot_name = bot_id[4:].replace("_", " ")
        init_prompt = get_role(bot_name)
        prompts = [init_prompt]
        prompts.append({"role": s['role'], "content": s["content"]} for s in messages)
        reply = chatbot.send_message(prompts)
        reply_json = {
            "role": "assistant",
            "content": reply,
            "room_id": room_id,
            "sender_id": bot_id,
            "timestamp": utcnow(),
            "read_by": [],
            "attachments": [
                # "url": String,
                # "type": String
            ]
        }
        messages.append(reply_json)
        db.update_chat_history(room_id, messages)
        # send bot response to user
        reply_json["sender_name"] = db.get_user_name(bot_id)
        print("bot_response", reply, flush=True)
        data = standard_socket_message("message_to_client", data=reply_json)
        emit("message_to_client", data, room=room_id)

    # call chatbot

    # boardcast chatbot response to all users in the room


@socketio.on("join_room")
def on_join(data):
    print("join_room", data, flush=True)
    data = data['data']
    user_id = get_online_user_by_sid(get_sid())
    room_id = data["room_id"]
    join_room(room_id, sid=get_sid())
    return ack(user_id + " has entered the room: " + room_id)


# @socketio.on("group_chat")
# def handle_group_chat(data):
#     room_id = data["room_id"]
#     message = data["message"]
#     user_id = data["user_id"]
#
#     data = {
#         "from_user_id": user_id,
#         "to_user_id": room_id,
#         "messages": [{"message": message, "timestamp": get_time()}],
#     }
#     return_message = {
#         "status": 200,
#         "message": "send message successfully",
#         "success": True,
#         "data": data,
#     }
#     print("group_chat", data, room_id, flush=True)
#     emit("group_chat", return_message, room=room_id)


@socketio.on("leave")
def on_leave(data):
    user_id = data["user_id"]
    room_id = data["room"]
    leave_room(room_id)
    send(user_id + " has left the room.", to=room_id)


def get_time():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


def get_online_users():
    return list(online_users.keys())


def standard_socket_message(event: str, data: dict):
    if data is None:
        data = {}
    response = {
        "event": event,
        "data": data,
        "timestamp": get_time(),
    }
    return response


def callback_message(event: str, data: dict):
    if data is None:
        data = {}
    response = {
        "event": event,
        "data": data,
        "timestamp": get_time(),
    }
    return response

# @socketio.on('change_status')
# def handle_change_status(data):
#     try:
#         user_id = data['user_id']
#         password = data['password']
#         status = data['status']
#         valid = db.login(user_id, password)
#         if valid["status"] == "success":
#             print("User [",user_id,"] Change Status to", status, flush=True)
#             return_message = {
#                 'status': 200,
#                 'message': "change status successfully",
#                 'success': True,
#                 'data':{}
#             }
#             if status == "online":
#                 online_users[user_id] = get_sid()
#                 # get offline messages
#                 messages = mq.dequeue_messages(user_id)
#                 print(messages, flush=True)
#                 return_message['data']['messages'] = messages
#                 emit('change_status', return_message, room=get_sid())
#             elif status == "offline":
#                 online_users.pop(user_id, None)
#                 emit('change_status', return_message, room=get_sid())
#             print("online users:",get_online_users(), flush=True)
#             return
#         else:
#             emit('change_status',error_out(valid["message"]), room=get_sid())
#     except Exception as e:
#         print(e, flush=True)
#         emit('change_status',error_out(str(e)), room=get_sid())
