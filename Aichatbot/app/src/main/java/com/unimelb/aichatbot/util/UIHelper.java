package com.unimelb.aichatbot.util;

import android.content.Context;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public class UIHelper {
    public static void hideActionBar(Context context) {
        // Hide the action bar
        assert ((AppCompatActivity) context).getSupportActionBar() != null;
        ((AppCompatActivity) context).getSupportActionBar().hide();
    }

    public static void setupActionBarBackButton(OnBackPressedDispatcher onBackPressedDispatcher, OnBackPressedCallback onBackPressedCallback) {
        
        onBackPressedDispatcher.addCallback(onBackPressedCallback);
    }
}
