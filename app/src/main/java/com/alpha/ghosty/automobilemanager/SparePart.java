package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 07-11-2016.
 */

public class SparePart{

    private String _id;
    private String _vehicleId;
    private String _partNo;
    private String _partName;
    private String _partDetail;
    private String _quantity;
    private String _image;
    private String _addedOn;

    private String[] _idAry;
    private String[] _vehicleIdAry;
    private String[] _partNoAry;
    private String[] _partNameAry;
    private String[] _partDetailAry;
    private String[] _quantityAry;
    private String[] _imageAry;
    private String[] _addedOnAry;

    public SparePart(){

    }

    public SparePart(String id,String vehicleId,String partNo,String partName,String partDetail,
                     String quantity,String image,String addedOn){
        this._id=id;
        this._vehicleId=vehicleId;
        this._partNo=partNo;
        this._partName=partName;
        this._partDetail=partDetail;
        this._quantity=quantity;
        this._image=image;
        this._addedOn=addedOn;
    }

    public SparePart(String vehicleId,String partNo,String partName,String partDetail,
                     String quantity,String image,String addedOn){
        this._vehicleId=vehicleId;
        this._partNo=partNo;
        this._partName=partName;
        this._partDetail=partDetail;
        this._quantity=quantity;
        this._image=image;
        this._addedOn=addedOn;
    }

    public SparePart(String vehicleId,String partNo,String partName,String partDetail,
                     String quantity,String image){
        this._vehicleId=vehicleId;
        this._partNo=partNo;
        this._partName=partName;
        this._partDetail=partDetail;
        this._quantity=quantity;
        this._image=image;
    }

    public String getId(){return this._id;}
    public String getVehicleId(){return this._vehicleId;}
    public String getPartNo(){return this._partNo;}
    public String getPartName(){return this._partName;}
    public String getPartDetail(){return this._partDetail;}
    public String getQuantity(){return this._quantity;}
    public String getImage(){return this._image;}
    public String getAddedOn(){return this._addedOn;}
}
