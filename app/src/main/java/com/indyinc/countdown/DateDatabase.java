package com.indyinc.countdown;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DateDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DateDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "DateTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_FORMAT = "Format";
    Context context;

    //create a constructor to initialize the data
    public DateDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Modified to include ID as a primary key and autoincrement
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_FORMAT + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Modified insertDate method
    public void insertDate(DateItem dateItem){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, dateItem.getTitle());
        values.put(COLUMN_DATE, dateItem.getDate());
        values.put(COLUMN_FORMAT, dateItem.getFormat());
        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    // Modified delete method to use id
    public void deleteEvent(DateItem dateItem){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COLUMN_ID + " = " + dateItem.getId() + ";";
        sqLiteDatabase.execSQL(query);
    }


    public ArrayList<DateItem> getAllDates() {
        ArrayList<DateItem> dateItems = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
        int formatIndex = cursor.getColumnIndex(COLUMN_FORMAT);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String title = cursor.getString(titleIndex);
                String date = cursor.getString(dateIndex);
                String format = cursor.getString(formatIndex);
                dateItems.add(new DateItem(id, title, date, format)); // Assuming constructor with id
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Sort the list based on millisUntilEvent
        dateItems.sort((item1, item2) -> {
            long millis1 = getMillisUntilEvent(item1.getDate());
            long millis2 = getMillisUntilEvent(item2.getDate());
            return Long.compare(millis1, millis2);
        });

        return dateItems;
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
    public void deleteDate(DateItem dateItem){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COLUMN_TITLE + " = '" + dateItem.getTitle() + "' AND " +
                COLUMN_DATE + " = '" + dateItem.getDate() + "' AND " +
                COLUMN_FORMAT + " = '" + dateItem.getFormat() + "';";
        sqLiteDatabase.execSQL(query);
    }


}
