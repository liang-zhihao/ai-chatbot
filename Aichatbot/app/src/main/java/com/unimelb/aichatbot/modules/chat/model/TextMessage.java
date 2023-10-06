package com.unimelb.aichatbot.modules.chat.model;

import com.unimelb.aichatbot.modules.chat.model.type.MessageType;
import com.unimelb.aichatbot.modules.chat.model.type.SenderType;

import java.util.Date;

public class TextMessage extends Message {
    private String markdownText;

    public TextMessage(String content, String sender, SenderType senderType, Date timestamp, String markdownText) {
        super(content, MessageType.TEXT, sender, senderType, timestamp);
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

