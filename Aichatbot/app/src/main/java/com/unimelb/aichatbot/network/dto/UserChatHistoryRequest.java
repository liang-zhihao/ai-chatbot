package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class UserChatHistoryRequest {
    @SerializedName("room_id")
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public UserChatHistoryRequest(String roomId) {
        this.roomId = roomId;
    }
}

