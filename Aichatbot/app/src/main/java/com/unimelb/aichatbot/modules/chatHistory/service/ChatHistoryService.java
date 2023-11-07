package com.unimelb.aichatbot.modules.chatHistory.service;

import com.unimelb.aichatbot.modules.chatHistory.responsObject.RecentChatResponse;
import com.unimelb.aichatbot.network.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatHistoryService {


    @GET("api/chat/list_user_chat_rooms")
    Call<BaseResponse<List<RecentChatResponse>>> getUserChatList(@Query("user_id") String userId);

}

