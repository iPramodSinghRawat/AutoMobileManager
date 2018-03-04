package com.alpha.ghosty.automobilemanager;
/**
 * Vehicle Data Class
 * Created by iPramodSinghRawat on 26-12-2015.
 * Author & Developer: Pramod Singh Rawat
 */
public class VehicleData {
    private String _id;
    private String _regNumber;
    private String _brand;
    private String _model;
    private String _dateOfPurchase;
    private String _fuelTankCapacity;
    private String _type;
    private String _color;
    private String _fuelType;
    private String _comment;
    private String _addedOn;

    private String[] _idAry;
    private String[] _regNumberAry;
    private String[] _brandAry;
    private String[] _modelAry;
    private String[] _dateOfPurchaseAry;
    private String[] _fuelTankCapacityAry;
    private String[] _typeAry;
    private String[] _colorAry;
    private String[] _fuelTypeAry;
    private String[] _commentAry;
    private String[] _addedOnAry;

    public VehicleData() { }
    public VehicleData(String regNumber, String brand,String model, String dateOfPurchase,
                       String fuelTankCapacity,String type, String color, String fuelType, String comment) {
        this._regNumber = regNumber;
        this._brand = brand;
        this._model = model;
        this._dateOfPurchase = dateOfPurchase;
        this._fuelTankCapacity=fuelTankCapacity;
        this._type = type;
        this._color=color;
        this._fuelType = fuelType;
        this._comment=comment;
    }

    public VehicleData(String id,String regNumber, String brand,String model, String dateOfPurchase,
                       String fuelTankCapacity,String type, String color, String fuelType, String comment) {
        this._id = id;
        this._regNumber = regNumber;
        this._brand = brand;
        this._model = model;
        this._dateOfPurchase = dateOfPurchase;
        this._fuelTankCapacity=fuelTankCapacity;
        this._type = type;
        this._color=color;
        this._fuelType = fuelType;
        this._comment=comment;
    }

    public VehicleData(String id,String regNumber, String brand,String model, String dateOfPurchase,
                       String fuelTankCapacity,String type, String color, String fuelType, String comment, String addedOn) {
        this._id = id;
        this._regNumber = regNumber;
        this._brand = brand;
        this._model = model;
        this._dateOfPurchase = dateOfPurchase;
        this._fuelTankCapacity=fuelTankCapacity;
        this._type = type;
        this._color=color;
        this._fuelType = fuelType;
        this._comment=comment;
        this._addedOn = addedOn;
    }

    public VehicleData(String[] idAry,String[] regNumberAry, String[] brandAry,String[] modelAry,
                       String[] dateOfPurchaseAry,
                       String[] fuelTankCapacityAry,String[] typeAry, String[] colorAry,
                       String[] fuelTypeAry, String[] commentAry, String[] addedOnAry) {
        this._idAry = idAry;
        this._regNumberAry = regNumberAry;
        this._brandAry = brandAry;
        this._modelAry = modelAry;
        this._dateOfPurchaseAry = dateOfPurchaseAry;
        this._fuelTankCapacityAry=fuelTankCapacityAry;
        this._typeAry = typeAry;
        this._colorAry=colorAry;
        this._fuelTypeAry = fuelTypeAry;
        this._commentAry=commentAry;
        this._addedOnAry = addedOnAry;
    }

    public String getId() {return this._id;}
    public String getRegNumber() {return this._regNumber; }
    public String getBrand() {return this._brand; }
    public String getModel() {return this._model; }
    public String getDateOfPurchase() {return this._dateOfPurchase;}
    public String getFuelTankCapacity() {return this._fuelTankCapacity;}
    public String getType() { return this._type; }
    public String getColor() { return this._color; }
    public String getFuelType() {return this._fuelType;}
    public String getComment() {return this._comment;}
    public String getAddedOn() { return this._addedOn; }

    public void setId(String id){this._id=id;}
    public void setRegNumber(String regNumber){this._regNumber=regNumber;}
    public void setBrand(String brand){this._brand=brand;}
    public void setModel(String model){this._model=model;}
    public void setDateOfPurchase(String dateOfPurchase){this._dateOfPurchase=dateOfPurchase;}
    public void setFuelTankCapacity(String fuelTankCapacity){this._fuelTankCapacity=fuelTankCapacity;}
    public void setType(String type){this._type=type;}
    public void setColor(String color){this._color=color;}
    public void setFuelType(String fuelType){this._fuelType=fuelType;}
    public void setComment(String comment){this._comment=comment;}
    public void setAddedOn(String addedOn){this._addedOn=addedOn;}


    public void setIdary(String[] idAry){this._idAry = idAry;}
    public String[] getIdAry() { return this._idAry; }

    public void setRegNumberAry(String[] regNumberAry) {this._regNumberAry = regNumberAry; }
    public String[] getRegNumberAry() { return this._regNumberAry;}

    public void setBrandAry(String[] brandAry) {this._brandAry = brandAry;}
    public String[] getBrandAry() {return this._brandAry; }

    public void setModelAry(String[] modelAry) { this._modelAry = modelAry;}
    public String[] getModelAry() {return this._modelAry;}

    public void setDateOfPurchaseAry(String[] dateOfPurchaseAry) {this._dateOfPurchaseAry = dateOfPurchaseAry;}
    public String[] getDateOfPurchaseAry() { return this._dateOfPurchaseAry;}

}
