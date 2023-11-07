package com.unimelb.aichatbot.modules.chatHistory;

import java.util.List;

public class HistoryItem {
    private String roomId;
    private String lastMessage;
    private String imageUrl; // Drawable resource ID

    private List<String> participants;
    private String roomName;

    public List<String> getParticipants() {
        return participants;
    }


    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public HistoryItem(String roomId, String lastMessage, String imageUrl, String roomName, List<String> participants) {
        this.roomId = roomId;
        this.lastMessage = lastMessage;
        this.imageUrl = imageUrl;
        this.roomName = roomName;
        this.participants = participants;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

// Constructor, getters, and setters omitted for brevity
}