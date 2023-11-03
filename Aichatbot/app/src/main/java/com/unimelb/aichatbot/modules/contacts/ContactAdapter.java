package com.unimelb.aichatbot.modules.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.RowItemFriendBinding;
import com.unimelb.aichatbot.modules.common.model.FriendListItem;
import com.unimelb.aichatbot.modules.searchFriend.adapter.FriendAdapter;


import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.FriendViewHolder> {
    private List<FriendListItem> mData;
    private Context context;

    private ContactAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, FriendListItem item);
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        RowItemFriendBinding binding;

        public FriendViewHolder(RowItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

    // Constructor
    public ContactAdapter(Context context, List<FriendListItem> data, ContactAdapter.OnItemClickListener listener) {
        this.context = context;
        this.mData = data;
        this.onItemClickListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_item_friend, parent, false);
        return new ContactAdapter.FriendViewHolder(RowItemFriendBinding.bind(view));

    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        FriendListItem current = mData.get(position);
        RowItemFriendBinding binding = holder.binding;
        binding.textName.setText(current.getName());
        binding.textDescription.setText(current.getDescription());
        Glide.with(context).load(current.getAvatarUrl()).into(binding.imageAvatar);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position, current));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }
}




