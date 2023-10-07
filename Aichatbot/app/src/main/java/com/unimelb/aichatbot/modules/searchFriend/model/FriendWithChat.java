package com.unimelb.aichatbot.modules.searchFriend.model;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unimelb.aichatbot.R;

public class FriendWithChat extends Friend {
    String chat;


    public FriendWithChat(String avatarUrl, String name, String chat) {
        super(avatarUrl, name);
        this.chat = chat;
    }

    public View getView(View convertView, ViewGroup parent, Context mContext) {
        convertView = super.getView(convertView, parent, mContext);

        TextView textChat = (TextView) convertView.findViewById(R.id.text_chat);
        textChat.setText(this.chat);
        textChat.setVisibility(View.VISIBLE);

        return convertView;
    }
}