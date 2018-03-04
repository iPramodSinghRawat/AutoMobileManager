package com.alpha.ghosty.automobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddInsurance extends SuperActivity {

    EditText odoMtrRdingET, providerET, insuranceDateBeginET, insuranceDateEndET, costET, detailsET;
    RadioGroup receiptOptRG;
    RadioButton picOption, takePhoto;//, noNeed;
    ImageView receiptView;
    TextView selectVehicle,resultTv,typeOfInsuranceTV,lastRecordTV;

    //private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    String vehicleId, provider, typeOfInsurance, odoMeterReading, insuranceDateBegin,
            insuranceDateEnd,cost, details, picOptn="0",lastDataType, lastDataReading="0",
            lastDataDate="0/0/0", vDateOfPurchase="0/0/0",lastInsuranceReading="0",
            lastInsuranceBeginDate="0/0/0", lastInsuranceEndDate="0/0/0";

    ArrayAdapter<String> insuranceType,userVehicleLists;

    //private String m_Text = "";

    AlertDialog.Builder builderSingle;

    VehicleData[] objectAry;

    DBhandler dbHandler;

    int dateEtId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insurance);
        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        //
        providerET = (EditText) findViewById(R.id.providerET);
        odoMtrRdingET = (EditText) findViewById(R.id.odoMtrRdingET);
        insuranceDateBeginET = (EditText) findViewById(R.id.insuranceDateBeginET);
        insuranceDateEndET = (EditText) findViewById(R.id.insuranceDateEndET);
        costET = (EditText) findViewById(R.id.costET);
        detailsET = (EditText) findViewById(R.id.detailsET);

        receiptOptRG = (RadioGroup) findViewById(R.id.receiptOptRG);
        takePhoto = (RadioButton) findViewById(R.id.takePhoto);

        receiptView = (ImageView) findViewById(R.id.receiptView);

        selectVehicle = (TextView) findViewById(R.id.selectVehicle);
        lastRecordTV = (TextView) findViewById(R.id.lastRecordTV);
        typeOfInsuranceTV = (TextView) findViewById(R.id.typeOfInsuranceTV);
        resultTv = (TextView) findViewById(R.id.resultTv);

        insuranceType = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        insuranceType.add("Comprehensive");
        insuranceType.add("Liability Insurance");
        insuranceType.add("Third Party");
        insuranceType.add("other");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //insuranceDateBeginET.setText(day+ " / " + (month + 1) + " / " + year);
        //insuranceDateBegin=year+"-"+(month + 1)+"-"+day;

        //insuranceDateEndET.setText(day + " / " + (month + 1) + " / " + (year+1));
        //insuranceDateEnd=(year+1)+"-"+(month + 1)+"-"+day;

        insuranceDateEndET.setKeyListener(null);
        insuranceDateBeginET.setClickable(true);
        insuranceDateBeginET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                dateEtId=v.getId();
                showDialog(v.getId());
            }
        });
        insuranceDateBeginET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateEtId=v.getId();
                    showDialog(v.getId());
                } else {
                }
            }
        });

        insuranceDateEndET.setKeyListener(null);
        insuranceDateEndET.setClickable(true);
        insuranceDateEndET.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                dateEtId=v.getId();
                showDialog(v.getId());
            }
        });
        insuranceDateEndET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateEtId=v.getId();
                    showDialog(v.getId());
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

        if(dtLen==1){
            vehicleId = objectAry[0].getId();
            vDateOfPurchase = objectAry[0].getDateOfPurchase();
            fetchLastInsurance(vehicleId);
            selectVehicle.setText(userVehicleLists.getItem(0) + "");
        }else{selectVehicle(null);}

    }

    /* Function: To Select Vehicle For Which Service Done */
    public void selectVehicle(View view){
        builderSingle = new AlertDialog.Builder(this);
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
                        fetchLastInsurance(vehicleId);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                    }
                });
        builderSingle.show();
    }

    public void fetchLastInsurance(String vehicleId){

        String[] lastData;//=new String[4];

        lastData=dbHandler.getLastData(vehicleId);
        lastDataType=lastData[0];
        lastDataReading=lastData[1];
        lastDataDate=lastData[2];
        //String pricePaid=lastData[3];

        lastRecordTV.setText("Last Record:\n Type:"+ lastData[0] +
                "\n at:"+ lastDataReading +
                "\n Date: " + date2Show(lastDataDate) +
                "\n Paid: " + lastData[3]);

        InsuranceData object=dbHandler.getLastInsuranceData(vehicleId);
        //FillingData objectt=dbHandler.getLastFillingData(vehicleId);
        lastInsuranceReading=object.getMeterReading();
        lastInsuranceBeginDate=object.getDateBegin();
        lastInsuranceEndDate=object.getDateEnd();
        //lastServiceDate=object.getDate();

        //lastInsuranceReading="1190";//Testing data
        //lastInsuranceBeginDate="2008-4-28";//Testing data
        //lastInsuranceEndDate="2009-4-28";//Testing data

        lastRecordTV.setText(lastRecordTV.getText()+
                "\n\n Last Insurance Done at : " + lastInsuranceReading
                +"\n On: " + date2Show(lastInsuranceBeginDate)+" To: " + date2Show(lastInsuranceEndDate));


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lastInsuranceReading", lastInsuranceReading); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastInsuranceBeginDate", lastInsuranceBeginDate); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("lastInsuranceEndDate", lastInsuranceEndDate); ///*Saving Data To Protect Change on Camera Intent/
        editor.putString("vehicleId", vehicleId); ///*Saving Data To Protect Change on Camera Intent/
        editor.commit();

    }

    public void submitInsuranceData(View view){
        //processSubmitInsuranceData();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processSubmitInsuranceData();
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

    public void processSubmitInsuranceData(){

        if(sharedpreferences.getString("lastInsuranceReading", null)!=null){
            lastInsuranceReading = sharedpreferences.getString("lastInsuranceReading", null);
        }
        if(sharedpreferences.getString("lastInsuranceBeginDate", null)!=null){
            lastInsuranceBeginDate = sharedpreferences.getString("lastInsuranceBeginDate", null);
        }
        if(sharedpreferences.getString("lastInsuranceEndDate", null)!=null){
            lastInsuranceEndDate = sharedpreferences.getString("lastInsuranceEndDate", null);
        }
        if(sharedpreferences.getString("vehicleId", null)!=null){
            vehicleId = sharedpreferences.getString("vehicleId", null);
        }
        if(sharedpreferences.getString("typeOfInsurance", null)!=null){
            typeOfInsurance = sharedpreferences.getString("typeOfInsurance", null);
        }

        provider=providerET.getText().toString();
        odoMeterReading=odoMtrRdingET.getText().toString();
        //nxtService=nxtServiceET.getText().toString();
        //date=dateET.getText().toString();
        cost=costET.getText().toString();
        details=detailsET.getText().toString();

        String picOptionVl="none";
        //serviceDate=date;
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

        if(vehicleId==null || vehicleId.equals("")){
            Toast.makeText(this, "Select A Vehicle", Toast.LENGTH_SHORT).show();
            resultTv.setText("Error: Select A Vehicle ");
        }else if(typeOfInsurance==null || typeOfInsurance.equals("")){
            Toast.makeText(this, "Select A Type of Insurance", Toast.LENGTH_SHORT).show();
            resultTv.setText("Error: Select A Type of Insurance");
            selectInsuranceType(null);
        }else if(odoMeterReading.equals("")) {
            odoMtrRdingET.setError("Enter OdoMeter Reading !");
            odoMtrRdingET.setHint("OdoMeter Reading From Your Vehicle DashBoard");
            resultTv.setText("Error: Enter OdoMeter Reading !");
        }else if(provider==null || provider.equals("")) {
            providerET.setError("Enter Insurance Provider");
            providerET.setHint("Insurance Provider");
            resultTv.setText("Error: Enter Insurance Provider !");
        }else if (cost==null || cost.equals("")) {
            costET.setError("Enter Price Paid");
            costET.setHint("Price Paid");
            resultTv.setText("Error: Enter How Must Paid !");
        }else if (details==null || details.equals("")) {
            detailsET.setError("Enter Details Paid");
            detailsET.setHint("Details");
            resultTv.setText("Error: Enter Details !");
        }else if(dateDiffrence(insuranceDateBegin.replace("-","/"),vDateOfPurchase.replace("-","/"))<0){
            Toast.makeText(this, "Check Your Begin Date, Verify With Vehicle Date of Purchase",Toast.LENGTH_LONG).show();
            resultTv.setText("Error: Check Your Begin Date, Verify With Vehicle Date of Purchase");
        }else if(dateDiffrence(insuranceDateBegin.replace("-","/"),insuranceDateEnd.replace("-","/"))>=0){
            Toast.makeText(this, "Check Your Dates",Toast.LENGTH_LONG).show();
            resultTv.setText("Error: Check Your Dates Begin Date Must be Smaller");
        }
        //else if(proceedSubmitInsuranceDataOnVerification("Insurance")==false){ }
        /*else if(proceedSubmitInsuranceDataOnVerification("other")==false){
        }
        */
        else{
            resultTv.setText("Data OK ");

            InsuranceData object=new InsuranceData(vehicleId,provider,typeOfInsurance,insuranceDateBegin,insuranceDateEnd,odoMeterReading,cost,details,picOptn);
            boolean rtn=dbHandler.putInsuranceData(object);

            if(rtn==true){
                //String file_name = "testingService";// Returning ID
                InsuranceData object2=dbHandler.getLastInsuranceData("0");
                resultTv.setText( "Data Inserted: "
                        + "\n ID: "+object2.getId()
                        + "\n VehicleId: " + object2.getVehicleId()
                        + "\n Type: " + object2.getType()
                        + "\n OdoMeterReading: " + object2.getMeterReading()
                        + "\n Date Begin: " + object2.getDateBegin()
                        + "\n Date End: " + object2.getDateEnd()
                        + "\n Cost: " + object2.getCost()
                        + "\n Detail: " + object2.getDetails()
                        + "\n Receipt: " + object2.getReceipt()
                        + "\n AddedOn: " + object2.getAddedOn());
//                                + "\n NextService: " + object2.getNextService()

                if(picOptn.equals("1")){
                    String fileName = "insurance_receipt_" + vehicleId + "_" + object2.getId();
                    //moveImageFile(file_name);
                    moveImageFile(fileName,receiptView,resultTv);
                    ///* Submit and Redirect To Preview Page with ID
                    //set text_pref shered preference null
                    //SharedPreferences sharedpreferences = getSharedPreferences("text_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("file_path");
                    editor.commit();
                }

                Intent intent = new Intent(this, InsuranceRecordView.class);
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
		editor.remove("lastInsuranceReading");
		editor.remove("lastInsuranceBeginDate");
		editor.remove("lastInsuranceEndDate");
		editor.remove("vehicleId");
		editor.remove("typeOfInsurance");
		editor.commit();
	}

    public void selectInsuranceType(final View view){

        //Toast.makeText(this, "You Set: ", Toast.LENGTH_LONG).show();
        //usrCurrencyTxt.setText("Rs hhgfd");
        builderSingle = new AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Insurance Type");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOfInsuranceTV.setText("Select Insurance Type (Tap Here)");
                        //typeOfService = null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(insuranceType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeOfInsurance = insuranceType.getItem(which);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        if (typeOfInsurance.equals("other")) {
                            enterInsuranceType(view);
                        } else {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("typeOfInsurance", typeOfInsurance); ///*Saving Data To Protect Change on Camera Intent/
                            editor.commit();
                            typeOfInsuranceTV.setText("Insurance Type: " + typeOfInsurance + " (Tap To Change)");
                        }
                    }
                });
        builderSingle.show();
    }
    public void enterInsuranceType(View view){
        //final String[] mText = new String[1];
        //Builder
        builderSingle = new AlertDialog.Builder(this);

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
                typeOfInsurance = input.getText().toString();
                //typeOfInsurance = m_Text;
                typeOfInsuranceTV.setText("Insurance Type: " + typeOfInsurance + " (Tap To Change)");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("typeOfInsurance", typeOfInsurance); ///*Saving Data To Protect Change on Camera Intent/
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
 /*       if(dateEtId == R.id.insuranceDateBeginET || dateEtId == R.id.insuranceDateEndET){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);  //date is dateSetListener as per your code in question
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            //return new DatePickerDialog(this, datePickerListener, year, month, day);
            return datePickerDialog;
            //return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        else{
            //TimePickerDialog timePickerDialog= new TimePickerDialog(this, timePickerListener, hour, min, false);
            //timePickerDialog.getT
            //return new TimePickerDialog(this, timePickerListener, hour, min, false);
        }

*/
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);  //date is dateSetListener as per your code in question
        if(dateEtId == R.id.insuranceDateBeginET){
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        //return new DatePickerDialog(this, datePickerListener, year, month, day);
        return datePickerDialog;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            //Toast.makeText(getApplicationContext(), Integer.toString(view.getId()), Toast.LENGTH_LONG).show();
            if (dateEtId == R.id.insuranceDateBeginET) {
                insuranceDateBeginET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
                insuranceDateBegin=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;

            } else{
                insuranceDateEndET.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
                insuranceDateEnd=selectedYear+"-"+(selectedMonth + 1)+"-"+selectedDay;
            }
            //Toast.makeText(getApplicationContext(), Integer.toString(R.id.dateOfDepartureET), Toast.LENGTH_LONG).show();
        }
    };


    /* Function to select or take Image from camera */
    //private void selectImage(View view) {
    public void selectImage(View view) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
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
                    startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

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

    /*start: To Be ReEvaluate: Blow are Functions to Validate Input Record: NotUsing Right Now ReEvaluation */
    public boolean proceedSubmitInsuranceDataOnVerification(String dataType){

        if(dataType.equals("insuranceData")){
            if(lastInsuranceReading.equals("0")){return true;}

            //resultTv.setText(lastInsuranceBeginDate.replace("-","/")+"-"+insuranceDateEnd.replace("-","/")+" ="+(dateDiffrence(lastInsuranceBeginDate.replace("-","/"),insuranceDateEnd.replace("-","/"))));
            //if entrin old record the end date must be less then lastbegin date and odo meter mut be less then
            else if((dateDiffrence(lastInsuranceBeginDate.replace("-","/"),insuranceDateEnd.replace("-","/"))<0)&& (Integer.parseInt(odoMeterReading)<Integer.parseInt(lastInsuranceReading))){
                Toast.makeText(this, "1.1 Check Your Insurance Date Verify with Last Insurance Records",Toast.LENGTH_LONG).show();
                resultTv.setText("1.1 Check Your Insurance Date Verify with Last Insurance Records");
                return false;
            }//if entrin New record the begin date must be greater then lstend date and odo meter mut be greater then last
            else if((dateDiffrence(insuranceDateBegin.replace("-","/"),lastInsuranceEndDate.replace("-","/"))<0)&& (Integer.parseInt(odoMeterReading)>Integer.parseInt(lastInsuranceReading))){
                Toast.makeText(this, "2.1 Check Your Insurance Date Verify with Last Insurance Records",Toast.LENGTH_LONG).show();
                resultTv.setText("2.1 Check Your Insurance Date Verify with Last Insurance Records");
                return false;
            }
            return true;
        }else if(lastDataType.equals("Insurance")){
            return proceedSubmitInsuranceDataOnVerification("insuranceData");
            //if(proceedSubmitFillingDataOnVerification("fuelData")==false){ return false;}
        }else{
            if(proceedSubmitInsuranceDataOnVerification("insuranceData")==false){
                return false;
            }else{
                //resultTv.setText(insuranceDateBegin.replace("-","/")+"-"+lastDataDate.replace("-","/")+" ="+(dateDiffrence(insuranceDateBegin.replace("-","/"),lastDataDate.replace("-","/"))));
                /*Validating With Last Record */
                /* if current odometer is less then last and filldate is Greater then lastdate current:show Error  */
                if(lastDataReading.equals("0")){return true;}
                else if((Integer.parseInt(odoMeterReading)<=Integer.parseInt(lastDataReading)) && (dateDiffrence(insuranceDateBegin.replace("-","/"),lastDataDate.replace("-","/"))>0)){
                    Toast.makeText(this, "Error1.1: Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error1.1: Match Records With Last Record Entered");
                    return false;
                }
                /* if current odometer is Greater then last and filldate is less then lastdate current:show Error  */
                else if((Integer.parseInt(odoMeterReading)>=Integer.parseInt(lastDataReading)) && (dateDiffrence(insuranceDateBegin.replace("-","/"),lastDataDate.replace("-","/"))<0)){
                    Toast.makeText(this, "Error2.1: Check Your record Before Entering",Toast.LENGTH_LONG).show();
                    resultTv.setText("Error2.1: Match Records With Last Record Entered");
                    return false;
                }
                return true;
            }
        }
    }
    /* End: */
}
