package com.indyinc.countdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/** @noinspection ALL*/
public class HomeAct extends AppCompatActivity {

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
        setContentView(R.layout.act_home);

        // Set the navigation bar color
        if (isDarkTheme) {
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_dark));
        } else {
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_light));
        }



        bottomNavigationView = findViewById(R.id.navigationView);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnItemSelectedListener(navItemSelectedListener);


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

            //restart the activity with a fade-in and fade-out animation
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
                intent = new Intent(HomeAct.this, AddAct.class);
            } else if (id == R.id.menu_events) {
                intent = new Intent(HomeAct.this, EventsAct.class);
            }

            if (intent != null) {
                //pass in if the theme is dark or not
                intent.putExtra(IS_DARK_THEME, isDarkTheme);
                startActivity(intent);
                overridePendingTransition(R.anim.hold, R.anim.fade_in);
                //close the current activity
                finish();
                return true;
            }

            return false;
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close all activities and close the app
        this.finishAffinity();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
