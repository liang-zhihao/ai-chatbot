package com.unimelb.aichatbot.modules.chatroom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimelb.aichatbot.modules.chatroom.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageViewModel extends ViewModel {
    // LiveData for message list
    private final MutableLiveData<List<Message>> messagesLiveData = new MutableLiveData<>();

    public LiveData<List<Message>> getMessages() {
        return messagesLiveData;
    }

    // Logic for adding, removing or modifying messages
    public void addMessage(Message message) {
        List<Message> currentMessages = messagesLiveData.getValue();
        if (currentMessages == null) {
            currentMessages = new ArrayList<>();
        }
        currentMessages.add(message);
        messagesLiveData.setValue(currentMessages);
    }

    public void setMessages(List<Message> messages) {
        messagesLiveData.setValue(messages);
    }
    // Other logic related to messages and UI state
//    send message
//    request message
}
