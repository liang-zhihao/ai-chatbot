package com.unimelb.aichatbot.modules.newChat;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * TODO change to item layout to contact
 *  start new group chat
 * */
public class NewChatActivity extends AppCompatActivity {

    private static final String TAG = "NewChatActivity";

    private RecyclerView recyclerView;

    private ChooseUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        List<ChatUser> users = Arrays.asList(
                new ChatUser("John Doe", "", ""),
                new ChatUser("Jane Smith", "", "")
                // Add more sample data
        );

        adapter = new ChooseUserAdapter(users);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            handleMultipleSelections();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleMultipleSelections() {
        List<ChatUser> selectedUsers = new ArrayList<>();
        for (ChatUser user : adapter.getChatUsers()) {
            if (user.isSelected()) {
                selectedUsers.add(user);
            }
        }

        Log.d(TAG, "Selected Users: " + selectedUsers);
        // Here, you can do whatever you want with the selected users.
        // For example, starting a group chat, or displaying their names.
        StringBuilder names = new StringBuilder();
        for (ChatUser user : selectedUsers) {
            names.append(user.getName()).append("\n");
        }

        Toast.makeText(this, "Selected Users:\n" + names, Toast.LENGTH_LONG).show();
    }

}
