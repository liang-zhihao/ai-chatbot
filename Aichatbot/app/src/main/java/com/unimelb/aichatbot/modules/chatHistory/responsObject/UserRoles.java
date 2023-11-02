package com.unimelb.aichatbot.modules.chatHistory.responsObject;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserRoles {
    @SerializedName("data")
    private Data data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;
    @SerializedName("success")
    private boolean success;

    // Getters and setters...

    public static class Data {
        @SerializedName("roles")
        private List<String> roles;

        @Override
        public String toString() {
            return "Data{" +
                    "roles=" + roles +
                    '}';
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }

    @Override
    public String toString() {
        return "UserRoles{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", success=" + success +
                '}';
    }

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
