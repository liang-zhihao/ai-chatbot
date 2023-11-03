package com.unimelb.aichatbot.modules.chatHistory.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.FragmentItemBinding;
import com.unimelb.aichatbot.modules.chatHistory.placeholder.PlaceholderContent;
import com.unimelb.aichatbot.modules.chatHistory.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ChatHistoryItemRecyclerViewAdapter extends RecyclerView.Adapter<ChatHistoryItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;
    private final OnItemClickListener mListener;
    public ChatHistoryItemRecyclerViewAdapter(List<PlaceholderContent.PlaceholderItem> items, OnItemClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(PlaceholderContent.PlaceholderItem item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        //holder.mImageView.setImageResource(mValues.get(position).imageResourceId);
        String role =mValues.get(position).image;
        if(role.equals("DonaldTrump")){
            holder.mImageView.setImageResource(R.drawable.donald_trump);
        } else if (role.equals("MarkZuckerberg")) {
            holder.mImageView.setImageResource(R.drawable.mark_zuckerberg);
        }else if (role.equals("SteveJobs")) {
            holder.mImageView.setImageResource(R.drawable.steve_jobs);
        }else if (role.equals("Einstein")) {
            holder.mImageView.setImageResource(R.drawable.einstein);
        }else if (role.equals("GordonRamsay")) {
            holder.mImageView.setImageResource(R.drawable.gordon_ramsay);
        }else if (role.equals("ElizabethII")) {
            holder.mImageView.setImageResource(R.drawable.elizabeth_ii);
        }else if (role.equals("TroyeSivan")) {
            holder.mImageView.setImageResource(R.drawable.troye_sivan);
        }

        holder.mDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when delete is clicked
                // position will tell you which item was clicked
                onDeleteClicked(position);
            }
        });
    }
    private void onDeleteClicked(int position) {
        // Handle the delete action for the item at the given position
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public PlaceholderItem mItem;
        public ImageView mDeleteImageView;
        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mImageView = binding.imageView;
            mDeleteImageView = binding.delete;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(mItem);
                    }
                }
            });

            // Set the click listener for the delete button


        }





        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}