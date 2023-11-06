package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.databinding.FragmentRecentChatBinding;
import com.unimelb.aichatbot.modules.chatHistory.ChatHistoryViewModel;
import com.unimelb.aichatbot.modules.chatHistory.HistoryItem;
import com.unimelb.aichatbot.modules.chatHistory.adapter.ChatHistoryItemAdapter;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.RecentChatResponse;
import com.unimelb.aichatbot.modules.chatHistory.service.ChatHistoryService;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.newChat.NewChatActivity;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.socketio.SocketClient;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 *
 * Request recent chat
 * Render recent chat
 *
 *
 * */

public class RecentChatFragment extends Fragment implements CustomViewController {

    private static final String TAG = "RecentChatFragment";
    private RecyclerView recyclerView;
    ProgressBar progressBar;

    private FragmentRecentChatBinding binding;
    private Button newChatBtn;

    private ChatHistoryViewModel historyViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // TODO Loading animation
        initializeView();
        initializeRecyclerView();
        initializeActionBar();
        initializeListener();
        initializeViewModel();
        loadUserRecentChat();
        // init socket client
        SocketClient.startConnection();

        SocketClient.getInstance().emitInitializeConnection(LoginManager.getInstance(requireContext().getApplicationContext()).getUserId());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    private void showLoading(boolean isLoading) {
        progressBar.bringToFront();
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }


    @Override
    public void initializeView() {

        newChatBtn = binding.button;
    }

    public void initializeViewModel() {
        ChatHistoryItemAdapter.OnItemClickListener listener = (pos, item) -> {
            // to do when click
            Toast.makeText(getContext(), "Item clicked: " + item.getLastMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra("roomId", item.getRoomId());
            intent.putExtra("roomName", item.getRoomName());
            startActivity(intent);

        };

        historyViewModel = new ViewModelProvider(this).get(ChatHistoryViewModel.class);
        historyViewModel.getHistoryItems().observe(getViewLifecycleOwner(), items -> {
            // Update UI here
            recyclerView.setAdapter(new ChatHistoryItemAdapter(getContext(), items, listener));
        });

    }

    @Override
    public void initializeRecyclerView() {
        recyclerView = binding.recentChatRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void initializeListener() {
        newChatBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "New chat", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), NewChatActivity.class));
        });
    }

    @Override
    public void initializeActionBar() {
        // OnBackPressed is different from the back button in the action bar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle("Recent Chat");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void loadUserRecentChat() {

        String userId = LoginManager.getInstance(requireContext().getApplicationContext()).getUserId();


        ChatHistoryService chatHistoryService = RetrofitFactory.create(ChatHistoryService.class);
        chatHistoryService.getUserChatList(userId).enqueue(new MyCallback<List<RecentChatResponse>>() {
            @Override
            public void onSuccess(BaseResponse<List<RecentChatResponse>> result) {
                //  render chat list
                List<RecentChatResponse> chats = result.getData();
                for (RecentChatResponse chat : chats) {
                    Log.i(TAG, "get room id: " + chat.getRoomId());
                }
                List<HistoryItem> historyItems = chats.stream()
                        .map(chat -> new HistoryItem(chat.getRoomId(), chat.getLastMessage(), "https://dummyimage.com/300", chat.getName())).collect(Collectors.toList());
                //     toast
                historyViewModel.setHistoryItems(historyItems);
                Toast.makeText(getContext(), "Load recent chat success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(ErrorResponse error, Throwable t) {
                if (error == null) {
                    //     toast error
                    Toast.makeText(getContext(), "Load failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //     print throwable
                    t.printStackTrace();
                    return;
                } else {
                    //     toast error

                    Toast.makeText(getContext(), "Load failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}