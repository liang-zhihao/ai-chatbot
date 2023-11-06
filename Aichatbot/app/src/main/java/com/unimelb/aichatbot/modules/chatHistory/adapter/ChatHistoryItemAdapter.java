package com.unimelb.aichatbot.modules.chatHistory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimelb.aichatbot.R;

import com.unimelb.aichatbot.databinding.LayoutChatItemBinding;
import com.unimelb.aichatbot.modules.chatHistory.HistoryItem;

import java.util.ArrayList;
import java.util.List;


public class ChatHistoryItemAdapter extends RecyclerView.Adapter<ChatHistoryItemAdapter.ChatHistoryItemViewHolder> {

    private final List<HistoryItem> itemList = new ArrayList<>();
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position, HistoryItem item);
    }

    private final ChatHistoryItemAdapter.OnItemClickListener onItemClickListener;

    public ChatHistoryItemAdapter(Context context, List<HistoryItem> itemList, ChatHistoryItemAdapter.OnItemClickListener listener) {
        this.itemList.addAll(itemList);
        this.onItemClickListener = listener;
        this.context = context;
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

        holder.mIdView.setText(item.getRoomName());
        holder.mContentView.setText(item.getLastMessage());
        Glide.with(context).load(item.getImageUrl()).into(holder.mImageView);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position, item));
        
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
            mContentView = binding.textDescription;
            mImageView = binding.imageView;
        }


    }
}