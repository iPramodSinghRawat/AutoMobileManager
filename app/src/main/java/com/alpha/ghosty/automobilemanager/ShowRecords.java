package com.alpha.ghosty.automobilemanager;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowRecords extends SuperActivity {

    ArrayAdapter<String> serviceType,userVehicleLists;
    android.app.AlertDialog.Builder builderSingle;
    VehicleData[] objectAry;

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    String vehicleId="";
    TextView lastFillRecordTv,lastServiceRecordTv,lastOExpenseRecordTv,lastTripRecordTv,lastInsuranceRecordTv,selectVehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);

        dbHandler = new DBhandler(this, null, null, 1);

        selectVehicle = (TextView) findViewById(R.id.selectVehicle);

        lastFillRecordTv = (TextView) findViewById(R.id.lastFillRecordTv);
        lastServiceRecordTv = (TextView) findViewById(R.id.lastServiceRecordTv);
        lastOExpenseRecordTv = (TextView) findViewById(R.id.lastOExpenseRecordTv);
        lastTripRecordTv = (TextView) findViewById(R.id.lastTripRecordTv);
        lastInsuranceRecordTv = (TextView) findViewById(R.id.lastInsuranceRecordTv);

        objectAry=dbHandler.userVehicleDetails();
        int dtLen=objectAry.length;
        userVehicleLists = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);
        // userVehicleLists.add("TVS"); userVehicleLists.add("Hindustan Motor");

        for(int l=0;l<dtLen;l++){
            userVehicleLists.add(objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+") Type:"+objectAry[l].getType());
        }

        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().containsKey("vehicleId")){
                vehicleId=getIntent().getStringExtra("vehicleId");
                fetchLastRecords(vehicleId);
            }
        }else if(dtLen<2){
            vehicleId=objectAry[0].getId();
            fetchLastRecords(vehicleId);
        }else{selectVehicle(null);}

    }

    public void selectVehicle(View view){
        builderSingle = new android.app.AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Vehicle");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectVehicle.setText("Select Vehicle (Tap Here)");
                        vehicleId = null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(userVehicleLists,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vehicleId = objectAry[which].getId();//.getItem(which);
                        //slctVId = vehicleIdAry[which];//.getItem(which);
                        fetchLastRecords(vehicleId);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                    }
                });
        builderSingle.show();
    }

    public void fetchLastRecords(String vehicleId){
        String tapOption="";

        VehicleData objectvd=dbHandler.getVehicleData(vehicleId);
        FillingData objectfd=dbHandler.getLastFillingData(vehicleId);
        ServiceData objectsd=dbHandler.getLastServiceData(vehicleId);
        ExpenseData objected=dbHandler.getLastOExpenseData(vehicleId);
        TripData objecttd=dbHandler.getTripData(vehicleId);

        InsuranceData objectid=dbHandler.getLastInsuranceData(vehicleId);

        //public InsuranceData getLastInsuranceData(String vehicleId){

        selectVehicle.setText(objectvd.getBrand()+" "+objectvd.getModel()+" ("+objectvd.getRegNumber()+")");

        tapOption="\n Tap Here to Get All Filling Records of This Vehicle";
        if(objectfd.getAddedOn().equals("NA")){
            lastFillRecordTv.setClickable(false);
            tapOption="";
        }else{
            lastFillRecordTv.setClickable(true);
        }

        lastFillRecordTv.setText("at MeterReading: " + objectfd.getMeterReading()
                + "\n On Date: " + date2Show(objectfd.getDate())
                + "\n Volume: " + objectfd.getVolume()
                + "\n Price: " + objectfd.getPrice()
                + "\n Record Added On: " + objectfd.getAddedOn()
                + tapOption);

        tapOption="\n Tap Here to Get All Service Records of This Vehicle";
        if(objectsd.getAddedOn().equals("NA")){
            lastServiceRecordTv.setClickable(false);
            tapOption="";
        }else{
            lastServiceRecordTv.setClickable(true);
        }
        lastServiceRecordTv.setText("at MeterReading: " + objectsd.getMeterReading()
                + "\n Service Type: " + objectsd.getServiceType()
                + "\n On Date: " + date2Show(objectsd.getDate())
                + "\n Price: " + objectsd.getPrice()
                + "\n Record Added On: " + objectsd.getAddedOn()
                + tapOption);

        tapOption="\n Tap Here to Get All Other Expense Records of This Vehicle";
        if(objected.getAddedOn().equals("NA")){
            lastOExpenseRecordTv.setClickable(false);
            tapOption="";
        }else{
            lastOExpenseRecordTv.setClickable(true);
        }
        lastOExpenseRecordTv.setText("at MeterReading: " + objected.getMeterReading()
                + "\n Service Type: " + objected.getExpenseType()
                + "\n On Date: " + date2Show(objected.getDate())
                + "\n Price: " + objected.getPrice()
                + "\n Record Added On: " + objected.getAddedOn()
                + tapOption);

        tapOption="\n Tap Here to Get All Trip Records of This Vehicle";
        if(objecttd.getAddedOn().equals("NA")){
            lastTripRecordTv.setClickable(false);
            tapOption="";
        }else{
            lastTripRecordTv.setClickable(true);
        }

        lastTripRecordTv.setText("Location Departure: " + objecttd.getLocationDeparture()
                + "\n DateOfDeparture: " + date2Show(objecttd.getDateOfDeparture())
                + "\n MeterReading Departure: " + objecttd.getMeterReadingDeparture()
                + "\n CostOfTrip: " + objecttd.getCostOfTrip()
                + "\n Record Added On: " + objecttd.getAddedOn()
                + tapOption);

        tapOption="\n Tap Here to Get All Trip Records of This Vehicle";

        if(objectid.getAddedOn().equals("NA")){
            lastInsuranceRecordTv.setClickable(false);
            tapOption="";
        }else{
            lastInsuranceRecordTv.setClickable(true);
        }

        lastInsuranceRecordTv.setText("Type: " + objectid.getType()
                + "\n Meter Reading: " + objectid.getMeterReading()
                + "\n Begin On: " + date2Show(objectid.getDateBegin())
                + "\n Expire: " + date2Show(objectid.getDateEnd())
                + "\n Cost: " + objectid.getCost()
                + "\n Record Added On: " + objectid.getAddedOn()
                + tapOption);
    }

    public void redirect2FuelFillView(View view){
        if(vehicleId.equals("") || vehicleId==null){
            selectVehicle.setText("Select A Vehicle First (Tap Hre)");
        }else{
            Intent intent = new Intent(this, FuelFillDataView.class);
            intent.putExtra("vehicleId",vehicleId);
            intent.putExtra("caller","ShowRecords");
            startActivity(intent);
            finish();
        }
    }
    public void redirect2ServiceView(View view){
        if(vehicleId.equals("") || vehicleId==null){
            selectVehicle.setText("Select A Vehicle First (Tap Hre)");
        }else{
            Intent intent = new Intent(this, ServiceDataView.class);
            intent.putExtra("vehicleId",vehicleId);
            intent.putExtra("caller","ShowRecords");
            startActivity(intent);
            finish();
        }
    }
    public void redirect2oExpenseView(View view){
        if(vehicleId.equals("") || vehicleId==null){
            selectVehicle.setText("Select A Vehicle First (Tap Hre)");
        }else{
            Intent intent = new Intent(this, OtherExpensesView.class);
            intent.putExtra("vehicleId",vehicleId);
            intent.putExtra("caller","ShowRecords");
            startActivity(intent);
            finish();
        }
    }

    public void redirect2TripView(View view){
        if(vehicleId.equals("") || vehicleId==null){
            selectVehicle.setText("Select A Vehicle First (Tap Hre)");
        }else{
            Intent intent = new Intent(this, TripRecordView.class);
            intent.putExtra("vehicleId",vehicleId);
            intent.putExtra("caller","ShowRecords");
            startActivity(intent);
            finish();
        }
    }

    public void redirect2InsuranceView(View view){
        if(vehicleId.equals("") || vehicleId==null){
            selectVehicle.setText("Select A Vehicle First (Tap Hre)");
        }else{
            Intent intent = new Intent(this, InsuranceRecordView.class);
            intent.putExtra("vehicleId",vehicleId);
            intent.putExtra("caller","ShowRecords");
            startActivity(intent);
            finish();
        }
    }
}
