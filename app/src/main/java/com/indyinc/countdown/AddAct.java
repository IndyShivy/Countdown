package com.indyinc.countdown;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsetsController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;
import java.util.Objects;


public class AddAct extends AppCompatActivity {

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
                getWindow().setStatusBarColor(getColor(R.color.event_background_light));


        }


        // Get the root layout
        ConstraintLayout rootLayout = findViewById(R.id.addLayout);
        EditText eventTitle = findViewById(R.id.eventTitleGet);
        ViewPager viewPager = findViewById(R.id.viewPager);
        Button addButton = findViewById(R.id.addButton);
        AddAdapt adapter = new AddAdapt();
        viewPager.setAdapter(adapter);




        Button datePickerButton = findViewById(R.id.showDatePickerButton);
        datePickerButton.setText(R.string.Select);
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
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAct.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // Format month and day to ensure two digits
                        @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", monthOfYear + 1);
                        @SuppressLint("DefaultLocale") String formattedDay = String.format("%02d", dayOfMonth);
                        date = formattedDay + "/" + formattedMonth + "/" + year;

                        // Update button text with selected date
                        datePickerButton.setText(date);
                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });


        //add button event that will check if the event name and date has been selected
        addButton.setOnClickListener(v -> {
            String select = "Select";
            //check if the event name is empty
            if (eventTitle.getText().toString().isEmpty()) {
                Toast.makeText(AddAct.this, "Please enter an event name", Toast.LENGTH_SHORT).show();
                eventTitle.requestFocus();
            } else if (date.isEmpty()) {
                Toast.makeText(AddAct.this, "Please select a date", Toast.LENGTH_SHORT).show();
            } else {

                //have this only contain words and spaces
                eventTitle.setText(eventTitle.getText().toString().replaceAll("[^a-zA-Z ]", ""));
                DateItem dateItem = new DateItem(eventTitle.getText().toString().trim(), date, adapter.getFormat());
                db = new DateDatabase(AddAct.this);
                db.insertDate(dateItem);
                date = " ";
                Toast.makeText(AddAct.this, "Event added!", Toast.LENGTH_SHORT).show();
                datePickerButton.setText(select);
                eventTitle.setText("");
            }
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


        // Hide the keyboard when the user presses enter
        eventTitle.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });


        // On touch for the root to close the keyboard
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(AddAct.this, HomeAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }






    private final NavigationBarView.OnItemSelectedListener navItemSelectedListener = item -> {
        Intent intent = null;

        int id = item.getItemId();
        if (id == R.id.menu_add) {
            return true;
        } else if (id == R.id.menu_home) {
            intent = new Intent(AddAct.this, HomeAct.class);
        } else if (id == R.id.menu_events) {
            intent = new Intent(AddAct.this, EventsAct.class);
        }

        if (intent != null) {
            intent.putExtra(IS_DARK_THEME, isDarkTheme);
            startActivity(intent);
            overridePendingTransition(R.anim.hold, R.anim.fade_in);
            return true;
        }

        return false;
    };


    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_add);
    }
}
