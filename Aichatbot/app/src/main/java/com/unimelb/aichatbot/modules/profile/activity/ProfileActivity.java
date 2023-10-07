package com.unimelb.aichatbot.modules.profile.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);


        setContentView(R.layout.profile_layout);
    }
}