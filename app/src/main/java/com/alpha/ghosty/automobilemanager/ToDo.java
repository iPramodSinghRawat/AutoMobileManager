package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 08-11-2016.
 */

public class ToDo{
    String id,vehicleId,title,detail,addedOn;

    public ToDo(){}

    public ToDo(String idV,String vehicleIdV,String titleV,String detailV,String addedOnV){
        this.id=idV;
        this.vehicleId=vehicleIdV;
        this.title=titleV;
        this.detail=detailV;
        this.addedOn=addedOnV;
    }

    public ToDo(String vehicleIdV,String titleV,String detailV,String addedOnV){
        this.vehicleId=vehicleIdV;
        this.title=titleV;
        this.detail=detailV;
        this.addedOn=addedOnV;
    }

    public ToDo(String vehicleIdV,String titleV,String detailV){
        this.vehicleId=vehicleIdV;
        this.title=titleV;
        this.detail=detailV;
    }

    public String getId(){return this.id;}
    public String getVehicleId(){return this.vehicleId;}
    public String getTitle(){return this.title;}
    public String getDetail(){return this.detail;}
    public String getAddedOn(){return this.addedOn;}

}
