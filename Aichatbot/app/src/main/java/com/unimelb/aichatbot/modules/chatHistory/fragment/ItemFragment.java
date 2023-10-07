package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.chatHistory.adapter.ChatHistoryItemRecyclerViewAdapter;
import com.unimelb.aichatbot.modules.chatHistory.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ChatHistoryItemRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        // get view
        RecyclerView recyclerView = view.findViewById(R.id.chat_list);

        // set list listener
        ChatHistoryItemRecyclerViewAdapter adapter = new ChatHistoryItemRecyclerViewAdapter(
                PlaceholderContent.ITEMS,
                new ChatHistoryItemRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(PlaceholderContent.PlaceholderItem item) {
                        // to do when click
//                        TODO add intent to message activity and pass chatroom id
                        Toast.makeText(getContext(), "Item clicked: " + item.content, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                        startActivity(intent);
                        intent.putExtra("item_id", item.id);
                    }
                }
        );

        // set list adapter
        recyclerView.setAdapter(adapter);
        mAdapter = adapter;
        return view;
    }

    public void updateList() {
        // update list content
        PlaceholderContent.toggleItems();
        // notify list has changed
        mAdapter.notifyDataSetChanged();
    }

}