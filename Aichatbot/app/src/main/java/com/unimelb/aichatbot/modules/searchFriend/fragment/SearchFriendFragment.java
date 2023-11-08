package com.unimelb.aichatbot.modules.searchFriend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.databinding.FragmentSearchBinding;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.common.model.FriendListItem;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomRequest;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomResponse;
import com.unimelb.aichatbot.modules.newChat.service.NewChatService;
import com.unimelb.aichatbot.modules.searchFriend.SearchViewModel;
import com.unimelb.aichatbot.modules.searchFriend.adapter.FriendAdapter;
import com.unimelb.aichatbot.modules.searchFriend.model.AddFriendRequest;
import com.unimelb.aichatbot.modules.searchFriend.model.SearchFriendRequest;
import com.unimelb.aichatbot.modules.searchFriend.model.SearchFriendResponse;
import com.unimelb.aichatbot.modules.searchFriend.service.SearchFriendService;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.network.dto.UserInfoResponse;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 *  todo test: search friend and start a new chat
 * */
public class SearchFriendFragment extends Fragment implements CustomViewController {

    private FragmentSearchBinding binding;
    private FriendAdapter friendAdapter;

    private SearchViewModel searchViewModel;

    private SearchView searchView;
    private static final String TAG = "SearchFriendFragment";
    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeView();

        // searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        initializeRecyclerView();
        initializeViewModel();
        setupSearchViewListener();
        return root;
    }

    private void setupSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFriends(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    public void searchFriends(String query) {

        SearchFriendRequest request = new SearchFriendRequest(query);
        // TODO api call to search friends and with auth token
        RetrofitFactory.create(SearchFriendService.class).searchFriendByName(request).enqueue(new MyCallback<SearchFriendResponse>() {
            @Override
            public void onSuccess(BaseResponse<SearchFriendResponse> result) {
                List<UserInfoResponse> users = result.getData().getUsers();
                List<FriendListItem> friends = users.stream().map(user -> new FriendListItem(user.getAvatar(), user.getUsername(), user.getUserId())).collect(Collectors.toList());
                searchViewModel.setFriends(friends);
            }

            @Override
            public void onError(ErrorResponse error, @NonNull Throwable t) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onError: " + error.getMessage());
                }
                if (t != null) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onError: " + t.getMessage());
                    t.printStackTrace();
                }
            }
        });
    }


    public void startNewChatRequest(FriendListItem item) {
        // TODO onclick item lister: api request and start chat with friend
        SearchFriendService searchFriendService = RetrofitFactory.createWithAuth(SearchFriendService.class, Objects.requireNonNull(getContext()).getApplicationContext());
        String friendId = item.getUserId();
        String userId = LoginManager.getInstance(requireContext().getApplicationContext()).getUserId();

        searchFriendService.addFriend(new AddFriendRequest(userId, friendId)).enqueue(new MyCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                Log.i(TAG, "onSuccess: " + result.toString());
                Toast.makeText(getContext(), "Add friend success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ErrorResponse error, Throwable t) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (t != null) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });
        String username = LoginManager.getInstance(requireContext().getApplicationContext()).getUsername();
        String friendName = item.getName();

        List<String> participants = new ArrayList<>();
        participants.add(userId);
        participants.add(friendId);
        String chatName = username + ", " + friendName;
        RetrofitFactory.create(NewChatService.class).createChatroom(new NewChatRoomRequest(participants, chatName, userId)).enqueue(new MyCallback<NewChatRoomResponse>() {
            @Override
            public void onSuccess(BaseResponse<NewChatRoomResponse> result) {
                Toast.makeText(getContext(), "Create room success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("roomId", result.getData().getChatRoomId());
                startActivity(intent);
            }

            @Override
            public void onError(ErrorResponse error, Throwable t) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (t != null) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void initializeView() {
        searchView = binding.searchTextSearch;
    }

    @Override
    public void initializeListener() {

    }

    @Override
    public void initializeActionBar() {

    }

    @Override
    public void initializeViewModel() {
        FriendAdapter.OnItemClickListener onItemClickListener = new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, FriendListItem item) {

                startNewChatRequest(item);
            }
        };
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getFriends().observe(getViewLifecycleOwner(), friends -> {
            // When data changes, update the adapter's data set
            friendAdapter = new FriendAdapter(getContext(), friends, onItemClickListener);
            recyclerView.setAdapter(friendAdapter);
        });
    }

    @Override
    public void initializeRecyclerView() {
        recyclerView = binding.recyclerViewSearch; // Updated ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
