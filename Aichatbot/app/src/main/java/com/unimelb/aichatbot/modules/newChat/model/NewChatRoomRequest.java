package com.unimelb.aichatbot.modules.newChat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewChatRoomRequest {
    @SerializedName("participants")
    private List<String> participants;
    @SerializedName("chat_name")
    private String chatName;
    @SerializedName("created_by")
    private String createdBy;

    public NewChatRoomRequest(List<String> participants, String chatName, String createdBy) {
        this.participants = participants;
        this.chatName = chatName;
        this.createdBy = createdBy;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
