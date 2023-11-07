import configparser
import json
import os
from datetime import datetime, timezone
from typing import List

from flask import jsonify, current_app

file_path = os.path.abspath(__file__)
dir_path = os.path.dirname(file_path)


def error_out(msg, code):
    return jsonify({"status": code, "message": msg, "success": False, "data": {}}), code


def get_time():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


def get_roles():
    role_dir = os.path.join(dir_path, "roles")
    roles = os.listdir(role_dir)
    roles = [r[:-5] for r in roles]
    o = {}
    for i in range(len(roles)):
        name = roles[i]
        id = "bot_" + name.replace(" ", "_")
        o[id] = get_role(name)

    return o


def get_logger():
    return current_app.logger


def get_config():
    config = configparser.ConfigParser()
    config.read(os.path.join(dir_path, "config.ini"))
    return config


def standard_response(status, message, success=True, data=None, http_status=200):
    if data is None:
        data = {}
    response = {
        "status": status,
        "message": message,
        "success": success,
        "data": data,
        "timestamp": utcnow(),
    }
    return jsonify(response), http_status


def get_role(role_id) -> dict:
    try:
        role = json.load(
            open(
                os.path.join(dir_path, "roles", role_id + ".json"),
                encoding="utf-8",
            )
        )
        return role
    except Exception as e:
        return error_out(str(e), 401)


def intersection(lst1: List, lst2: List):
    return list(set(lst1) & set(lst2))


def utcnow() -> str:
    return datetime.now(timezone.utc).isoformat()
