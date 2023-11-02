package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatHistory.adapter.ChatHistoryItemAdapter;

/*
 *
 * Request recent chat
 * Render recent chat
 *
 *
 * */

public class ChatHistoryFragment extends Fragment {


    private RecyclerView recyclerView;
    ProgressBar progressBar;

    private MaterialButton btnSend;
    private ChatHistoryViewModel historyViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recent_chat, container, false);

        // TODO Loading animation


        recyclerView = root.findViewById(R.id.recent_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // get view
        ChatHistoryItemAdapter.OnItemClickListener listener = (pos, item) -> {
            // to do when click
//                        TODO add intent to message activity and pass chatroom id
            Toast.makeText(getContext(), "Item clicked: " + item.getContent(), Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(getActivity(), MessageActivity.class);
            // startActivity(intent);
            // intent.putExtra("item_id", item.getId());
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        historyViewModel = new ViewModelProvider(this).get(ChatHistoryViewModel.class);
        historyViewModel.getHistoryItems().observe(getViewLifecycleOwner(), items -> {
            // Update UI here
            recyclerView.setAdapter(new ChatHistoryItemAdapter(items, listener));
        });

        // to do when click


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


}