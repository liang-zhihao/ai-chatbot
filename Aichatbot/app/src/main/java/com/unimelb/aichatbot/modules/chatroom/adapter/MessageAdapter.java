package com.unimelb.aichatbot.modules.chatroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatroom.model.Message;
import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;
import com.unimelb.aichatbot.util.ImgUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.noties.markwon.Markwon;

public class MessageAdapter extends ListAdapter<Message, MessageAdapter.MessageViewHolder> {

    private final Context context;
    private final Markwon markwon; // Assuming this is for Markdown rendering

    public MessageAdapter(@NonNull Context context) {
        super(new MessageDiff());
        this.context = context;
        markwon = Markwon.builder(context).build();
    }

    @Override
    public void submitList(@NonNull List<Message> list) {
        submitList(list, null);
        notifyItemChanged(list.size() - 1);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Adjust your view creation based on viewType
        View view = inflater.inflate(R.layout.layout_comp_msg_box, parent, false);
        if (viewType == MessageAdapter.ViewType.VIEW_TYPE_TEXT_ME.value
                || viewType == MessageAdapter.ViewType.VIEW_TYPE_VIDEO_ME.value
                || viewType == MessageAdapter.ViewType.VIEW_TYPE_VOICE_ME.value
                || viewType == MessageAdapter.ViewType.VIEW_TYPE_MAP_ME.value
                || viewType == MessageAdapter.ViewType.VIEW_TYPE_IMAGE_ME.value) {
            //            remove the avatar and time
            LinearLayout layout = view.findViewById(R.id.ll_avatar_time);
            layout.setVisibility(View.GONE);
//            change card color
            CardView cardView = view.findViewById(R.id.card_gchat_message_other);
            cardView.setCardBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.primary, null));
//            change text color
            TextView textView = view.findViewById(R.id.tv_gchat_message);
            textView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }  /* Handle bot viewTypes accordingly */
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = getItem(position); // Use getItem(position) instead of messageList.get(position)

        // Bind your views here
        holder.messageTextView.setText(message.getContent());
        markwon.setMarkdown(holder.messageTextView, message.getContent());
//            messageViewHolder.avatarImageView.setImageURI(message.getAvatar());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.getDefault());
        String formattedDate = sdf.format(message.getTimestamp());
        String senderAndTime = message.getSenderName() + " - " + formattedDate;
        holder.timeTextView.setText(senderAndTime);
        // TODO real avatar
        ImgUtil.setImgView(context, message.getSenderId(), holder.avatarImageView);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        MessageType type = message.getType();
        SenderType senderType = message.getSenderType();
        switch (senderType) {
            case ME:
                switch (type) {
                    case TEXT:
                        return ViewType.VIEW_TYPE_TEXT_ME.value;
                    case IMAGE:
                        return ViewType.VIEW_TYPE_IMAGE_ME.value;
                    case VIDEO:
                        return ViewType.VIEW_TYPE_VIDEO_ME.value;
                    case VOICE:
                        return ViewType.VIEW_TYPE_VOICE_ME.value;
                    case MAP:
                        return ViewType.VIEW_TYPE_MAP_ME.value;

                }
                break;
            case OTHER:
                switch (type) {
                    case TEXT:
                        return ViewType.VIEW_TYPE_TEXT_OTHER.value;
                    case IMAGE:
                        return ViewType.VIEW_TYPE_IMAGE_OTHER.value;
                    case VIDEO:
                        return ViewType.VIEW_TYPE_VIDEO_OTHER.value;
                    case VOICE:
                        return ViewType.VIEW_TYPE_VOICE_OTHER.value;
                    case MAP:
                        return ViewType.VIEW_TYPE_MAP_OTHER.value;
                }
                break;
            case BOT:
                switch (type) {
                    case TEXT:
                        return ViewType.VIEW_TYPE_TEXT_BOT.value;
                    case IMAGE:
                        return ViewType.VIEW_TYPE_IMAGE_BOT.value;
                    case VIDEO:
                        return ViewType.VIEW_TYPE_VIDEO_BOT.value;
                    case VOICE:
                        return ViewType.VIEW_TYPE_VOICE_BOT.value;
                    case MAP:
                        return ViewType.VIEW_TYPE_MAP_BOT.value;

                }
                break;
        }
        return 0; // Invalid View Type
    }

    enum ViewType {
        VIEW_TYPE_TEXT_ME(1),
        VIEW_TYPE_TEXT_OTHER(2),
        VIEW_TYPE_IMAGE_ME(3),
        VIEW_TYPE_IMAGE_OTHER(4),
        VIEW_TYPE_VIDEO_ME(5),
        VIEW_TYPE_VIDEO_OTHER(6),
        VIEW_TYPE_MAP_ME(7),
        VIEW_TYPE_MAP_OTHER(8),
        VIEW_TYPE_VOICE_ME(9),
        VIEW_TYPE_VOICE_OTHER(10),
        VIEW_TYPE_TEXT_BOT(11),
        VIEW_TYPE_IMAGE_BOT(12),
        VIEW_TYPE_VIDEO_BOT(13),
        VIEW_TYPE_MAP_BOT(14),
        VIEW_TYPE_VOICE_BOT(15);

        private final int value;

        ViewType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // Rest of your Adapter including the MessageDiff and MessageViewHolder remains the same
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        final TextView messageTextView;
        final TextView timeTextView;
        final CircleImageView avatarImageView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.tv_gchat_message);
            timeTextView = itemView.findViewById(R.id.tv_msg__msg_time);
            avatarImageView = itemView.findViewById(R.id.iv_msg__avatar);

            // Initialize other views as per your layout
        }
    }
}

class MessageDiff extends DiffUtil.ItemCallback<Message> {

    @Override
    public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
        // Replace with your actual condition
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
        // Replace with your actual condition
        return oldItem.equals(newItem);
    }
}
