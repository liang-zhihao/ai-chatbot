package com.unimelb.aichatbot.modules.chatroom.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatroom.adapter.MessageAdapter;
import com.unimelb.aichatbot.modules.chatroom.MessageViewModel;
import com.unimelb.aichatbot.modules.chatroom.model.BottomFragment;
import com.unimelb.aichatbot.modules.chatroom.model.Message;
import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;
import com.unimelb.aichatbot.modules.chatroom.service.ChatService;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ChatHistoryItem;
import com.unimelb.aichatbot.network.dto.ChatWithBotRequest;
import com.unimelb.aichatbot.network.dto.ChatWithBotResponse;
import com.unimelb.aichatbot.network.dto.UserChatHistoryRequest;
import com.unimelb.aichatbot.network.dto.UserChatHistoryResponse;
import com.unimelb.aichatbot.util.KeyboardUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


public class MessageActivity extends AppCompatActivity {

    Logger logger = Logger.getLogger("MessageActivity");
    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private MaterialButton sendMessageButton, groupButton;
    private LinearLayout bottomSheetLayout;
    private MessageViewModel messageViewModel;
    private MessageAdapter messageAdapter;
    private int operationStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setupActionBar();
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        setupViewModel();
        setupViews();
        initializeRecyclerView();
        setClickListeners();
// Check if the app has internet permission


