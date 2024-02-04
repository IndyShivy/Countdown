package com.indyinc.countdown;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;
import android.view.ViewGroup;

public class AddAct extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    private boolean isDarkTheme;
    public DateDatabase db;
    public String date;
    public String format;

    @SuppressLint({"ClickableViewAccessibility", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_add);
        date = "";
        format = "";
        ImageView gradientBackground = findViewById(R.id.gradientBackground);
        ImageView cloudsOne = findViewById(R.id.cloudsOne);
        ImageView cloudsTwo = findViewById(R.id.cloudsTwo);
        ImageView cloudsThree = findViewById(R.id.cloudsThree);

        TextInputLayout formatInputLayout = findViewById(R.id.formatInputLayout);
        //set the selected menu options as add and setup listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_add);
        bottomNavigationView.setOnItemSelectedListener(navItemSelectedListener);



        //Get the app's theme from shared preferences then set it as the app's theme
        SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);

        // Set the theme
        if (isDarkTheme) {
            // Set the background gradient
            gradientBackground.setImageResource(R.drawable.act_add_background_grad_dark);

            //set the clouds and move them to the right position
            cloudsOne.setImageResource(R.drawable.act_add_clouds_one_cat_dark);
            ViewGroup.LayoutParams layoutParams = cloudsOne.getLayoutParams();
            float density = getResources().getDisplayMetrics().density;
            int extraWidth = (int) (20 * density);
            layoutParams.width = layoutParams.width + extraWidth;

            cloudsOne.setLayoutParams(layoutParams);
            cloudsOne.setTranslationX(-80);
            cloudsOne.setTranslationY(30);

            cloudsTwo.setImageResource(R.drawable.act_add_clouds_two_cat_dark);
            cloudsTwo.setTranslationX(170);
            cloudsTwo.setTranslationY(60);

            cloudsThree.setImageResource(R.drawable.act_add_clouds_three_cat_dark);
            cloudsThree.setTranslationY(40);
            formatInputLayout.setTranslationY(130);


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Objects.requireNonNull(getWindow().getInsetsController()).setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
                getWindow().setNavigationBarColor(getColor(R.color.act_all_navbar_background_light));
                getWindow().getDecorView().setBackgroundColor(getColor(R.color.act_all_light_background));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(getColor(R.color.act_all_light_background));
                gradientBackground.setImageResource(R.drawable.act_all_background_grad_light);

        }

        // Get the root layout
        ConstraintLayout rootLayout = findViewById(R.id.addLayout);
        EditText eventTitle = findViewById(R.id.eventTitleGet);
        Button addButton = findViewById(R.id.addButton);

        TextInputEditText eventFormatGet = findViewById(R.id.eventFormatGet);
        eventFormatGet.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Close the keyboard
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    focusedView.clearFocus();
                }

                showFormatPickerDialog(eventFormatGet, isDarkTheme);
                return true;
            }
            return false;
        });

        // Get the date picker
        TextInputEditText eventDateGet = findViewById(R.id.eventDateGet);
        eventDateGet.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Close the keyboard
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
                int mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAct.this,
                        (view, year, monthOfYear, dayOfMonth) -> {
                            // Format month and day to ensure two digits
                            @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", monthOfYear + 1);
                            @SuppressLint("DefaultLocale") String formattedDay = String.format("%02d", dayOfMonth);
                            date = formattedDay + "/" + formattedMonth + "/" + year;

                            // Update button text with selected date
                            eventDateGet.setText(date);
                        }, mYear, mMonth, mDayOfMonth);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

                return true;
            }
            return false;
        });

        //add button event that will check if the event name and date has been selected
        addButton.setOnClickListener(v -> {
            //check if the event name is empty
            if (eventTitle.getText().toString().isEmpty()) {
                Toast.makeText(AddAct.this, "Please enter an event name", Toast.LENGTH_SHORT).show();
                eventTitle.requestFocus();
            } else if (date.isEmpty()) {
                Toast.makeText(AddAct.this, "Please select a date", Toast.LENGTH_SHORT).show();

            } else if (Objects.requireNonNull(eventFormatGet.getText()).toString().isEmpty()) {
                Toast.makeText(AddAct.this, "Please select a format", Toast.LENGTH_SHORT).show();
            } else {
                //have this only contain words and spaces
                eventTitle.setText(eventTitle.getText().toString());
                //.replaceAll("[^a-zA-Z ]", "")
                DateItem dateItem = new DateItem(eventTitle.getText().toString().trim(), date, format);
                db = new DateDatabase(AddAct.this);
                db.insertDate(dateItem);
                date = " ";
                format = " ";
                Toast.makeText(AddAct.this, "Event added!", Toast.LENGTH_SHORT).show();
                eventDateGet.setText("");
                eventTitle.setText("");
                eventFormatGet.setText("");
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return true;
        } else if (id == R.id.menu_events) {
            intent = new Intent(AddAct.this, EventsAct.class);
        }

        if (intent != null) {
            intent.putExtra(IS_DARK_THEME, isDarkTheme);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

    // In your activity or fragment
    public void showFormatPickerDialog(TextInputEditText eventFormatGet, boolean isDarkTheme) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isDarkTheme) {
            dialog.setContentView(R.layout.act_add_format_picker_dark);
        } else {
            dialog.setContentView(R.layout.act_add_format_picker_light);
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button dayButton = dialog.findViewById(R.id.dayButton);
        Button weekButton = dialog.findViewById(R.id.weekButton);
        Button fortnightButton = dialog.findViewById(R.id.fortnightButton);
        Button monthButton = dialog.findViewById(R.id.monthButton);

        //if outside of the dialog is clicked then close the dialog
        dialog.setCanceledOnTouchOutside(true);
        dayButton.setOnClickListener(v -> {
            format = "Day";
            eventFormatGet.setText(format);
            dialog.dismiss();
        });

        weekButton.setOnClickListener(v -> {
            format = "Week";
            eventFormatGet.setText(format);
            dialog.dismiss();
        });

        fortnightButton.setOnClickListener(v -> {
           format = "Fortnight";
            eventFormatGet.setText(format);
            dialog.dismiss();
        });

        monthButton.setOnClickListener(v -> {
            format = "Month";
            eventFormatGet.setText(format);
            dialog.dismiss();
        });

        dialog.show();
    }
}
