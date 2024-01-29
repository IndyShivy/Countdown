package com.indyinc.countdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;


public class HomeAct extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    private boolean isDarkTheme;
    BottomNavigationView bottomNavigationView;
    SwitchMaterial darkModeSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the app's theme from shared preferences then set it as the app's theme
        SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);
        System.out.println("isDarkTheme: " + isDarkTheme);
        AppCompatDelegate.setDefaultNightMode(isDarkTheme ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.act_home);
        setStatusBarColor();


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
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                HomeAct.this.finishAffinity();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
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
    private void setStatusBarColor() {
        if (isDarkTheme) {
            getWindow().setNavigationBarColor(getColor(R.color.black));
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.event_background_dark));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Objects.requireNonNull(getWindow().getInsetsController()).setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_light));
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.event_background_light));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.event_background_light));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusBarColor();
    }
}
