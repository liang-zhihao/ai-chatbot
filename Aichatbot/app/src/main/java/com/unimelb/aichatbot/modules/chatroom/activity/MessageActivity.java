package com.unimelb.aichatbot.modules.chatroom.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.ActivityMessageBinding;
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
import com.unimelb.aichatbot.socketio.BaseEvent;
import com.unimelb.aichatbot.socketio.SocketClient;
import com.unimelb.aichatbot.socketio.dto.MessageEvents;
import com.unimelb.aichatbot.socketio.dto.RoomMessageData;
import com.unimelb.aichatbot.util.KeyboardUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class MessageActivity extends AppCompatActivity implements CustomViewController {

    Logger logger = Logger.getLogger("MessageActivity");
    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private MaterialButton sendMessageButton, groupButton;
    private LinearLayout bottomSheetLayout;
    private MessageViewModel messageViewModel;
    private MessageAdapter messageAdapter;
    private int operationStatus = 0;
    private ActivityMessageBinding binding;

    Map<String, SenderType> roleToSenderType = new HashMap<>();

    {
        roleToSenderType.put("user", SenderType.ME);
        roleToSenderType.put("assistant", SenderType.OTHER);
        roleToSenderType.put("system", SenderType.BOT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);

        requestPermission();
        initializeActionBar();
        initializeViewModel();
        initializeView();
        initializeRecyclerView();
        initializeListener();
        initializeBackAction();
// Check if the app has internet permission
        // This callback is only called when MyFragment is at least started
        loadChatHistory();


    }

    @Override
    public void initializeView() {
        messageRecyclerView = binding.messageRecyclerView;
        messageEditText = findViewById(R.id.message_edit_text);
        sendMessageButton = findViewById(R.id.send_message_button);
        groupButton = findViewById(R.id.message_more_options_button);
        bottomSheetLayout = findViewById(R.id.message_bottom_layout);
    }

    @Override
    public void initializeListener() {
        sendMessageButton.setOnClickListener(v -> handleSendMessageBtn());
        groupButton.setOnClickListener(v -> handleGroupButtonBtn());
        messageEditText.setOnClickListener(v -> closeBottomSheetBtn());


    }

    private void initializeBackAction() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loadChatHistory() {

        // List<Message> messages = createMessages();
        ChatService chatService = RetrofitFactory.createWithAuth(ChatService.class, MessageActivity.this);
//       send request to get all messages
//         TODO remove hard code
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
                if (chatHistoryItemList.isEmpty()) {
                    Toast.makeText(MessageActivity.this, "No messages", Toast.LENGTH_SHORT).show();
                    return;
                }
                // convert chatHistoryList to messages
                List<Message> messages1 = new ArrayList<>();
                // TODO date is wrong
                Date currentDate = new Date(); // Reuse the same date object for all messages

                for (ChatHistoryItem chatHistoryItem : chatHistoryItemList) {
                    // Get the sender type based on the role from the map
                    SenderType senderType = roleToSenderType.get(chatHistoryItem.getRole());

                    // If the role is not known, continue to the next iteration
                    if (senderType == null) continue;
                    messages1.add(new Message(
                            chatHistoryItem.getContent(),
                            MessageType.TEXT,
                            chatHistoryItem.getRole(),
                            senderType,
                            currentDate
                    ));

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

    private void requestPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

    }

    @Override
    public void initializeActionBar() {
        // OnBackPressed is different from the back button in the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Chat");
            actionBar.setDisplayHomeAsUpEnabled(true);

            this.addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                    if (menuItem.getItemId() == android.R.id.home) {
                        finish();
                        return true;
                    } else return false;
                }
            }, this, Lifecycle.State.RESUMED);
        }
    }


    private void initializeViewModel() {
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


    private void handleSendMessageBtn() {
        String messageContent = messageEditText.getText().toString();
        // if (messageContent.isEmpty()) {
        //     return;
        // }
        // TODO DEBUG hard code
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
                // TODO hard code
                Message newMessage = new Message(chatWithBotResponse.getReply(), MessageType.TEXT, "Edie", SenderType.OTHER, new Date());
                appendMessageToUI(newMessage);

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

    private void handleGroupButtonBtn() {
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
                closeBottomSheetBtn();
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


    private void closeBottomSheetBtn() {
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

    private void appendMessageToUI(Message message) {
        messageViewModel.addMessage(message);
        messageAdapter.notifyDataSetChanged();
    }

    private void initializeSocketListener() {
        SocketClient.getInstance().on(MessageEvents.MESSAGE.getStr(), args -> {
            Gson gson = new Gson();
            String json = (String) args[0];
            BaseEvent<RoomMessageData> event = gson.fromJson(json, BaseEvent.class);
            System.out.println("Message received: " + event.getData().getMessage());
            //     Update UI
            Message newMessage = new Message(event.getData().getMessage(), MessageType.TEXT, "Edie", SenderType.OTHER, new Date());
            appendMessageToUI(newMessage);
        });
    }

}