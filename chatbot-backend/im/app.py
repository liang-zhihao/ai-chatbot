from datetime import datetime
import os
from flask import Flask
from flask import request
from database import MongoDB
import configparser
from flask_socketio import SocketIO, send, emit, join_room, leave_room
from message_queue import MessageQueue

file_path = os.path.abspath(__file__)
dir_path = os.path.dirname(file_path)
db = MongoDB()

mq = MessageQueue()


# get configuration
config = configparser.ConfigParser()
config.read(os.path.join(dir_path, "config.ini"))
JWT_SECRET_KEY = config.get("secret", "JWT_SECRET_KEY")
CHAT_LENGTH_LIMIT = 256


def error_out(msg):
    return {"message": msg, "success": False, "data": {}}


# create the app
app = Flask(__name__)

# configure the app
app.config["SECRET_KEY"] = "tv4v9b87t9b7r27yrihdhqcjkfhvutyv3827"
socketio = SocketIO(app, cors_allowed_origins="*")

online_users = {}
connected_users = {}


def get_sid():
    return request.sid


# socketio
@socketio.on("connect")
def handle_connect():
    # user_id = request.args.get('user_id')
    # if not user_id or user_id in connected_users:
    #     return False
    # connected_users[user_id] = request.sid
    print("User Connected", request.sid, flush=True)
    return_message = {
        "status": 201,
        "message": "connect successfully?",
        "sid": get_sid(),
        "success": True,
        "data": {},
    }
    emit("connect2", return_message, room=request.sid)


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
#                 online_users[user_id] = request.sid
#                 # get offline messages
#                 messages = mq.dequeue_messages(user_id)
#                 print(messages, flush=True)
#                 return_message['data']['messages'] = messages
#                 emit('change_status', return_message, room=request.sid)
#             elif status == "offline":
#                 online_users.pop(user_id, None)
#                 emit('change_status', return_message, room=request.sid)
#             print("online users:",get_online_users(), flush=True)
#             return
#         else:
#             emit('change_status',error_out(valid["message"]), room=request.sid)
#     except Exception as e:
#         print(e, flush=True)
#         emit('change_status',error_out(str(e)), room=request.sid)


@socketio.event
def initialize_connection(data):
    try:
        # TODO contain jwt token in the future
        user_id = data["user_id"]
        sid = request.sid
        online_users[sid] = user_id
        return {
            "status": 200,
            "message": "connect successfully",
            "success": True,
            "data": {},
        }
    except Exception as e:
        print(e, flush=True)


@socketio.on("disconnect")
def handle_disconnect():
    # sid is key
    if request.sid in online_users:
        user_id = online_users[request.sid]
        online_users.pop(request.sid, None)
        print("User Disconnected", user_id, flush=True)
        return_message = {
            "status": 200,
            "message": "disconnect successfully",
            "success": True,
            "data": {},
        }
        emit("disconnect", return_message, room=request.sid)


@socketio.event
def private_message(data):
    to_user_id = data["to_user_id"]
    message = data["message"]
    from_user_id = online_users[request.sid]
    # if to_user_id not in online_users:
    #     # save message to queue
    #     mq.enqueue_message(from_user_id, to_user_id, message)
    #     print("private_message", {"from": from_user_id, "message": message}, to_user_id)
    #     return
    # find sid by userid
    to_sid = next(sid for sid, user_id in online_users.items() if user_id == to_user_id)
    data = {
        "timestamp": get_time(),
        "from_user_id": from_user_id,
        "message": message,
    }
    return_message = data
    print("private_message", data, to_user_id, flush=True)
    emit("private_message", return_message, to=to_sid)


@socketio.on("message")
def handleMessage(msg):
    print("Message: " + msg)
    # send(msg, broadcast=True)


@socketio.on("join")
def on_join(data):
    user_id = data["user_id"]
    room_id = data["room_id"]
    join_room(room_id)
    send(user_id + " has entered the room.", to=room_id)


@socketio.on("group_chat")
def handle_group_chat(data):
    room_id = data["room_id"]
    message = data["message"]
    user_id = data["user_id"]

    data = {
        "from_user_id": user_id,
        "to_user_id": room_id,
        "messages": [{"message": message, "timestamp": get_time()}],
    }
    return_message = {
        "status": 200,
        "message": "send message successfully",
        "success": True,
        "data": data,
    }
    print("group_chat", data, room_id, flush=True)
    emit("group_chat", return_message, room=room_id)


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


if __name__ == "__main__":
    socketio.run(app, host="0.0.0.0", port=8001)
