package com.alpha.ghosty.automobilemanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddNewReminder extends SuperActivity {

    private Calendar calendar;
    private int year, month, day,hour,min;//,sec;
    DBhandler dbHandler;

    EditText dateET,timeET,titleET,detailET;
    TextView resultTv;

    String date,time,title,detail;
    int EtId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder);
        dbHandler = new DBhandler(this, null, null, 1);

        dateET = (EditText) findViewById(R.id.dateET);
        timeET = (EditText) findViewById(R.id.timeET);
        titleET = (EditText) findViewById(R.id.titleET);
        detailET = (EditText) findViewById(R.id.detailET);
        resultTv = (TextView) findViewById(R.id.resultTv);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //dateET.setText((day-1) + "/" + (month + 1) + "/" + year);
        //date=year+"-"+(month + 1)+"-"+(day-1);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        //timeET.setText(pad(hour)+":"+pad(min)+":"+pad(calendar.get(Calendar.SECOND)));
        //time=pad(hour)+":"+pad(min)+":"+pad(00);

        dateET.setKeyListener(null);
        dateET.setClickable(true);
        dateET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showDialog(v.getId());
            }
        });
        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(v.getId());
                } else {
                }
            }
        });

        timeET.setKeyListener(null);
        timeET.setClickable(true);
        timeET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showDialog(v.getId());
            }
        });
        timeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(v.getId());
                } else {
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void getDate(View view){
        showDialog(view.getId());
    }

    @SuppressWarnings("deprecation")
    public void getTime(View view){
        showDialog(view.getId());
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        EtId=id;
        //Toast.makeText(getApplicationContext(), Integer.toString(id), Toast.LENGTH_LONG).show();
        if(EtId == R.id.dateET){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);  //date is dateSetListener as per your code in question
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            //return new DatePickerDialog(this, datePickerListener, year, month, day);
            return datePickerDialog;
            //return new DatePickerDialog(this, datePickerListener, year, month, day);
        }else{
            //TimePickerDialog timePickerDialog= new TimePickerDialog(this, timePickerListener, hour, min, false);
            //timePickerDialog.getT
            return new TimePickerDialog(this, timePickerListener, hour, min, false);
        }

    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            //Toast.makeText(getApplicationContext(), Integer.toString(view.getId()), Toast.LENGTH_LONG).show();

                dateET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
                date=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;

            //Toast.makeText(getApplicationContext(), Integer.toString(R.id.dateOfDepartureET), Toast.LENGTH_LONG).show();
        }
    };


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            min = minutes;
            timeET.setText(pad(hour)+":"+pad(min)+":"+pad(00));
            time=pad(hour)+":"+pad(min)+":"+pad(00);

        }
    };

    public void putReminder(View view){
        InputMethodManager inputManager =(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processPutReminder();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Data Confirm ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void processPutReminder(){

        title=titleET.getText().toString();
        detail=detailET.getText().toString();


        if (title.equals("")) {
            titleET.setError("Title Required");
            titleET.setHint("Enter Title");
        }else if (detail.equals("")) {
            detailET.setError("Detail Required");
            detailET.setHint("Enter Details");
        }else{
            boolean rtn=dbHandler.putReminderData(new ReminderData(title,detail,date,time));

            if(rtn==true){
                //Intent intent = new Intent(this, TripRecordView.class);
                startActivity(new Intent(this, Reminders.class));
                finish();
            }else{
                resultTv.setText("Error Try Again : " + rtn);
            }
        }

    }
}
