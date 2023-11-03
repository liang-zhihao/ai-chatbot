package com.unimelb.aichatbot.modules.chatHistory.service;

import com.unimelb.aichatbot.modules.chatHistory.requestObject.GetChatHistoryRequest;
import com.unimelb.aichatbot.modules.chatHistory.requestObject.GetUserRoleRequest;
import com.unimelb.aichatbot.modules.chatHistory.requestObject.LoginRequest;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserChatHistory;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserInfo;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserRoles;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChatHistoryService {

    @POST("/api/chatbot/roles")
    Call<UserRoles> getUserRoles(@Header("Authorization") String token,
                                 @Body GetUserRoleRequest getUserRoleRequest);


    @POST("api/chatbot/get_chat_history")
    Call<UserChatHistory> getChatHistory(@Header("Authorization") String token, @Body GetChatHistoryRequest getChatHistoryRequest);


}

