package com.unimelb.aichatbot.modules.chatHistory.service;

import com.unimelb.aichatbot.modules.chatHistory.requestObject.GetChatHistoryRequest;
import com.unimelb.aichatbot.modules.chatHistory.requestObject.GetUserRoleRequest;
import com.unimelb.aichatbot.modules.chatHistory.requestObject.LoginRequest;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.RecentChatResponse;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserChatHistory;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserInfo;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserRoles;
import com.unimelb.aichatbot.network.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatHistoryService {

    // @POST("/api/chatbot/roles")
    // Call<UserRoles> getUserRoles(@Header("Authorization") String token,
    //                              @Body GetUserRoleRequest getUserRoleRequest);
    //
    //
    // @POST("api/chatbot/get_chat_history")
    // Call<UserChatHistory> getChatHistory(@Header("Authorization") String token, @Body GetChatHistoryRequest getChatHistoryRequest);

    @GET("api/chat/list_user_chat_rooms")
    Call<BaseResponse<List<RecentChatResponse>>> getUserChatList(@Query("user_id") String userId);

}

