package com.unimelb.aichatbot.modules.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.R;

public class SignUpActivity extends AppCompatActivity {

    TextView login_txt;
    Button sign_UpBt;

    EditText nameEt, emailEt, passwordEt, confirmPswdEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login_txt = findViewById(R.id.login_tv);
        sign_UpBt = findViewById(R.id.signup_btn);

        nameEt = findViewById(R.id.name_et);
        emailEt= findViewById(R.id.email_txt);
        passwordEt = findViewById(R.id.pswd_txt);
//        TODO need a confirm password
//        confirmPswdEt = findViewById(R.id.confirm_pswd_txt);

        sign_UpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        TODO check user input
//TODO send sign up request
                Intent intent = new Intent(SignUpActivity.this, ChooseBotActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
