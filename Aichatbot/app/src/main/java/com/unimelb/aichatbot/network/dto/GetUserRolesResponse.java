package com.unimelb.aichatbot.network.dto;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserRolesResponse {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("roles")
    private List<String> roles;

    // Constructors, getters, and setters
}
