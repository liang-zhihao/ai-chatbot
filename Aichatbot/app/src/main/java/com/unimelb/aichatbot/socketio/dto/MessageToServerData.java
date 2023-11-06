package com.unimelb.aichatbot.socketio.dto;

import com.google.gson.annotations.SerializedName;

public class MessageToServerData {
    @SerializedName("room_id")

    private String roomId;
    @SerializedName("message")

    private String message;


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