package com.unimelb.aichatbot.modules.chatHistory.responsObject;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;


public class RecentChatResponse {
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("last_message")
    private String lastMessage; // Assuming 'null' is a valid value and represents no message.

    private String name;
    private List<String> participants;

    @SerializedName("room_id")
    private String roomId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RecentChatResponse{" +
                "createdAt='" + createdAt + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", name='" + name + '\'' +
                ", participants=" + participants +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}