        // List<Message> messages = createMessages();
        ChatService chatService = RetrofitFactory.createWithAuth(ChatService.class, MessageActivity.this);
//       send request to get all messages
        UserChatHistoryRequest userChatHistoryRequest = new UserChatHistoryRequest();
        userChatHistoryRequest.setUserId("loading8425@gmail.com");
        userChatHistoryRequest.setChatbotId("Einstein");
        // get chat history
        chatService.getAllMessages(userChatHistoryRequest.getUserId(), userChatHistoryRequest.getChatbotId()).enqueue(new MyCallback<UserChatHistoryResponse>() {
            @Override
            public void onSuccess(BaseResponse<UserChatHistoryResponse> result) {
                // Your success logic here
                UserChatHistoryResponse userChatHistoryResponse = result.getData();
                List<ChatHistoryItem> chatHistoryItemList = userChatHistoryResponse.getChatHistory();
                assert chatHistoryItemList != null;
                // convert chatHistoryList to messages
                List<Message> messages1 = new ArrayList<>();
                for (ChatHistoryItem chatHistoryItem : chatHistoryItemList) {
                    switch (chatHistoryItem.getRole()) {
                        case "user":
                            messages1.add(new Message(chatHistoryItem.getContent(), MessageType.TEXT, "Loading", SenderType.ME, new Date()));
                            break;
                        case "assistant":
                            messages1.add(new Message(chatHistoryItem.getContent(), MessageType.TEXT, "Edie", SenderType.OTHER, new Date()));
                            break;
                        case "system":
                            messages1.add(new Message(chatHistoryItem.getContent(), MessageType.TEXT, "Edie", SenderType.BOT, new Date()));
                            break;
                    }

                }

                if (chatHistoryItemList.isEmpty()) {
                    Toast.makeText(MessageActivity.this, "No messages", Toast.LENGTH_SHORT).show();
                    return;
                }
                logger.info("messages: " + chatHistoryItemList);
                messageAdapter.submitList(messages1);

            }

            @Override
            public void onError(BaseResponse error, @NonNull Throwable t) {
                // Your failure logic here
                if (error != null) {
                    // Handle server-defined error
                    Log.d("error", error.getMessage());
                    Toast.makeText(MessageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (t != null) {
                    // Handle other types of errors (like network issues)
                    t.printStackTrace();
                    Toast.makeText(MessageActivity.this, "Server is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Chat");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setupViews() {
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendMessageButton = findViewById(R.id.send_message_button);
        groupButton = findViewById(R.id.message_more_options_button);
        bottomSheetLayout = findViewById(R.id.message_bottom_layout);
    }

    private void setupViewModel() {
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getMessages().observe(this, messages -> {
            messageAdapter.submitList(messages); // Or however you update your adapter
            messageRecyclerView.scrollToPosition(messages.size() - 1);
        });
    }

    private void initializeRecyclerView() {
        messageAdapter = new MessageAdapter(this);
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setClickListeners() {
        sendMessageButton.setOnClickListener(v -> handleSendMessage());
        groupButton.setOnClickListener(v -> handleGroupButton());
        messageEditText.setOnClickListener(v -> closeBottomSheet());
    }

    private void handleSendMessage() {
        String messageContent = messageEditText.getText().toString();
        // if (messageContent.isEmpty()) {
        //     return;
        // }
        messageContent = "Only say test";
        messageViewModel.addMessage(new Message(messageContent, MessageType.TEXT, "Loading", SenderType.ME, new Date()));
        messageEditText.setText("");

        ChatService chatService = RetrofitFactory.createWithAuth(ChatService.class, MessageActivity.this);
        ChatWithBotRequest chatWithBotRequest = new ChatWithBotRequest();
        chatWithBotRequest.setChatbotId("Einstein");
        chatWithBotRequest.setUserId("loading8425@gmail.com");
        chatWithBotRequest.setMessage(messageContent);

        chatService.sendMessage(chatWithBotRequest).enqueue(new MyCallback<ChatWithBotResponse>() {
            @Override
            public void onSuccess(BaseResponse<ChatWithBotResponse> result) {
                // Your success logic here
                ChatWithBotResponse chatWithBotResponse = result.getData();
                Toast.makeText(MessageActivity.this, "Successfully sent message!", Toast.LENGTH_SHORT).show();
                messageViewModel.addMessage(new Message(chatWithBotResponse.getReply(), MessageType.TEXT, "Edie", SenderType.OTHER, new Date()));
                messageAdapter.notifyDataSetChanged();

                Log.d("messages", messageViewModel.getMessages().getValue().toString());

            }

            @Override
            public void onError(BaseResponse error, Throwable t) {
                // Your failure logic here
                if (error != null) {
                    // Handle server-defined error
                    Toast.makeText(MessageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("error", error.getMessage());
                } else if (t != null) {
                    // Handle other types of errors (like network issues)
                    t.printStackTrace();
                    Toast.makeText(MessageActivity.this, "Server is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void handleGroupButton() {
        // Existing logic for handling group button click
//
        View currentFocus;
        switch (operationStatus) {
            case 0:
                showBottomSheetDialog();
                operationStatus = 1;
                break;
            case 1:
                messageEditText.requestFocus();
                currentFocus = this.getCurrentFocus();
                KeyboardUtil.openKeyboard(this, currentFocus);
                closeBottomSheet();
                operationStatus = 2;
                break;
            case 2:
                showBottomSheetDialog();
                currentFocus = this.getCurrentFocus();
                KeyboardUtil.hideKeyboard(this, currentFocus);
                messageEditText.clearFocus();
                operationStatus = 1;
        }
    }

    @Override
    public void onBackPressed() {
        if (operationStatus == 0) {
            finish();
        }

        closeBottomSheet();
        operationStatus = 0;
    }

    private void closeBottomSheet() {
        if (hasPopupBottomSheet()) {
            Fragment bottom = getSupportFragmentManager().findFragmentById(R.id.message_bottom_layout);
            if (bottom != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(bottom)
                        .commit();
            }
        }

    }

    private boolean hasPopupBottomSheet() {
        return getSupportFragmentManager().findFragmentById(R.id.message_bottom_layout) != null;
    }

    private void showBottomSheetDialog() {
        if (hasPopupBottomSheet()) {
            return;
        }
        BottomFragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.message_bottom_layout, bottomFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            // your other cases here
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Other existing methods remain mostly unchanged
}