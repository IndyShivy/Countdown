package com.indyinc.countdown;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.widget.SwitchCompat;


public class MainActivity extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the theme from SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);
        setTheme(isDarkTheme ? R.style.Theme_Countdown_Dark : R.style.Theme_Countdown_Light);

        setContentView(R.layout.activity_main);

        SwitchCompat darkModeSwitch = findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(isDarkTheme);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the theme in SharedPreferences
            sharedPreferences.edit().putBoolean(IS_DARK_THEME, isChecked).apply();

            // Recreate the activity for the theme change to take effect
            recreate();
        });
    }
}
