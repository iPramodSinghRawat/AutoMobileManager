package com.alpha.ghosty.automobilemanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.AlertDialog.Builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddTripRecord extends SuperActivity {

    /* TODO:
    Check DateTime or Depature with Date and Time of Retrun of LAst Trip record By Converting Date Time in java milliseconds
    Checking trip record Never Enter Before Purchase Date
    */

    EditText departureLocationET,odoMeterDepartureET,dateOfDepartureET,timeOfDepartureET,
            returnLocationET,odoMeterReturnET,dateOfReturnET,timeOfReturnET,priceET;

    RadioGroup receiptOptRG;
    RadioButton picOption,takePhoto;

    private Calendar calendar;
    private int year, month, day,hour,min;//,sec;
    ImageView receiptView;
    TextView selectVehicle,lastTripTv,typeOfTripTV,resultTv;

    int EtId=0;

    String vehicleId="",typeOfTrip,departureLocation,odoMeterDeparture,dateOfDeparture,timeOfDeparture,
            returnLocation,odoMeterReturn,dateOfReturn,timeOfReturn,price,picOptn="0",
            lastTripDepatureReading="",lastTripReturnReading="",lastTripDepatureDate="0/0/0",
            lastTripReturnDate="0/0/0",lastDataType="",lastDataReading="0",lastDataDate="0/0/0",
            vDateOfPurchase="";

    ArrayAdapter<String> tripType,userVehicleLists;
    Builder builderSingle;
    VehicleData[] objectAry;

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_record);
        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        departureLocationET = (EditText) findViewById(R.id.departureLocationET);
        odoMeterDepartureET = (EditText) findViewById(R.id.odoMeterDepartureET);
        dateOfDepartureET = (EditText) findViewById(R.id.dateOfDepartureET);
        timeOfDepartureET = (EditText) findViewById(R.id.timeOfDepartureET);

        returnLocationET = (EditText) findViewById(R.id.returnLocationET);
        odoMeterReturnET = (EditText) findViewById(R.id.odoMeterReturnET);
        dateOfReturnET = (EditText) findViewById(R.id.dateOfReturnET);
        timeOfReturnET = (EditText) findViewById(R.id.timeOfReturnET);

        priceET = (EditText) findViewById(R.id.priceET);

        receiptOptRG = (RadioGroup) findViewById(R.id.receiptOptRG);
        takePhoto = (RadioButton) findViewById(R.id.takePhoto);

        selectVehicle = (TextView) findViewById(R.id.selectVehicle);
        lastTripTv = (TextView) findViewById(R.id.lastTripTv);
        typeOfTripTV = (TextView) findViewById(R.id.typeOfTripTV);

        resultTv = (TextView) findViewById(R.id.resultTv);
        receiptView = (ImageView) findViewById(R.id.receiptView);

        tripType = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);

        tripType.add("Adventure");
        tripType.add("Business");
        tripType.add("Personal");
        tripType.add("other");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //dateOfDepartureET.setText(day + " / " + (month + 1) + " / " + year+ " (Tap Here To Change)");
        //dateOfDepartureET.setText((day-1) + " / " + (month + 1) + " / " + year);
        //dateOfDeparture=year+"-"+(month + 1)+"-"+(day-1);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        //timeOfDepartureET.setText(pad(hour)+":"+pad(min)+":"+pad(calendar.get(Calendar.SECOND)));

        //dateOfReturnET.setText(day + " / " + (month + 1) + " / " + year + " (Tap Here To Change)");
        //dateOfReturnET.setText(day + " / " + (month + 1) + " / " + year);
        //dateOfReturn=year+"-"+(month + 1)+"-"+day;

        dateOfDepartureET.setKeyListener(null);
        dateOfDepartureET.setClickable(true);
        dateOfDepartureET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                EtId=v.getId();
                showDialog(v.getId());
            }
        });
        dateOfDepartureET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EtId=v.getId();
                    showDialog(v.getId());
                } else {
                }
            }
        });

        timeOfDepartureET.setKeyListener(null);
        timeOfDepartureET.setClickable(true);
        timeOfDepartureET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                EtId=v.getId();
                showDialog(v.getId());
            }
        });
        timeOfDepartureET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    EtId=v.getId();
                    showDialog(v.getId());
                } else {
                }
            }
        });


        dateOfReturnET.setKeyListener(null);
        dateOfReturnET.setClickable(true);
        dateOfReturnET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                EtId=v.getId();
                showDialog(v.getId());
            }
        });
        dateOfReturnET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EtId=v.getId();
                    showDialog(v.getId());
                } else {
                }
            }
        });

        timeOfReturnET.setKeyListener(null);
        timeOfReturnET.setClickable(true);
        timeOfReturnET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                EtId=v.getId();
                showDialog(v.getId());
            }
        });
        timeOfReturnET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EtId=v.getId();
                    showDialog(v.getId());
                } else {
                }
            }
        });

        objectAry=dbHandler.userVehicleDetails();
        int dtLen=objectAry.length;
        userVehicleLists = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item);
        // userVehicleLists.add("TVS"); userVehicleLists.add("Hindustan Motor");

        for(int l=0;l<dtLen;l++){
            userVehicleLists.add(objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+") Type:"+objectAry[l].getType());
        }

        if(dtLen==1){
            vehicleId = objectAry[0].getId();//.getItem(which);
            vDateOfPurchase = objectAry[0].getDateOfPurchase();//.getItem(which);
            //fuelCapacity=objectAry[0].getFuelTankCapacity();//.getItem(which);
            //slctVId = vehicleIdAry[which];//.getItem(which);
            fetchLastTrip(vehicleId);// to fetch last record of trip
            //usrCurrency=getResources().getString(currencySymb[which]);
            selectVehicle.setText(userVehicleLists.getItem(0) + "");
        }else{selectVehicle(null);}
    }


    /* Function: To Select Vehicle For Which Service Done */
    public void selectVehicle(View view){
        builderSingle = new Builder(this);
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
                        fetchLastTrip(vehicleId);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                    }
                });
        builderSingle.show();
    }

    /* Function: to Fetch Last Filling Details */
    public void fetchLastTrip(String vehicleId){

        String[] lastData;//=new String[4];

        lastData=dbHandler.getLastData(vehicleId);
        //String dataType=lastData[0];
        lastDataType=lastData[0];
        lastDataReading=lastData[1];
        lastDataDate=lastData[2];
        //String pricePaid=lastData[3];
        lastTripTv.setText("Last Record:\n Type:"+ lastData[0] +
                "\n at:"+ lastDataReading +
                "\n Date: " + date2Show(lastDataDate) +
                "\n Paid: " + lastData[3]);

        TripData objectt=dbHandler.getTripData(vehicleId);
        //FillingData objectt=dbHandler.getLastFillingData(vehicleId);
        lastTripDepatureReading=objectt.getMeterReadingDeparture();
        lastTripReturnReading=objectt.getMeterReadingReturn();
        lastTripDepatureDate=objectt.getDateOfDeparture();
        lastTripReturnDate=objectt.getDateOfReturn();
        //lastTripTv.setText("Last Trip Done Ended at: " + lastReading);

        lastTripTv.setText(lastTripTv.getText()+"\n\n Last Trip Done Start at: "+lastTripDepatureReading+" Ended at:" + lastTripReturnReading +
                "\n Date: From " + date2Show(lastTripDepatureDate)+" To: " + date2Show(lastTripReturnDate));

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lastTripDepatureReading", lastTripDepatureReading); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastTripReturnReading", lastTripReturnReading); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastTripDepatureDate", lastTripDepatureDate); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastTripReturnDate", lastTripReturnDate); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastDataReading", lastDataReading); /*Saving Data To Protect Change on Camera Intent*/
        editor.putString("lastDataDate", lastDataDate); /*Saving Data To Protect Change on Camera Intent*/
        editor.putString("vehicleId", vehicleId); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("vDateOfPurchase", vDateOfPurchase); /*Saving Data To Protect Change on Camera Intent*/
        editor.commit();
    }

    public void selectTripType(final View view){

        //Toast.makeText(this, "You Set: ", Toast.LENGTH_LONG).show();
        //usrCurrencyTxt.setText("Rs hhgfd");
        builderSingle = new Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Trip Type");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOfTripTV.setText("Select Trip Type (Tap Here)");
                        typeOfTrip = null;
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(tripType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOfTrip = tripType.getItem(which);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        if (typeOfTrip.equals("other")) {
                            enterTrip(view);
                        } else {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("typeOfTrip", typeOfTrip); ///*Saving Data To Protect Change on Camera Intent/
                            editor.commit();
                            typeOfTripTV.setText("Trip Type: " + typeOfTrip + " (Tap To Change)");
                        }
                    }
                });
        builderSingle.show();
    }
    public void enterTrip(View view){
        //final String[] mText = new String[1];
        //android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builderSingle = new Builder(this);

        builderSingle.setTitle("Enter Service Done");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builderSingle.setView(input);
        // Set up the buttons
        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                typeOfTrip = m_Text;
                typeOfTripTV.setText("Trip Type: " + typeOfTrip + " (Tap To Change)");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("typeOfTrip", typeOfTrip); ///*Saving Data To Protect Change on Camera Intent/
                editor.commit();
            }
        });
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //typeOfTrip = null;
                dialog.cancel();
            }
        });
        builderSingle.show();
        //return mText[0];
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void submitTripRecords(View view){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        //Toast.makeText(getBaseContext(),"Yes Clicked",Toast.LENGTH_LONG).show();
                        processSubmitTripRecords();
                        //Toast.makeText(getBaseContext(), " DataId :", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        //Toast.makeText(getBaseContext(), "No Clicked",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Data Confirm ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    public void processSubmitTripRecords(){
    //    String vehicleId,typeOfTrip,departureLocation,odoMeterDeparture,dateOfDeparture,timeOfDeparture,
      //          returnLocation,odoMeterReturn,dateOfReturn,timeOfReturn,price,picOptn="0";

        //vehicleId="1_test";
        //typeOfTrip="Business";

        if(sharedpreferences.getString("lastTripDepatureReading", null)!=null){
            lastTripDepatureReading = sharedpreferences.getString("lastTripDepatureReading", null);
        }
        if(sharedpreferences.getString("lastTripReturnReading", null)!=null){
            lastTripReturnReading = sharedpreferences.getString("lastTripReturnReading", null);
        }
        if(sharedpreferences.getString("lastTripDepatureDate", null)!=null){
            lastTripDepatureDate = sharedpreferences.getString("lastTripDepatureDate", null);
        }
        if(sharedpreferences.getString("lastTripReturnDate", null)!=null){
            lastTripReturnDate = sharedpreferences.getString("lastTripReturnDate", null);
        }
        if(sharedpreferences.getString("lastDataReading", null)!=null){
            lastDataReading = sharedpreferences.getString("lastDataReading", null);
        }
        if(sharedpreferences.getString("lastDataDate", null)!=null){
            lastDataDate = sharedpreferences.getString("lastDataDate", null);
        }
        if(sharedpreferences.getString("vehicleId", null)!=null){
            vehicleId = sharedpreferences.getString("vehicleId", null);
        }
        if(sharedpreferences.getString("vDateOfPurchase", null)!=null){
            vDateOfPurchase = sharedpreferences.getString("vDateOfPurchase", null);
        }
        if(sharedpreferences.getString("typeOfTrip", null)!=null){
            typeOfTrip = sharedpreferences.getString("typeOfTrip", null);
        }
        //lastReading = sharedpreferences.getString("lastReading", null);
        //vehicleId = sharedpreferences.getString("vehicleId", null);
        //typeOfTrip = sharedpreferences.getString("typeOfTrip", null);//String

        departureLocation=departureLocationET.getText().toString();
        odoMeterDeparture=odoMeterDepartureET.getText().toString();
        //dateOfDeparture=dateOfDepartureET.getText().toString();
        timeOfDeparture=timeOfDepartureET.getText().toString();

        returnLocation=returnLocationET.getText().toString();
        odoMeterReturn=odoMeterReturnET.getText().toString();
        //dateOfReturn=dateOfReturnET.getText().toString();
        timeOfReturn=timeOfReturnET.getText().toString();

        price=priceET.getText().toString();

        String picOptionVl;

        if (receiptOptRG.getCheckedRadioButtonId() == -1) {
            picOptionVl = "none";
        } else {
            int selectedId2 = receiptOptRG.getCheckedRadioButtonId();  // find the radiobutton by returned id
            picOption = (RadioButton) findViewById(selectedId2);
            picOptionVl = picOption.getText().toString();
        }

        if (picOptionVl.equals("Take Photo")) {
            picOptn = "1";
        } else {
            picOptn = "0";
        }

        if( vehicleId==null || vehicleId.equals("")){
            Toast.makeText(this, "Select A Vehicle ", Toast.LENGTH_SHORT).show();
            resultTv.setText("Error: Select A Vehicle ");
        }else if(typeOfTrip==null || typeOfTrip.equals("")){
            Toast.makeText(this, "Select A Type of Trip", Toast.LENGTH_SHORT).show();
            resultTv.setText("Error: Select A Type of Trip");
            selectTripType(null);
        }else if(lastTripDepatureReading==null || lastTripDepatureReading.equals("")){
            Toast.makeText(this, "Select A Vehicle", Toast.LENGTH_SHORT).show();
        }else if (departureLocation.equals("")) {
            departureLocationET.setError("Enter Location You are Going");
            departureLocationET.setHint("Departure to (Location)");
        }else if (odoMeterDeparture.equals("")) {
            odoMeterDepartureET.setError("Enter OdoMeter Reading (at the Time of Departure)!");
            odoMeterDepartureET.setHint("OdoMeter Reading From Your Vehicle DashBoard (at the Time of Departure)");
        }else if (dateOfDeparture.equals("") || dateOfDeparture==null) {
            dateOfDepartureET.setError("Enter Date of Departure!");
            dateOfDepartureET.setHint("Enter Departure Date");
        }else if (timeOfDeparture.equals("") || timeOfDeparture==null) {
            timeOfDepartureET.setError("Enter Time of Departure!");
            timeOfDepartureET.setHint("Enter Departure Time");
        }else if (returnLocation.equals("")) {
            returnLocationET.setError("Enter Return Location");
            returnLocationET.setHint("Returning to (Location)");
        }else if (odoMeterReturn.equals("")) {
            odoMeterReturnET.setError("Enter OdoMeter Reading (at Return)!");
            odoMeterReturnET.setHint("OdoMeter Reading From Your Vehicle DashBoard (at the Time of Return)");
        }else if (dateOfReturn==null || dateOfReturn.equals("")) {
            dateOfReturnET.setError("Enter Date of Return!");
            dateOfReturnET.setHint("Enter Return Date");
        }else if (timeOfReturn==null || timeOfReturn.equals("")) {
            timeOfReturnET.setError("Enter Time of Return!");
            timeOfReturnET.setHint("Enter Return Time");
        }else if (price==null || price.equals("")) {
            priceET.setError("Enter Cost !");
            priceET.setHint("Enter Cost ");
        }
        /*date or departure can't be less then vehicle Purcase date */
        else if(dateDiffrence(dateOfDeparture.replace("-","/"),vDateOfPurchase.replace("-","/"))<0){
            Toast.makeText(this, "1 Check Your Date, Verify With Vehicle Date of Purchase",Toast.LENGTH_LONG).show();
            resultTv.setText("Error1: Check Your Date, Verify With Vehicle Date of Purchase");
        }
        else if(Integer.parseInt(odoMeterReturn)<=Integer.parseInt(odoMeterDeparture)){
            //error Check Ododmeter Entries
            Toast.makeText(this, "Error Check Both Ododmeter Entries",Toast.LENGTH_LONG).show();
            resultTv.setText("Error Check Both Ododmeter Entries");
        }
        else if(dateDiffrence(year+"/"+(month+1)+"/"+day,dateOfDeparture.replace("-","/"))<0){
            Toast.makeText(this, "2. Check Your Date Of Departure ",Toast.LENGTH_SHORT).show();
            resultTv.setText("Error2: Check Your Date Of Departure "+year+"/"+month+"/"+day+"-"+dateOfDeparture.replace("-","/"));
            //resultTv.setText(Integer.toString((int) dateDiffrence(year+"/"+month+"/"+day,dateOfDeparture.replace("-","/"))));
        }
        else if(dateDiffrence(year+"/"+(month+1)+"/"+day,dateOfReturn.replace("-","/"))<0){
            Toast.makeText(this, "Check Your Date Of Return ",Toast.LENGTH_SHORT).show();
            resultTv.setText("Check Your Date Of Return ");
        }
        else if(dateDiffrence(dateOfReturn.replace("-","/"),dateOfDeparture.replace("-","/"))<0){
            Toast.makeText(this, "Check Your Dates Entered By You ",Toast.LENGTH_SHORT).show();
            resultTv.setText("Check Your Dates Entered By You");
        }
        //else if(proceedSubmitTripDataOnVerification("Trip")==false){}
        //else if(proceedSubmitTripDataOnVerification("other")==false){}
        else{
            resultTv.setText("Data OK");

            resultTv.setText(vehicleId+" \n "+typeOfTrip+" \n "+odoMeterDeparture+" \n "+date2Show(dateOfDeparture)+
                    " \n "+timeOfDeparture+" \n "+returnLocation+" \n "+odoMeterReturn+" \n "+date2Show(dateOfReturn)+" \n "+timeOfReturn+
                    " \n "+price+" \n "+picOptn);

            TripData object=new TripData(vehicleId,typeOfTrip,departureLocation,returnLocation,
                    dateOfDeparture,dateOfReturn,timeOfDeparture,timeOfReturn,
                    odoMeterDeparture,odoMeterReturn,price,picOptn);

            //String rtn=dbHandler.putTripData(object);
            boolean rtn=dbHandler.putTripData(object);
            //if(rtn.equals("1")){
            if(rtn==true){
                //String file_name = "testingService";// Returning ID
                TripData object2=dbHandler.getTripData(vehicleId);

                if(picOptn.equals("1")){

                    String fileName = "trip_receipt_" + vehicleId + "_" + object2.getId();
                    moveImageFile(fileName,receiptView,resultTv);

                    //moveImageFile(file_name);
                    ///* Submit and Redirect To Preview Page with ID
                    //set text_pref shered preference null
                    //SharedPreferences sharedpreferences = getSharedPreferences("text_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("file_path");
                    editor.commit();
                }

                Intent intent = new Intent(this, TripRecordView.class);
                intent.putExtra("vehicleId",vehicleId);
                startActivity(intent);
                finish();
            }else{
                resultTv.setText("Error Try Again : " + rtn);
            }

            removeRecordSharedPreferences();
        }
    }

    public void removeRecordSharedPreferences(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("lastTripDepatureReading");
        editor.remove("lastTripReturnReading");
        editor.remove("lastTripDepatureDate");
        editor.remove("lastTripReturnDate");
        editor.remove("lastDataReading");
        editor.remove("lastDataDate");
        editor.remove("vehicleId");
        editor.remove("vDateOfPurchase");
        editor.remove("typeOfTrip");
        editor.commit();
    }

    @SuppressWarnings("deprecation")
    public void getDate(View view){
        showDialog(view.getId());
    }

    @SuppressWarnings("deprecation")
    public void getTime(View view){
        showDialog(view.getId());
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
       //EtId=id;
        //Toast.makeText(getApplicationContext(), Integer.toString(id), Toast.LENGTH_LONG).show();
        if(EtId == R.id.dateOfDepartureET || EtId == R.id.dateOfReturnET){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);  //date is dateSetListener as per your code in question
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            //return new DatePickerDialog(this, datePickerListener, year, month, day);
            return datePickerDialog;
            //return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        else{
            //TimePickerDialog timePickerDialog= new TimePickerDialog(this, timePickerListener, hour, min, false);
            //timePickerDialog.getT
            return new TimePickerDialog(this, timePickerListener, hour, min, false);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            //Toast.makeText(getApplicationContext(), Integer.toString(view.getId()), Toast.LENGTH_LONG).show();
            if (EtId == R.id.dateOfDepartureET) {
                dateOfDepartureET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
                dateOfDeparture=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;
            } else{
                dateOfReturnET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
                dateOfReturn=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;
            }
            //Toast.makeText(getApplicationContext(), Integer.toString(R.id.dateOfDepartureET), Toast.LENGTH_LONG).show();
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            min = minutes;
            if (EtId == R.id.timeOfDepartureET) {
                timeOfDepartureET.setText(pad(hour)+":"+pad(min)+":"+pad(00));
                timeOfDeparture=pad(hour)+":"+pad(min)+":"+pad(00);
            } else{
                timeOfReturnET.setText(pad(hour)+":"+pad(min)+":"+pad(00));
                timeOfReturn=pad(hour)+":"+pad(min)+":"+pad(00);
            }
        }

    };

    /* Function to select or take Image from camera */
    //private void selectImage(View view) {
    public void selectImage(View view) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder(AddFuelFillData.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                    picOptn="1";//Flag To Set Pic Select 1;
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);

                    picOptn="1";//Flag To Set Pic Select 1;
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    takePhoto.setChecked(false);
                    picOptn="0";//Flag To Set Pic Select 0;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                //onSelectFromGalleryResult(data);
                selectGetShowImageFromGallery(data,receiptView);
            else if (requestCode == REQUEST_CAMERA)
                //onCaptureImageResult(data);
                getShowImageFromCamera(data,receiptView);
        }else{
            takePhoto.setChecked(false);
            picOptn = "0";//Flag To Set Pic Select 0;
        }
    }

    /* To Be ReEvaluate: Blow are Functions to Validate Input Record: NotUsing Right Now ReEvaluation */
    public boolean proceedSubmitTripDataOnVerification(String dataType){
        //return true;

        if(dataType.equals("tripData")){
            ///*Compring with lastTripRecord
            //newDepDate=oldReturnDate:Entered By User
            //lastReturnDate=lastDepDate:Entered By User
            //Toast.makeText(this, "lastTripReturnReading "+lastTripReturnReading,Toast.LENGTH_LONG).show();
            if(lastTripReturnReading.equals("0")){return true;}
            //if odometerDep<odometerDepLastTrip AND
            else if((Integer.parseInt(odoMeterDeparture)<Integer.parseInt(lastTripDepatureReading)) &&
                    (Integer.parseInt(odoMeterReturn)<=Integer.parseInt(lastTripDepatureReading)) &&
                    (dateDiffrence(dateOfDeparture.replace("-","/"),lastTripDepatureDate.replace("-","/"))<0) &&
                    (dateOfReturn.equals(lastTripDepatureDate))){
                return true;
            }else if((Integer.parseInt(odoMeterDeparture)>=Integer.parseInt(lastTripReturnReading)) &&
                    (Integer.parseInt(odoMeterReturn)>Integer.parseInt(lastTripReturnReading)) &&
                    (dateOfDeparture.equals(lastTripReturnDate)) &&
                    (dateDiffrence(dateOfReturn.replace("-","/"),lastTripReturnDate.replace("-","/"))>0) ){
                return true;
            }
            else if((Integer.parseInt(odoMeterDeparture)<Integer.parseInt(lastTripDepatureReading)) &&
                    (Integer.parseInt(odoMeterReturn)<Integer.parseInt(lastTripDepatureReading)) &&
                    (dateDiffrence(dateOfDeparture.replace("-","/"),lastTripDepatureDate.replace("-","/"))<0) &&
                    (dateDiffrence(dateOfReturn.replace("-","/"),lastTripDepatureDate.replace("-","/"))<0) ){
                return true;
            }
            else if((Integer.parseInt(odoMeterDeparture)>Integer.parseInt(lastTripReturnReading)) &&
                    (Integer.parseInt(odoMeterReturn)>Integer.parseInt(lastTripReturnReading)) &&
                    (dateDiffrence(dateOfDeparture.replace("-","/"),lastTripReturnDate.replace("-","/"))>0) &&
                    (dateDiffrence(dateOfReturn.replace("-","/"),lastTripReturnDate.replace("-","/"))>0) ){
                return true;
            }
            /*
            else if((dateDiffrence(dateOfDeparture.replace("-","/"),lastTripReturnDate.replace("-","/"))<0)&& (Integer.parseInt(odoMeterDeparture)<Integer.parseInt(lastTripReturnReading))){
                Toast.makeText(this, "1.1 Check Your dateOfDeparture, Verify With lastTripReturnDate ",Toast.LENGTH_LONG).show();
                resultTv.setText("1.1 Error: Check Your dateOfDeparture, Verify With lastTripReturnDate ");
                return false;
            }
            else if((dateOfDeparture.equals(lastTripReturnDate))&& (Integer.parseInt(odoMeterDeparture)<Integer.parseInt(lastTripReturnReading))){
                Toast.makeText(this, "1.2 Check Your dateOfDeparture, Verify With lastTripReturnDate ",Toast.LENGTH_LONG).show();
                resultTv.setText("1.2 Error: Check Your dateOfDeparture, Verify With lastTripReturnDate ");
                return false;
            }

            else if((dateDiffrence(dateOfReturn.replace("-","/"),lastTripDepatureDate.replace("-","/"))<0)&& (Integer.parseInt(odoMeterReturn)>Integer.parseInt(lastTripDepatureReading))){
                Toast.makeText(this, "2.1 Check Your dateOfReturn, Verify With odoMeterReturn ",Toast.LENGTH_LONG).show();
                resultTv.setText("2.1 Error: Check Your dateOfReturn, Verify With odoMeterReturn ");
                return false;
            }
            else if((dateOfReturn.equals(lastTripDepatureDate))&& (Integer.parseInt(odoMeterReturn)<Integer.parseInt(lastTripDepatureReading))){
                Toast.makeText(this, "2.2 Check Your dateOfReturn, Verify With odoMeterReturn ",Toast.LENGTH_LONG).show();
                resultTv.setText("2.2 Error: Check Your dateOfReturn, Verify With odoMeterReturn ");
                return false;
            }
            */
            else{
                Toast.makeText(this, "A. Check Your Trip Data Entered, Verify With Last Trip Data ",Toast.LENGTH_LONG).show();
                resultTv.setText("Error: A. Check Your Trip Data Entered, Verify With Last Trip Data");
                return false;
                //return true;
            }
        }
        else if(lastDataType.equals("Trip")){
            return proceedSubmitTripDataOnVerification("tripData");
            //if(proceedSubmitFillingDataOnVerification("fuelData")==false){ return false;}
        }
        else{
            if(proceedSubmitTripDataOnVerification("tripData")==false){
                return false;
            }
            else{
                //Toast.makeText(this, "lastDataReading "+lastDataReading,Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "dateOfDeparture-lastDataDate "+dateOfDeparture.replace("-","/")+"-"+lastDataDate.replace("-","/"),Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "dateOfDeparture-lastDataDate "+dateOfDeparture.replace("-","/")+"-"+lastDataDate.replace("-","/"),Toast.LENGTH_LONG).show();

                ///*Compring with lastRecord
                if(lastDataReading.equals("0")){return true;}
                //if odoMeterDeparture<lastDataReading and dateOfDeparture>lastDataDate
                else if((Integer.parseInt(odoMeterDeparture)<Integer.parseInt(lastDataReading)) && (dateDiffrence(dateOfDeparture.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "1.1 Check Your dateOfDeparture, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("1.1 Error: Check dateOfDeparture Date, Verify With lastDataDate ");
                    return false;
                }
                //if odoMeterDeparture>lastDataReading and dateOfDeparture<lastDataDate
                else if((Integer.parseInt(odoMeterDeparture)>Integer.parseInt(lastDataReading)) && (dateDiffrence(dateOfDeparture.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "1.1.a Check Your dateOfDeparture, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("1.1.b Error: Check dateOfDeparture Date, Verify With lastDataDate ");
                    return false;
                }
                //if odoMeterReturn<lastDataReading and dateOfReturn>lastDataDate
                else if((Integer.parseInt(odoMeterReturn)<Integer.parseInt(lastDataReading)) && (dateDiffrence(dateOfReturn.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "1.1 Check Your dateOfReturn, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("1.1 Error: Check dateOfReturn Date, Verify With lastDataDate ");
                    return false;
                }
                //if odoMeterReturn>lastDataReading and dateOfReturn<lastDataDate
                else if((Integer.parseInt(odoMeterReturn)>Integer.parseInt(lastDataReading)) && (dateDiffrence(dateOfReturn.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "1.1.a Check Your dateOfReturn, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("1.1.b Error: Check dateOfReturn Date, Verify With lastDataDate ");
                    return false;
                }

                //if odoMeterReturn<lastDataReading and dateOfReturn>lastDataDate
                else if((Integer.parseInt(odoMeterReturn)<Integer.parseInt(lastDataReading)) && (dateDiffrence(dateOfDeparture.replace("-","/"),lastDataDate.replace("-","/"))>0) && (dateDiffrence(dateOfReturn.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "3.1 Check Your dateS & MeterReading, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("3.1 Error: Check dateS & MeterReading, Verify With lastDataDate ");
                    return false;
                }
                else if((Integer.parseInt(odoMeterReturn)>Integer.parseInt(lastDataReading)) && (dateDiffrence(dateOfDeparture.replace("-","/"),lastDataDate.replace("-","/"))<0) && (dateDiffrence(dateOfReturn.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "3.2 Check Your dateS & MeterReading, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("3.2 Error: Check dateS & MeterReading Date, Verify With lastDataDate ");
                    return false;
                }

                //if dateOfDeparture is<lastDataDate  and dateOfReturn=lastDataDate then  odoMeterDeparture>lastDataReading

                //if dateOfDeparture is=lastDataDate  and dateOfReturn>lastDataDate then  odoMeterDeparture<lastDataReading
                /*
                else if((dateOfDeparture.equals(lastDataDate))&& (Integer.parseInt(odoMeterDeparture)<Integer.parseInt(lastDataReading))){
                    Toast.makeText(this, "1.2 Check Your dateOfDeparture, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("1.2 Error: Check Your dateOfDeparture, Verify With lastDataDate ");
                    return false;
                }
                else if((dateOfReturn.equals(lastDataDate))&& (Integer.parseInt(odoMeterReturn)>Integer.parseInt(lastDataReading))){
                    Toast.makeText(this, "2.2 Check Your dateOfReturn, Verify With lastDataDate ",Toast.LENGTH_LONG).show();
                    resultTv.setText("2.2 Error: Check Your dateOfReturn, Verify With lastDataDate ");
                    return false;
                }
                */
                else{
                    return true;
                }
            }
        }
    }
}
