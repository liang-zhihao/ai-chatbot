package com.unimelb.aichatbot.modules.landing;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.account.activity.LoginActivity;
import com.unimelb.aichatbot.util.UIHelper;

public class LandingActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    CircularProgressButton getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing); // Assuming the XML file is named "activity_landing.xml"
        setupWindowAnimations();
        // Initialize views
        initializeViews();

        // Set onClick listeners
        setOnClickListeners();

        initSakeDetector();
    }

    private void initSakeDetector() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(), "Shake detected!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initializeViews() {
        getStartedButton = findViewById(R.id.getStartedButton);
        UIHelper.hideActionBar(this);

    }

    private void setOnClickListeners() {


        getStartedButton.setOnClickListener(v -> {
            // Handle get started button click
            handleGetStartedClick();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    private void handleGetStartedClick() {
        // For simplicity, showing a toast. You can navigate to a login activity or perform other actions.
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }
}
