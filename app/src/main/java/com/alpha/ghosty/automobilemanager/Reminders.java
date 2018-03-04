package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Reminders extends SuperActivity {

    DBhandler dbHandler;
    ListView remindersLV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        dbHandler = new DBhandler(this, null, null, 1);
        remindersLV = (ListView) findViewById(R.id.remindersLV);

        populateReminders();
    }

    public void redirect2AddNewReminder(View view){
        startActivity(new Intent(getBaseContext(),AddNewReminder.class));
        finish();
    }

    public void populateReminders(){

        Cursor reminderList=dbHandler.getReminderData(null);

        //Toast.makeText(this, "reminderList.getCount()"+reminderList.getCount(),Toast.LENGTH_SHORT).show();

        if(reminderList.getCount()>0){

            ArrayList<String> idList = new ArrayList<String>();
            ArrayList<String> titleList = new ArrayList<String>();
            ArrayList<String> detailList = new ArrayList<String>();
            ArrayList<String> dateList = new ArrayList<String>();
            ArrayList<String> timeList = new ArrayList<String>();

        reminderList.moveToFirst();

        while(reminderList.isAfterLast() == false){
            idList.add(reminderList.getString(reminderList.getColumnIndex("_id")));
            titleList.add(reminderList.getString(reminderList.getColumnIndex("title")));
            detailList.add(reminderList.getString(reminderList.getColumnIndex("detail")));
            dateList.add(date2Show(reminderList.getString(reminderList.getColumnIndex("date"))));
            timeList.add(time2Show(reminderList.getString(reminderList.getColumnIndex("time"))));
            reminderList.moveToNext();
        }

        if(!reminderList.isClosed()){reminderList.close();}

        remindersLV.setAdapter(new ReminderListAdapter(this,
                idList.toArray(new String[idList.size()]),
                titleList.toArray(new String[titleList.size()]),
                detailList.toArray(new String[detailList.size()]),
                dateList.toArray(new String[dateList.size()]),
                timeList.toArray(new String[timeList.size()])));

        }else{Toast.makeText(this, "No Reminders",Toast.LENGTH_SHORT).show();
        }

    }

}
