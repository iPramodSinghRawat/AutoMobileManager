package com.alpha.ghosty.automobilemanager;
/**
 * Created by iPramodSinghRawat on 30-12-2015.
 * Service Data Class To Handle Service Data
 * Author & Developer: Pramod Singh Rawat
 */
public class ServiceData {

    private String _id;
    private String _vehicleId;
    private String _serviceType;
    private String _meterReading;
    private String _date;
    private String _price;
    private String _receipt;
    //private String _nextService;
    private String _addedOn;

    public ServiceData() {}

    public ServiceData(String vehicleId,String serviceType,String meterReading, String date,
                       String price, String receipt){//,String nextService) {
        this._vehicleId = vehicleId;
        this._serviceType=serviceType;
        this._meterReading = meterReading;
        this._date = date;
        this._price = price;
        this._receipt = receipt;
        //this._nextService=nextService;
    }

    public ServiceData(String vehicleId,String serviceType,String meterReading,String date,
                       String price, String receipt,String addedOn) {
        this._vehicleId = vehicleId;
        this._serviceType=serviceType;
        this._meterReading = meterReading;
        this._date = date;
        this._price = price;
        this._receipt = receipt;
        //this._nextService=nextService;
        this._addedOn = addedOn;
    }

    public ServiceData(String id, String vehicleId,String serviceType, String meterReading,
                       String date, String price, String receipt,String addedOn) {
        this._id = id;
        this._vehicleId = vehicleId;
        this._serviceType=serviceType;
        this._meterReading = meterReading;
        this._date = date;
        this._price = price;
        this._receipt = receipt;
        //this._nextService=nextService;
        this._addedOn = addedOn;
    }

    public String getId() {return this._id;}
    public String getVehicleId() {return this._vehicleId; }
    public String getServiceType() {return this._serviceType; }
    public String getMeterReading() {return this._meterReading; }
    public String getDate() {return this._date; }
    public String getPrice() { return this._price; }
    public String getReceipt() {return this._receipt;}
    //public String getNextService() {return this._nextService;}
    public String getAddedOn() { return this._addedOn; }

    public void setId(String id) {this._id = id;}
    public void setVehicleId(String vehicleId) {this._vehicleId=vehicleId; }
    public void setServiceType(String serviceType) {this._serviceType=serviceType;}
    public void setMeterReading(String meterReading){this._meterReading=meterReading; }
    public void setDate(String date){this._date=date; }
    public void setPrice(String price) { this._price=price; }
    public void setReceipt(String receipt) {this._receipt=receipt;}
    //public void setNextService(String nextService) {this._nextService=nextService;}
    public void setAddedOn(String addedOn) {this._addedOn=addedOn; }

}
