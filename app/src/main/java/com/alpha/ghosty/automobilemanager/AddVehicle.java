package com.alpha.ghosty.automobilemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddVehicle extends SuperActivity{
                //implements AdapterView.OnItemSelectedListener{
    ArrayAdapter<String> vBrand;
    AlertDialog.Builder builderSingle;
    String slctVBrand,vDateOfPurchase="";//,slctVModel;
    //private DatePicker datePicker;
    private Calendar calendar;
    private TextView resultTv,vSlectedTBrandTv;//,vSlectedBrandTv;
    private EditText yearPurchaseET,vModelET,regNumberET,tankCapacityET,vColorET,fuelTypeET,commentET;
    RadioGroup vTypeRg;
    RadioButton vTypeRb;
    //private TextView yearPurchaseET;
    private int year, month, day;

    DBhandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        dbHandler = new DBhandler(this, null, null, 1);

        vTypeRg = (RadioGroup) findViewById(R.id.vTypeRg);
        vModelET = (EditText) findViewById(R.id.vModelET);
        regNumberET = (EditText) findViewById(R.id.regNumberET);
        yearPurchaseET = (EditText) findViewById(R.id.yearPurchaseET);
        tankCapacityET = (EditText) findViewById(R.id.tankCapacityET);
        vColorET = (EditText) findViewById(R.id.vColorET);
        fuelTypeET = (EditText) findViewById(R.id.fuelTypeET);
        commentET = (EditText) findViewById(R.id.commentET);
        resultTv = (TextView) findViewById(R.id.resultTv);
        vSlectedTBrandTv = (TextView) findViewById(R.id.vSlectedTBrandTv);

        vBrand = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);
        vBrand.add("Maruti"); vBrand.add("Suzuki"); vBrand.add("Honda"); vBrand.add("Tesla");
        vBrand.add("Toyota"); vBrand.add("Hyundai"); vBrand.add("Mahindra");
        vBrand.add("Yamaha"); vBrand.add("Bajaj"); vBrand.add("Hero");
        vBrand.add("TVS"); vBrand.add("Hindustan Motor");
        vBrand.add("other");

        //No Need of Below Calender Object Delete later
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        ///yearPurchaseET.setText(day + " / " + (month + 1) + " / " + year);
        yearPurchaseET.setKeyListener(null);
        vDateOfPurchase=year+"-"+(month + 1)+"-"+day;

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
                } else {}
            }
        });
    }

    public void selectVBrand(final View view){
        builderSingle = new AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Brand");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vSlectedTBrandTv.setText("Select Brand(Tap Here)");
                        slctVBrand=null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(vBrand,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        slctVBrand = vBrand.getItem(which);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        if(slctVBrand.equals("other")){
                            SeletdV(view);
                        }else{
                            vSlectedTBrandTv.setText("Selected Brand: " + slctVBrand+" (Tap To Change)");
                        }
                    }
                });
        builderSingle.show();
    }

    public void SeletdV(View view){
        //final String[] mText = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter Brand Name");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                slctVBrand = input.getText().toString();
                vSlectedTBrandTv.setText("Entered Brand: " + slctVBrand+" (Tap To Change)");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                slctVBrand=null;
                dialog.cancel();
            }
        });
        builder.show();
        //return mText[0];
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
        }
    };

    public void submitVehicleData(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processSubmitVehicleData();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data Confirm ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    /*Function; Submitting Vehicle Data To Database */
    public void processSubmitVehicleData(){
        /* Add Picture Option */

        String vType,vBrand,vModel,vRegNumber,tankCapacity,vColor,vFuelType,vComment;

        //usr_currency = (RadioGroup)findViewById(R.id.usr_currency);

        int selectedId = vTypeRg.getCheckedRadioButtonId();  // find the radiobutton by returned id
        vTypeRb = (RadioButton) findViewById(selectedId);
        vType = vTypeRb.getText().toString();

        vBrand=slctVBrand;

        vModel=vModelET.getText().toString();
        vRegNumber=regNumberET.getText().toString();
        //vDateOfPurchase=yearPurchaseET.getText().toString();
        tankCapacity=tankCapacityET.getText().toString();
        vColor=vColorET.getText().toString();
        vFuelType=fuelTypeET.getText().toString();
        vComment=commentET.getText().toString();

        if(vBrand==null){
            Toast.makeText(this, "Select or Enter A Brand ",Toast.LENGTH_SHORT).show();
        }else if (vModel.equals("")) {
            vModelET.setError("Vehicle Model Required!");
            vModelET.setHint("Please Model");
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
        }else if(dateDiffrence(year+"/"+month+"/"+day,vDateOfPurchase.replace("-","/"))<0){
            Toast.makeText(this, "Check Your Date ",Toast.LENGTH_SHORT).show();
        }else{

            /* Submit and Redirect To Preview Page with ID */
            VehicleData object=new VehicleData(vRegNumber,vBrand,vModel,vDateOfPurchase,tankCapacity,vType,vColor,vFuelType,vComment);

            //String rtn=dbHandler.putVehicleData(object);
            boolean rtn=dbHandler.putVehicleData(object);
            if(rtn==true){

                VehicleData object2=dbHandler.getVehicleData("0");
                /*
                resultTv.setText("Result:"+" \n Id: "+object2.getId()+" \n RegNumber: "+object2.getRegNumber()+" \n Brand: "+object2.getBrand()
                        +" \n Model: "+object2.getModel()+" \n Date of Purchase: "+object2.getDateOfPurchase()
                        +" \n Tank Capacity: "+object2.getFuelTankCapacity()+" \n Type: "+object2.getType()+
                        " \n Color: "+object2.getColor()+" \n FuelType: "+object2.getFuelType()+" \n Comment: "+object2.getComment()
                        +" \n Addedon: "+object2.getAddedOn());
                */
                Intent intent = new Intent(this, VehicleDetails.class);
                intent.putExtra("dataId",object2.getId());
                startActivity(intent);
                finish();
            }else{
                resultTv.setText("Error Try Again : "+ rtn);
            }

        }
    }
}
