package com.unimelb.aichatbot.modules.newChat.model;

import com.google.gson.annotations.SerializedName;

public class NewChatRoomResponse {
    @SerializedName("room_id")
    public String chatRoomId;

    public NewChatRoomResponse(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
