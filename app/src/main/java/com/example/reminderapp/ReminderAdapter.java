package com.example.reminderapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter {

    private ArrayList<Reminder> reminderData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewReminder;
        public TextView textImportance;
        public TextView textRday;
        public Button deleteButton;

        public ContactViewHolder (@NonNull View itemView) {

            super(itemView);
            textViewReminder = itemView.findViewById(R.id.textReminderName);
            textImportance = itemView.findViewById(R.id.textImportanceName);
            textRday = itemView.findViewById(R.id.textReminderDate);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);



            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

        }

        public TextView getContactTextView() {

            return textViewReminder;

        }

        public TextView getPhoneTextView() {
            return textImportance;

        }
        public TextView getReminderDateTextView() {
            return textRday;

        }

        public Button getDeleteButton() {

            return deleteButton;
        }




    }

    public ReminderAdapter(ArrayList<Reminder> arrayList, Context context) {
        reminderData = arrayList;
        parentContext = context;//this one photo


    }



    public void setOnItemClickListener(View.OnClickListener itemClickListener) {

        mOnItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        return new ContactViewHolder(v);

    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, final int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(reminderData.get(position).getReminderName());
        cvh.getPhoneTextView().setText(reminderData.get(position).getImportance());
        cvh.getReminderDateTextView().setText(reminderData.get(position).getReminderDate());

        if(isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteItem(position);
                }
            });

        } else {

            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }

    }




    @Override
    public int getItemCount() {
        return reminderData.size();

    }



    //okay
    private void deleteItem(int position) {

        Reminder reminder = reminderData.get(position);
        ReminderDataSource ds = new ReminderDataSource(parentContext);

        try {
            ds.open();
            boolean didDelete = ds.deleteContact(reminder.getReminderID());
            ds.close();

            if(didDelete) {
                reminderData.remove(position);
                notifyDataSetChanged();

            } else {

                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();

            }

        }
        catch(Exception e) {


        }


    }

    public void setDelete(boolean b) {

        isDeleting = b;
    }


}
