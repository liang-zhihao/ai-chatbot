from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required

# Add additional imports as needed

from app.utils.geo import GeoLocation

from app.utils.common import error_out, get_time, get_role

import os, json

import random

from app.utils.chatbot import Chatbot


geo_bp = Blueprint("geo_bp", __name__)
geo = GeoLocation()
chatbot = Chatbot()


@geo_bp.route("/api/geo/get_nearby_restaurants", methods=["POST"])
@jwt_required()
def get_nearby_restaurants():
    # Get nearby restaurants logic here
    try:
        latitude = request.json["latitude"]
        longitude = request.json["longitude"]
        restaurants = geo.get_nearby_restaurants(latitude, longitude)
        if restaurants == None:
            return error_out("get restaurants failed", 401)
        return (
            jsonify(
                {
                    "status": 200,
                    "message": "get restaurants successfully",
                    "success": True,
                    "data": restaurants,
                }
            ),
            200,
        )
    except Exception as e:
        return error_out(str(e), 401)


@geo_bp.route("/api/geo/get_restaurant_details", methods=["POST"])
@jwt_required()
def get_restaurant_details():
    # Get restaurant details logic here
    try:
        place_id = request.json["place_id"]
        details = geo.get_restaurant_details(place_id)
        if details == None:
            return error_out("get restaurant details failed", 401)
        return (
            jsonify(
                {
                    "status": 200,
                    "message": "get restaurant details successfully",
                    "success": True,
                    "data": details,
                }
            ),
            200,
        )
    except Exception as e:
        return error_out(str(e), 401)


@geo_bp.route("/api/geo/recommend_restaurants", methods=["POST"])
@jwt_required()
def recommend_restaurants():
    # Recommend restaurants logic here
    try:
        latitude = request.json["latitude"]
        longitude = request.json["longitude"]
        init_message = get_role("GordonRamsay")

        # random choose 5 restaurants
        restaurants = geo.get_nearby_restaurants(latitude, longitude)
        if restaurants == None:
            return error_out("get restaurants failed", 401)
        num = min(5, len(restaurants))
        restaurants = random.sample(restaurants, num)

        # get restaurant details
        for restaurant in restaurants:
            details = geo.get_restaurant_details(restaurant["place_id"])
            if details == None:
                return error_out("get restaurant details failed", 401)
            restaurant["details"] = details

        restaurants = json.dumps(restaurants)
        restaurants = (
            "According to below information, give me some advice on which restaurant I should go"
            + restaurants
        )

        question = [init_message, {"role": "user", "content": restaurants}]

        print(question, flush=True)

        reply = chatbot.send_message(question)
        time = get_time()

        return (
            jsonify(
                {
                    "status": 200,
                    "message": "recommend restaurants successfully",
                    "success": True,
                    "data": {"time": time, "reply": reply},
                }
            ),
            200,
        )
    except Exception as e:
        return error_out(str(e), 401)
