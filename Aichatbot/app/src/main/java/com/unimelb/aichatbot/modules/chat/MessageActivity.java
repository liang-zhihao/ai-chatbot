package com.unimelb.aichatbot.modules.chat;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chat.model.BottomFragment;
import com.unimelb.aichatbot.modules.chat.model.Message;
import com.unimelb.aichatbot.modules.chat.model.type.MessageType;
import com.unimelb.aichatbot.modules.chat.model.type.SenderType;
import com.unimelb.aichatbot.modules.chat.services.ApiService;
import com.unimelb.aichatbot.modules.chat.services.RetrofitClient;
import com.unimelb.aichatbot.util.KeyboardUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private MaterialButton sendMessageButton, groupButton;
    private LinearLayout bottomSheetLayout;
    private MessageViewModel viewModel;
    private MessageAdapter messageAdapter;
    Logger logger = Logger.getLogger("MessageActivity");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);


        viewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        setupViews();
        observeViewModel();
        initializeRecyclerView();
        setClickListeners();
// Check if the app has internet permission


        List<Message> messages = createMessages();
        ApiService apiService = RetrofitClient.getService();
        Call<List<Message>> call = apiService.getAllMessages();
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> messages = response.body();
                assert messages != null;
                if (messages.isEmpty()) {
                    Toast.makeText(MessageActivity.this, "No messages", Toast.LENGTH_SHORT).show();
                    return;
                }
                logger.info("messages: " + messages);
                messageAdapter.submitList(messages);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MessageActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isNetwork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void setupViews() {
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendMessageButton = findViewById(R.id.send_message_button);
        groupButton = findViewById(R.id.message_more_options_button);
        bottomSheetLayout = findViewById(R.id.message_bottom_layout);
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, messages -> {
            messageAdapter.submitList(messages); // Or however you update your adapter
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
        if (!messageContent.isEmpty()) {

            Message message = createMessages().get(0);
            ApiService apiService = RetrofitClient.getService();
            Call<Void> call = apiService.sendMessage(message);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Handle successful message send
                        messageEditText.setText("");
                    } else {
                        // Handle error in sending message
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Handle failure in API call
                }
            });
        }
    }

    private int operationStatus = 0;

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

    public static List<Message> createMessages() {
        List<Message> messages = new ArrayList<>();

        // Assuming MessageType contains TEXT, IMAGE, VIDEO, etc.
        // Assuming SenderType contains ME, OTHER, BOT, etc.

        for (int i = 0; i < 20; i++) {
            // Create Date object for current time
            Date timestamp = new Date();

            // Constructing dummy content, sender, and avatarUrl
            String content = "Message content " + i;
            String sender = "Sender " + (i % 3 + 1); // Will create Sender 1, Sender 2, and Sender 3 cyclically.
            MessageType type = MessageType.values()[i % MessageType.values().length]; // Will cycle through MessageType enum values.
            SenderType senderType = SenderType.values()[i % SenderType.values().length]; // Will cycle through SenderType enum values.

            // Create a new Message object and add it to the list
            Message message = new Message(content, type, sender, senderType, timestamp);
            messages.add(message);
        }

        return messages;
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


    // Other existing methods remain mostly unchanged
}