package com.unimelb.aichatbot;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.unimelb.aichatbot.util.KeyboardUtils;

import org.commonmark.node.Node;

import java.util.ArrayList;
import java.util.List;

import io.noties.markwon.Markwon;

public class MessageActivity extends AppCompatActivity {

    // Define views as member variables
    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private MaterialButton sendMessageButton;
    private MaterialButton groupButton;

    private LinearLayout bottomSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_box);  // Change with your actual layout file name

        // Initialize views using findViewById
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendMessageButton = findViewById(R.id.send_message_button);
        groupButton = findViewById(R.id.message_more_options_button);

        bottomSheetLayout = findViewById(R.id.message_bottom_layout);

        List<MessageAdapter.Message> messages = new ArrayList<>();


        messages.add(new MessageAdapter.Message("June 11", "John Doe", "**Hello there!**", "9:00"));
        messages.add(new MessageAdapter.Message("June 10", "John Grady Cole", "Message Text 1", "8:00"));
        messages.add(new MessageAdapter.Message("June 11", "John Doe", "Message Text 2", "9:00"));

// Add more messages as needed
        MessageAdapter messageAdapter = new MessageAdapter(this, messages);
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Setting up RecyclerView (You will need to set up an adapter for it)
        // messageRecyclerView.setAdapter(yourAdapter);
        // messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting click listener for the send message button
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle send button click event here
                // For instance, you might want to send the message from the EditText to your backend or database
                String message = messageEditText.getText().toString();
                if (!message.isEmpty()) {
                    // Your message sending logic here
                    messageEditText.setText("");  // Clear the EditText after sending
                }
            }
        });

        // Setting click listener for the group button
        groupButton.setOnClickListener(v -> {
            View currentFocus = this.getCurrentFocus();

//
            if (!hasPopupBottomSheet() && currentFocus == null) {
                showBottomSheetDialog();
                return;
            }

            if (hasPopupBottomSheet()) {
                messageEditText.requestFocus();
                currentFocus = this.getCurrentFocus();
                KeyboardUtils.openKeyboard(this, currentFocus);
                showBottomSheetDialog();
            } else {
                showBottomSheetDialog();
                KeyboardUtils.hideKeyboard(this, currentFocus);
                messageEditText.clearFocus();


            }
        });

//
        messageEditText.setOnClickListener(v -> {
            closeBottomSheet();
        });
    }

    private boolean hasPopupBottomSheet() {
        return bottomSheetLayout.getChildCount() > 1;
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void closeBottomSheet() {
        if (hasPopupBottomSheet()) {
            bottomSheetLayout.removeViewAt(1);
            return;
        }

    }

    private void showBottomSheetDialog() {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        if (bottomSheetLayout.getChildCount() > 1) {
            bottomSheetLayout.removeViewAt(1);
            return;
        }
        bottomSheetLayout.addView(view);

        ImageButton sendPictureButton = view.findViewById(R.id.message_add_photo_button);
        sendPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Handle send picture action

            }
        });

        ImageButton handleMapBtn = view.findViewById(R.id.message_map_button);
        handleMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Handle record voice action

            }
        });

        // ... set click listeners for other options

    }
}

