package com.unimelb.aichatbot.ui.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.R;

public class choose_chara extends AppCompatActivity {

    Button choose_Bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_charas);

        choose_Bt = findViewById(R.id.chooseBt);
        choose_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(choose_chara.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
