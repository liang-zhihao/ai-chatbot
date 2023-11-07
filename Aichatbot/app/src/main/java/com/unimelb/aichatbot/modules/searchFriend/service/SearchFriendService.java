package com.unimelb.aichatbot.modules.searchFriend.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.unimelb.aichatbot.modules.searchFriend.model.AddFriendRequest;
import com.unimelb.aichatbot.modules.searchFriend.model.SearchFriendRequest;
import com.unimelb.aichatbot.modules.searchFriend.model.SearchFriendResponse;
import com.unimelb.aichatbot.network.BaseResponse;

import java.util.List;

public interface SearchFriendService {
    @POST("api/user/search_user")
    Call<BaseResponse<SearchFriendResponse>> searchFriendByName(@Body SearchFriendRequest searchFriendRequest);

    //     add friend
    @POST("api/user/add_friend")
    Call<BaseResponse> addFriend(@Body AddFriendRequest addFriendRequest);

}
