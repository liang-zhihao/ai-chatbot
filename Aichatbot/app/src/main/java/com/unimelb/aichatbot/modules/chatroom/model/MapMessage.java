package com.unimelb.aichatbot.modules.chatroom.model;

import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;

import java.util.Date;

public class MapMessage extends Message {
    private double latitude;
    private double longitude;

    public MapMessage(String content, String sender, SenderType senderType, Date timestamp, double latitude, double longitude) {
        super(content, MessageType.MAP, sender, senderType, timestamp);
        this.latitude = latitude;
        this.longitude = longitude;
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
