package com.indyinc.countdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DateDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DateDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "DateTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_FORMAT = "Format";
    Context context;
    String dbPath;

    //create a constructor to initialize the data
    public DateDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create a table
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_FORMAT + " TEXT);";
        sqLiteDatabase.execSQL(query);
        dbPath = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertDate(String title, String date, String format){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_TITLE + ", " +
                COLUMN_DATE + ", " +
                COLUMN_FORMAT + ") VALUES ('" +
                title + "', '" +
                date + "', '" +
                format + "');";
        sqLiteDatabase.execSQL(query);
    }

    public void deleteEvent(DateItem dateItem){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COLUMN_TITLE + " = '" + dateItem.getTitle() + "' AND " +
                COLUMN_DATE + " = '" + dateItem.getDate() + "' AND " +
                COLUMN_FORMAT + " = '" + dateItem.getFormat() + "';";
        sqLiteDatabase.execSQL(query);
    }


    public ArrayList<DateItem> getAllDates(){
        ArrayList<DateItem> dateItems = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
        int formatIndex = cursor.getColumnIndex(COLUMN_FORMAT);

        if (titleIndex != -1 && dateIndex != -1 && formatIndex != -1) {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(titleIndex);
                    String date = cursor.getString(dateIndex);
                    String format = cursor.getString(formatIndex);
                    dateItems.add(new DateItem(title, date, format));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return dateItems;
    }
}
