package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 03-01-2016.
 */
public class TripData {

    /**id, vehicleId typeOfTrip locationDeparture locationReturn dateOfDeparture dateOfReturn timeOfDeparture timeOfReturn
     * meterReadingDeparture  meterReadingReturn costOfTrip receipt addedOn
     */
    private String _id;
    private String _vehicleId;
    private String _typeOfTrip;
    private String _locationDeparture;
    private String _locationReturn;
    private String _dateOfDeparture;
    private String _dateOfReturn;
    private String _timeOfDeparture;
    private String _timeOfReturn;
    private String _meterReadingDeparture;
    private String _meterReadingReturn;
    private String _costOfTrip;
    private String _receipt;
    private String _addedOn;

    public TripData(){}

    public TripData(String vehicleId,String typeOfTrip,String locationDeparture,String locationReturn,
                    String dateOfDeparture,String dateOfReturn,String timeOfDeparture,String timeOfReturn,
                    String meterReadingDeparture,String meterReadingReturn,String costOfTrip,
                    String receipt){
        this._vehicleId=vehicleId;
        this._typeOfTrip=typeOfTrip;
        this._locationDeparture=locationDeparture;
        this._locationReturn=locationReturn;
        this._dateOfDeparture=dateOfDeparture;
        this._dateOfReturn=dateOfReturn;
        this._timeOfDeparture=timeOfDeparture;
        this._timeOfReturn=timeOfReturn;
        this._meterReadingDeparture=meterReadingDeparture;
        this._meterReadingReturn=meterReadingReturn;
        this._costOfTrip=costOfTrip;
        this._receipt=receipt;
    }

    public TripData(String vehicleId,String typeOfTrip,String locationDeparture,String locationReturn,
                    String dateOfDeparture,String dateOfReturn,String timeOfDeparture,String timeOfReturn,
                    String meterReadingDeparture,String meterReadingReturn,String costOfTrip,
                    String receipt,String addedOn){
        this._vehicleId=vehicleId;
        this._typeOfTrip=typeOfTrip;
        this._locationDeparture=locationDeparture;
        this._locationReturn=locationReturn;
        this._dateOfDeparture=dateOfDeparture;
        this._dateOfReturn=dateOfReturn;
        this._timeOfDeparture=timeOfDeparture;
        this._timeOfReturn=timeOfReturn;
        this._meterReadingDeparture=meterReadingDeparture;
        this._meterReadingReturn=meterReadingReturn;
        this._costOfTrip=costOfTrip;
        this._receipt=receipt;
        this._addedOn=addedOn;
    }

    public TripData(String id,String vehicleId,String typeOfTrip,String locationDeparture,String locationReturn,
                    String dateOfDeparture,String dateOfReturn,String timeOfDeparture,String timeOfReturn,
                             String meterReadingDeparture,String meterReadingReturn,String costOfTrip,
                             String receipt,String addedOn){
        this._id=id;
        this._vehicleId=vehicleId;
        this._typeOfTrip=typeOfTrip;
        this._locationDeparture=locationDeparture;
        this._locationReturn=locationReturn;
        this._dateOfDeparture=dateOfDeparture;
        this._dateOfReturn=dateOfReturn;
        this._timeOfDeparture=timeOfDeparture;
        this._timeOfReturn=timeOfReturn;
        this._meterReadingDeparture=meterReadingDeparture;
        this._meterReadingReturn=meterReadingReturn;
        this._costOfTrip=costOfTrip;
        this._receipt=receipt;
        this._addedOn=addedOn;
    }

    public String getId() {return this._id;}
    public String getVehicleId() {return this._vehicleId; }
    public String getTypeOfTrip() {return this._typeOfTrip; }

    public String getLocationDeparture() {return this._locationDeparture; }
    public String getLocationReturn() {return this._locationReturn; }

    public String getDateOfDeparture() {return this._dateOfDeparture; }
    public String getDateOfReturn() {return this._dateOfReturn; }

    public String getTimeOfDeparture() {return this._timeOfDeparture; }
    public String getTimeOfReturn() {return this._timeOfReturn; }

    public String getMeterReadingDeparture() {return this._meterReadingDeparture; }
    public String getMeterReadingReturn() {return this._meterReadingReturn; }

    public String getCostOfTrip() {return this._costOfTrip; }
    public String getReceipt() {return this._receipt; }
    public String getAddedOn() { return this._addedOn; }

    public void setId(String id) {this._id = id;}
    public void setVehicleId(String vehicleId) {this._vehicleId=vehicleId; }
    public void setTypeOfTrip(String typeOfTrip) {this._typeOfTrip=typeOfTrip;}

    public void setLocationDeparture(String locationDeparture) {this._locationDeparture=locationDeparture;}
    public void setLocationReturn(String locationReturn) {this._locationReturn=locationReturn;}

    public void setDateOfDeparture(String dateOfDeparture) {this._dateOfDeparture=dateOfDeparture;}
    public void setDateOfReturn(String dateOfReturn) {this._dateOfReturn=dateOfReturn;}

    public void setTimeOfDeparture(String timeOfDeparture) {this._timeOfDeparture=timeOfDeparture;}
    public void setTimeOfReturn(String timeOfReturn) {this._timeOfReturn=timeOfReturn;}

    public void setMeterReadingDeparture(String meterReadingDeparture) {this._meterReadingDeparture=meterReadingDeparture;}
    public void setMeterReadingReturn(String meterReadingReturn) {this._meterReadingReturn=meterReadingReturn;}

    public void setCostOfTrip(String costOfTrip) {this._costOfTrip=costOfTrip;}
    public void setReceipt(String receipt) {this._receipt=receipt;}
    public void setAddedOn(String addedOn) {this._addedOn=addedOn; }

}
