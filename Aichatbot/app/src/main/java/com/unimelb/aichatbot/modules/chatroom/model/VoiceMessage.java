package com.unimelb.aichatbot.modules.chatroom.model;

import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;

import java.util.Date;

public class VoiceMessage extends Message {
    private String voiceUrl;

    public VoiceMessage(String content, MessageType type, String senderId, SenderType senderType, String timestampStr, String senderName, String voiceUrl) {
        super(content, type, senderId, senderType, timestampStr, senderName);
        this.voiceUrl = voiceUrl;
    }

    public VoiceMessage(String content, MessageType type, String senderId, SenderType senderType, Date timestamp, String senderName, String voiceUrl) {
        super(content, type, senderId, senderType, timestamp, senderName);
        this.voiceUrl = voiceUrl;
    }

    // Getter and Setter for voiceUrl
    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    // Create Getter and Setter for senderType

}

