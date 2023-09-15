package com.unimelb.aichatbot.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    // Function to open the keyboard
    public static void openKeyboard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    // Function to hide the keyboard
    public static void hideKeyboard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    // Function to check if the keyboard is shown
    public static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeightDpThreshold = 100;
        int rootViewHeight = rootView.getHeight();
        int rootViewVisibleHeight = rootView.getRootView().getHeight();
        int heightDiff = rootViewVisibleHeight - rootViewHeight;
        float density = rootView.getResources().getDisplayMetrics().density;

        return heightDiff > softKeyboardHeightDpThreshold * density;
    }
}

