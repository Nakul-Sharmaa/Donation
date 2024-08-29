package com.example.donation;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bloodcamp.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_USERS = "users";
    public static final String TABLE_DONATIONS = "donations2";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT, PASSWORD TEXT, AGE INTEGER, BLOODGROUP TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_DONATIONS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_EMAIL TEXT, DATE TEXT, UNITS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONATIONS);
        onCreate(db);
    }

    public long addUser(String name, String email, String password, int age, String bloodGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("EMAIL", email);
        contentValues.put("PASSWORD", password);
        contentValues.put("AGE", age);
        contentValues.put("BLOODGROUP", bloodGroup);

        long res = db.insert(TABLE_USERS, null, contentValues);
        db.close();
        return res;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE EMAIL=?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getString(5));
            cursor.close();
            return user;
        }
        return null;
    }

    public boolean updateUser(String oldEmail, String newName, String newEmail, int newAge, String newBloodGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", newName);
        contentValues.put("EMAIL", newEmail);
        contentValues.put("AGE", newAge);
        contentValues.put("BLOODGROUP", newBloodGroup);

        long result = db.update(TABLE_USERS, contentValues, "EMAIL = ?", new String[]{oldEmail});
        db.close();
        return result != -1;
    }

    public long addDonation(String email, String date, int units) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_EMAIL", email);
        contentValues.put("DATE", date);
        contentValues.put("UNITS", units);

        long res = db.insert(TABLE_DONATIONS, null, contentValues);
        db.close();
        return res;
    }

    public ArrayList<DonationRecord> getDonationHistory(String email) {
        ArrayList<DonationRecord> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_DONATIONS + " WHERE USER_EMAIL=?", new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE"));
                    @SuppressLint("Range") int units = cursor.getInt(cursor.getColumnIndex("UNITS"));
                    history.add(new DonationRecord(date, units));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return history;
    }
}
