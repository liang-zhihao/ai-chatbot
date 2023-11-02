package com.unimelb.aichatbot.socketio.dto;

import com.google.gson.annotations.SerializedName;

public class RoomMessageData {
    @SerializedName("room_id")

    private String roomId;
    @SerializedName("message")

    private String message;

    @SerializedName("from_user_id")

    private String fromUserId;


    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getters and setters
}