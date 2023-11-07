package com.unimelb.aichatbot.modules.contacts.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.databinding.FragmentContactBinding;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.chatroom.model.ChatDetailResponse;
import com.unimelb.aichatbot.modules.chatroom.service.ChatService;
import com.unimelb.aichatbot.modules.common.model.FriendListItem;
import com.unimelb.aichatbot.modules.contacts.ContactAdapter;
import com.unimelb.aichatbot.modules.contacts.ContactViewModel;
import com.unimelb.aichatbot.modules.newChat.model.ChatUser;
import com.unimelb.aichatbot.modules.newChat.model.FriendListRequest;
import com.unimelb.aichatbot.modules.newChat.model.FriendListResponse;
import com.unimelb.aichatbot.modules.newChat.service.NewChatService;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.List;
import java.util.stream.Collectors;

/*
 * todo existing  friends and start a new chat or existing chat
 * */
public class ContactFragment extends Fragment implements CustomViewController {

    private FragmentContactBinding binding;
    private ContactAdapter contactAdapter;

    private RecyclerView recyclerView;
    private ContactViewModel contactViewModel;
    private static final String TAG = "ContactFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeRecyclerView();
        initializeViewModel();


        return root;
    }


    @Override
    public void initializeViewModel() {
        ContactAdapter.OnItemClickListener listener = (position, item) -> {
            String userId = LoginManager.getInstance(getApplicationContext()).getUserId();
            RetrofitFactory.createWithAuth(ChatService.class, getApplicationContext()).getChatRoomDetailsByFriendId(item.getUserId(), userId).enqueue(new MyCallback<ChatDetailResponse>() {
                @Override
                public void onSuccess(BaseResponse<ChatDetailResponse> result) {
                    Log.i(TAG, "onSuccess: " + result);
                    Intent intent = new Intent(requireActivity(), MessageActivity.class);
                    intent.putExtra("roomId", result.getData().getId());
                   
                    startActivity(intent);
                }

                @Override
                public void onError(ErrorResponse error, Throwable t) {
                    if (error != null) {
                        Log.i(TAG, "onError: " + Log.e(TAG, "onError: " + error.getMessage(), t));
                    }
                    if (t != null) {
                        t.printStackTrace();
                    }
                }
            });

        };


        contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getFriends().observe(getViewLifecycleOwner(), friends -> {
            // When data changes, update the adapter's data set
            contactAdapter = new ContactAdapter(requireContext(), friends, listener);
            recyclerView.setAdapter(contactAdapter);
        });


    }

    private Context getApplicationContext() {
        return requireContext().getApplicationContext();
    }

    public void loadFriends() {

        String userId = LoginManager.getInstance(getApplicationContext()).getUserId();
        //     todo load friends by api
        NewChatService newChatService = RetrofitFactory.createWithAuth(NewChatService.class, getApplicationContext());

        FriendListRequest friendListRequest = new FriendListRequest(userId);
        newChatService.getFriends(friendListRequest).enqueue(new MyCallback<FriendListResponse>() {
            @Override
            public void onSuccess(BaseResponse<FriendListResponse> result) {
                Log.i(TAG, "onSuccess: " + result);
                // Using Stream (requires API level 24 or higher) to convert and collect in one line.
                List<FriendListItem> users = result.getData().getFriends().stream().map(friend -> new FriendListItem(
                        friend.getAvatar(),
                        friend.getUsername(),
                        friend.getUserId()
                )).collect(Collectors.toList());
                contactViewModel.setFriends(users);
            }

            @Override
            public void onError(ErrorResponse error, Throwable t) {
                if (error != null) {
                    Log.i(TAG, "onError: " + Log.e(TAG, "onError: " + error.getMessage(), t));
                }
                if (t != null) {
                    t.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initializeRecyclerView() {
        recyclerView = binding.recyclerViewContacts; // Updated ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        loadFriends();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // binding = null;
    }

    @Override
    public void initializeView() {

    }

    @Override
    public void initializeListener() {

    }

    @Override
    public void initializeActionBar() {

    }
}