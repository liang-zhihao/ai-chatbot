package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllRolesResponse {
    @SerializedName("roles")
    private List<String> roles;

    // Constructors, getters, and setters
}
