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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddOtherExpenses extends SuperActivity {

    EditText odoMtrRdingET,dateET,priceET;

    RadioGroup receiptOptRG;
    RadioButton picOption,takePhoto;//,noNeed;
    //private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    ImageView receiptView;
    TextView selectVehicle,lastOExpenseTv,typeOfExpenseTV,resultTv;
    VehicleData[] objectAry;
    String vehicleId="",typeOfExpense="",odoMeterReading,oExpensedate,date,price,picOptn="0",
            lastDataType,lastDataReading="0",lastDataDate="0/0/0",lastOExpenseReading="0",
            lastOExpenseDate,vDateOfPurchase="";//slctVModel

    ArrayAdapter<String> oExpenseType,userVehicleLists;
    Builder builderSingle;

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    //SharedPreferences sharedpreferences;
    //private String lastReading="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_expenses);
        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        odoMtrRdingET = (EditText) findViewById(R.id.odoMtrRdingET);
        dateET = (EditText) findViewById(R.id.dateET);
        priceET = (EditText) findViewById(R.id.priceET);

        receiptOptRG = (RadioGroup) findViewById(R.id.receiptOptRG);
        takePhoto = (RadioButton) findViewById(R.id.takePhoto);
        resultTv = (TextView) findViewById(R.id.resultTv);
        receiptView = (ImageView) findViewById(R.id.receiptView);

        selectVehicle = (TextView) findViewById(R.id.selectVehicle);
        lastOExpenseTv = (TextView) findViewById(R.id.lastOExpenseTv);
        typeOfExpenseTV = (TextView) findViewById(R.id.typeOfExpenseTV);

        oExpenseType = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        oExpenseType.add("Accident");
        oExpenseType.add("Cleaning and Washing");
        oExpenseType.add("Fine");
        oExpenseType.add("Parking");
        oExpenseType.add("Toll Tax");
        oExpenseType.add("Tow");
        oExpenseType.add("Small Part Change");
        oExpenseType.add("other");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date=year+"-"+(month + 1)+"-"+day;

        //dateET.setText(day + " / " + (month + 1) + " / " + year);
        dateET.setKeyListener(null);
        dateET.setClickable(true);
        dateET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    showDialog(0);
                } else {
                }
            }
        });

        objectAry=dbHandler.userVehicleDetails();
        int dtLen=objectAry.length;
        userVehicleLists = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);
        // userVehicleLists.add("TVS"); userVehicleLists.add("Hindustan Motor");

        for(int l=0;l<dtLen;l++){
            userVehicleLists.add(objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+") Type:"+objectAry[l].getType());
        }

        //userVehicleDetails
        if(dtLen==1){
            vehicleId = objectAry[0].getId();//.getItem(which);
            vDateOfPurchase = objectAry[0].getDateOfPurchase();//.getItem(which);
            //fuelCapacity=objectAry[0].getFuelTankCapacity();//.getItem(which);
            //slctVId = vehicleIdAry[which];//.getItem(which);
            fetchLastOExpense(vehicleId);
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
                        fetchLastOExpense(vehicleId);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                    }
                });
        builderSingle.show();
    }

    public void fetchLastOExpense(String vehicleId){

        String[] lastData;//=new String[4];

        lastData=dbHandler.getLastData(vehicleId);
        lastDataType=lastData[0];
        lastDataReading=lastData[1];
        lastDataDate=lastData[2];
        //String pricePaid=lastData[3];

        lastOExpenseTv.setText("Last Record:\n Type:"+ lastData[0] +
                "\n at:"+ lastDataReading +
                "\n Date: " + date2Show(lastDataDate) +
                "\n Paid: " + lastData[3]);

        ExpenseData objectt=dbHandler.getLastOExpenseData(vehicleId);
        //FillingData objectt=dbHandler.getLastOExpenseData(vehicleId);
        lastOExpenseReading=objectt.getMeterReading();
        lastOExpenseDate=objectt.getDate();
        lastOExpenseTv.setText(lastOExpenseTv.getText()+
                "\n\n Last Expenses Done at: " + lastOExpenseReading+
                "\n On: "+date2Show(lastOExpenseDate));

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lastOExpenseReading", lastOExpenseReading); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastOExpenseDate", lastOExpenseDate); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastDataReading", lastDataReading); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastDataDate", lastDataDate); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("vehicleId", vehicleId); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("vDateOfPurchase", vDateOfPurchase); /*Saving Data To Protect Change on Camera Intent*/
        editor.commit();

    }
    public void selectExpenseType(final View view){

        //Toast.makeText(this, "You Set: ", Toast.LENGTH_LONG).show();
        //usrCurrencyTxt.setText("Rs hhgfd");
        builderSingle = new Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Expense Type");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOfExpenseTV.setText("Select Expense Type (Tap Here)");
                        typeOfExpense = null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(oExpenseType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOfExpense = oExpenseType.getItem(which);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        if (typeOfExpense.equals("other")) {
                            enterExpense(view);
                        } else {
                            typeOfExpenseTV.setText("Expense Type: " + typeOfExpense + " (Tap To Change)");

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("typeOfExpense", typeOfExpense); ///*Saving Data To Protect Change on Camera Intent/
                            editor.commit();
                        }
                    }
                });
        builderSingle.show();
    }
    public void enterExpense(View view){
        //final String[] mText = new String[1];
        //android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builderSingle = new Builder(this);

        builderSingle.setTitle("Enter Expense Done");
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
                typeOfExpense = m_Text;
                typeOfExpenseTV.setText("Expense Type: " + typeOfExpense + " (Tap To Change)");

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("typeOfExpense", typeOfExpense); ///*Saving Data To Protect Change on Camera Intent/
                editor.commit();
            }
        });
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                typeOfExpense = null;
                dialog.cancel();
            }
        });
        builderSingle.show();
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
        //return new DatePickerDialog(this, datePickerListener, year, month, day);
        return datePickerDialog;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            dateET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
            date=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;
        }
    };

    public void submitExpenseData(View view){
        //processSubmitExpenseData();
        ///*
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        //Toast.makeText(getBaseContext(),"Yes Clicked",Toast.LENGTH_LONG).show();
                        processSubmitExpenseData();
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

    public void processSubmitExpenseData(){

        //vehicleId="1_test";
        //typeOfExpense="testingOExpense";
        //String lastReading = sharedpreferences.getString("lastReading", null);
        //String vehicleId = sharedpreferences.getString("vehicleId", null);
        //String typeOfExpense = sharedpreferences.getString("typeOfExpense", null);

        if(sharedpreferences.getString("lastOExpenseReading", null)!=null){
            lastOExpenseReading = sharedpreferences.getString("lastOExpenseReading", null);
        }

        if(sharedpreferences.getString("lastOExpenseDate", null)!=null){
            lastOExpenseDate = sharedpreferences.getString("lastOExpenseDate", null);
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
        if(sharedpreferences.getString("typeOfExpense", null)!=null){
            typeOfExpense = sharedpreferences.getString("typeOfExpense", null);
        }

        odoMeterReading=odoMtrRdingET.getText().toString();
        //date=dateET.getText().toString();
        price=priceET.getText().toString();

        String picOptionVl;
        oExpensedate=date;
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

        if(vehicleId.equals("")||vehicleId==null){
            Toast.makeText(this, "Select A Vehicle ", Toast.LENGTH_SHORT).show();
        }else if(typeOfExpense==null || typeOfExpense.equals("")){
            Toast.makeText(this, "Select A typeOfExpense ", Toast.LENGTH_SHORT).show();
            selectExpenseType(null);
        }else if (odoMeterReading.equals("")) {
            odoMtrRdingET.setError("Enter OdoMeter Reading !");
            odoMtrRdingET.setHint("OdoMeter Reading From Your Vehicle DashBoard");
        }
        /*
        else if (Integer.parseInt(odoMeterReading)<=Integer.parseInt(lastOExpenseReading)) {
            odoMtrRdingET.setError("Enter Proper OdoMeter Reading !");
            odoMtrRdingET.setHint("Check OdoMeter Reading Entered");
        }
        */
        else if (oExpensedate.equals("")) {
            dateET.setError("Enter Date!");
            dateET.setHint("Enter Date");
        }else if (price.equals("")) {
            priceET.setError("Enter Price Paid");
            priceET.setHint("Price Paid");
        }else if(dateDiffrence(oExpensedate.replace("-","/"),vDateOfPurchase.replace("-","/"))<0){
            Toast.makeText(this, "Check Your Date, Verify With Vehicle Date of Purchase",Toast.LENGTH_LONG).show();
            resultTv.setText("Error: Check Your Date, Verify With Vehicle Date of Purchase");
        }
        /*
        else if(proceedSubmitOtherExpenseDataOnVerification("other")==false){
        }
        */
        else{
            //Toast.makeText(this, "Data OK To Enter ",Toast.LENGTH_LONG).show();
            //resultTv.setText("Data OK To Enter ");
            ///*
            ExpenseData object=new ExpenseData(vehicleId,typeOfExpense,odoMeterReading,oExpensedate,price,picOptn);
            //resultTv.setText(object.getVehicleId()+" \n "+object.getExpenseType()+" \n "+object.getMeterReading()+" \n "+object.getDate()+" \n "+object.getPrice()+" \n "+object.getReceipt());

            //String rtn=dbHandler.putOExpenseData(object);
            boolean rtn=dbHandler.putOExpenseData(object);
            //if(rtn.equals("1")){
            if(rtn==true){
                //String file_name = "testingService";// Returning ID
                ExpenseData object2=dbHandler.getLastOExpenseData("0");
                resultTv.setText( "Data Inserted: "
                                + "\n ID: "+object2.getId()
                                + "\n VehicleId: " + object2.getVehicleId()
                                + "\n OdoMeterReading: " + object2.getMeterReading()
                                + "\n Expense Type: " + object2.getExpenseType()
                                + "\n Date: " + object2.getDate()
                                + "\n Paid: " + object2.getPrice()
                                + "\n Receipt: " + object2.getReceipt()
                                + "\n AddedOn: " + object2.getAddedOn());

                if(picOptn.equals("1")){
                    String fileName = "oexpenses_receipt_" + vehicleId + "_" + object2.getId();
                    //moveImageFile(file_name);
                    moveImageFile(fileName,receiptView,resultTv);
                    //* Submit and Redirect To Preview Page with ID
                    //set text_pref shered preference null
                    //SharedPreferences sharedpreferences = getSharedPreferences("text_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("file_path");
                    editor.commit();
                }

                Intent intent = new Intent(this, OtherExpensesView.class);
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
        editor.remove("lastOExpenseReading");
        editor.remove("lastOExpenseDate");
        editor.remove("lastDataReading");
        editor.remove("lastDataDate");
        editor.remove("vehicleId");
        editor.remove("vDateOfPurchase");
        editor.remove("typeOfExpense");
        editor.commit();
    }

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
    public boolean proceedSubmitOtherExpenseDataOnVerification(String dataType){

        if(dataType.equals("otherExpenseData")){

            if(lastOExpenseReading.equals("0")){return true;}
            /*Validating With Last Fuel Filling Record */
            /* if odometer is less then current and date is Greater then current:show Error  */
            else if((Integer.parseInt(odoMeterReading)<Integer.parseInt(lastOExpenseReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastOExpenseDate.replace("-","/"))>0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error3.1: Match Records With Last Filling Record Entered");
                return false;
            }
            /* if odometer is Greater then current and date is less then current:show Error  */
            else if((Integer.parseInt(odoMeterReading)>Integer.parseInt(lastOExpenseReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastOExpenseDate.replace("-","/"))<0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error4.1: Match Records With Last Filling Record Entered");
                return false;
            }
            /*Validating With Last Fuel Filling Record if OdodMetereReading is Same*/
            /* if current odometer is less then last and filldate is Greater then lastdate current:show Error  */
            else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastOExpenseReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastOExpenseDate.replace("-","/"))>0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error3.2: Match Records With Last Filling Record Entered");
                return false;
            }
            /* if current odometer is Greater then last and filldate is less then lastdate current:show Error  */
            else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastOExpenseReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastOExpenseDate.replace("-","/"))<0)){
                Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                resultTv.setText("Error4.2: Match Records With Last Filling Record Entered");
                return false;
            }

            else if(dateDiffrence(year+"/"+(month+1)+"/"+day,oExpensedate.replace("-","/"))<0){
                Toast.makeText(this, "Check Your Date ",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        else if(lastDataType.equals("otherExpense")){
            return proceedSubmitOtherExpenseDataOnVerification("otherExpenseData");
            //if(proceedSubmitFillingDataOnVerification("fuelData")==false){ return false;}
        }else{
            if(proceedSubmitOtherExpenseDataOnVerification("otherExpenseData")==false){
                return false;
            }
            else{
                if(lastDataReading.equals("0")){return true;}
                /*Validating With Last Record */
                /* if current odometer is less then last and filldate is Greater then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)<Integer.parseInt(lastDataReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error1.1: Match Records With Last Record Entered");
                    return false;
                }
                /* if current odometer is Greater then last and filldate is less then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)>Integer.parseInt(lastDataReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error2.1: Match Records With Last Record Entered");
                    return false;
                }
                /*Validating With Last Record if OdodMetereReading is Same*/
                /* if current odometer is less then last and filldate is Greater then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastDataReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error1.2: Match Records With Last Record Entered");
                    return false;
                }
                /* if current odometer is Greater then last and filldate is less then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)==Integer.parseInt(lastDataReading)) && (dateDiffrence(oExpensedate.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error2.2: Match Records With Last Record Entered");
                    return false;
                }
                return true;
            }
        }
    }
}
