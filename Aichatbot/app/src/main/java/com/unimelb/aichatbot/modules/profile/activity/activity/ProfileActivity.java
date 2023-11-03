package com.unimelb.aichatbot.modules.profile.activity.activity;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.profile.activity.Fragment.InputNameBottomSheetDialogFragment;
import com.unimelb.aichatbot.modules.profile.activity.Fragment.InputPasswordBottomSheetDialogFragment;

public class ProfileActivity extends AppCompatActivity
        implements InputNameBottomSheetDialogFragment.OnNameUpdatedListener , InputPasswordBottomSheetDialogFragment.OnPasswordUpdatedListener { // 实现接口

    private TextView nameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        nameButton = findViewById(R.id.buttonName);

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputNameBottomSheetDialogFragment bottomSheetDialogFragment = new InputNameBottomSheetDialogFragment();
                bottomSheetDialogFragment.setOnNameUpdatedListener(ProfileActivity.this);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "InputNameBottomSheetDialogFragment");
            }
        });
        TextView buttonPassword = findViewById(R.id.buttonPassword);
        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputPasswordBottomSheetDialogFragment bottomSheetDialogFragment = new InputPasswordBottomSheetDialogFragment();
                bottomSheetDialogFragment.setPasswordUpdatedListener(ProfileActivity.this);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onNameUpdated(String newName) {
        if(nameButton != null) {
            nameButton.setText(newName);
        }
    }

    @Override
    public void onPwdUpdated(String pwd) {
        Log.e("XXX", pwd);
    }
}