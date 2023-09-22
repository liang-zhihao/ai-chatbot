package com.unimelb.aichatbot.ui.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.R;

public class log_in extends AppCompatActivity {
    TextView sign_up_txt;
    Button login_Bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sign_up_txt = findViewById(R.id.signupTxt);
        login_Bt = findViewById(R.id.loginBt);

        login_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(log_in.this, choose_chara.class);
                startActivity(intent);
                finish();
            }
        });
        sign_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(log_in.this, sign_up.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
