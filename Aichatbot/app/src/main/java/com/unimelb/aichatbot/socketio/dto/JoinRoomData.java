package com.unimelb.aichatbot.socketio.dto;

import com.google.gson.annotations.SerializedName;

public class JoinRoomData {
    @SerializedName("room_id")
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public JoinRoomData(String roomId) {
        this.roomId = roomId;
    }
}
