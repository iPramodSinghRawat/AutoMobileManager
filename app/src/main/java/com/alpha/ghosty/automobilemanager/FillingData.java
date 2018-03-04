package com.alpha.ghosty.automobilemanager;
/**
 * Created by iPramodSinghRawat on 27-12-2015.
 * Fuel Filling Class To Handle Fuel Filling Data
 * Author & Developer: Pramod Singh Rawat
 */
public class FillingData {

    private String _id;
    private String _vehicleId;
    private String _fuelType;
    private String _meterReading;
    private String _date;
    private String _volume;
    private String _price;
    private String _receipt;
    private String _addedOn;

    public FillingData() {}

    public FillingData(String vehicleId,String fuelType, String meterReading,String date, String volume, String price, String receipt) {
        this._vehicleId = vehicleId;
        this._fuelType = fuelType;
        this._meterReading = meterReading;
        this._date = date;
        this._volume = volume;
        this._price = price;
        this._receipt = receipt;
    }

    public FillingData(String vehicleId,String fuelType, String meterReading,String date, String volume, String price, String receipt,
                       String addedOn) {
        this._vehicleId = vehicleId;
        this._fuelType = fuelType;
        this._meterReading = meterReading;
        this._date = date;
        this._volume = volume;
        this._price = price;
        this._receipt = receipt;
        this._addedOn = addedOn;
    }

    public FillingData(String id, String vehicleId,String fuelType, String meterReading, String date, String volume, String price, String receipt,
                       String addedOn) {
        this._id = id;
        this._fuelType = fuelType;
        this._vehicleId = vehicleId;
        this._meterReading = meterReading;
        this._date = date;
        this._volume = volume;
        this._price = price;
        this._receipt = receipt;
        this._addedOn = addedOn;
    }

    public String getId() {return this._id;}
    public String getVehicleId() {return this._vehicleId; }
    public String getFuelType() {return this._fuelType; }
    public String getMeterReading() {return this._meterReading; }
    public String getDate() {return this._date; }
    public String getVolume() {return this._volume;}
    public String getPrice() { return this._price; }
    public String getReceipt() {return this._receipt;}
    public String getAddedOn() { return this._addedOn; }

    public void setVId(String id) {this._id = id;}
    public void setVehicleId(String vehicleId) {this._vehicleId=vehicleId; }
    public void setMeterReading(String meterReading){this._meterReading=meterReading; }
    public void setDate(String date){this._date=date; }
    public void setVolume(String volume) {this._volume=volume;}
    public void setPrice(String price) { this._price=price; }
    public void setReceipt(String receipt) {this._receipt=receipt;}
    public void setAddedOn(String addedOn) {this._addedOn=addedOn; }

}
