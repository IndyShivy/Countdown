package com.indyinc.countdown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

/** @noinspection ALL*/
public class EventsAct extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    private boolean isDarkTheme;
    private EventsAdapt myEventsAdapter;
    private RecyclerView eventsRecycler;
    private DateDatabase db;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_events);

        //set the selected menu options as add and setup listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_events);
        bottomNavigationView.setOnItemSelectedListener(navItemSelectedListener);

        //Get the app's theme from shared preferences then set it as the app's theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);

        if (isDarkTheme) {
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_dark));
        } else {
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_light));
        }

        //set the context
        context = this;
        db = new DateDatabase(context);
        myEventsAdapter = new EventsAdapt(new ArrayList<>(), context);
        eventsRecycler = findViewById(R.id.eventsRecycler);
        eventsRecycler.setAdapter(myEventsAdapter);
        eventsRecycler.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(context));
        populateRecyclerView();



    }
    private final NavigationBarView.OnItemSelectedListener  navItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = null;

            int id = item.getItemId();
            if (id == R.id.menu_events) {
                return true;
            } else if (id == R.id.menu_home) {
                intent = new Intent(EventsAct.this, HomeAct.class);
            } else if (id == R.id.menu_add) {
                intent = new Intent(EventsAct.this, AddAct.class);
            }

            if (intent != null) {
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(IS_DARK_THEME, isDarkTheme);
                startActivity(intent);
                overridePendingTransition(R.anim.hold, R.anim.fade_in);

                return true;
            }

            return false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventsAct.this, HomeAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void populateRecyclerView(){
        ArrayList<DateItem> dateItems = db.getAllDates();
        myEventsAdapter = new EventsAdapt(dateItems, context);
        eventsRecycler.setAdapter(myEventsAdapter);
        eventsRecycler.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(context));
        myEventsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_events);
    }


}
