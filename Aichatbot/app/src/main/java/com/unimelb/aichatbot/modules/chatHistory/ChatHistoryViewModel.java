package com.unimelb.aichatbot.modules.chatHistory;

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
        historyItemList.add(new HistoryItem("21", "Hello", "https://dummyimage.com/300", "Trump"));
        historyItemList.add(new HistoryItem("321", "Hi", "https://dummyimage.com/300", "Trump"));
        historyItemList.add(new HistoryItem("21", "How are you?", "https://dummyimage.com/300", "Trump"));
        //     to livedata
        historyItems.setValue(historyItemList);
    }

    public void setHistoryItems(List<HistoryItem> historyItems) {
        this.historyItems.setValue(historyItems);
    }
}
