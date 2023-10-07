package com.unimelb.aichatbot.modules.searchFriend.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimelb.aichatbot.R;

public class NewFriend extends Friend {
    public NewFriend(String avatarUrl, String name) {
        super(avatarUrl, name);
    }

    public View getView(View convertView, ViewGroup parent, Context mContext) {
        convertView = super.getView(convertView, parent, mContext);

        Button button = (Button) convertView.findViewById(R.id.button_friend);
        button.setVisibility(View.VISIBLE);

        return convertView;
    }

}

