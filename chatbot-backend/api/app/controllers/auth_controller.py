from flask import Blueprint, request
from flask_jwt_extended import create_access_token

from app.utils.common import error_out, standard_response
from app.utils.database import db

# Add additional imports as needed

auth_bp = Blueprint("auth_bp", __name__)


@auth_bp.route("/api/register", methods=["POST"])
def register():
    # Registration logic here
    try:
        user_id = request.json["user_id"]
        username = request.json["username"]
        password = request.json["password"]
        status = db.create_user(user_id, username, password)
        if status["status"] == "success":
            return standard_response(
                status=200,
                message=status["message"],
                success=True,
                data={},
                http_status=200,
            )
    except Exception as e:
        return error_out(str(e), 401)
    return error_out(status["message"], 401)


@auth_bp.route("/api/login", methods=["POST"])
def login():
    # Login logic here
    try:
        user_id = request.json["user_id"]
        password = request.json["password"]
        status = db.login(user_id, password)
        print(status)
        if status["status"] == "success":
            access_token = create_access_token(identity=user_id)
            return standard_response(
                200, status["message"], True, {"access_token": access_token}
            )

    except Exception as e:
        return error_out(str(e), 401)
    return error_out(status["message"], 401)
