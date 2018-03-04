package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 27-04-2016.
 */
public class ReminderData {

    private String _id;
    private String _title;
    private String _details;
    private String _date;
    private String _time;
    private String _addedOn;

    public ReminderData(){}

    public ReminderData(String title,String details,String date, String time){
        this._title=title;
        this._details=details;
        this._date=date;
        this._time=time;
    }

    public ReminderData(String id, String title,String details,String date, String time, String addedOn){
        this._id=id;
        this._title=title;
        this._details=details;
        this._date=date;
        this._time=time;
        this._addedOn=addedOn;
    }

    public String getId(){return this._id;}
    public String getTitle(){return this._title;}
    public String getDetails(){return this._details;}
    public String getDate(){return this._date;}
    public String getTime(){return this._time;}
    public String getAddedOn(){return this._addedOn;}

}
