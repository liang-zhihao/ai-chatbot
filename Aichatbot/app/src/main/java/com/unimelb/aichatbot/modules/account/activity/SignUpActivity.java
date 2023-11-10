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

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.account.service.RegisterResponse;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.network.dto.SignUpRequest;
import com.unimelb.aichatbot.modules.account.service.AccountService;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.util.LoginManager;
import com.unimelb.aichatbot.util.UIHelper;

public class SignUpActivity extends AppCompatActivity {

    TextView loginTv;
    CircularProgressButton signUpBtn;

    EditText nameEt, emailEt, passwordEt, confirmPswdEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        UIHelper.hideActionBar(this);
        loginTv = findViewById(R.id.login_tv);
        signUpBtn = findViewById(R.id.signup_btn);

        nameEt = findViewById(R.id.name_et);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        //        TODO need a confirm password
        //        confirmPswdEt = findViewById(R.id.confirm_pswd_txt);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initialize Retrofit service
                AccountService accountService = RetrofitFactory.create(AccountService.class);
                String username = nameEt.getText().toString();
                String userId = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if (userId.isEmpty() && password.isEmpty() && username.isEmpty()) {
                    String msg = "Please enter your Name, Email and password";
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else if (username.isEmpty()) {
                    String msg = "Name cannot be empty.";
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else if (userId.isEmpty()) {
                    String msg = "Email cannot be empty.";
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    String msg = "Password cannot be empty.";
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    // username = "loading842522";
                    // userId = "1@22.comm";
                    // password = "123456789";
                    // Create a sign-up request
                    SignUpRequest request = new SignUpRequest();
                    request.setUsername(username);
                    request.setUserId(userId);
                    request.setPassword(password);

                    // Asynchronously send the sign-up request

                    accountService.register(request).enqueue(new MyCallback<RegisterResponse>() {
                        @Override
                        public void onSuccess(BaseResponse<RegisterResponse> result) {
                            // Successfully signed up
                            signUpBtn.revertAnimation();
                            Toast.makeText(SignUpActivity.this, "You're all set! Sign-in to get started.", Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance(getApplicationContext()).saveLoginInfo(userId, username, result.getData().getAccessToken());
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(ErrorResponse error, @NonNull Throwable t) {
                            // Handle errors
                            signUpBtn.revertAnimation();
                            if (error != null) {
                                // Handle error defined by the server
                                Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            } else if (t != null) {
                                // Handle other types of errors (e.g., network errors)
                                t.printStackTrace();
                                // Toast.makeText(SignUpActivity.this, "Server is not available", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
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

