package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.unimelb.aichatbot.BuildConfig;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.FragmentHomeBinding;
import com.unimelb.aichatbot.modules.chatHistory.requestObject.GetChatHistoryRequest;
import com.unimelb.aichatbot.modules.chatHistory.requestObject.GetUserRoleRequest;
import com.unimelb.aichatbot.modules.chatHistory.requestObject.LoginRequest;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserChatHistory;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserInfo;
import com.unimelb.aichatbot.modules.chatHistory.service.ChatHistoryService;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserRoles;
import com.unimelb.aichatbot.network.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatHistoryFragment extends Fragment {
    ItemFragment itemFragment;
    private FragmentHomeBinding binding;
    private String userToken;
    ChatHistoryService apiService;
    UserRoles userRoles;
    UserChatHistory userChatHistory;

    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_layout, container, false);
        TextView seeMoreButton = root.findViewById(R.id.see_more);
        progressBar = root.findViewById(R.id.progressBar);
        itemFragment = (ItemFragment) getChildFragmentManager().findFragmentById(R.id.chatHistory);
        showLoading(true);

        // do request, create retrofit

        apiService = RetrofitFactory.create(ChatHistoryService.class);

        // do login request
        doLoginRequest();


        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemFragment != null) {
                    itemFragment.toggleItems();
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


        // do login request
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
        // do get UserRoles
        Call<UserRoles> callUserRoles = apiService.getUserRoles("Bearer " + userToken, getUserRoleRequest);
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
        List<UserChatHistory> userChatHistoryList = new ArrayList<>();
        for (String role : userRoles.getData().getRoles()) {
            GetChatHistoryRequest getChatHistoryRequest = new GetChatHistoryRequest("yc975@xxxmail.com", role);

            // Call<UserChatHistory> callUserRoles = apiService.getChatHistory("Bearer "+userToken,"yc975@xxxmail.com",role);

            Call<UserChatHistory> callUserRoles = apiService.getChatHistory("Bearer " + userToken, getChatHistoryRequest);


            callUserRoles.enqueue(new Callback<UserChatHistory>() {
                @Override
                public void onResponse(Call<UserChatHistory> call, Response<UserChatHistory> response) {
                    if (response.isSuccessful()) {
                        userChatHistory = response.body();

                        Log.d("11", userChatHistory.getData().toString());
                        userChatHistoryList.add(userChatHistory);
                        itemFragment.displayListItem(userChatHistoryList);

                        showLoading(false);

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

    private void showLoading(boolean isLoading) {
        progressBar.bringToFront();

        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

}