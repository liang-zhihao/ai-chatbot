package com.unimelb.aichatbot.network.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ChatHistoryItemDto {
    @SerializedName("content")
    private String content;

    @SerializedName("role")
    private String role;
    // 2023-10-07 18:11:10
    // UUID type is used for room_id and sender_id to ensure the format matches UUID structure in JSON.
    @SerializedName("room_id")
    private String roomId;

    @SerializedName("sender_id")
    private String senderId;
    @SerializedName("sender_name")
    private String senderName;
    // List type for read_by to store multiple UUIDs if necessary, assuming read_by is an array of user IDs.
    @SerializedName("read_by")
    private List<String> readBy;


    // Utilizing the Date type for timestamp to handle the date-time format provided.
    private String timestamp;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "ChatHistoryItem{" +
                "content='" + content + '\'' +
                ", role='" + role + '\'' +
                ", roomId='" + roomId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", readBy=" + readBy +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

