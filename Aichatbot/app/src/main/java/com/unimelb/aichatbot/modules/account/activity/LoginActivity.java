package com.unimelb.aichatbot.modules.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.network.dto.LoginRequest;
import com.unimelb.aichatbot.network.dto.LoginResponse;
import com.unimelb.aichatbot.modules.account.service.AccountService;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.util.LoginManager;
import com.unimelb.aichatbot.util.UIHelper;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    TextView signUpTv;
    CircularProgressButton loginBtn;

    EditText emailEt, passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UIHelper.hideActionBar(this);
        signUpTv = findViewById(R.id.signupTxt);
        loginBtn = findViewById(R.id.loginBt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
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
                // TODO    Debug mode
                if (userId.equals("a")) {
                    userId = "amy1";

                } else if (userId.equals("b")) {
                    userId = "amy2";
                } else {
                    userId = "amy1";
                }
                password = "1234567890";
                loginBtn.startAnimation();
                //     send login request to server

                Call<BaseResponse<LoginResponse>> call = accountService.login(new LoginRequest(userId, password));

                call.enqueue(new MyCallback<LoginResponse>() {

                    @Override
                    public void onSuccess(BaseResponse<LoginResponse> result) {
                        loginBtn.revertAnimation();

                        // Your success logic here
                        LoginResponse loginResponse = result.getData();

                        // Save user info to shared preferences

                        LoginManager loginManager = new LoginManager(getApplicationContext());
                        loginManager.saveLoginInfo(loginResponse.getUserId(), loginManager.getUsername(), loginResponse.getAccessToken());
                        // Show success message
                        Toast.makeText(LoginActivity.this, "Welcome " + loginManager.getUserId(), Toast.LENGTH_SHORT).show();
                        // Optionally navigate to another activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(ErrorResponse error, Throwable t) {
                        // Your failure logic here
                        loginBtn.revertAnimation();
                        if (error != null) {
                            // Handle server-defined error
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle other types of errors (like network issues)
                            if (t != null) {
                                t.printStackTrace();
                            }
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
