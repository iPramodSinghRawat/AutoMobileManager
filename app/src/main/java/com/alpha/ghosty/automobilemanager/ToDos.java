package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ToDos extends SuperActivity {

    ListView toDosLV;

    DBhandler dbHandler;
    String [] toDosIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_dos);

        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        toDosLV = (ListView) findViewById(R.id.toDosLV);

        toDosIds=dbHandler.getToDoRecordIds();
        if(toDosIds==null){
            Toast.makeText(getBaseContext(), "No Records to Show",Toast.LENGTH_SHORT).show();
        }else{
            if(toDosIds.length>0){
                populateToDo();
            }else{
                Toast.makeText(getBaseContext(), "No Records to Show",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void redirect2AddNewToDo(View view){
        startActivity(new Intent(this,AddToDo.class));
        finish();
    }

    public void populateToDo(){
        ArrayList<String> idList = new ArrayList<String>();
        ArrayList<String> vehicleList = new ArrayList<String>();
        ArrayList<String> titleList = new ArrayList<String>();
        ArrayList<String> detailList = new ArrayList<String>();
        ArrayList<String> addedOnList = new ArrayList<String>();

        for(int i=0;i<toDosIds.length;i++){
            ToDo obj=dbHandler.getToDoDetail(toDosIds[i]);

            idList.add(obj.getId());

            VehicleData vdObj=dbHandler.getVehicleData(obj.getVehicleId());

            vehicleList.add(vdObj.getBrand()+" "+vdObj.getModel()+" ("+vdObj.getRegNumber()+")");

            //vehicleList.add(obj.getVehicleId());

            titleList.add(obj.getTitle());
            detailList.add(obj.getDetail());
            addedOnList.add(obj.getAddedOn());
        }

        toDosLV.setAdapter(new ToDoListAdapter(this,
                idList.toArray(new String[idList.size()]),
                vehicleList.toArray(new String[vehicleList.size()]),
                titleList.toArray(new String[titleList.size()]),
                detailList.toArray(new String[detailList.size()]),
                addedOnList.toArray(new String[addedOnList.size()])));
    }
}