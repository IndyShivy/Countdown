package com.indyinc.countdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.switchmaterial.SwitchMaterial;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    private boolean isDarkTheme;
    BottomNavigationView bottomNavigationView;
    SwitchMaterial darkModeSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the app's theme from shared preferences then set it as the app's theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);
        System.out.println("isDarkTheme: " + isDarkTheme);
        AppCompatDelegate.setDefaultNightMode(isDarkTheme ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnItemSelectedListener(navItemSelectedListener);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);

        //set the switch to the correct state
        darkModeSwitch.setChecked(isDarkTheme);

        //switch listener
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
           //get the switch state and save it to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_DARK_THEME, isChecked);
            editor.apply();

            //set the app's theme
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

            //recreate the activity to apply the theme
            recreate();
        });
    }

    private final NavigationBarView.OnItemSelectedListener  navItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = null;

            int id = item.getItemId();
            if (id == R.id.menu_home) {
                return true;
            } else if (id == R.id.menu_add) {
                intent = new Intent(HomeActivity.this, AddActivity.class);
            } else if (id == R.id.menu_myevents) {
                intent = new Intent(HomeActivity.this, MyEventsActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            }

            return false;
        }
    };
}
