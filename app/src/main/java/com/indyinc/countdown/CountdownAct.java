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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    TextView sarahPhrases;

    ChipGroup chipGroup;
    Chip chipDay, chipWeek, chipFortnight, chipMonth;
    String format;

    private CountDownTimer countDownTimer;

    @SuppressLint("NonConstantResourceId")
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

        //fill myWords
        ArrayList<String> mySentences = new ArrayList<>();
        mySentences.add("Hermoine Granger wannabe line: I'm not an owl!");
        mySentences.add("Hermoine Granger wannabe line: It's leviosaaaa!");
        mySentences.add("Uppies! Uppies! Uppies!");
        mySentences.add("Alfoil...only Queensland calls it that Sarah...");
        mySentences.add("What do you get when you cross a struggling artist with a missed Centrelink payment? You get what you fucking deserve!");
        mySentences.add("What are you doing ya dirty stopout");
        mySentences.add("Most used phrase: I'm super shy, super shy");
        mySentences.add("Most controversial take: No I don't lick my dogs feet");
        mySentences.add("Most likely to make a song from these lyrics: Take the PT! (Public Transport)");

        mySentences.add("Favourite nickname: Hi pookiebear");
        mySentences.add("Top 1 gaming moment: Struggling to jump to a tiny ledge without realising you would be jumping to the same spot you came from...You trying your best to complete a square in Alan Wake");




        chipGroup = findViewById(R.id.chipGroup);
        chipDay = findViewById(R.id.chipDay);
        chipWeek = findViewById(R.id.chipWeek);
        chipFortnight = findViewById(R.id.chipFortnight);
        chipMonth = findViewById(R.id.chipMonth);

        chipDay.setChecked(true);

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
        dfDay = findViewById(R.id.dfDay);
        dfHours = findViewById(R.id.dfHours);
        dfMinutes = findViewById(R.id.dfMinutes);
        dfSeconds = findViewById(R.id.dfSeconds);
        dfWeek = findViewById(R.id.dfWeek);
        dfWeekDays = findViewById(R.id.dfWeekDay);
        dfFortnightWeek = findViewById(R.id.dfFortnightWeek);
        //Month format
        dfMonth = findViewById(R.id.dfMonth);

        Intent intent = getIntent();
        format = intent.getStringExtra("eventFormat");
        String title = intent.getStringExtra("eventTitle");
        System.out.println("Title: checker " + title);
        String finalTitle = '\"'+ title+ '\"';
        eventTitle.setText(finalTitle);

        highlightSelectedFormat(format);

        String date = intent.getStringExtra("eventDate");
        String format = intent.getStringExtra("eventFormat");
        startCountdown(date, format);

        sarahPhrases = findViewById(R.id.sarahPhrases);
        sarahPhrases.setVisibility(View.GONE);

        //if the date is 06/02/, and the title is My Birthday, then show the sarahPhrases TextView
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        try {
            assert date != null;
            cal.setTime(Objects.requireNonNull(sdf.parse(date)));
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) + 1; // Adding 1 because Calendar.MONTH is zero-based

            System.out.println("Day: " + day);
            System.out.println("Month: " + month);

            assert title != null;
            String lowerCaseTitle = title.toLowerCase();
            //remove end spaces or start spaces from the title
            lowerCaseTitle = lowerCaseTitle.trim();

            if(day ==8){
                System.out.println("This fires day 8");
            }
            if(month == 2){
                System.out.println("This fires month 2");
            }
            if (lowerCaseTitle.equals("my birthday")) {
                System.out.println("This fires birthday");
            }

            if (day == 8 && month == 2 && lowerCaseTitle.equals("my birthday")) {

                System.out.println("This fires birthday");
                // Generate a random index within the bounds of the mySentences list
                int randomIndex = (int) (Math.random() * mySentences.size());
                // Get the sentence at the random index
                String randomSentence = mySentences.get(randomIndex);
                String finalSentence = '\"'+ randomSentence+ '\"';
                sarahPhrases.setVisibility(View.VISIBLE);
                sarahPhrases.setText(finalSentence);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }



        chipDay.setOnClickListener(v -> startCountdown(date, "Day"));
        chipWeek.setOnClickListener(v -> startCountdown(date, "Week"));
        chipFortnight.setOnClickListener(v -> startCountdown(date, "Fortnight"));
        chipMonth.setOnClickListener(v -> startCountdown(date, "Month"));

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

}
