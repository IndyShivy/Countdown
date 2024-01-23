package com.indyinc.countdown;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Objects;

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
        SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);

        isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);

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
        }


        //set the context
        context = this;
        db = new DateDatabase(context);
        //add some test data
//        db.insertDate(new DateItem("Day", "11/11/24", "Day"));
//        db.insertDate(new DateItem("Week", "21/12/24", "Week"));
//        db.insertDate(new DateItem("Fortnight", "21/12/24", "Fortnight"));
//        db.insertDate(new DateItem("Month", "21/12/24", "Month"));

        myEventsAdapter = new EventsAdapt(new ArrayList<>(), context);
        eventsRecycler = findViewById(R.id.eventsRecycler);
        eventsRecycler.setAdapter(myEventsAdapter);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(context));
        populateRecyclerView();

        //set the cardView background to the day image
       // eventsRecycler.setBackgroundResource(R.drawable.day);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(EventsAct.this, HomeAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        myEventsAdapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(EventsAct.this, CountdownAct.class);
            intent.putExtra("eventTitle", item.getTitle());
            intent.putExtra("eventDate", item.getDate());
            intent.putExtra("eventFormat", item.getFormat());
            System.out.println("item.getDate(): " + item.getDate());
            startActivity(intent);
            overridePendingTransition(R.anim.hold, R.anim.fade_in);
        });

        //#ecf7fd

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
