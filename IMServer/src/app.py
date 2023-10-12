from datetime import datetime
import os, json
from flask import Flask, render_template, abort, jsonify
from flask import request
from .database import MongoDB
import configparser
from flask_jwt_extended import create_access_token, jwt_required, get_jwt_identity, JWTManager
from flask_socketio import SocketIO, send, emit, disconnect
from .message_queue import MessageQueue

file_path = os.path.abspath(__file__)
dir_path = os.path.dirname(file_path)
db = MongoDB()

mq = MessageQueue()


# get configuration
config = configparser.ConfigParser()
config.read(os.path.join(dir_path,'config.ini'))
JWT_SECRET_KEY = config.get('secret', 'JWT_SECRET_KEY')
CHAT_LENGTH_LIMIT = config.getint('openai', 'CHAT_LENGTH_LIMIT')

def error_out(msg):
    return {
        'message': msg,
        'success': False,
        'data':{}
    }

# create the app
app = Flask(__name__)

# configure the app
app.config['SECRET_KEY'] = 'tv4v9b87t9b7r27yrihdhqcjkfhvutyv3827'
socketio = SocketIO(app, cors_allowed_origins="*")

online_users = {}
connected_users = {}

# socketio
@socketio.on('connect')
def handle_connect():
    # user_id = request.args.get('user_id')
    # if not user_id or user_id in connected_users:
    #     return False
    # connected_users[user_id] = request.sid
    print("User Connected", request.sid, flush=True)
    return_message = {
                    'status': 200,
                    'message': "connect successfully",
                    'sid': request.sid,
                    'success': True,
                    'data':{}
                }
    emit('connect', return_message, room=request.sid)

@socketio.on('change_status')
def handle_change_status(data):
    try:
        user_id = data['user_id']
        password = data['password']
        status = data['status']
        valid = db.login(user_id, password)
        if valid["status"] == "success":
            print("User [",user_id,"] Change Status to", status, flush=True)
            return_message = {
                'status': 200,
                'message': "change status successfully",
                'success': True,
                'data':{}
            }
            if status == "online":
                online_users[user_id] = request.sid
                # get offline messages
                messages = mq.dequeue_messages(user_id)
                print(messages, flush=True)
                return_message['data']['messages'] = messages
                emit('change_status', return_message, room=request.sid)
            elif status == "offline":
                online_users.pop(user_id, None)
                emit('change_status', return_message, room=request.sid)
            print("online users:",get_online_users(), flush=True)
            return
        else:
            emit('change_status',error_out(valid["message"]), room=request.sid)
    except Exception as e:
        print(e, flush=True)
        emit('change_status',error_out(str(e)), room=request.sid)

@socketio.on('disconnect')
def handle_disconnect():
    # find user_id by sid
    user_id = None  
    for key, value in online_users.items():
        if value == request.sid:
            user_id = key
            break
    if user_id:
        online_users.pop(user_id, None)
        print('User disconnected', user_id, request.sid, flush=True)

@socketio.on('private_message')
def handle_private_message(data):
    to_user_id = data['to_user_id']
    message = data['message']
    from_user_id = data['from_user_id']

    if to_user_id not in online_users:
        # save message to queue
        mq.enqueue_message(from_user_id, to_user_id, message)
        print('private_message', {'from': from_user_id, 'message': message},to_user_id)
        return
    to_sid = online_users[to_user_id]
    data = {
        "from_user_id":from_user_id,
        "to_user_id": to_user_id,
        "messages":[{"message":message,'timestamp':get_time()}]
    }
    return_message = {
        'status': 200,
        'message': "send message successfully",
        'success': True,
        'data':data
    }
    print('private_message', data,to_user_id, flush=True)
    emit('private_message', return_message, room=to_sid)

@socketio.on('message')
def handleMessage(msg):
    print('Message: ' + msg)
    #send(msg, broadcast=True)

def get_time():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")

def get_online_users():
    return list(online_users.keys())

if __name__ == '__main__':
    socketio.run(app, host='0.0.0.0', port=8001)