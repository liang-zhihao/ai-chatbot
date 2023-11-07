package com.unimelb.aichatbot.modules.searchFriend.model;

import com.unimelb.aichatbot.modules.common.model.FriendListItem;

public class NewFriendListItem extends FriendListItem {
    public NewFriendListItem(String avatarUrl, String name, String userId) {
        super(avatarUrl, name, userId);
    }

    // public View getView(View convertView, ViewGroup parent, Context mContext) {
    //     convertView = super.getView(convertView, parent, mContext);
    //
    //     Button button = (Button) convertView.findViewById(R.id.button_friend);
    //     button.setVisibility(View.VISIBLE);
    //
    //     return convertView;
    // }

}

