package com.unimelb.aichatbot.modules.searchFriend.model;

public class SearchFriendRequest {
    private String prefix;

    public SearchFriendRequest(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
