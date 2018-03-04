package com.alpha.ghosty.automobilemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class FuelFillDataView extends SuperActivity
        implements SimpleGestureFilter.SimpleGestureListener {

    TextView vehicleDetailTv,typeOfFuelTv,odoMtrRdingTv,fillingDateTv,volumeTv,priceTv,receiptTv,addedOnTv;
    ImageView receiptView;
    //String[] fuelIdAry;
    FillingData[] object;
    String vehicleId;
    int dtLen=0,i=0;
    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    private SimpleGestureFilter detector;
    String crntRecordId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_fill_data_view);

        dbHandler = new DBhandler(this, null, null, 1);

        vehicleDetailTv = (TextView) findViewById(R.id.vehicleDetailTv);
        typeOfFuelTv = (TextView) findViewById(R.id.typeOfFuelTv);
        odoMtrRdingTv = (TextView) findViewById(R.id.odoMtrRdingTv);
        fillingDateTv = (TextView) findViewById(R.id.fillingDateTv);
        volumeTv = (TextView) findViewById(R.id.volumeTv);
        priceTv = (TextView) findViewById(R.id.priceTv);
        receiptTv = (TextView) findViewById(R.id.receiptTv);
        addedOnTv = (TextView) findViewById(R.id.addedOnTv);
        receiptView = (ImageView) findViewById(R.id.receiptView);

        vehicleId=getIntent().getStringExtra("vehicleId");
        object=dbHandler.fuelFillingData(vehicleId);
        dtLen=object.length;

        VehicleData object2=dbHandler.getVehicleData(vehicleId);
        vehicleDetailTv.setText(object2.getBrand()+" "+object2.getModel()+" ("+object2.getRegNumber()+")");

        detector = new SimpleGestureFilter(this,this);

        showFuelFillData(i);//here i=0;
    }

    public void showFuelFillData(int index){
        crntRecordId=object[index].getId();
        //vehicleDetailTv.setText("Vehicle: " + object[index].getVehicleId());/* Put Vehicle Dat a Here */

        typeOfFuelTv.setText("Type of Fuel: "+ object[index].getFuelType());
        odoMtrRdingTv.setText("Filled at Meter Reading: "+ object[index].getMeterReading());
        fillingDateTv.setText("Fill On : "+ date2Show(object[index].getDate()));
        volumeTv.setText("Volume: "+ object[index].getVolume());
        priceTv.setText("Paid: "+ object[index].getPrice());
        receiptTv.setText("Receipt: "+ object[index].getReceipt());
        addedOnTv.setText("Data Added On : " + object[index].getAddedOn());

        if (object[index].getReceipt().equals("0")) {
            receiptTv.setText("Receipt: N/A ");
            receiptView.setImageDrawable(null);
        }else {
            receiptTv.setText("Receipt: Yes");
            String fileName = "fuel_filling_receipt_" + object[index].getVehicleId() + "_" + object[index].getId()+".jpg";
            //vehicleFuelTypeTv.setText("FuelType: "+object.getFuelType());
            //String filename=file_name+".jpg";
            String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+appDataFolder+"/"+fileName;
            File destination = new File(destinationPath);
            show_image_file(receiptView, destination);
        }
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
    }

    public void showPre(){
        int j=i-1;
        if(j<0){
            Toast.makeText(this, "Start of List", Toast.LENGTH_SHORT).show();
            i=0;
        }
        else{
            showFuelFillData(j);
            i--;
        }
    }
    public void showNext(){
        int j=i+1;
        if(j>= dtLen){
            Toast.makeText(this, "End of List", Toast.LENGTH_SHORT).show();
            i= dtLen-1;
        }
        else{
            showFuelFillData(j);
            i++;
        }
    }

    @Override
    public void onDoubleTap() {

        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
        //processSubmitInsuranceData();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processDeleteData("fuel");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Confirm To Delete This Record ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void processDeleteData(String recordType){

        String returnString=dbHandler.deleteData(recordType,crntRecordId);
        if(returnString.equals("1")){
            Toast.makeText(getBaseContext(),"Record Deleted",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(),ShowRecords.class);
            intent.putExtra("vehicleId",vehicleId);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getBaseContext(),returnString,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        if(getIntent().getExtras().containsKey("caller")) {
            Intent intent = new Intent(getBaseContext(),ShowRecords.class);
            intent.putExtra("vehicleId",vehicleId);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getBaseContext(),MenuPage.class);
            startActivity(intent);
            finish();
        }
    }
}
