package com.unimelb.aichatbot.modules.chatHistory;

public class HistoryItem {
    private String id;
    private String content;
    private int imageResource; // Drawable resource ID

    public HistoryItem(String id, String content, int imageResource) {
        this.id = id;
        this.content = content;
        this.imageResource = imageResource;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
    // Constructor, getters, and setters omitted for brevity
}