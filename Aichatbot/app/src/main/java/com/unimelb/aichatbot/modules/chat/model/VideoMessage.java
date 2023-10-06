package com.unimelb.aichatbot.modules.chat.model;

import com.unimelb.aichatbot.modules.chat.model.type.MessageType;
import com.unimelb.aichatbot.modules.chat.model.type.SenderType;

import java.util.Date;

public class VideoMessage extends Message {
    private String videoUrl;

    public VideoMessage(String content, String sender, SenderType senderType, Date timestamp, String videoUrl) {
        super(content, MessageType.VIDEO, sender, senderType, timestamp);
        this.videoUrl = videoUrl;
    }

    // Getter and Setter for videoUrl
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    // Create Getter and Setter for senderType

}

