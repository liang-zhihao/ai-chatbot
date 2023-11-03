package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.unimelb.aichatbot.CustomViewController;

import com.unimelb.aichatbot.databinding.FragmentRecentChatBinding;
import com.unimelb.aichatbot.modules.chatHistory.ChatHistoryViewModel;
import com.unimelb.aichatbot.modules.chatHistory.adapter.ChatHistoryItemAdapter;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;

/*
 *
 * Request recent chat
 * Render recent chat
 *
 * TODO improve chat history item UI
 * */

public class RecentChatFragment extends Fragment implements CustomViewController {


    private RecyclerView recyclerView;
    ProgressBar progressBar;

    private FragmentRecentChatBinding binding;
    private MaterialButton btnSend;

    private ChatHistoryViewModel historyViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // TODO Loading animation
        initializeRecyclerView();
        initializeActionBar();

        initializeViewModel();
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
        recyclerView = binding.recentChatRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

    }

    public void initializeViewModel() {
        ChatHistoryItemAdapter.OnItemClickListener listener = (pos, item) -> {
            // to do when click
//                        TODO add intent to message activity and pass chatroom id
            Toast.makeText(getContext(), "Item clicked: " + item.getLastMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra("room_id", item.getRoomId());
            intent.putExtra("room_name", item.getLastMessage());
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
}