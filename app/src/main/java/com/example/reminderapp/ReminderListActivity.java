package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class ReminderListActivity extends AppCompatActivity {
    RecyclerView reminderList;
    ReminderAdapter reminderAdapter;

    ArrayList<Reminder> reminders;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();

            int position = viewHolder.getAdapterPosition();
            int reminderID = reminders.get(position).getReminderID();


            Intent intent = new Intent(ReminderListActivity.this, MainActivity.class);
            intent.putExtra("reminderId", reminderID);

            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        initListButton();
        initSettingsButton();
        initDeleteSwitch();
        initAddContactButton();


    }

    @Override
    public void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("MyReminderListPreferences", Context.MODE_PRIVATE).getString("sortfield", "remindername");
        String sortOrder = getSharedPreferences("MyReminderListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        ReminderDataSource ds = new ReminderDataSource(this);
        try {
            ds.open();
            reminders = ds.getReminders(sortBy, sortOrder);
            ds.close();
            if (reminders.size() > 0) {
                reminderList = findViewById(R.id.rvReminders);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                reminderList.setLayoutManager(layoutManager);
                reminderAdapter = new ReminderAdapter(reminders, this);
                reminderAdapter.setOnItemClickListener(onItemClickListener);
                reminderList.setAdapter(reminderAdapter);
            }
            else {
                Intent intent = new Intent(ReminderListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }

    }

    private void initAddContactButton() {
        Button newContact = findViewById(R.id.buttonAddReminder);
        newContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                reminderAdapter.setDelete(status);
                reminderAdapter.notifyDataSetChanged();
            }
        });
    }





    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setEnabled(false);
    }


    private void initSettingsButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListActivity.this, ReminderSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


}