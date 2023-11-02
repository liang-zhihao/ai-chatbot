package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatHistory.HistoryItem;
import com.unimelb.aichatbot.modules.chatHistory.service.ChatHistoryService;
import com.unimelb.aichatbot.network.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryViewModel extends ViewModel {
    private MutableLiveData<List<HistoryItem>> historyItems;


    public LiveData<List<HistoryItem>> getHistoryItems() {
        if (historyItems == null) {
            historyItems = new MutableLiveData<>();
            loadHistoryItems();
        }
        return historyItems;
    }

    public void addItem(HistoryItem newItem) {
        List<HistoryItem> currentItems = historyItems.getValue();
        if (currentItems != null) {
            currentItems.add(newItem);
            historyItems.setValue(currentItems); // Triggers the observer on LiveData
        }
    }

    private void loadHistoryItems() {
        // This method should retrieve the recent history list from a local database or an API
        // Populate the history list with initial data
        List<HistoryItem> historyItemList = new ArrayList<>();
        historyItemList.add(new HistoryItem("Donald Trump", "Hello", R.drawable.donald_trump));
        historyItemList.add(new HistoryItem("Mark Zuckerberg", "Hi", R.drawable.mark_zuckerberg));
        historyItemList.add(new HistoryItem("Donald Trump", "How are you?", R.drawable.donald_trump));
        //     to livedata
        historyItems.setValue(historyItemList);
    }


}
