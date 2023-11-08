package com.unimelb.aichatbot.modules.chatroom.model;

import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;

import java.util.Date;

public class TextMessage extends Message {
    private String markdownText;

    public TextMessage(String content, MessageType type, String senderId, SenderType senderType, String timestampStr, String senderName, String markdownText) {
        super(content, type, senderId, senderType, timestampStr, senderName);
        this.markdownText = markdownText;
    }

    public TextMessage(String content, MessageType type, String senderId, SenderType senderType, Date timestamp, String senderName, String markdownText) {
        super(content, type, senderId, senderType, timestamp, senderName);
        this.markdownText = markdownText;
    }

    // Getter and Setter for markdownText
    public String getMarkdownText() {
        return markdownText;
    }

    public void setMarkdownText(String markdownText) {
        this.markdownText = markdownText;
    }

    // Create Getter and Setter for senderType

}

