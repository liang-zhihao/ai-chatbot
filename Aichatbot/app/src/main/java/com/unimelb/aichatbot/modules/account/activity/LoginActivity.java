package com.unimelb.aichatbot.modules.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.dto.LoginRequest;
import com.unimelb.aichatbot.network.dto.LoginResponse;
import com.unimelb.aichatbot.modules.account.service.AccountService;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.util.GsonHelper;
import com.unimelb.aichatbot.util.LoginManager;

import java.io.IOException;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    TextView signUpTv;
    CircularProgressButton loginBtn;

    EditText emailEt, passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpTv = findViewById(R.id.signupTxt);
        loginBtn = findViewById(R.id.loginBt);

        emailEt = findViewById(R.id.email_txt);
        passwordEt = findViewById(R.id.pswd_txt);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountService accountService = RetrofitFactory.create(AccountService.class);
                String userId = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                //                  check user input
                // if (userId.isEmpty() || password.isEmpty()) {
                //     Toast.makeText(LoginActivity.this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
                //     return;
                // }

                //     Debug mode
                userId = "loading8425@gmail.com";
                password = "123456789";
                loginBtn.startAnimation();
                //     send login request to server

                Call<BaseResponse<LoginResponse>> call = accountService.login(new LoginRequest(userId, password));
                String finalUserId = userId;
                call.enqueue(new MyCallback<LoginResponse>() {

                    @Override
                    public void onSuccess(BaseResponse<LoginResponse> result) {
                        loginBtn.revertAnimation();

                        // Your success logic here
                        LoginResponse loginResponse = result.getData();

                        // Save user info to shared preferences
                        LoginManager loginManager = new LoginManager(LoginActivity.this);
                        loginManager.saveLoginInfo(finalUserId, finalUserId, loginResponse.getAccessToken());

                        // Show success message
                        Toast.makeText(LoginActivity.this, "Welcome " + loginManager.getUserId(), Toast.LENGTH_SHORT).show();
                        // Optionally navigate to another activity
                        Intent intent = new Intent(LoginActivity.this, ChooseBotActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(BaseResponse error, @NonNull Throwable t) {
                        // Your failure logic here
                        loginBtn.revertAnimation();

                        if (error != null) {
                            // Handle server-defined error
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle other types of errors (like network issues)
                            t.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Server is not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
