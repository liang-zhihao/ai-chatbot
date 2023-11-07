package com.unimelb.aichatbot.modules.searchFriend.model;

import com.google.gson.annotations.SerializedName;

public class AddFriendRequest {
    //     {
//     "user_id": "loading1",
//     "friend_id": "loading8425"
// }
    @SerializedName("user_id")
    private String userId;
    @SerializedName("friend_id")
    private String friendId;

    public AddFriendRequest(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
