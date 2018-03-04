package com.alpha.ghosty.automobilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPage extends SuperActivity {
    DBhandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        //DBhandler dbHandler = new DBhandler(getApplicationContext(), null, null, 1);
        dbHandler = new DBhandler(this, null, null, 1);

        int uChk=dbHandler.check_user();
        //Toast.makeText(this, Integer.toString(uChk), Toast.LENGTH_SHORT).show();
        //Intent intent;
        if(uChk==0){
            //intent = new Intent(getApplicationContext(),UserRegistration.class);
            startActivity(new Intent(getApplicationContext(),UserRegistration.class));
            finish();
        }

        //dbHandler.createInsertFuelType();
        //dbHandler.reCreateFuelFilling_Table();
    }

    public void redirect2AddVehicle(View view){

        //Intent intent = new Intent(getBaseContext(),AddVehicle.class);
        startActivity(new Intent(getBaseContext(),AddVehicle.class));
        finish();

    }

    public void redirect2AddServiceData(View view){

        //Intent intent = new Intent(getBaseContext(),AddServiceData.class);
        startActivity(new Intent(getBaseContext(),AddServiceData.class));
        finish();

    }

    public void redirect2AddFuelData(View view){

        //Intent intent = new Intent(getBaseContext(),AddFuelFillData.class);
        startActivity(new Intent(getBaseContext(),AddFuelFillData.class));
        finish();

    }

    public void redirect2AddOtherData(View view){

        //Intent intent = new Intent(getBaseContext(),AddOtherExpenses.class);
        startActivity(new Intent(getBaseContext(),AddOtherExpenses.class));
        finish();

    }

    public void redirect2AddTripData(View view){

        //Intent intent = new Intent(getBaseContext(),AddTripRecord.class);
        startActivity(new Intent(getBaseContext(),AddTripRecord.class));
        finish();

    }

    public void redirect2AddInsurance(View view){


        //Intent intent = new Intent(getBaseContext(),AddInsurance.class);
        startActivity(new Intent(getBaseContext(),AddInsurance.class));
        finish();


    }

/*
    public void redirect2UserSetting(View view){

        Intent intent = new Intent(getBaseContext(),UserSetting.class);
        startActivity(intent);
        finish();

    }
*/
    public void redirect2VehicleDetail(View view){
        //Intent intent = new Intent(getBaseContext(),VehicleDetails.class);
        startActivity(new Intent(getBaseContext(),VehicleDetails.class));
        finish();
    }

    public void redirect2Statistics(View view){
        //Intent intent = new Intent(getBaseContext(),Statistics.class);
        startActivity(new Intent(getBaseContext(),Statistics.class));
        finish();
    }

    public void redirectShowRecords(View view){
        //Intent intent = new Intent(getBaseContext(),ShowRecords.class);
        startActivity(new Intent(getBaseContext(),ShowRecords.class));
        finish();
    }

    public void redirect2Reminders(View view){
        //Intent intent = new Intent(getBaseContext(),Reminders.class);
        startActivity(new Intent(getBaseContext(),Reminders.class));
        finish();
    }

    public void redirect2OtherOptions(View view){
        //Intent intent = new Intent(getBaseContext(),OtherOptions.class);
        startActivity(new Intent(getBaseContext(),OtherOptions.class));
        finish();
    }

}
