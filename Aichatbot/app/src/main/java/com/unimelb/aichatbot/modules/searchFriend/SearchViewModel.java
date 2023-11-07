package com.unimelb.aichatbot.modules.searchFriend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimelb.aichatbot.modules.common.model.FriendListItem;

import java.util.LinkedList;
import java.util.List;

public class SearchViewModel extends ViewModel {


    private MutableLiveData<List<FriendListItem>> mFriends;


    public LiveData<List<FriendListItem>> getFriends() {
        if (mFriends == null) {
            mFriends = new MutableLiveData<>();
            initFriends();
        }
        return mFriends;
    }

    private void initFriends() {
        List<FriendListItem> friendListItems = new LinkedList<>();
        // friendListItems.add(new FriendListItem("https://api.horosama.com/random.php", "Beth Murphy", ""));
        // friendListItems.add(new FriendListItem("https://api.horosama.com/random.php", "Bonelwa", ""));
        // friendListItems.add(new FriendListItem("https://api.horosama.com/random.php", "Leonardo Oliveira", ""));
        // friendListItems.add(new FriendListItem("https://api.horosama.com/random.php", "Bonelwa", "Are You Ready To Buy A Home Theater Audio…", ""));
        // friendListItems.add(new FriendListItem("https://api.horosama.com/random.php", "Leonardo Oliveira", "29 Motivational Quotes For Business And ", ""));
        mFriends.setValue(friendListItems);
    }

    public void addFriend(FriendListItem friendListItem) {
        List<FriendListItem> friendListItems = mFriends.getValue();
        assert friendListItems != null;
        friendListItems.add(friendListItem);
        mFriends.setValue(friendListItems);
    }

    public void setFriends(List<FriendListItem> friendListItems) {
        if (mFriends == null)
            mFriends = new MutableLiveData<>();
        mFriends.setValue(friendListItems);
    }
}
