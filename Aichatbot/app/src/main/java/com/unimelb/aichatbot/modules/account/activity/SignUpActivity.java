package com.unimelb.aichatbot.modules.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.BuildConfig;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.dto.SignUpRequest;
import com.unimelb.aichatbot.modules.account.service.AccountService;
import com.unimelb.aichatbot.network.RetrofitFactory;

public class SignUpActivity extends AppCompatActivity {

    TextView loginTv;
    Button signUpBtn;

    EditText nameEt, emailEt, passwordEt, confirmPswdEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loginTv = findViewById(R.id.login_tv);
        signUpBtn = findViewById(R.id.signup_btn);

        nameEt = findViewById(R.id.name_et);
        emailEt = findViewById(R.id.email_txt);
        passwordEt = findViewById(R.id.pswd_txt);
        //        TODO need a confirm password
        //        confirmPswdEt = findViewById(R.id.confirm_pswd_txt);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check user input (TODO)
                // Initialize Retrofit service
                AccountService accountService = RetrofitFactory.create(AccountService.class);
                String username = nameEt.getText().toString();
                String userId = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                username = "loading842522";
                userId = "1@22.comm";
                password = "123456789";
                // Create a sign-up request
                SignUpRequest request = new SignUpRequest();
                request.setUsername(username);
                request.setUserId(userId);
                request.setPassword(password);

                // Asynchronously send the sign-up request

                accountService.register(request).enqueue(new MyCallback<Void>() {
                    @Override
                    public void onSuccess(BaseResponse<Void> result) {
                        // Successfully signed up
                        Toast.makeText(SignUpActivity.this, "Successfully signed up!", Toast.LENGTH_SHORT).show();
                        // Navigate to ChooseBotActivity (or any other activity you want to go to upon successful registration)
                        Intent intent = new Intent(SignUpActivity.this, ChooseBotActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(BaseResponse error, @NonNull Throwable t) {
                        // Handle errors
                        if (error != null) {
                            // Handle error defined by the server
                            Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (t != null) {
                            // Handle other types of errors (e.g., network errors)
                            t.printStackTrace();
                            Toast.makeText(SignUpActivity.this, "Server is not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginTv.setOnClickListener(new View.OnClickListener() {
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