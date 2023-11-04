from flask import Blueprint, request, jsonify
 
from flask_jwt_extended import jwt_required, get_jwt_identity

# Add additional imports as needed
from app.utils.common import error_out, standard_response

from app.utils.database import db

user_bp = Blueprint("user_bp", __name__)


@user_bp.route("/api/user/get_user_info", methods=["POST"])
@jwt_required()
def get_user_info():
    # Get user info logic here
    try:
        user_id = request.json["user_id"]
        user_info = db.get_user_info(user_id)
        if user_info == None:
            return error_out("user not exists", 401)
        return standard_response(
            200, "Get user info successfully", True, {"user_info": user_info}
        )

    except Exception as e:
        return error_out(str(e), 401)


@user_bp.route("/api/user/change_password", methods=["POST"])
@jwt_required()
def change_password():
    # Change password logic here
    try:
        user_id = request.json["user_id"]
        old_password = request.json["old_password"]
        new_password = request.json["new_password"]
        if db.change_password(user_id, old_password, new_password):
            return standard_response(200, "Change password successfully")
        return error_out("change password failed", 401)
    except Exception as e:
        return error_out(str(e), 401)


@user_bp.route("/api/user/change_username", methods=["POST"])
@jwt_required()
def change_username():
    # Change username logic here
    try:
        user_id = request.json["user_id"]
        new_username = request.json["new_username"]
        if db.change_username(user_id, new_username):
            return standard_response(200, "Change password successfully")
        return error_out("change username failed", 401)
    except Exception as e:
        return error_out(str(e), 401)

# TODO get all users (search friends)

# add friend to friend list