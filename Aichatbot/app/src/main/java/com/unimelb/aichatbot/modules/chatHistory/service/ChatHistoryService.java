package com.unimelb.aichatbot.modules.chatHistory.service;

import com.unimelb.aichatbot.modules.requestObject.GetChatHistoryRequest;
import com.unimelb.aichatbot.modules.requestObject.GetUserRoleRequest;
import com.unimelb.aichatbot.modules.requestObject.LoginRequest;
import com.unimelb.aichatbot.modules.responsObject.UserChatHistory;
import com.unimelb.aichatbot.modules.responsObject.UserInfo;
import com.unimelb.aichatbot.modules.responsObject.UserRoles;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatHistoryService {

    @POST("/api/chatbot/roles")
    Call<UserRoles> getUserRoles(@Header("Authorization") String token,
                                 @Body GetUserRoleRequest getUserRoleRequest);

    @POST("/api/login")
    Call<UserInfo> getUserInfo(@Body LoginRequest loginRequest);

    @POST("api/chatbot/get_chat_history")
    Call<UserChatHistory> getChatHistory(@Header("Authorization") String token,@Body GetChatHistoryRequest getChatHistoryRequest);

//    @GET("api/chatbot/get_chat_history")
//    Call<UserChatHistory> getChatHistory(@Header("Authorization") String token, @Query("user_id") String userId,
//                                         @Query("chatbot_id") String chatbotId);
//
//    @HTTP(method = "GET", path = "api/chatbot/get_chat_history")
//    Call<UserChatHistory> getChatHistory(@Header("Authorization") String token,
//                                         @Query("user_id") String userId,
//                                         @Query("chatbot_id") String chatbotId);

}

