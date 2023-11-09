package com.unimelb.aichatbot.modules.common.model;

public class FriendListItem {
    private String avatarUrl;
    private String name;
    private String description;

    private String userId;

    public FriendListItem(String avatarUrl, String name, String userId) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.description = "";
        this.userId = userId;
    }

    public FriendListItem(String avatarUrl, String name, String description, String userId) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FriendListItem{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
