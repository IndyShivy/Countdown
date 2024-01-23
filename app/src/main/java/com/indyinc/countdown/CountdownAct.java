package com.indyinc.countdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class CountdownAct extends AppCompatActivity {

    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    TextView eventTitle;
    TextView dfDay, dfDayHours, dfDayMinutes, dfDaySeconds;
    TextView dfWeek, dfWeekDays, dfWeekHours, dfWeekMinutes, dfWeekSeconds;
    TextView dfFortnight, dfFortnightWeeks, dfFortnightDays, dfFortnightHours, dfFortnightMinutes, dfFortnightSeconds;
    TextView dfMonth, dfMonthWeeks, dfMonthDays, dfMonthHours, dfMonthMinutes, dfMonthSeconds;


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
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_dark));
        } else {
            getWindow().setNavigationBarColor(getColor(R.color.nav_background_light));
        }

        eventTitle = findViewById(R.id.eventTitle);
        dfDay = findViewById(R.id.dfDay);
        dfDayHours = findViewById(R.id.dfDayHours);
        dfDayMinutes = findViewById(R.id.dfDayMinutes);
        dfDaySeconds = findViewById(R.id.dfDaySeconds);
//        dfWeek = findViewById(R.id.dfWeek);
//        dfWeekDays = findViewById(R.id.dfWeekDays);
//        dfWeekHours = findViewById(R.id.dfWeekHours);
//        dfWeekMinutes = findViewById(R.id.dfWeekMinutes);
//        dfWeekSeconds = findViewById(R.id.dfWeekSeconds);
//        dfFortnight = findViewById(R.id.dfFortnight);
//        dfFortnightWeeks = findViewById(R.id.dfFortnightWeeks);
//        dfFortnightDays = findViewById(R.id.dfFortnightDays);
//        dfFortnightHours = findViewById(R.id.dfFortnightHours);
//        dfFortnightMinutes = findViewById(R.id.dfFortnightMinutes);
//        dfFortnightSeconds = findViewById(R.id.dfFortnightSeconds);
//        dfMonth = findViewById(R.id.dfMonth);
//        dfMonthWeeks = findViewById(R.id.dfMonthWeeks);
//        dfMonthDays = findViewById(R.id.dfMonthDays);
//        dfMonthHours = findViewById(R.id.dfMonthHours);
//        dfMonthMinutes = findViewById(R.id.dfMonthMinutes);
//        dfMonthSeconds = findViewById(R.id.dfMonthSeconds);



        Intent intent = getIntent();
        String title = intent.getStringExtra("eventTitle");
        eventTitle.setText(title);
        String date = intent.getStringExtra("eventDate");
        String format = intent.getStringExtra("eventFormat");
        long countdownMillis = getMillisUntilEvent(date);





        CountDownTimer countDownTimer = new CountDownTimer(countdownMillis, 1000) {

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
                        dfDay.setText(String.valueOf(days));
                        dfDayHours.setText(String.valueOf(hours));
                        dfDayMinutes.setText(String.valueOf(minutes));
                        dfDaySeconds.setText(String.valueOf(seconds));
                        break;
                    case "Week":
                        long weeks = days / 7;
                        days %= 7;
//                        dfWeek.setText(String.valueOf(weeks));
//                        dfWeekDays.setText(String.valueOf(days));
//                        dfWeekHours.setText(String.valueOf(hours));
//                        dfWeekMinutes.setText(String.valueOf(minutes));
//                        dfWeekSeconds.setText(String.valueOf(seconds));
                        break;
                    case "Fortnight":
                        long fortnights = days / 14;
//                        days %= 14;
//                        dfFortnight.setText(String.valueOf(fortnights));
//                        long weeksInFortnight = days / 7;
//                        days %= 7;
//                        dfFortnightWeeks.setText(String.valueOf(weeksInFortnight));
//                        dfFortnightDays.setText(String.valueOf(days));
//                        dfFortnightHours.setText(String.valueOf(hours));
//                        dfFortnightMinutes.setText(String.valueOf(minutes));
//                        dfFortnightSeconds.setText(String.valueOf(seconds));
                        break;
                    case "Month":
                        long months = days / 30;
//                        days %= 30;
//                        dfMonth.setText(String.valueOf(months));
//                        long weeksInMonth = days / 7;
//                        days %= 7;
//                        dfMonthWeeks.setText(String.valueOf(weeksInMonth));
//                        dfMonthDays.setText(String.valueOf(days));
//                        dfMonthHours.setText(String.valueOf(hours));
//                        dfMonthMinutes.setText(String.valueOf(minutes));
//                        dfMonthSeconds.setText(String.valueOf(seconds));
                        break;
                    default:
                        // Handle other formats or default case
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
