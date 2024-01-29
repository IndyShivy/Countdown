package com.indyinc.countdown;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

public class CountdownAct extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    TextView eventTitle;
    TextView dfDay, dfHours, dfMinutes, dfSeconds;
    TextView dfWeek, dfWeekDays;
    TextView dfFortnightWeek;
    TextView dfMonth;

    TextView dfDayLabel, dfHoursLabel, dfMinutesLabel, dfSecondsLabel;
    TextView dfWeekWeekLabel, dfWeekDayLabel, dfFortnightWeekLabel, dfMonthLabel;
    ImageView backButton;
    TextView backButtonLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_countdown);
        //set the selected menu options as add and setup listener
//        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
//        bottomNavigationView.setSelectedItemId(R.id.menu_events);
//        bottomNavigationView.setOnItemSelectedListener(navItemSelectedListener);


        //Get the app's theme from shared preferences then set it as the app's theme
        SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);
        boolean isDarkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false);

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

        eventTitle = findViewById(R.id.eventTitle);
        backButton = findViewById(R.id.backButton);
        backButtonLabel = findViewById(R.id.backButtonLabel);

        //labels
        dfDayLabel = findViewById(R.id.dfDayLabel);
        dfHoursLabel = findViewById(R.id.dfHoursLabel);
        dfMinutesLabel = findViewById(R.id.dfMinutesLabel);
        dfSecondsLabel = findViewById(R.id.dfSecondsLabel);
        dfWeekWeekLabel = findViewById(R.id.dfWeekWeekLabel);
        dfWeekDayLabel = findViewById(R.id.dfWeekDayLabel);
        dfFortnightWeekLabel = findViewById(R.id.dfFortnightWeekLabel);
        dfMonthLabel = findViewById(R.id.dfMonthLabel);

        //if the backButton or backButtonLabel is clicked, go back to the previous activity
        backButton.setOnClickListener(v -> finish());
        backButtonLabel.setOnClickListener(v -> finish());



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

        Intent intent = getIntent();
        String title = intent.getStringExtra("eventTitle");
        eventTitle.setText(title);
        String date = intent.getStringExtra("eventDate");
        String format = intent.getStringExtra("eventFormat");
        long countdownMillis = getMillisUntilEvent(date);





        CountDownTimer countDownTimer = new CountDownTimer(countdownMillis, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                Duration duration = Duration.ofMillis(millisUntilFinished);
                long days = duration.toDays();
                duration = duration.minusDays(days);
                long hours = duration.toHours();
                duration = duration.minusHours(hours);
                long minutes = duration.toMinutes();
                duration = duration.minusMinutes(minutes);
                long seconds = duration.getSeconds();

                switch (format) {
                    case "Day":
                        //set the labels for the day format
                        dfDay.setText(String.valueOf(days));
                        dfHours.setText(String.valueOf(hours));
                        dfMinutes.setText(String.valueOf(minutes));
                        dfSeconds.setText(String.valueOf(seconds));
                        System.out.print("Day format"+days);

                        //remove the labels for the other formats
                        dfWeekWeekLabel.setText("");
                        dfWeekDayLabel.setText("");
                        dfFortnightWeekLabel.setText("");
                        dfMonthLabel.setText("");
                        dfMonth.setText("");
                        dfWeekDays.setText("");
                        dfFortnightWeek.setText("");
                        dfWeek.setText("");
                        break;
                    case "Week":
                        //set the labels for the week format
                        long weeks = days / 7;
                        days %= 7;
                        dfWeek.setText(String.valueOf(weeks));
                        dfWeekDays.setText(String.valueOf(days));
                        dfHours.setText(String.valueOf(hours));
                        dfMinutes.setText(String.valueOf(minutes));
                        dfSeconds.setText(String.valueOf(seconds));

                        //remove the labels for the other formats
                        dfDayLabel.setText("");
                        dfFortnightWeekLabel.setText("");
                        dfMonthLabel.setText("");
                        dfMonth.setText("");
                        dfFortnightWeek.setText("");
                        break;
                    case "Fortnight":
                        long fortnights = days / 14;
                        days %= 14;
                        long weeksFormat = days / 7;
                        days %= 7;

                        dfWeek.setText(String.valueOf(fortnights));
                        dfFortnightWeek.setText(String.valueOf(weeksFormat));
                        dfWeekDays.setText(String.valueOf(days));
                        dfHours.setText(String.valueOf(hours));
                        dfMinutes.setText(String.valueOf(minutes));
                        dfSeconds.setText(String.valueOf(seconds));

                        if (fortnights > 1) {
                            dfWeekDayLabel.setText("Fortnights");
                        } else {
                            dfWeekDayLabel.setText("Fortnight");
                        }

                        if (weeksFormat == 1) {
                            dfFortnightWeekLabel.setText("Week");
                        } else {
                            dfFortnightWeekLabel.setText("Week");
                        }

                        //remove the labels for the other formats and set the labels
                        dfMonth.setText("");
                        dfDayLabel.setText("");
                        dfMonthLabel.setText("");
                        break;
                    case "Month":
                        long months = days / 30; // Assuming every month has 30 days
                        days %= 30;
                        long fortnightsFormat = days / 14;
                        days %= 14;
                        long weeksFormat2 = days / 7;
                        days %= 7;

                        // Setting date components
                        dfMonth.setText(String.valueOf(months));
                        dfWeek.setText(String.valueOf(fortnightsFormat));
                        dfFortnightWeek.setText(String.valueOf(weeksFormat2));
                        dfWeekDays.setText(String.valueOf(days));
                        dfWeekDays.setText(String.valueOf(days));

                        // Setting time components
                        dfHours.setText(String.valueOf(hours));
                        dfMinutes.setText(String.valueOf(minutes));
                        dfSeconds.setText(String.valueOf(seconds));

                        // Setting appropriate labels
                        dfDayLabel.setText("");
                        if (fortnightsFormat != 1) {
                            dfWeekDayLabel.setText("Fortnights");
                        } else {
                            dfWeekDayLabel.setText("Fortnight");
                        }
                        if (weeksFormat2 == 1) {
                            dfFortnightWeekLabel.setText("Week");
                        } else {
                            dfFortnightWeekLabel.setText("Weeks");
                        }
                        if (months != 1) {
                            dfMonthLabel.setText("Months");
                        } else {
                            dfMonthLabel.setText("Month");
                        }

                        break;
                    default:
                        break;
                }
            }

            public void onFinish() {
                //eventCountdown.setText("Event has started!");
            }
        }.start();
    }

    public long getMillisUntilEvent(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        try {
            Date eventDate = sdf.parse(dateString);
            assert eventDate != null;
            Instant eventInstant = eventDate.toInstant();
            Instant currentInstant = Instant.now();
            Duration duration = Duration.between(currentInstant, eventInstant);
            return duration.toMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public String getFormattedCountdownString(long durationMillis, String format) {
        Duration duration = Duration.ofMillis(durationMillis);
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();

        StringBuilder sb = new StringBuilder();

        switch (format) {
            case "Week":
                long weeks = days / 7;
                days %= 7;
                sb.append(weeks).append(" week").append(weeks != 1 ? "s" : "").append(", ");
                sb.append(days).append(" day").append(days != 1 ? "s" : "").append(", ");
                break;
            case "Fortnight":
                long fortnights = days / 14;
                days %= 14;
                sb.append(fortnights).append(" fortnight").append(fortnights != 1 ? "s" : "").append(", ");
                long weeksInFortnight = days / 7;
                days %= 7;
                sb.append(weeksInFortnight).append(" week").append(weeksInFortnight != 1 ? "s" : "").append(", ");
                sb.append(days).append(" day").append(days != 1 ? "s" : "").append(", ");
                break;
            case "Month":
                long months = days / 30;
                days %= 30;
                sb.append(months).append(" month").append(months != 1 ? "s" : "").append(", ");
                long weeksInMonth = days / 7;
                days %= 7;
                sb.append(weeksInMonth).append(" week").append(weeksInMonth != 1 ? "s" : "").append(", ");
                sb.append(days).append(" day").append(days != 1 ? "s" : "").append(", ");
                break;
            case "Day":
                sb.append(days).append(" day").append(days != 1 ? "s" : "").append(", ");
                break;
        }

        // For hours, minutes, and seconds, we always show them regardless of the format
        sb.append(hours).append(" hour").append(hours != 1 ? "s" : "").append(", ");
        sb.append(minutes).append(" minute").append(minutes != 1 ? "s" : "").append(", ");
        sb.append(seconds).append(" second").append(seconds != 1 ? "s" : "");

        return sb.toString();
    }


//    private final NavigationBarView.OnItemSelectedListener  navItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Intent intent = null;
//
//            int id = item.getItemId();
//            if (id == R.id.menu_events) {
//                return true;
//            } else if (id == R.id.menu_home) {
//                intent = new Intent(CountdownAct.this, HomeAct.class);
//            } else if (id == R.id.menu_add) {
//                intent = new Intent(CountdownAct.this, AddAct.class);
//            }
//
//            if (intent != null) {
//                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                intent.putExtra(IS_DARK_THEME, isDarkTheme);
//                startActivity(intent);
//                overridePendingTransition(R.anim.hold, R.anim.fade_in);
//
//                return true;
//            }
//
//            return false;
//        }
//    };
}
