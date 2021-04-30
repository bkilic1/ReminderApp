package com.example.reminderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderDataSource {
    private SQLiteDatabase database;
    private ReminderDBHelper dbHelper;

    public ReminderDataSource(Context context) {
        dbHelper = new ReminderDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertReminder(Reminder c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("remindername", c.getReminderName());
            initialValues.put("importance", c.getImportance());
            initialValues.put("date", c.getReminderDate());



            didSucceed = database.insert("reminder", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateReminder(Reminder c) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) c.getReminderID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("remindername", c.getReminderName());
            updateValues.put("importance", c.getImportance());
            updateValues.put("date", c.getReminderDate());


            didSucceed = database.update("reminder", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }
    public int getLastReminderId() {
        int lastId;
        try {
            String query = "Select MAX(_id) from reminder";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getReminderName() {
        ArrayList<String> reminderNames = new ArrayList<>();
        try {
            String query = "Select remindername from reminder";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                reminderNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            reminderNames = new ArrayList<String>();
        }
        return reminderNames;
    }

    public ArrayList<Reminder> getReminders(String sortField, String sortOrder) {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        try {
            String query = "SELECT  * FROM reminder ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Reminder newReminder;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newReminder = new Reminder();
                newReminder.setReminderID(cursor.getInt(0));
                newReminder.setReminderName(cursor.getString(1));
                newReminder.setImportance(cursor.getString(2));
                newReminder.setReminderDate(cursor.getString(3));

                reminders.add(newReminder);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            reminders = new ArrayList<Reminder>();
        }
        return reminders;
    }

    public Reminder getSpecificReminder(int reminderId) {
        Reminder reminder = new Reminder();
        String query = "SELECT  * FROM reminder WHERE _id =" + reminderId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            reminder.setReminderID(cursor.getInt(0));
            reminder.setReminderName(cursor.getString(1));
            reminder.setImportance(cursor.getString(2));
            reminder.setReminderDate(cursor.getString(3));


            cursor.close();
        }
        return reminder;
    }

    public boolean deleteContact(int reminderID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("reminder", "_id=" + reminderID, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

}
