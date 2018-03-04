package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SparePartDetail extends SuperActivity {

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);

    String recordId;

    TextView vehicleTv,partNameNoTv,partDetailsTv,quantityTv,addedOnTv;
    ImageView partIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_part_detail);

        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        vehicleTv=(TextView)findViewById(R.id.vehicleTv);
        partNameNoTv=(TextView)findViewById(R.id.partNameNoTv);
        partDetailsTv=(TextView)findViewById(R.id.partDetailsTv);
        quantityTv=(TextView)findViewById(R.id.quantityTv);
        addedOnTv=(TextView)findViewById(R.id.addedOnTv);

        partIV=(ImageView)findViewById(R.id.partIV);
        //String dataId;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("sparePartRecordId")) {
                recordId = getIntent().getStringExtra("sparePartRecordId");
                //i=vehicleIdAry.indexOf(dataId);
                /*for (int l=0;i<vehicleIdAry.length;i++) {
                    if (vehicleIdAry[l].equals(dataId)) {
                        i= l;
                        break;
                    }
                }
                */

                SparePart spObj=dbHandler.getSparePartDetail(recordId);

                VehicleData vdObj=dbHandler.getVehicleData(spObj.getVehicleId());

                vehicleTv.setText("Vehicle: "+vdObj.getBrand()+" "+vdObj.getModel()+" ("+vdObj.getRegNumber()+")");
                partNameNoTv.setText("Part: "+spObj.getPartName()+" ( "+spObj.getPartNo()+" )");
                partDetailsTv.setText(spObj.getPartDetail());
                quantityTv.setText("Quantity: "+spObj.getQuantity());
                addedOnTv.setText("Added On: "+spObj.getAddedOn());

                //Toast.makeText(this, " DataId :"+recordId, Toast.LENGTH_SHORT).show();

                if(spObj.getImage().equals("1")){
                    String fileName = "spare_part_image_" + spObj.getVehicleId() + "_" + spObj.getId()+".jpg";
                    String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+appDataFolder+"/"+fileName;
                    File destination = new File(destinationPath);
                    show_image_file(partIV, destination);
                }
                //crntVId=dataId;
                //showVehicleData(dataId);

            }
        }else{
            /*
            dataId=vehicleIdAry[0];
            //crntVId=dataId;
            Toast.makeText(this, " DataId :"+dataId, Toast.LENGTH_SHORT).show();
            showVehicleData(dataId);
            */
        }

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SpareParts.class));
        //finish();
    }
}
