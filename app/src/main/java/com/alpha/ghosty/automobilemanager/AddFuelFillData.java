package com.alpha.ghosty.automobilemanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFuelFillData extends SuperActivity {

    EditText fillingDateET,odoMtrRdingET,volumeET,priceET;
    ArrayAdapter<String> userVehicleLists;
    Builder builderSingle;
    VehicleData[] objectAry;
    RadioGroup receiptOptRG;
    RadioButton picOption,takePhoto;//,noNeed;
    //private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    ImageView receiptView;
    TextView selectVehicle,resultTv,lastFillDetails,typeOfFuelTV;

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    private String fillingDate,vehicleId="",odoMeterReading,fillDate,volume,price,picOptn="0",
            fuelType,lastDataType,lastDataReading="0",lastDataDate="0/0/0",
            vDateOfPurchase="0/0/0",fuelCapacity,lastFuelFillingReading,lastFuelFillDate;//receipt,

    ArrayAdapter<String> fuelTypeAA;

    Date minDatefDtPckr;//=new Date("1990-01-01 00:00");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fuel_fill_data);

        minDatefDtPckr=new Date(1990,Calendar.JANUARY,1);

        dbHandler = new DBhandler(this, null, null, 1);

        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        odoMtrRdingET = (EditText) findViewById(R.id.odoMtrRdingET);
        fillingDateET = (EditText) findViewById(R.id.fillingDateET);
        volumeET = (EditText) findViewById(R.id.volumeET);
        priceET = (EditText) findViewById(R.id.priceET);

        receiptOptRG = (RadioGroup) findViewById(R.id.receiptOptRG);
        takePhoto = (RadioButton) findViewById(R.id.takePhoto);
        //noNeed = (RadioButton) findViewById(R.id.noNeed);
        resultTv = (TextView) findViewById(R.id.resultTv);
        selectVehicle = (TextView) findViewById(R.id.selectVehicle);
        lastFillDetails = (TextView) findViewById(R.id.lastFillDetails);
        typeOfFuelTV = (TextView) findViewById(R.id.typeOfFuelTV);

        receiptView = (ImageView) findViewById(R.id.receiptView);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //fillingDateET.setText(day + " / " + (month + 1) + " / " + year);
        fillingDateET.setKeyListener(null);

        fillingDate=year+"-"+(month + 1)+"-"+day;

        fillingDateET.setClickable(true);
        fillingDateET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        fillingDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    showDialog(0);
                } else {
                }
            }
        });

        //vehicleIdAry=dbHandler.getDataIds("vehicle");
        //object=new VehicleData();
        //object=dbHandler.userVehicleDetails();
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
            fuelCapacity=objectAry[0].getFuelTankCapacity();//.getItem(which);
            //slctVId = vehicleIdAry[which];//.getItem(which);
            fetechFillingDetails(vehicleId); // to fetch last record
            //usrCurrency=getResources().getString(currencySymb[which]);
            selectVehicle.setText(userVehicleLists.getItem(0) + "");
        }
        else{selectVehicle(null);}
        //userVehicleDetails
        //mCtx.getPackageName();
        //Toast.makeText(getBaseContext(),"getPackageName: "+getPackageName(),Toast.LENGTH_LONG).show();

        String[] fuleTypes=dbHandler.getFuelType();
        //fuelTypeAA
        fuelTypeAA = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

        for(int f=0;f<fuleTypes.length;f++){
            fuelTypeAA.add(fuleTypes[f]);
        }
        fuelTypeAA.add("other");

    }

    public void selectFuelType(final View view){

        //Toast.makeText(this, "You Set: ", Toast.LENGTH_LONG).show();
        //usrCurrencyTxt.setText("Rs hhgfd");
        builderSingle = new Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Fuel Type");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOfFuelTV.setText("Select Fuel Type");
                        //typeOfService = null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(fuelTypeAA,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fuelType = fuelTypeAA.getItem(which);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        if (fuelType.equals("other")) {
                            enterFuelType(view);
                        } else {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("fuelType", fuelType); ///*Saving Data To Protect Change on Camera Intent/
                            editor.commit();
                            typeOfFuelTV.setText("Fuel Type: " + fuelType);
                        }
                    }
                });
        builderSingle.show();
    }

    public void enterFuelType(View view){
        builderSingle = new Builder(this);

        builderSingle.setTitle("Enter Fuel");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builderSingle.setView(input);
        // Set up the buttons
        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fuelType = input.getText().toString();
                typeOfFuelTV.setText("Fuel Type: " + fuelType);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("fuelType", fuelType); ///*Saving Data To Protect Change on Camera Intent/
                editor.commit();
            }
        });
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //typeOfService=null;
                dialog.cancel();
            }
        });
        builderSingle.show();
        //return mText[0];
    }

    /* Function: To Select Vehicle */
    public void selectVehicle(View view){
        builderSingle = new Builder(this);
        builderSingle.setTitle("Select Vehicle");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //selectVehicle.setText("Select Vehicle (Tap Here)");
                        vehicleId = null;
                        vDateOfPurchase = null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(userVehicleLists,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vehicleId = objectAry[which].getId();//.getItem(which);
                        vDateOfPurchase = objectAry[which].getDateOfPurchase();//.getItem(which);
                        fuelCapacity=objectAry[which].getFuelTankCapacity();//.getItem(which);
                        //slctVId = vehicleIdAry[which];//.getItem(which);
                        fetechFillingDetails(vehicleId);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                        Toast.makeText(getBaseContext(),"vDateOfPurchase"+vDateOfPurchase,Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.show();
    }

    /* Function: to Fetch Last Filling Details */
    public void fetechFillingDetails(String vehicleId){

        String[] lastData;//=new String[4];

        lastData=dbHandler.getLastData(vehicleId);
        lastDataType=lastData[0];
        lastDataReading=lastData[1];
        lastDataDate=lastData[2];
        //String pricePaid=lastData[3];

        lastFillDetails.setText("Last Record:\n Type:"+ lastData[0] +
                "\n at:"+ lastDataReading +
                "\n Date: " + date2Show(lastDataDate) +
                "\n Paid: " + lastData[3]);

        FillingData object=dbHandler.getLastFillingData(vehicleId);
        lastFuelFillingReading=object.getMeterReading();
        lastFuelFillDate=object.getDate();

        lastFillDetails.setText(lastFillDetails.getText()+"\n\n Last Record of Fuel Filling Record \n OdometerReading : " + lastFuelFillingReading +
                "\n Date: " + date2Show(lastFuelFillDate));
/*
        FillingData object2=dbHandler.getLastFillingData(vehicleId);
        lastReading=object2.getMeterReading();
        lastFillDate=object2.getDate();
        lastFillDetails.setText("Last Record of Filling OdometerReading : " + lastReading + " Date: " + lastFillDate);
*/
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lastDataReading", lastDataReading); /*Saving Data To Protect Change on Camera Intent*/
        editor.putString("lastDataDate", lastDataDate); /*Saving Data To Protect Change on Camera Intent*/
        editor.putString("vehicleId", vehicleId); /*Saving Data To Protect Change on Camera Intent*/
        editor.putString("vDateOfPurchase", vDateOfPurchase); /*Saving Data To Protect Change on Camera Intent*/
        editor.putString("lastFuelFillingReading", lastFuelFillingReading); /*Saving Data To Protect Change on Camera Intent*/
        editor.putString("lastFuelFillDate", lastFuelFillDate); /*Saving Data To Protect Change on Camera Intent*/
        editor.commit();


        //Date date;
        String dtStart = vDateOfPurchase+" 00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            minDatefDtPckr = format.parse(dtStart);
            //System.out.println(date);
            //datePickerDialog.getDatePicker().setMinDate(minDatefDtPckr.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            resultTv.setText(e.getMessage());
            minDatefDtPckr=new Date(1990,Calendar.JANUARY,1);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);  //date is dateSetListener as per your code in question
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    /* Function: DatePickerDialog */
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            fillingDateET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
            fillingDate=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;
        }
    };

    public void submitFuelFillData(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processSubmitFuelFillData();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Data Confirm ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    /* Function: Submit Fuel Filling Data */
    public void processSubmitFuelFillData(){
        InputMethodManager inputManager =(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        /*
        Check Fillling Date With Vehicle Purchase date
        Check Filling Date With Last Filling Date
        * */
        //SharedPreferences prefs = getSharedPreferences("text_pref", MODE_PRIVATE);
        //String lastReading = sharedpreferences.getString("lastReading", null);
        //String vehicleId = sharedpreferences.getString("vehicleId", null);

        /*if (odoMeterReading < lastReading  && dateFill > lastdate) : no Process */
        /*if (odoMeterReading > lastReading  && dateFill < lastdate) : no Process */

        if(sharedpreferences.getString("lastDataReading", null)!=null){
            lastDataReading = sharedpreferences.getString("lastDataReading", null);
        }
        if(sharedpreferences.getString("vehicleId", null)!=null){
            vehicleId = sharedpreferences.getString("vehicleId", null);
        }
        if(sharedpreferences.getString("lastDataDate", null)!=null){
            lastDataDate = sharedpreferences.getString("lastDataDate", null);
        }
        if(sharedpreferences.getString("vDateOfPurchase", null)!=null){
            vDateOfPurchase = sharedpreferences.getString("vDateOfPurchase", null);
        }

        if(sharedpreferences.getString("fuelType", null)!=null){
            fuelType = sharedpreferences.getString("fuelType", null);
        }

        if(sharedpreferences.getString("lastFuelFillingReading", null)!=null){
            lastFuelFillingReading = sharedpreferences.getString("lastFuelFillingReading", null);
        }

        if(sharedpreferences.getString("lastFuelFillDate", null)!=null){
            lastFuelFillDate = sharedpreferences.getString("lastFuelFillDate", null);
        }
        //editor.putString("lastFuelFillingReading", lastFuelFillingReading); /*Saving Data To Protect Change on Camera Intent*/
        //editor.putString("lastFuelFillDate", lastFuelFillDate); /*Saving Data To Protect Change on Camera Intent*/
        //String vehicleId,odoMeterReading,date,volume,price,picOptn;

        odoMeterReading=odoMtrRdingET.getText().toString();
        fillDate=fillingDate;//fillingDateET.getText().toString();
        volume=volumeET.getText().toString();
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

        //Toast.makeText(this, "FillDate - LastRecord = "+dateDiffrence(fillDate.replace("-","/"),lastDataDate.replace("-","/")),Toast.LENGTH_LONG).show();

        //picOptn=fuel_volume.getText().toString();
        if(vehicleId==null || vehicleId.equals("")){
            Toast.makeText(this, "Select A Vehicle ",Toast.LENGTH_SHORT).show();
        }else if(fuelType==null || fuelType.equals("")){
            Toast.makeText(this, "Select Fuel Type ",Toast.LENGTH_SHORT).show();
            selectFuelType(null);
        }else if (odoMeterReading==null || odoMeterReading.equals("")) {
            odoMtrRdingET.setError("Enter OdoMeter Reading !");
            odoMtrRdingET.setHint("OdoMeter Reading From Your Vehicle DashBoard");
        }else if (fillDate==null || fillDate.equals("")) {
            fillingDateET.setError("Enter Filling Date!");
            fillingDateET.setHint("Filling Date");
        }else if (volume==null || volume.equals("")) {
            volumeET.setError("Enter Volume Of Fuel");
            volumeET.setHint("Volume Of Fuel");
        }else if(Integer.parseInt(volume)>Integer.parseInt(fuelCapacity)){
            volumeET.setError("Check Volume of Fuel");
            volumeET.setHint("Volume Of Fuel Can't Be More Then Fuel Tank Capacity");
            resultTv.setText("Error: Volume Of Fuel Can't Be More Then Fuel Tank Capacity");
        }else if (price==null || price.equals("")) {
            priceET.setError("Enter Price Paid");
            priceET.setHint("Price Paid For Fuelling");
        }else if(dateDiffrence(fillDate.replace("-","/"),vDateOfPurchase.replace("-","/"))<0){
            Toast.makeText(this, "Check Your Date, Verify With Vehicle Date of Purchase",Toast.LENGTH_LONG).show();
            resultTv.setText("Error: Check Your Date, Verify With Vehicle Date of Purchase");
        }

        /* Below Two Verification does Not Needed */
        /*
		else if(validateFillingDataByLastData()==false){
            resultTv.setText("Error A.1: Check validateFillingDataByLastData");
        }
        else if(proceedSubmitFillingDataOnVerification("other")==false){
        }
        */
        else{
            //Toast.makeText(this, "Data OK To Enter ",Toast.LENGTH_LONG).show();
            resultTv.setText("Data OK To Enter ");

            //resultTv.setText(vehicleId+" \n "+odoMeterReading+" \n "+fillDate+" \n "+volume+" \n "+price+" \n "+picOptn);
            FillingData objectpt=new FillingData(vehicleId,fuelType,odoMeterReading,fillDate,volume,price,picOptn);
            //String rtn=dbHandler.putFuelFillData(objectpt);
            //String file_name = "testing";// Returning ID
            boolean rtn=dbHandler.putFuelFillData(objectpt);
            //if(rtn.equals("1")){
            if(rtn==true){
                FillingData object2=dbHandler.getLastFillingData("0");

                resultTv.setText("Data Inserted: "
                                +" \n Id: "+object2.getId()
                                +" \n Vehicle ID: "+object2.getVehicleId()
                                +" \n Fuel: "+object2.getFuelType()
                                +" \n OdometerReading: "+object2.getMeterReading()
                                +" \n Date: "+object2.getDate()
                                +" \n Volume: "+object2.getVolume()
                                +" \n Price: "+object2.getPrice()
                                +" \n Receipt: "+object2.getReceipt()
                                +" \n AddedOn: "+object2.getAddedOn());

                if(picOptn.equals("1")){
                    String fileName = "fuel_filling_receipt_" + vehicleId + "_" + object2.getId();
                    moveImageFile(fileName,receiptView,resultTv);
                    // Submit and Redirect To Preview Page with ID
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("file_path");
                    editor.commit();
                }

                Intent intent = new Intent(this, FuelFillDataView.class);
                intent.putExtra("vehicleId",vehicleId);
                startActivity(intent);
                finish();
            }else{
                resultTv.setText("Error Try Again : " + rtn);
            }
            removeRecordSharedPreferences();// For Test Purpose Remove it when Uncomment Above Code
       }
    }

	public void removeRecordSharedPreferences(){
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.remove("vehicleId");
		editor.remove("fuelType");
		editor.remove("lastDataReading");
		editor.remove("lastDataDate");
		editor.remove("vDateOfPurchase");
		editor.remove("lastFuelFillingReading");
		editor.remove("lastFuelFillDate");
		editor.commit();
	}

    /* Function: to select or take Image from camera */
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

                    picOptn = "1";//Flag To Set Pic Select 1;
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);

                    picOptn = "1";//Flag To Set Pic Select 1;
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    takePhoto.setChecked(false);
                    picOptn = "0";//Flag To Set Pic Select 0;
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
    public boolean checkRecordA2OdoAndDate(String vehicleId){
        /*
         get greatest odoreading and date
         compare record acording to this

         if(datein is diffrent from dateFetched){

         }

        */


        return false;
    }

    public boolean proceedSubmitFillingDataOnVerification(String dataType){

        if(dataType.equals("fuelData")){
            if(lastFuelFillingReading.equals("0")){return true;}
            //if dates are same return true

            /* Validating With Last Fuel Filling Record */
            /* Checking for Duplicate Entry */
            if(fillDate.equals(lastFuelFillDate) && odoMeterReading.equals(lastFuelFillingReading)){
                Toast.makeText(this, "Duplicate Data",Toast.LENGTH_LONG).show();
                resultTv.setText("Error: Duplicate Data");
                return false;
            }
            else if(fillDate.equals(lastFuelFillDate)){
                return true;
                //Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                //resultTv.setText("Error1.1: Match Records With Last Record");
                //return false;
            }
            else if((Integer.parseInt(odoMeterReading)<Integer.parseInt(lastFuelFillingReading)) && (dateDiffrence(fillDate.replace("-","/"),lastFuelFillDate.replace("-","/"))>0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error3.1: Match Records With Last Filling Record Entered");
                return false;
            }
            /* if odometer is Greater then current and date is less then current:show Error  */
            else if((Integer.parseInt(odoMeterReading)>Integer.parseInt(lastFuelFillingReading)) && (dateDiffrence(fillDate.replace("-","/"),lastFuelFillDate.replace("-","/"))<0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error4.1: Match Records With Last Filling Record Entered");
                return false;
            }
            /*Validating With Last Fuel Filling Record if OdodMetereReading is Same*/
            /* if current odometer is less then last and filldate is Greater then lastdate current:show Error  */
            else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastFuelFillingReading)) && (dateDiffrence(fillDate.replace("-","/"),lastFuelFillDate.replace("-","/"))>0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error3.2: Match Records With Last Filling Record Entered");
                return false;
            }
            /* if current odometer is Greater then last and filldate is less then lastdate current:show Error  */
            else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastFuelFillingReading)) && (dateDiffrence(fillDate.replace("-","/"),lastFuelFillDate.replace("-","/"))<0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error4.2: Match Records With Last Filling Record Entered");
                return false;
            }
            /* if Filling Date is Greater Then Current date i.e Checking for Future date */
            else if(dateDiffrence(year+"/"+(month+1)+"/"+day,fillDate.replace("-","/"))<0){
                //Toast.makeText(this, "Check Your Date "+year+"/"+(month+1)+"/"+day+"-"+fillDate.replace("-","/"),Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Check Your Date ",Toast.LENGTH_SHORT).show();
                resultTv.setText("Error : You Select Future Date");
                return false;
            }
            else{return true;}

        }
        else if(lastDataType.equals("fuel")){

            return proceedSubmitFillingDataOnVerification("fuelData");
            //if(proceedSubmitFillingDataOnVerification("fuelData")==false){ return false;}
        }
        else{

            if(proceedSubmitFillingDataOnVerification("fuelData")==false){
                return false;
            }
            else{
                if(lastDataReading.equals("0")){return true;}
                //if(lastDataReading==null|| lastDataReading.equals("0")){Toast.makeText(this, "lastDataReading NUll ",Toast.LENGTH_LONG).show(); return false;}
                //if(lastDataDate==null|| lastDataDate.equals("0")){Toast.makeText(this, "lastDataDate NUll ",Toast.LENGTH_LONG).show(); return false;}

                //if same day

                /*Validating With Last Record */
                if(fillDate.equals(lastDataDate)){
                    return true;
                    //Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    //resultTv.setText("Error1.1: Match Records With Last Record");
                    //return false;
                }
                /* if current odometer is less then last and filldate is Greater then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)<Integer.parseInt(lastDataReading)) && (dateDiffrence(fillDate.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error1.1: Match Records With Last Record");
                    return false;
                }
                /* if current odometer is Greater then last and filldate is less then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)>Integer.parseInt(lastDataReading)) && (dateDiffrence(fillDate.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error2.1: Match Records With Last Record");
                    return false;
                }

                /*Validating With Last Record if OdodMetereReading is Same*/
                /* if current odometer is less then last and filldate is Greater then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastDataReading)) && (dateDiffrence(fillDate.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error1.2: Match Records With Last Record");
                    return false;
                }
            /* if current odometer is Greater then last and filldate is less then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastDataReading)) && (dateDiffrence(fillDate.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error2.2: Match Records With Last Record");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateFillingDataByLastData(){

        //getLastDataByReading(String vehicleId,String MtrReading);

        String[] lastDatal=dbHandler.getLastDataByReading(vehicleId,odoMeterReading);
        String lastDataLType=lastDatal[0];
        String lastDataLReading=lastDatal[1];
        String lastDataLDate=lastDatal[2];
        //String pricePaid=lastData[3];

        FillingData objectl=dbHandler.getLastFillingDataByReading(vehicleId,odoMeterReading,"DESC");
        String lastFuelFillingReadingDESC=objectl.getMeterReading();
        String lastFuelFillDateDESC=objectl.getDate();

        if(lastDataLType.equals("fuel")){

            if(fillDate.equals(lastFuelFillDateDESC) && odoMeterReading.equals(lastFuelFillingReadingDESC)){
                Toast.makeText(this, "Duplicate Data Error ",Toast.LENGTH_LONG).show();
                resultTv.setText("Error 22: Duplicate Data");
                return false;
            }
            else if(fillDate.equals(lastDataLDate)){
                return true;
                //Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                //resultTv.setText("Error1.1: Match Records With Last Record");
                //return false;
            }
            else{return processValidateFillingDataByLastData(lastFuelFillDateDESC,lastFuelFillingReadingDESC);}

        }
        else{
            if(processValidateFillingDataByLastData(lastFuelFillDateDESC,lastFuelFillingReadingDESC)==false){
                return false;
            }
            else{
                if(fillDate.equals(lastDataLDate)){
                    return true;
                    //Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    //resultTv.setText("Error1.1: Match Records With Last Record");
                    //return false;
                }
                else{return processValidateFillingDataByLastData(lastDataLDate,lastDataLReading);}
            }
        }
        //return true;
    }

    public boolean processValidateFillingDataByLastData(String date,String meterReading){
        //Toast.makeText(this, fillDate+"  is Equal "+date,Toast.LENGTH_LONG).show();
        Toast.makeText(this, odoMeterReading+"=odoMeterReading  is Equal meterReading="+meterReading,Toast.LENGTH_LONG).show();
        // newdatesEnter>lastDatebyReading AND newmeterReading>meterReading
        // OR
        // newdatesEnter<lastDatebyReading AND newmeterReading<meterReading
        // Main Logic (Below)
        // fillDate>date AND odoMeterReading>=meterReading
        if((Integer.parseInt(odoMeterReading)>=Integer.parseInt(meterReading)) && (dateDiffrence(fillDate.replace("-","/"),date.replace("-","/"))>0)){
            Toast.makeText(this, "Check 1",Toast.LENGTH_LONG).show();
            //resultTv.setText("Error1.2: Match Records With Last Record");
            return true;
        }
        // fillDate<date AND odoMeterReading<=meterReading
        else if((Integer.parseInt(odoMeterReading)<=Integer.parseInt(meterReading)) && (dateDiffrence(fillDate.replace("-","/"),date.replace("-","/"))<0)){
            //Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
            //resultTv.setText("Error1.2: Match Records With Last Record");
            Toast.makeText(this, "Check 2",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    /* Common Function to Validate New Record */
    public boolean validatingInputRecord(String date,String meterReading){

        //Toast.makeText(this, fillDate+"  is Equal "+date,Toast.LENGTH_LONG).show();
        Toast.makeText(this, odoMeterReading+"=odoMeterReading  is Equal meterReading="+meterReading,Toast.LENGTH_LONG).show();
        // newdatesEnter>lastDatebyReading AND newmeterReading>meterReading
        // OR
        // newdatesEnter<lastDatebyReading AND newmeterReading<meterReading
        // Main Logic (Below)
        // fillDate>date AND odoMeterReading>=meterReading
        /* see below case in each
        if(odoMeterReading.equals(meterReading) && fillDate.equals(date)){
            Toast.makeText(this, "Duplicate Data",Toast.LENGTH_LONG).show();
            resultTv.setText("Error: Duplicate Data");
            return false;
        }
        */
        //else if((Integer.parseInt(odoMeterReading)>=Integer.parseInt(meterReading)) && (dateDiffrence(fillDate.replace("-","/"),date.replace("-","/"))>0)){
        if((Integer.parseInt(odoMeterReading)>Integer.parseInt(meterReading)) && (dateDiffrence(fillDate.replace("-","/"),date.replace("-","/"))>0)){
            Toast.makeText(this, "Check 1",Toast.LENGTH_LONG).show();
            //resultTv.setText("Error1.2: Match Records With Last Record");
            return true;
        }
        // fillDate<date AND odoMeterReading<=meterReading
        //else if((Integer.parseInt(odoMeterReading)<=Integer.parseInt(meterReading)) && (dateDiffrence(fillDate.replace("-","/"),date.replace("-","/"))<0)){
        else if((Integer.parseInt(odoMeterReading)<Integer.parseInt(meterReading)) && (dateDiffrence(fillDate.replace("-","/"),date.replace("-","/"))<0)){
            //Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
            //resultTv.setText("Error1.2: Match Records With Last Record");
            Toast.makeText(this, "Check 2",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
