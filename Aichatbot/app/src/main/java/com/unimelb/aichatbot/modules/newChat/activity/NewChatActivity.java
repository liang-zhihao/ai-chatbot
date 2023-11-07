package com.unimelb.aichatbot.modules.newChat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.RecentChatResponse;
import com.unimelb.aichatbot.modules.chatHistory.service.ChatHistoryService;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.newChat.model.ChatUser;
import com.unimelb.aichatbot.modules.newChat.adapter.ChooseUserAdapter;
import com.unimelb.aichatbot.modules.newChat.model.FriendListRequest;
import com.unimelb.aichatbot.modules.newChat.model.FriendListResponse;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomRequest;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomResponse;
import com.unimelb.aichatbot.modules.newChat.service.NewChatService;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * TODO change to item layout to contact
 *  start new group chat
 * */
public class NewChatActivity extends AppCompatActivity implements CustomViewController {

    private static final String TAG = "NewChatActivity";

    private RecyclerView recyclerView;

    private ChooseUserAdapter adapter;

    private HashMap<String, RecentChatResponse> chats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        loadUserRecentChat();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChooseUserAdapter(new ArrayList<>(), this);
        initializeActionBar();
        loadFriends();
    }

    public void loadUserRecentChat() {

        String userId = LoginManager.getInstance(getApplicationContext()).getUserId();
        ChatHistoryService chatHistoryService = RetrofitFactory.create(ChatHistoryService.class);
        chatHistoryService.getUserChatList(userId).enqueue(new MyCallback<List<RecentChatResponse>>() {
            @Override
            public void onSuccess(BaseResponse<List<RecentChatResponse>> result) {
                //  render chat list
                List<RecentChatResponse> chats = result.getData();
                NewChatActivity.this.chats = new HashMap<>();
                for (RecentChatResponse chat : chats) {
                    NewChatActivity.this.chats.put(chat.getRoomId(), chat);
                }
                //     toast
            }

            @Override
            public void onError(ErrorResponse error, Throwable t) {
                if (error == null) {
                    //     toast error
                    Toast.makeText(getApplicationContext(), "Load failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //     print throwable
                    t.printStackTrace();
                    return;
                } else {
                    //     toast error
                    Toast.makeText(getApplicationContext(), "Load failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                List<ChatUser> users = result.getData().getFriends().stream()
                        .map(friend -> new ChatUser(friend.getUsername(), friend.getAvatar(), "", friend.getUserId()))
                        .collect(Collectors.toList());

                adapter.setChatUsers(users);
                adapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            handleMultipleSelections();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String containsParticipant(List<String> userIds) {
        for (String roomId : chats.keySet()) {
            List<String> participants = Objects.requireNonNull(this.chats.get(roomId)).getParticipants();
            assert participants != null;
            if (new HashSet<>(participants).containsAll(userIds) && participants.size() == userIds.size()) {
                return roomId;
            }
        }
        return null;

    }

    private void handleMultipleSelections() {
        List<ChatUser> selectedUsers = new ArrayList<>();
        for (ChatUser user : adapter.getChatUsers()) {
            if (user.isSelected()) {
                selectedUsers.add(user);
            }
        }
        Log.i(TAG, "Selected Users: " + selectedUsers);
        // Here, you can do whatever you want with the selected users.
        // For example, starting a group chat, or displaying their names.
        // todo select users
        // todo check if chatroom exists
        String roomId = containsParticipant(selectedUsers.stream().map(ChatUser::getUserId).collect(Collectors.toList()));
        Intent intent = new Intent(NewChatActivity.this, MessageActivity.class);
        if (roomId != null) {
            // todo go to chatroom
            intent.putExtra("roomId", roomId);
            intent.putExtra("roomName", Objects.requireNonNull(chats.get(roomId)).getName());
            startActivity(intent);
            finish();
        } else {
            // todo create chatroom
            newChatroomRequest(selectedUsers);
        }


    }

    public void newChatroomRequest(List<ChatUser> selectedUsers) {
        String userId = LoginManager.getInstance(getApplicationContext()).getUserId();
        String username = LoginManager.getInstance(getApplicationContext()).getUsername();

        StringBuilder names = new StringBuilder(username);

        // string such as group chat of a, b, c, d
        for (ChatUser user : selectedUsers) {
            names.append(", ").append(user.getName());
        }
        String chatroomName = names.toString();
        //
        List<String> userIds = selectedUsers.stream().map(ChatUser::getUserId).collect(Collectors.toList());
        // add myself
        userIds.add(userId);
        NewChatRoomRequest newChatRoomRequest = new NewChatRoomRequest(userIds, chatroomName, userId);
        RetrofitFactory.createWithAuth(NewChatService.class, getApplicationContext()).createChatroom(newChatRoomRequest).enqueue(new MyCallback<NewChatRoomResponse>() {
            @Override
            public void onSuccess(BaseResponse<NewChatRoomResponse> result) {
                Log.i(TAG, "onSuccess: " + result);
                String chatroom = result.getData().getChatRoomId();
                Intent intent = new Intent(NewChatActivity.this, MessageActivity.class);
                intent.putExtra("roomId", chatroom);
                intent.putExtra("roomName", chatroomName);
                startActivity(intent);
                finish();
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
    public void initializeView() {

    }

    @Override
    public void initializeListener() {

    }


    @Override
    public void initializeActionBar() {
        // OnBackPressed is different from the back button in the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle("New Chat");
            actionBar.setDisplayHomeAsUpEnabled(true);
            this.addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                    if (menuItem.getItemId() == android.R.id.home) {
                        finish();
                        return true;
                    } else return false;
                }
            }, this, Lifecycle.State.RESUMED);
        }
    }

    @Override
    public void initializeViewModel() {

    }

    @Override
    public void initializeRecyclerView() {

    }
}
