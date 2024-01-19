package com.indyinc.countdown;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    private boolean isDarkTheme;
    public DateDatabase db;
    public String date;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add);
        date = "";

        //set the selected menu options as add and setup listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_add);
        bottomNavigationView.setOnItemSelectedListener(navItemSelectedListener);


        //Get the app's theme from shared preferences then set it as the app's theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);

        if (isDarkTheme) {
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_dark));
        } else {
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_light));
        }


        // Get the root layout
        ConstraintLayout rootLayout = findViewById(R.id.addLayout);
        EditText eventTitle = findViewById(R.id.eventTitle);
        ViewPager viewPager = findViewById(R.id.viewPager);
        Button addButton = findViewById(R.id.addButton);
        TextView datePicker = findViewById(R.id.dateView);
        CustomPagerAdapter adapter = new CustomPagerAdapter();
        viewPager.setAdapter(adapter);



        // Set the touch listener
        rootLayout.setOnTouchListener((v, event) -> {
            if (!(v instanceof EditText)) {
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    focusedView.clearFocus();
                }
            }
            return false;
        });

        //add button event that will check if the event name and date has been selected
        addButton.setOnClickListener(v -> {
            //check if the event name is empty
            if (eventTitle.getText().toString().isEmpty()) {
                Toast.makeText(AddActivity.this, "Please enter an event name", Toast.LENGTH_SHORT).show();
                eventTitle.requestFocus();
            } else if (date.isEmpty()) {
                Toast.makeText(AddActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
            } else {

                System.out.println("Title" + eventTitle.getText().toString() + "Date" + date + "Format" + adapter.getFormat());
                date = "";


//                //add the event to the database
//                db = new DateDatabase(AddActivity.this);
//                db.addDate(new DateItem(eventTitle.getText().toString(), date, adapter.getFormat()));

            }
        });


        // Set the OnEditorActionListener
        eventTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        Button datePickerButton = findViewById(R.id.showDatePickerButton);
        datePickerButton.setOnClickListener(v -> {
            //close the keyboard
            View focusedView = getCurrentFocus();
            if (focusedView != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                focusedView.clearFocus();
            }
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            date = dayOfMonth+ "/" + monthOfYear+1 + "/" + year;
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Handle page scroll
            }

            @Override
            public void onPageSelected(int position) {
                // Update the current position in the adapter
                adapter.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Handle scroll state changes
            }
        });
    }


    private final NavigationBarView.OnItemSelectedListener navItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = null;

            int id = item.getItemId();
            if (id == R.id.menu_add) {
                return true;
            } else if (id == R.id.menu_home) {
                intent = new Intent(AddActivity.this, HomeActivity.class);
            } else if (id == R.id.menu_myevents) {
                intent = new Intent(AddActivity.this, MyEventsActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(R.anim.hold, R.anim.fade_in);
                finish();
                return true;
            }

            return false;
        }
    };




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //start the home intent
        startActivity(new Intent(AddActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
