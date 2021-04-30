package com.example.reminderapp;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Reminder currentReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListButton();

        initSettingsButton();
        initToggleButton();

        currentReminder = new Reminder();
        initTextChangedEvents();
        initSaveButton();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initContact(extras.getInt("reminderID"));

        } else {

            currentReminder = new Reminder();
        }
    }
    private void initListButton() {

        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,ReminderListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }


        });

    }



    private void initSettingsButton() {

        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,ReminderSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }


        });

    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());

            }

        });

    }

    private void setForEditing(boolean enabled) {
        EditText editName = findViewById(R.id.editReminder);
        EditText editImportance = findViewById(R.id.editImportance);
        EditText editDate = findViewById(R.id.editReminderDate);


        Button buttonSave = findViewById(R.id.buttonSave);

        editName.setEnabled(enabled);
        editImportance.setEnabled(enabled);
        editDate.setEnabled(enabled);

        buttonSave.setEnabled(enabled);

        if(enabled) {

            editName.requestFocus();
        }


    }





    private void initTextChangedEvents() {

        final EditText etReminderName = (EditText) findViewById(R.id.editReminder);
        etReminderName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentReminder.setReminderName(etReminderName.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etReminderImportance = (EditText) findViewById(R.id.editImportance);
        etReminderImportance.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentReminder.setImportance(etReminderImportance.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etReminderDate = (EditText) findViewById(R.id.editReminderDate);
        etReminderDate.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentReminder.setReminderDate(etReminderDate.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });


    }

    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                boolean wasSuccessful = false;
                ReminderDataSource ds = new ReminderDataSource(MainActivity.this);
                try {
                    ds.open();

                    if (currentReminder.getReminderID() == -1) {
                        wasSuccessful = ds.insertReminder(currentReminder);
                        int newId = ds.getLastReminderId();
                        currentReminder.setReminderID(newId);

                    } else {
                        wasSuccessful = ds.updateReminder(currentReminder);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = (EditText) findViewById(R.id.editReminder);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        EditText editAddress = (EditText) findViewById(R.id.editImportance);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
        EditText editCity = (EditText) findViewById(R.id.editReminderDate);
        imm.hideSoftInputFromWindow(editCity.getWindowToken(), 0);

    }

    private void initContact(int id) {
        ReminderDataSource ds = new ReminderDataSource(MainActivity.this);
        try {
            ds.open();
            currentReminder = ds.getSpecificReminder(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
        }

        EditText editReminder = findViewById(R.id.editReminder);
        EditText editImportance = findViewById(R.id.editImportance);
        EditText editReminderDate = findViewById(R.id.editReminderDate);


        editReminder.setText(currentReminder.getReminderName());
        editImportance.setText(currentReminder.getImportance());
        editReminderDate.setText(currentReminder.getReminderDate());

    }






}