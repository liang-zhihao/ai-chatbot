package com.unimelb.aichatbot.modules.newChat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.R;

import java.util.List;

public class ChooseUserAdapter extends RecyclerView.Adapter<ChooseUserAdapter.ViewHolder> {
    private List<ChatUser> chatUsers;

    public ChooseUserAdapter(List<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatUser user = chatUsers.get(position);
        // Remove listener, update state, then add it back

        holder.chatUserName.setText(user.getName());

        holder.chatUserDesc.setText(user.getDescription());

        holder.chatUserCheckbox.setOnCheckedChangeListener(null);

        holder.chatUserCheckbox.setChecked(user.isSelected());

        holder.itemView.setOnClickListener(v -> {
            holder.chatUserCheckbox.setChecked(!holder.chatUserCheckbox.isChecked());
        });
        holder.chatUserCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            user.setSelected(holder.chatUserCheckbox.isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return chatUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox chatUserCheckbox;
        TextView chatUserName;


        TextView chatUserDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatUserCheckbox = itemView.findViewById(R.id.chatUserCheckbox);
            chatUserName = itemView.findViewById(R.id.text_name);
            chatUserDesc = itemView.findViewById(R.id.text_description);
            // Ensure changes in checkbox state update the ChatUser object

            // chatUserCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //     int position = getAdapterPosition();
            //     if (position != RecyclerView.NO_POSITION) {
            //         chatUsers.get(position).setSelected(isChecked);
            //     }
            // });
        }

    }

    public List<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(List<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }
}

