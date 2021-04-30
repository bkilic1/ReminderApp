package com.example.reminderapp;

import android.provider.MediaStore;

import java.util.Calendar;

public class Reminder {
    private int reminderID;
    private String reminderName;



    private String reminderDate;
    private String importance;

    public Reminder() {
        reminderID = -1;


    }

    public int getReminderID() {
        return reminderID;
    }

    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }


    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }
    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }






}
