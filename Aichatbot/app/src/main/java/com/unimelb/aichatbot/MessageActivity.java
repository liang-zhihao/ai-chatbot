package com.unimelb.aichatbot;

import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_box);  // Change with your actual layout file name

        // Initialize views using findViewById
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendMessageButton = findViewById(R.id.send_message_button);
        groupButton = findViewById(R.id.message_more_options_button);


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
        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle group button click event here
                // Perhaps you want to open a group chat or show group options
            }
        });
    }
}
