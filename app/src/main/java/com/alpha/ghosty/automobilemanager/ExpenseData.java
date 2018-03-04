package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 03-01-2016.
 * Expense Data Class To Handle Expense
 * Author & Developer: Pramod Singh Rawat
 */
public class ExpenseData {
    /* id,vehicleId,type,meterReading,dateOfExpense,price,receipt,addedOn */

    private String _id;
    private String _vehicleId;
    private String _type;
    private String _meterReading;
    private String _date;
    private String _price;
    private String _receipt;
    private String _addedOn;

    public ExpenseData(){}

    public ExpenseData(String vehicleId,String type,String meterReading,String date,String price,String receipt){
        this._vehicleId=vehicleId;
        this._type=type;
        this._meterReading=meterReading;
        this._date=date;
        this._price=price;
        this._receipt=receipt;
    }

    public ExpenseData(String vehicleId,String type,String meterReading,String date,String price,String receipt,String addedOn){
        this._vehicleId=vehicleId;
        this._type=type;
        this._meterReading=meterReading;
        this._date=date;
        this._price=price;
        this._receipt=receipt;
        this._addedOn=addedOn;
    }

    public ExpenseData(String id,String vehicleId,String type,String meterReading,String date,String price,String receipt,String addedOn){
        this._id=id;
        this._vehicleId=vehicleId;
        this._type=type;
        this._meterReading=meterReading;
        this._date=date;
        this._price=price;
        this._receipt=receipt;
        this._addedOn=addedOn;
    }

    public String getId() {return this._id;}
    public String getVehicleId() {return this._vehicleId; }
    public String getExpenseType() {return this._type; }
    public String getMeterReading() {return this._meterReading; }
    public String getDate() {return this._date; }
    public String getPrice() { return this._price; }
    public String getReceipt() {return this._receipt;}
    public String getAddedOn() { return this._addedOn; }

    public void setId(String id) {this._id = id;}
    public void setVehicleId(String vehicleId) {this._vehicleId=vehicleId; }
    public void setExpenseType(String type) {this._type=type;}
    public void setMeterReading(String meterReading){this._meterReading=meterReading; }
    public void setDate(String date){this._date=date; }
    public void setPrice(String price) { this._price=price; }
    public void setReceipt(String receipt) {this._receipt=receipt;}
    public void setAddedOn(String addedOn) {this._addedOn=addedOn; }
}
