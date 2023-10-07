package com.unimelb.aichatbot.modules.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.R;

public class LoginActivity extends AppCompatActivity {
    TextView signUpTv;
    Button loginBtn;

    EditText emailEt, passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpTv = findViewById(R.id.signupTxt);
        loginBtn = findViewById(R.id.loginBt);

        emailEt= findViewById(R.id.email_txt);
        passwordEt = findViewById(R.id.pswd_txt);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO check user input
//                TODO send login request to server

               Intent intent = new Intent(LoginActivity.this, ChooseBotActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
