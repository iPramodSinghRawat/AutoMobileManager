package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 05-05-2016.
 */
public class InsuranceData {

    private String _id;
    private String _vehicleId;
    private String _provider;
    private String _type;
    private String _dateBegin;
    private String _dateEnd;
    private String _meterReading;
    private String _cost;
    private String _details;
    private String _receipt;
    private String _addedOn;

    public InsuranceData(){}

    public InsuranceData(String id,String vehicleId,String provider,String type,String dateBegin,String dateEnd,String meterReading,String cost,String details,String receipt,String addedOn){
        this._id=id;
        this._vehicleId=vehicleId;
        this._provider=provider;
        this._type=type;
        this._dateBegin=dateBegin;
        this._dateEnd=dateEnd;
        this._meterReading=meterReading;
        this._cost=cost;
        this._details=details;
        this._receipt=receipt;
        this._addedOn=addedOn;
    }

    public InsuranceData(String vehicleId,String provider,String type,String dateBegin,String dateEnd,String meterReading,String cost,String details,String receipt){
        this._vehicleId=vehicleId;
        this._provider=provider;
        this._type=type;
        this._dateBegin=dateBegin;
        this._dateEnd=dateEnd;
        this._meterReading=meterReading;
        this._cost=cost;
        this._details=details;
        this._receipt=receipt;
    }

    public String getId(){return this._id;}
    public String getVehicleId(){return this._vehicleId;}
    public String getProvider(){return this._provider;}
    public String getType(){return this._type;}
    public String getDateBegin(){return this._dateBegin;}
    public String getDateEnd(){return this._dateEnd;}
    public String getMeterReading(){return this._meterReading;}
    public String getCost(){return this._cost;}
    public String getDetails(){return this._details;}
    public String getReceipt(){return this._receipt;}
    public String getAddedOn(){return this._addedOn;}

}
