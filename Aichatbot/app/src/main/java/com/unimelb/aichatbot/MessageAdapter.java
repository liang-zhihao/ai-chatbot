package com.unimelb.aichatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import io.noties.markwon.Markwon;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;

    // Constructor to initialize the message list and context
    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_message_box, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
// obtain an instance of Markwon
        final Markwon markwon = Markwon.create(context);

// set markdown
        markwon.setMarkdown(holder.dateTextView, message.getDate());
        markwon.setMarkdown(holder.userNameTextView, message.getUserName());
        markwon.setMarkdown(holder.messageTextView, message.getMessageText());
//        markwon.setMarkdown(holder.timestampTextView, message.getTimestamp());
//        holder.userNameTextView.setText(message.getUserName());
//        holder.messageTextView.setText(message.getMessageText());
        // Set image resource if you have one
        // holder.profileImageView.setImageResource(message.getImageResource());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        ImageView profileImageView;
        TextView userNameTextView;
        CardView messageCardView;
        TextView messageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.text_gchat_date_other);
            profileImageView = itemView.findViewById(R.id.image_gchat_profile_other);
            userNameTextView = itemView.findViewById(R.id.text_gchat_user_other);
            messageCardView = itemView.findViewById(R.id.card_gchat_message_other);
            messageTextView = itemView.findViewById(R.id.text_gchat_message_other);
        }
    }

    // Create a Message class to hold the data for each message
    public static class Message {
        private String date;
        private String userName;
        private String messageText;
        private String timestamp;
        // private int imageResource; // If you have an image resource

        public Message(String date, String userName, String messageText, String timestamp/*, int imageResource*/) {
            this.date = date;
            this.userName = userName;
            this.messageText = messageText;
            this.timestamp = timestamp;
            // this.imageResource = imageResource;
        }

        public String getDate() {
            return date;
        }

        public String getUserName() {
            return userName;
        }

        public String getMessageText() {
            return messageText;
        }

        public String getTimestamp() {
            return timestamp;
        }

        // public int getImageResource() {
        //     return imageResource;
        // }
    }
}

