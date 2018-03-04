package com.alpha.ghosty.automobilemanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import java.util.Date;

public class EditVehicleDetails extends SuperActivity {
    /*
    private EditText vehicleTypeET,vehicleBrandTv,vehicleModelTv,vehicleRegNumberTv,vehicleDateOfPurchaseTv,vehicleTankCapacityTv,vehicleColorTv,
            vehicleFuelTypeTv,vehicleCommentTv,vehicleAddedOnTv;//,pageSubTitle;
    */
    String vehicleId,vType,vBrand,vDateOfPurchase="";//,vModel
    private TextView vehicleTypeTv,vehicleBrandTv,resultTv;
    private EditText modelET,regNumberET,yearPurchaseET,tankCapacityET,vColorET,fuelTypeET,commentET;
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle_details);

        dbHandler = new DBhandler(this, null, null, 1);

        resultTv = (TextView) findViewById(R.id.resultTv);
        //vTypeRg = (RadioGroup) findViewById(R.id.vTypeRg);
        vehicleTypeTv = (TextView) findViewById(R.id.vehicleTypeTv);
        vehicleBrandTv = (TextView) findViewById(R.id.vehicleBrandTv);
        modelET = (EditText) findViewById(R.id.modelET);
        regNumberET = (EditText) findViewById(R.id.regNumberET);
        yearPurchaseET = (EditText) findViewById(R.id.yearPurchaseET);
        tankCapacityET = (EditText) findViewById(R.id.tankCapacityET);
        vColorET = (EditText) findViewById(R.id.vColorET);
        fuelTypeET = (EditText) findViewById(R.id.fuelTypeET);
        commentET = (EditText) findViewById(R.id.commentET);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "No Vehicle Data to Process, Redirecting to List ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(),VehicleDetails.class);
            startActivity(intent);
            finish();
        }
        else{
            vehicleId = getIntent().getStringExtra("vehicleId");
            Toast.makeText(this, "Vehicle Data to Process with ID: "+ vehicleId, Toast.LENGTH_SHORT).show();
            VehicleData object=dbHandler.getVehicleData(vehicleId);

            vDateOfPurchase=object.getDateOfPurchase();

            String dateArray2Show[] = vDateOfPurchase.split("-");
            //vDateOfPurchase=dateArray[0]+"-"+dateArray[1]+"-"+dateArray[2];
            String vDateOfPurchaseShow=date2Show(vDateOfPurchase);// dateArray2Show[0]+"/"+dateArray2Show[1]+"/"+dateArray2Show[2];
            //String vDateOfPurchaseShow=vDateOfPurchase;

            year = Integer.parseInt(dateArray2Show[0]);
            month = Integer.parseInt(dateArray2Show[1]);
            day = Integer.parseInt(dateArray2Show[2]);

            vType=object.getType();
            vBrand=object.getBrand();

            vehicleTypeTv.setText("Type: "+vType);
            vehicleBrandTv.setText("Brand: " +vBrand);

            modelET.setText(object.getModel());
            regNumberET.setText(object.getRegNumber());
            //yearPurchaseET.setText(object.getDateOfPurchase());
            yearPurchaseET.setText(vDateOfPurchaseShow);
            tankCapacityET.setText(object.getFuelTankCapacity());
            vColorET.setText(object.getColor());
            fuelTypeET.setText(object.getFuelType());
            commentET.setText(object.getComment());
        }


        //year = calendar.getY.YEAR;
        //month = Calendar.MONTH;
        //day = Calendar.DAY_OF_MONTH;

        yearPurchaseET.setClickable(true);
        yearPurchaseET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        yearPurchaseET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    showDialog(0);
                } else {
                }
            }
        });
    }

    public void redirectToVehicleList(View view){
        Intent intent = new Intent(getBaseContext(),VehicleDetails.class);
        intent.putExtra("dataId",vehicleId);
        startActivity(intent);
        finish();
    }

    public void updateVehicleData(View view){
        /* Add Picture Option */

        String vModel,vRegNumber,tankCapacity,vColor,vFuelType,vComment;

        vModel=modelET.getText().toString();
        vRegNumber=regNumberET.getText().toString();
        //vDateOfPurchase=yearPurchaseET.getText().toString();
        tankCapacity=tankCapacityET.getText().toString();
        vColor=vColorET.getText().toString();
        vFuelType=fuelTypeET.getText().toString();
        vComment=commentET.getText().toString();

        if(vBrand==null){
            Toast.makeText(this, "Select or Enter A Brand ",Toast.LENGTH_SHORT).show();
        }else if (vModel.equals("")) {
            modelET.setError("Vehicle Model Required!");
            modelET.setHint("Please Model");
        }else if (vRegNumber.equals("")) {
            regNumberET.setError("RegNumber(License Plate) is required!");
            regNumberET.setHint("Please enter Vehicle RegNumber(License Plate Number)");
        }else if (vDateOfPurchase.equals("")) {
            yearPurchaseET.setError("Select Date of Purchase");
            yearPurchaseET.setHint("Select Date of Purchase (Tap Here)");
        }else if (tankCapacity.equals("")) {
            tankCapacityET.setError("Tank Capacity is required!");
            tankCapacityET.setHint("Please Enter Fuel Tank Capacity of Vehicle ");
        }else if (vColor.equals("")) {
            vColorET.setError("Color Required !");
            vColorET.setHint("Please Enter Color ");
        }else if (vFuelType.equals("")) {
            fuelTypeET.setError("Fuel Type is required!");
            fuelTypeET.setHint("Please enter Fuels Used By Vehicle");
        }else if(vBrand==null || vBrand.equals("")){
            resultTv.setText("Select Brand !!! ");
            Toast.makeText(this, " Select Brand !!!  ", Toast.LENGTH_SHORT).show();
        }else{
/*
            Toast.makeText(this, "dateDiffrence "+
                            year+"/"+month+"/"+day+"-"+vDateOfPurchase.replace("-","/")+", "+
                    dateDiffrence(year+"/"+month+"/"+day,vDateOfPurchase.replace("-","/")),Toast.LENGTH_SHORT).show();
*/
            DBhandler dbHandler = new DBhandler(this, null, null, 1);
            /* Submit and Redirect To Preview Page with ID */
            VehicleData object=new VehicleData(vehicleId,vRegNumber,vBrand,vModel,vDateOfPurchase,tankCapacity,vType,vColor,vFuelType,vComment);

            //String rtn=dbHandler.updateVehicleData(object,vehicleId);
            boolean rtn=dbHandler.updateVehicleData(object);
            //if(rtn.equals("1")){
            if(rtn==true){
                //VehicleData object2=dbHandler.getVehicleData(vehicleId);
                /*
                resultTv.setText("Result:"+" \n Id: "+object2.getId()+" \n RegNumber: "+object2.getRegNumber()+" \n Brand: "+object2.getBrand()
                        +" \n Model: "+object2.getModel()+" \n Date of Purchase: "+object2.getDateOfPurchase()
                        +" \n Tank Capacity: "+object2.getFuelTankCapacity()+" \n Type: "+object2.getType()+
                        " \n Color: "+object2.getColor()+" \n FuelType: "+object2.getFuelType()+" \n Comment: "+object2.getComment()
                        +" \n Addedon: "+object2.getAddedOn());
                */
                Intent intent = new Intent(this, VehicleDetails.class);
                intent.putExtra("dataId",vehicleId);
                startActivity(intent);
                finish();
            }else{
                resultTv.setText("Error Try Again : "+ rtn);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);  //date is dateSetListener as per your code in question
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePickerDialog;
        //return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            yearPurchaseET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
            vDateOfPurchase=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;
            //Toast.makeText(getApplicationContext(), "vDateOfPurchase "+vDateOfPurchase,Toast.LENGTH_SHORT).show();
        }
    };
}
