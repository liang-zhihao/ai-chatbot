package com.unimelb.aichatbot.modules.chat.model;

import com.unimelb.aichatbot.modules.chat.model.type.MessageType;
import com.unimelb.aichatbot.modules.chat.model.type.SenderType;

import java.util.Date;

public class VoiceMessage extends Message {
    private String voiceUrl;

    public VoiceMessage(String content, String sender, SenderType senderType, Date timestamp, String voiceUrl) {
        super(content, MessageType.VOICE, sender, senderType, timestamp);
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

