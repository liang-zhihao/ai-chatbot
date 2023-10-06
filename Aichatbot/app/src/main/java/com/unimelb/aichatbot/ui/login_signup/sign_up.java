package com.unimelb.aichatbot.ui.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.R;

public class sign_up extends AppCompatActivity {

    TextView login_txt;
    Button sign_UpBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        login_txt = findViewById(R.id.login_tv);
        sign_UpBt = findViewById(R.id.signupBt);

        sign_UpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this, choose_chara.class);
                startActivity(intent);
                finish();
            }
        });

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(sign_up.this, log_in.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
