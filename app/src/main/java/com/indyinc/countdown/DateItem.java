package com.indyinc.countdown;

public class DateItem {
    private String title;
    private String date;
    private String format;

    public DateItem(String title, String date, String format) {
        this.title = title;
        this.date = date;
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getFormat() {
        return format;
    }
}
