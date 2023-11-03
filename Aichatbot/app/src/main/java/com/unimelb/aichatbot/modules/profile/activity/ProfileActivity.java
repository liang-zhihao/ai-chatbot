package com.unimelb.aichatbot.modules.profile.activity;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.aichatbot.R;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            this.addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                    if (menuItem.getItemId() == android.R.id.home) {
                        finish();
                        return true;
                    } else return false;
                }
            }, this, Lifecycle.State.RESUMED);
        }
        setContentView(R.layout.profile_layout);

        TextView nameButton = findViewById(R.id.buttonName);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputNameBottomSheetDialogFragment bottomSheetDialogFragment = new InputNameBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        TextView buttonPassword = findViewById(R.id.buttonPassword);
        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputPasswordBottomSheetDialogFragment bottomSheetDialogFragment = new InputPasswordBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

    }


}