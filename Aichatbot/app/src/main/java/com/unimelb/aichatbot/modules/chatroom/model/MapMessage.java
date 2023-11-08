package com.unimelb.aichatbot.modules.chatroom.model;

import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;

import java.util.Date;

public class MapMessage extends Message {
    private double latitude;
    private double longitude;

    public MapMessage(String content, MessageType type, String senderId, SenderType senderType, String timestampStr, String senderName, double latitude) {
        super(content, type, senderId, senderType, timestampStr, senderName);
        this.latitude = latitude;
    }

    public MapMessage(String content, MessageType type, String senderId, SenderType senderType, Date timestamp, String senderName, double latitude) {
        super(content, type, senderId, senderType, timestamp, senderName);
        this.latitude = latitude;
    }

    // Getters and Setters for latitude and longitude
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
