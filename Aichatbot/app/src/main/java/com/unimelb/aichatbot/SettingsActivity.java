package com.unimelb.aichatbot;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.unimelb.aichatbot.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            EditTextPreference sPEditUsername = findPreference("username");
            EditTextPreference sPEditPassword = findPreference("password");

            if (sPEditUsername != null) {
                sPEditUsername.setOnPreferenceChangeListener(this);
            }

            if (sPEditPassword != null) {
                sPEditPassword.setOnPreferenceChangeListener(this);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            if ("username".equals(key)) {
                // Do something when username changes
                return true; // Save the new value
            } else if ("password".equals(key)) {
                // Do something when password changes
                return true; // Save the new value
            }
            return false; // Don't save the new value
        }
    }
}
