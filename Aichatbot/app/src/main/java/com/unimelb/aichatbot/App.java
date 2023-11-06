package com.unimelb.aichatbot;

import android.app.Application;

import com.unimelb.aichatbot.util.LoginManager;

public class App extends Application {
    private LoginManager loginManager;

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }
}
