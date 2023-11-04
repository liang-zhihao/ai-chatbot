from flask import Flask, render_template, abort, jsonify
from datetime import datetime
import os, sys
import configparser
import json

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
    return roles


def get_config():
    config = configparser.ConfigParser()
    config.read(os.path.join(dir_path, "config.ini"))
    return config


def standard_response(status, message, success=True, data={}, http_status=200):
    response = {"status": status, "message": message, "success": success, "data": data}
    return jsonify(response), http_status


def get_role(role_id):
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
