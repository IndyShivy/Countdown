package com.indyinc.countdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class HomeAct extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    private boolean isDarkTheme;
    BottomNavigationView bottomNavigationView;
    SwitchMaterial darkModeSwitch;

    TextView eventTitle;
    TextView dfDay, dfHours, dfMinutes, dfSeconds;
    TextView dfWeek, dfWeekDays;
    TextView dfFortnightWeek;
    TextView dfMonth;

    TextView dfDayLabel, dfHoursLabel, dfMinutesLabel, dfSecondsLabel;
    TextView dfWeekWeekLabel, dfWeekDayLabel, dfFortnightWeekLabel, dfMonthLabel;


    ChipGroup chipGroup;
    Chip chipDay, chipWeek, chipFortnight, chipMonth;
    DateDatabase db = new DateDatabase(this);

    private CountDownTimer countDownTimer;


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
        chipGroup = findViewById(R.id.chipGroup);
        chipDay = findViewById(R.id.chipDay);
        chipWeek = findViewById(R.id.chipWeek);
        chipFortnight = findViewById(R.id.chipFortnight);
        chipMonth = findViewById(R.id.chipMonth);

        chipDay.setChecked(true);

        eventTitle = findViewById(R.id.eventTitle);

        //labels
        dfDayLabel = findViewById(R.id.dfDayLabel);
        dfHoursLabel = findViewById(R.id.dfHoursLabel);
        dfMinutesLabel = findViewById(R.id.dfMinutesLabel);
        dfSecondsLabel = findViewById(R.id.dfSecondsLabel);
        dfWeekWeekLabel = findViewById(R.id.dfWeekWeekLabel);
        dfWeekDayLabel = findViewById(R.id.dfWeekDayLabel);
        dfFortnightWeekLabel = findViewById(R.id.dfFortnightWeekLabel);
        dfMonthLabel = findViewById(R.id.dfMonthLabel);


        //day format
        dfDay = findViewById(R.id.dfDay);
        dfHours = findViewById(R.id.dfHours);
        dfMinutes = findViewById(R.id.dfMinutes);
        dfSeconds = findViewById(R.id.dfSeconds);


        //Week format
        dfWeek = findViewById(R.id.dfWeek);
        dfWeekDays = findViewById(R.id.dfWeekDay);


        //Fortnight format
        //dfWeekDays = findViewById(R.id.dfWeekDayF); (Fortnight)
        dfFortnightWeek = findViewById(R.id.dfFortnightWeek);
        // dfWeek = findViewById(R.id.dfWeek);

        //Month format
        dfMonth = findViewById(R.id.dfMonth);
        //dfWeekDays = findViewById(R.id.dfWeekDayF); (Fortnight)
        //dfFortnightWeeks = findViewById(R.id.dfFortnightWeek);
        // dfWeek = findViewById(R.id.dfWeek);

        // Update the views here


        //view visibility
        dfDay.setVisibility(View.GONE);
        dfDayLabel.setVisibility(View.GONE);
        dfMonth.setVisibility(View.GONE);
        dfMonthLabel.setVisibility(View.GONE);
        dfWeek.setVisibility(View.GONE);
        dfWeekDayLabel.setVisibility(View.GONE);
        dfFortnightWeek.setVisibility(View.GONE);
        dfFortnightWeekLabel.setVisibility(View.GONE);
        dfWeekDays.setVisibility(View.GONE);
        dfWeekWeekLabel.setVisibility(View.GONE);

        String title;
        String date;
        String format;

        //get the first date and format from the database
        ArrayList<DateItem> dateItems = db.getAllDates();
        //delete all dates from the db if the date is in the past
        for (DateItem dateItem : dateItems) {
            if (getMillisUntilEvent(dateItem.getDate()) < 0) {
                db.deleteDate(dateItem);
            }
        }
        dateItems = db.getAllDates();
        if (dateItems.size() > 0) {
            title = dateItems.get(0).getTitle();
            String finalTitle = '\"' + title + '\"';
            eventTitle.setText(finalTitle);
            date = dateItems.get(0).getDate();
            format = dateItems.get(0).getFormat();
            startCountdown(date, format);
            highlightSelectedFormat(format);
            String finalDate = date;
            chipDay.setOnClickListener(v -> startCountdown(finalDate, "Day"));
            chipWeek.setOnClickListener(v -> startCountdown(finalDate, "Week"));
            chipFortnight.setOnClickListener(v -> startCountdown(finalDate, "Fortnight"));
            chipMonth.setOnClickListener(v -> startCountdown(finalDate, "Month"));
        } else {
            eventTitle.setText(R.string.Add_an_Event);
            dfDay.setVisibility(View.GONE);
            dfDayLabel.setVisibility(View.GONE);
            dfMonth.setVisibility(View.VISIBLE);
            dfMonthLabel.setVisibility(View.VISIBLE);
            dfWeek.setVisibility(View.VISIBLE);
            dfWeekDayLabel.setVisibility(View.VISIBLE);
            dfFortnightWeek.setVisibility(View.VISIBLE);
            dfFortnightWeekLabel.setVisibility(View.VISIBLE);
            dfWeekDays.setVisibility(View.VISIBLE);
            dfWeekWeekLabel.setVisibility(View.VISIBLE);
        }
    }


    public long getMillisUntilEvent(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date eventDate = sdf.parse(dateString);
            return eventDate != null ? eventDate.getTime() - System.currentTimeMillis() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public void setTimeLabels(long hours, long minutes, long seconds) {
        dfHoursLabel.setText(hours != 1 ? "HOURS" : "HOUR");
        dfMinutesLabel.setText(minutes != 1 ? "MINUTES" : "MINUTE");
        dfSecondsLabel.setText(seconds != 1 ? "SECONDS" : "SECOND");
    }
    private void updateCountdownDisplay(long millisUntilFinished, String format) {
        Duration duration = Duration.ofMillis(millisUntilFinished);
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();

        String formattedDays = String.format(Locale.getDefault(), "%02d", days);
        String formattedHours = String.format(Locale.getDefault(), "%02d", hours);
        String formattedMinutes = String.format(Locale.getDefault(), "%02d", minutes);
        String formattedSeconds = String.format(Locale.getDefault(), "%02d", seconds);

        //set all textview to be visible
        dfDay.setVisibility(View.VISIBLE);
        dfDayLabel.setVisibility(View.VISIBLE);
        dfHours.setVisibility(View.VISIBLE);
        dfHoursLabel.setVisibility(View.VISIBLE);
        dfMinutes.setVisibility(View.VISIBLE);
        dfMinutesLabel.setVisibility(View.VISIBLE);
        dfSeconds.setVisibility(View.VISIBLE);
        dfSecondsLabel.setVisibility(View.VISIBLE);
        dfWeek.setVisibility(View.VISIBLE);
        dfWeekDayLabel.setVisibility(View.VISIBLE);
        dfWeekDays.setVisibility(View.VISIBLE);
        dfWeekWeekLabel.setVisibility(View.VISIBLE);
        dfFortnightWeek.setVisibility(View.VISIBLE);
        dfFortnightWeekLabel.setVisibility(View.VISIBLE);
        dfMonth.setVisibility(View.VISIBLE);
        dfMonthLabel.setVisibility(View.VISIBLE);


        switch (format) {
            case "Day":
                dfDay.setText(formattedDays);
                dfHours.setText(formattedHours);
                dfMinutes.setText(formattedMinutes);
                dfSeconds.setText(formattedSeconds);
                dfDayLabel.setText(days != 1 ? "DAYS" : "DAY");
                //set the labels for the day format
                setTimeLabels( hours, minutes, seconds);

                //remove the labels for the other formats
                dfWeekWeekLabel.setText("");
                dfWeekDayLabel.setText("");
                dfFortnightWeekLabel.setText("");
                dfMonthLabel.setText("");
                dfMonth.setText("");
                dfWeekDays.setText("");
                dfFortnightWeek.setText("");
                dfWeek.setText("");

                //view visibility
                dfMonth.setVisibility(View.GONE);
                dfMonthLabel.setVisibility(View.GONE);
                dfWeek.setVisibility(View.GONE);
                dfWeekDayLabel.setVisibility(View.GONE);
                dfFortnightWeek.setVisibility(View.GONE);
                dfFortnightWeekLabel.setVisibility(View.GONE);
                dfWeekDays.setVisibility(View.GONE);
                dfWeekWeekLabel.setVisibility(View.GONE);


                break;
            case "Week":
                //set the labels for the week format
                long weeks = days / 7;
                days %= 7;
                dfWeek.setText(String.format(Locale.getDefault(), "%02d", weeks));
                dfWeekDays.setText(String.format(Locale.getDefault(), "%02d", days));
                dfHours.setText(formattedHours);
                dfMinutes.setText(formattedMinutes);
                dfSeconds.setText(formattedSeconds);

                //set the labels for the day format
                setTimeLabels( hours, minutes, seconds);
                dfWeekDayLabel.setText(weeks != 1 ? "WEEKS" : "WEEK");
                dfWeekWeekLabel.setText(weeks != 1 ? "DAYS" : "DAY");

                //remove the labels for the other formats
                dfDayLabel.setText("");
                dfFortnightWeekLabel.setText("");
                dfMonthLabel.setText("");
                dfMonth.setText("");
                dfFortnightWeek.setText("");

                //view visibility
                dfDay.setVisibility(View.GONE);
                dfDayLabel.setVisibility(View.GONE);
                break;
            case "Fortnight":
                long fortnights = days / 14;
                days %= 14;
                long weeksFormat = days / 7;
                days %= 7;

                dfWeek.setText(String.format(Locale.getDefault(), "%02d", fortnights));
                dfFortnightWeek.setText(String.format(Locale.getDefault(), "%02d", weeksFormat));
                dfWeekDays.setText(String.format(Locale.getDefault(), "%02d", days));
                dfHours.setText(formattedHours);
                dfMinutes.setText(formattedMinutes);
                dfSeconds.setText(formattedSeconds);

                //set the labels for the day format
                setTimeLabels( hours, minutes, seconds);
                dfWeekDayLabel.setText(fortnights != 1 ? "FORTNIGHTS" : "FORTNIGHT");
                dfFortnightWeekLabel.setText(weeksFormat != 1 ? "WEEKS" : "WEEK");
                dfWeekWeekLabel.setText(days != 1 ? "DAYS" : "DAY");

                //remove the labels for the other formats and set the labels
                dfMonth.setText("");
                dfDayLabel.setText("");
                dfMonthLabel.setText("");

                //view visibility
                dfDay.setVisibility(View.GONE);
                dfDayLabel.setVisibility(View.GONE);
                break;
            case "Month":
                long months = days / 30; // Assuming every month has 30 days
                days %= 30;
                long fortnightsFormat = days / 14;
                days %= 14;
                long weeksFormat2 = days / 7;
                days %= 7;

                // Setting date components
                dfMonth.setText(String.format(Locale.getDefault(), "%02d", months));
                dfWeek.setText(String.format(Locale.getDefault(),"%02d",fortnightsFormat));
                dfFortnightWeek.setText(String.format(Locale.getDefault(),"%02d",weeksFormat2));
                dfWeekDays.setText(String.format(Locale.getDefault(),"%02d",days));
                dfWeekDays.setText(String.format(Locale.getDefault(),"%02d",days));

                // Setting time components
                dfHours.setText(formattedHours);
                dfMinutes.setText(formattedMinutes);
                dfSeconds.setText(formattedSeconds);

                // Setting appropriate labels
                //set the labels for the day format
                setTimeLabels( hours, minutes, seconds);
                dfMonthLabel.setText(months != 1 ? "MONTHS" : "MONTH");
                dfWeekDayLabel.setText(fortnightsFormat != 1 ? "FORTNIGHTS" : "FORTNIGHT");
                dfFortnightWeekLabel.setText(weeksFormat2 != 1 ? "WEEKS" : "WEEK");

                dfDayLabel.setText("");

                //view visibility
                dfDay.setVisibility(View.GONE);
                dfDayLabel.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void startCountdown(String date, String format) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        long countdownMillis = getMillisUntilEvent(date);
        highlightSelectedFormat(format);
        countDownTimer = new CountDownTimer(countdownMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                updateCountdownDisplay(millisUntilFinished, format);
            }

            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void highlightSelectedFormat(String format) {
        switch (format) {
            case "Day":
                chipDay.setChecked(true);
                break;
            case "Week":
                chipWeek.setChecked(true);
                break;
            case "Fortnight":
                chipFortnight.setChecked(true);
                break;
            case "Month":
                chipMonth.setChecked(true);
                break;
        }
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
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //close the current activity
                finish();
                return true;
            }

            return false;
        }
    };
    private void setStatusBarColor() {
        if (isDarkTheme) {
            //if the theme is dark
            getWindow().setNavigationBarColor(getColor(R.color.testing2));
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.testing4));
        } else {
            //if the theme is light
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Objects.requireNonNull(getWindow().getInsetsController()).setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
            getWindow().setNavigationBarColor(getColor(R.color.navbar_background_light));
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.light_background));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.light_background));
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        setStatusBarColor();
    }

}
