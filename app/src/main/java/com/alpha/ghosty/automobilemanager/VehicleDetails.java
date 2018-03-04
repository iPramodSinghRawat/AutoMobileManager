package com.alpha.ghosty.automobilemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleDetails extends SuperActivity
        implements SimpleGestureFilter.SimpleGestureListener {

    //String dataId=getIntent().getStringExtra("dataID");
    private TextView vehicleTypeTv,vehicleBrandTv,vehicleModelTv,vehicleRegNumberTv,
            vehicleDateOfPurchaseTv,vehicleTankCapacityTv,vehicleColorTv,
            vehicleFuelTypeTv,vehicleCommentTv,vehicleAddedOnTv;//,pageSubTitle;
    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    String[] vehicleIdAry;
    String crntVId;
    int dtLen;
    int i=0;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        dbHandler = new DBhandler(this, null, null, 1);
        //String dataId = getIntent().getStringExtra("dataId");
        //Toast.makeText(this, " DataId :"+dataId, Toast.LENGTH_SHORT).show();

        vehicleTypeTv = (TextView) findViewById(R.id.vehicleTypeTv);
        vehicleBrandTv = (TextView) findViewById(R.id.vehicleBrandTv);
        vehicleModelTv = (TextView) findViewById(R.id.vehicleModelTv);
        vehicleRegNumberTv = (TextView) findViewById(R.id.vehicleRegNumberTv);
        vehicleDateOfPurchaseTv = (TextView) findViewById(R.id.vehicleDateOfPurchaseTv);
        vehicleTankCapacityTv = (TextView) findViewById(R.id.vehicleTankCapacityTv);
        vehicleColorTv = (TextView) findViewById(R.id.vehicleColorTv);
        vehicleFuelTypeTv = (TextView) findViewById(R.id.vehicleFuelTypeTv);
        vehicleCommentTv = (TextView) findViewById(R.id.vehicleCommentTv);
        vehicleAddedOnTv = (TextView) findViewById(R.id.vehicleAddedOnTv);
        //pageSubTitle = (TextView) findViewById(R.id.pageSubTitle);
        //showVehicleData(dataId);
        ///Fetch List of vehicle id in an array
        vehicleIdAry=dbHandler.getDataIds("vehicle");
        dtLen=vehicleIdAry.length;
        //vehicleIdAry=dbHandler.getDataIds("fuel");

        String dataId;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("dataId")) {
                dataId = getIntent().getStringExtra("dataId");
                //i=vehicleIdAry.indexOf(dataId);
                for (int l=0;i<vehicleIdAry.length;i++) {
                    if (vehicleIdAry[l].equals(dataId)) {
                        i= l;
                        break;
                    }
                }
                //Toast.makeText(this, " Data-Id :"+dataId, Toast.LENGTH_SHORT).show();
                //crntVId=dataId;
                showVehicleData(dataId);
            }
        }else{
            dataId=vehicleIdAry[0];
            //crntVId=dataId;
            //Toast.makeText(this, " DataId :"+dataId, Toast.LENGTH_SHORT).show();
            showVehicleData(dataId);
        }
        detector = new SimpleGestureFilter(this,this);
    }
    //class mainCls=this;
    public void deleteVehicle(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processDeleteVehicle();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        //Toast.makeText(getBaseContext(), "No Clicked",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    public void processDeleteVehicle(){
        //crntVId
        String returnString=dbHandler.deleteVehicleData(crntVId);
        if(returnString.equals("1")){
            Toast.makeText(getBaseContext(),"Vehicle Data Deleted",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(),VehicleDetails.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getBaseContext(),returnString,Toast.LENGTH_LONG).show();
        }
    }

    public void redirectToEditVehicleData(View view){
        Intent intent = new Intent(getBaseContext(),EditVehicleDetails.class);
        intent.putExtra("vehicleId",crntVId);
        startActivity(intent);
        finish();
    }

    public void showVehicleData(String id){

        VehicleData object2=dbHandler.getVehicleData(id);
        crntVId=id;
        vehicleTypeTv.setText("Type: "+object2.getType());
        vehicleBrandTv.setText("Brand: "+object2.getBrand());
        vehicleModelTv.setText("Model: "+object2.getModel());
        vehicleRegNumberTv.setText("RegNumber: "+object2.getRegNumber());
        vehicleDateOfPurchaseTv.setText("Year of Purchase: "+date2Show(object2.getDateOfPurchase()));
        vehicleTankCapacityTv.setText("Fuel Tank Capacity: "+object2.getFuelTankCapacity());
        vehicleColorTv.setText("Color: "+object2.getColor());
        vehicleFuelTypeTv.setText("FuelType: "+object2.getFuelType());
        vehicleCommentTv.setText("Comment: "+object2.getComment());
        vehicleAddedOnTv.setText("AddedOn: "+object2.getAddedOn());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //View v = getCurrentFocus();
        //Toast.makeText(MainActivity.this, "I am Touched", Toast.LENGTH_LONG).show();
        this.detector.onTouchEvent(event);
        boolean ret = super.dispatchTouchEvent(event);
        return ret;
    }

    @Override
    public void onSwipe(int direction) {
        // String str = "";
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT :
                showPre();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :
                showNext();
                break;
            /*
            case SimpleGestureFilter.SWIPE_DOWN :
                //str = "Swipe Down";
                //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case SimpleGestureFilter.SWIPE_UP :
                //str = "Swipe Up";
                //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            */
        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void showPre(){
        int j=i-1;
        if(j<0){
            Toast.makeText(this, "Start of List", Toast.LENGTH_SHORT).show();
            i=0;
        }else{
            showVehicleData(vehicleIdAry[j]);
            i--;
        }
    }
    public void showNext(){
        int j=i+1;
        if(j>= dtLen){
            Toast.makeText(this, "End of List", Toast.LENGTH_SHORT).show();
            i= dtLen-1;
        }else{
            showVehicleData(vehicleIdAry[j]);
            i++;
        }
    }

    @Override
    public void onDoubleTap() {
        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
}
