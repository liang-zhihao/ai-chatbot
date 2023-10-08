package com.unimelb.aichatbot.modules.requestObject;

import com.google.gson.annotations.SerializedName;

public class GetUserRoleRequest {
    @SerializedName("user_id")
    private String userId;

    public  GetUserRoleRequest(String userId) {
        this.userId = userId;
    }
}
