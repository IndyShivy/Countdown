package com.indyinc.countdown;

public class DateItem {
    private long id;
    private final String title;
    private final String date;
    private final String format;

    public DateItem(String title, String date, String format) {
        this.title = title;
        this.date = date;
        this.format = format;
    }
    public DateItem(long id, String title, String date, String format) {
        this.title = title;
        this.date = date;
        this.format = format;
        this.id = id;
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

    public long getId() {
        return id;
    }

}
