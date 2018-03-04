package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpareParts extends SuperActivity {

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);

    String[] spRecordIds;
    ListView sparePartsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_parts);

        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        sparePartsLV = (ListView) findViewById(R.id.sparePartsLV);
        ///*
        spRecordIds=dbHandler.getSparePartRecordIds();

        if(spRecordIds==null){
            Toast.makeText(getBaseContext(), "No Records to Show",Toast.LENGTH_SHORT).show();
        }else{
            if(spRecordIds.length>0){
                populateSpareParts();
                //Toast.makeText(getBaseContext(), "No of Records "+spRecordIds.length,Toast.LENGTH_SHORT).show();
                //SparePart obj=dbHandler.getSparePartDetail(spRecordIds[0]);
                //Toast.makeText(getBaseContext(), "Image "+obj.getImage(),Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "No Records to Show",Toast.LENGTH_SHORT).show();
            }
        }
       // */
    }

    public void redirect2AddNewSparePart(View view){
        startActivity(new Intent(this,AddSparePart.class));
        finish();
    }

    public void populateSpareParts(){
        //Cursor reminderList=dbHandler.getReminderData(null);

        ArrayList<String> idList = new ArrayList<String>();
        ArrayList<String> vehicleList = new ArrayList<String>();
        ArrayList<String> partNoList = new ArrayList<String>();
        ArrayList<String> partNameList = new ArrayList<String>();
        ArrayList<String> partDetailList = new ArrayList<String>();
        ArrayList<String> quantityList = new ArrayList<String>();
        ArrayList<String> imageList = new ArrayList<String>();
        ArrayList<String> addedOnList = new ArrayList<String>();

        for(int i=0;i<spRecordIds.length;i++){
            SparePart obj=dbHandler.getSparePartDetail(spRecordIds[i]);

            idList.add(obj.getId());

            VehicleData vdObj=dbHandler.getVehicleData(obj.getVehicleId());

            vehicleList.add(vdObj.getBrand()+" "+vdObj.getModel()+" ("+vdObj.getRegNumber()+")");

            partNoList.add(obj.getPartNo());
            partNameList.add(obj.getPartName());
            partDetailList.add(obj.getPartDetail());
            quantityList.add(obj.getQuantity());
            imageList.add(obj.getImage());
            addedOnList.add(obj.getAddedOn());

        }

        sparePartsLV.setAdapter(new SparePartListAdapter(this,
                idList.toArray(new String[idList.size()]),
                vehicleList.toArray(new String[vehicleList.size()]),
                partNoList.toArray(new String[partNoList.size()]),
                partNameList.toArray(new String[partNameList.size()]),
                partDetailList.toArray(new String[partDetailList.size()]),
                quantityList.toArray(new String[quantityList.size()]),
                imageList.toArray(new String[imageList.size()]),
                addedOnList.toArray(new String[addedOnList.size()])));

    }
}
