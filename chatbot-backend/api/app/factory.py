import os
from flask import Flask, jsonify

from app.utils.common import error_out, get_config


from app.controllers.auth_controller import auth_bp
from app.controllers.user_controller import user_bp
from app.controllers.chatbot_controller import chatbot_bp
from app.controllers.geo_controller import geo_bp


from flask_jwt_extended import (
    JWTManager,
)

# from flask_socketio import SocketIO, send, emit


def create_app():
    app = Flask("app", instance_relative_config=True)
    config = get_config()
    # create the app
    JWT_SECRET_KEY = config.get("secret", "JWT_SECRET_KEY")
    CHAT_LENGTH_LIMIT = config.getint("openai", "CHAT_LENGTH_LIMIT")
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

    @app.route("/", methods=["GET"])
    def server_status():
        # Server status logic here
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

    app.register_blueprint(auth_bp)
    app.register_blueprint(user_bp)
    app.register_blueprint(chatbot_bp)
    app.register_blueprint(geo_bp)

    return app


app = create_app()
