package com.unimelb.aichatbot.modules.newChat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.newChat.model.ChatUser;
import com.unimelb.aichatbot.util.ImgUtil;
import com.unimelb.aichatbot.util.UIHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseUserAdapter extends RecyclerView.Adapter<ChooseUserAdapter.ViewHolder> {
    private List<ChatUser> chatUsers;
    private Context context;

    public ChooseUserAdapter(List<ChatUser> chatUsers, Context context) {
        this.chatUsers = chatUsers;
        this.context = context;
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


        holder.chatUserCheckbox.setOnCheckedChangeListener(null);

        holder.chatUserCheckbox.setChecked(user.isSelected());
        ImgUtil.setImgView(context, user.getAvatarUrl(), holder.chatUserImage);
        holder.itemView.setOnClickListener(v -> {
            holder.chatUserCheckbox.setChecked(!holder.chatUserCheckbox.isChecked());
        });
        holder.tag.setText(UIHelper.isBot(user.getUserId()) ? "Bot" : "Human");
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

        CircleImageView chatUserImage;
        TextView tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatUserImage = itemView.findViewById(R.id.image_avatar);
            chatUserCheckbox = itemView.findViewById(R.id.chatUserCheckbox);
            chatUserName = itemView.findViewById(R.id.text_name);
            tag = itemView.findViewById(R.id.tv_tag);
            // Ensure changes in checkbox state update the ChatUser object


        }

    }

    public List<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(List<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }
}

