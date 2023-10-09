from datetime import datetime
import os, json
from flask import Flask, render_template, abort, jsonify
from flask import request
from .database import MongoDB
import configparser
from flask_jwt_extended import create_access_token, jwt_required, get_jwt_identity, JWTManager
from flask_socketio import SocketIO, send, emit


file_path = os.path.abspath(__file__)
dir_path = os.path.dirname(file_path)
db = MongoDB()

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

# Mapping of username to session ID
users = {}

# socketio
@socketio.on('connect')
def handle_connect():
    print("User Connected", request.sid, type(request.sid), flush=True)
    return_message = {
                        'status': 200,
                        'message': "connect successfully",
                        'sid': request.sid,
                        'success': True,
                        'data':{}
                    }
    emit('connect', return_message, room=request.sid)

@socketio.on('disconnect')
def handle_disconnect():
    print('Client disconnected', request.sid, flush=True)
    send('Client disconnected '+ request.sid, broadcast=True)

@socketio.on('private_message')
def handle_private_message(data):
    to_username = data['to']
    message = data['message']
    from_username = data['from']
    to_sid = users.get(to_username)

    print('private_message', {'from': from_username, 'message': message},to_username)
    emit('private_message', {'from': from_username, 'message': message}, room=to_username)

@socketio.on('message')
def handleMessage(msg):
    print('Message: ' + msg)
    send(msg, broadcast=True)

def get_time():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")

if __name__ == '__main__':
    socketio.run(app, host='0.0.0.0', port=8001)