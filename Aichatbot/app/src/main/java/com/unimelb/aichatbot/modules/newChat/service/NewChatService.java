package com.unimelb.aichatbot.modules.newChat.service;

import com.unimelb.aichatbot.modules.newChat.model.FriendListRequest;
import com.unimelb.aichatbot.modules.newChat.model.FriendListResponse;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomRequest;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomResponse;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.dto.ChatWithBotRequest;
import com.unimelb.aichatbot.network.dto.ChatWithBotResponse;
import com.unimelb.aichatbot.network.dto.CreateChatbotRequest;
import com.unimelb.aichatbot.network.dto.CreateChatbotResponse;
import com.unimelb.aichatbot.network.dto.UserChatHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewChatService {
    // @GET("messages")
    // Call<List<Message>> getAllMessages();

    // get all friends
    @POST("api/user/get_friends")
    Call<BaseResponse<FriendListResponse>> getFriends(@Body FriendListRequest request);

    // new recent chat
    @POST("api/chat/create_chat_room")
    Call<BaseResponse<NewChatRoomResponse>> createChatroom(@Body NewChatRoomRequest newChatRoomRequest);
}


