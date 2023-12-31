package com.unimelb.aichatbot.modules.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.util.UIHelper;

public class ChooseBotActivity extends AppCompatActivity {

    Button chooseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_charas);
        UIHelper.hideActionBar(this);
        chooseBtn = findViewById(R.id.chooseBt);
        chooseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseBotActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
