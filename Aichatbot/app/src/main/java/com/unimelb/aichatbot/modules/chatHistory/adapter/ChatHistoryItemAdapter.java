package com.unimelb.aichatbot.modules.chatHistory.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.R;

import com.unimelb.aichatbot.databinding.LayoutChatItemBinding;
import com.unimelb.aichatbot.modules.chatHistory.HistoryItem;

import java.util.ArrayList;
import java.util.List;


public class ChatHistoryItemAdapter extends RecyclerView.Adapter<ChatHistoryItemAdapter.ChatHistoryItemViewHolder> {

    private final List<HistoryItem> itemList = new ArrayList<>();


    public interface OnItemClickListener {
        void onItemClick(int position, HistoryItem item);
    }

    private final ChatHistoryItemAdapter.OnItemClickListener onItemClickListener;

    public ChatHistoryItemAdapter(List<HistoryItem> itemList, ChatHistoryItemAdapter.OnItemClickListener listener) {
        this.itemList.addAll(itemList);
        this.onItemClickListener = listener;
    }


    @Override
    public ChatHistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_chat_item, parent, false);
        return new ChatHistoryItemViewHolder(LayoutChatItemBinding.bind(view));

    }

    @Override
    public void onBindViewHolder(final ChatHistoryItemViewHolder holder, int position) {
        HistoryItem item = itemList.get(position);

        holder.mIdView.setText(item.getId());
        holder.mContentView.setText(item.getContent());
        // holder.mImageView.setImageResource(mValues.get(position).imageResourceId);
        // String role = itemList.get(position).image;
        // if (role.equals("Donald Trump")) {
        //     holder.mImageView.setImageResource(R.drawable.donald_trump);
        // } else if (role.equals("Mark Zuckerberg")) {
        //     holder.mImageView.setImageResource(R.drawable.mark_zuckerberg);
        // }
        holder.bind(position, item, onItemClickListener);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ChatHistoryItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;


        public ChatHistoryItemViewHolder(LayoutChatItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mImageView = binding.imageView;

        }

        public void bind(int position, final HistoryItem item, final ChatHistoryItemAdapter.OnItemClickListener listener) {
            // Bind data to the views...
            itemView.setOnClickListener(v -> listener.onItemClick(position, item));
        }

    }
}