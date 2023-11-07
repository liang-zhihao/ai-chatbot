package com.unimelb.aichatbot.modules.searchFriend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.RowItemFriendBinding;
import com.unimelb.aichatbot.modules.common.model.FriendListItem;
import com.unimelb.aichatbot.util.ImgUtil;
import com.unimelb.aichatbot.util.MD5Util;
import com.unimelb.aichatbot.util.UIHelper;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private List<FriendListItem> mData;

    private Context context;

    private FriendAdapter.OnItemClickListener onItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and you provide access to all the views for a data item in a view holder.
    public interface OnItemClickListener {
        void onItemClick(int position, FriendListItem item);
    }

    // Constructor
    public FriendAdapter(Context context, List<FriendListItem> data, OnItemClickListener listener) {
        this.context = context;
        this.mData = data;
        this.onItemClickListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_item_friend, parent, false);

        return new FriendAdapter.FriendViewHolder(RowItemFriendBinding.bind(view));

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        FriendListItem current = mData.get(position);

        RowItemFriendBinding binding = holder.binding;
        binding.textName.setText(current.getName());
        binding.textDescription.setText(current.getDescription());

        binding.tvTag.setText(UIHelper.isBot(current.getUserId()) ? "Bot" : "Human");
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position, current));
        ImgUtil.setImgView(context, current.getAvatarUrl(), binding.imageAvatar);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        RowItemFriendBinding binding;
        public View v;

        public FriendViewHolder(RowItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            v = binding.getRoot();
        }


    }

}

