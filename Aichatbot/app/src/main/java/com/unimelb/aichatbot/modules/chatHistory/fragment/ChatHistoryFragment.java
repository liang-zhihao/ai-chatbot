package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.FragmentHomeBinding;
import com.unimelb.aichatbot.modules.requestObject.GetChatHistoryRequest;
import com.unimelb.aichatbot.modules.requestObject.GetUserRoleRequest;
import com.unimelb.aichatbot.modules.requestObject.LoginRequest;
import com.unimelb.aichatbot.modules.responsObject.UserChatHistory;
import com.unimelb.aichatbot.modules.responsObject.UserInfo;
import com.unimelb.aichatbot.modules.chatHistory.service.ChatHistoryService;
import com.unimelb.aichatbot.modules.responsObject.UserRoles;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatHistoryFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String userToken;
    ChatHistoryService apiService;
    UserRoles userRoles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_layout, container, false);
        TextView seeMoreButton = root.findViewById(R.id.see_more);


        //do request, create retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chat.unimelb.games/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ChatHistoryService.class);

        //do login request
        doLoginRequest();


        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemFragment itemFragment = (ItemFragment) getChildFragmentManager().findFragmentById(R.id.chatHistory);
                if (itemFragment != null) {
                    itemFragment.updateList();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void doLoginRequest() {


        //do login request
        LoginRequest loginRequest = new LoginRequest("yc975@xxxmail.com", "15151515");
        Call<UserInfo> call = apiService.getUserInfo(loginRequest);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo = response.body();


                    userToken = userInfo.getData().getAccessToken();
                    Log.d("11", userToken);
                    doGetUserRolesRequest();
                } else {

                    Log.d("22", response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

                Log.d("33", t.toString());
            }
        });

    }


    public void doGetUserRolesRequest() {

        GetUserRoleRequest getUserRoleRequest = new GetUserRoleRequest("yc975@xxxmail.com");
        //do get UserRoles
        Call<UserRoles> callUserRoles = apiService.getUserRoles("Bearer "+userToken,getUserRoleRequest);
        callUserRoles.enqueue(new Callback<UserRoles>() {
            @Override
            public void onResponse(Call<UserRoles> call, Response<UserRoles> response) {
                if (response.isSuccessful()) {
                    userRoles = response.body();

                    Log.d("11", userRoles.toString());
                    doGetChatHistoryRequest();

                } else {

                    Log.d("22", response.toString());
                }
            }
            @Override
            public void onFailure(Call<UserRoles> call, Throwable t) {

                Log.d("33", t.toString());
            }
        });

    }

    public void doGetChatHistoryRequest() {

        for (String role:userRoles.getData().getRoles()){
            GetChatHistoryRequest getChatHistoryRequest = new GetChatHistoryRequest("yc975@xxxmail.com",role);

            //Call<UserChatHistory> callUserRoles = apiService.getChatHistory("Bearer "+userToken,"yc975@xxxmail.com",role);

            Call<UserChatHistory> callUserRoles = apiService.getChatHistory("Bearer "+userToken,getChatHistoryRequest);


            callUserRoles.enqueue(new Callback<UserChatHistory>() {
                @Override
                public void onResponse(Call<UserChatHistory> call, Response<UserChatHistory> response) {
                    if (response.isSuccessful()) {
                        UserChatHistory userChatHistory = response.body();

                        Log.d("11", userRoles.toString());

                    } else {

                        Log.d("22", response.toString());
                    }
                }
                @Override
                public void onFailure(Call<UserChatHistory> call, Throwable t) {

                    Log.d("33", t.toString());
                }
            });



        }

    }

}