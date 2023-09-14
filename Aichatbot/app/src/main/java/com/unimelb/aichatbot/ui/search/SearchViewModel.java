package com.unimelb.aichatbot.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {
    class Friend {
        String AvatarUrl;
        String Name;

        public Friend(String avatarUrl, String name) {
            AvatarUrl = avatarUrl;
            Name = name;
        }

        public String getAvatarUrl() {
            return AvatarUrl;
        }

        public String getName() {
            return Name;
        }
    }

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Friend> mFriends;


    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Friends");
        mFriends = new MutableLiveData<>();
        mFriends.postValue(new Friend("","Beth Murphy"));
        mFriends.postValue(new Friend("","Bonelwa"));
        mFriends.postValue(new Friend("","Leonardo Oliveira"));
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Friend> getFriends() {
        return  mFriends;
    }
}
