package com.alpha.ghosty.automobilemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UserRegistration extends SuperActivity {

    File sd = Environment.getExternalStorageDirectory();
    File data = Environment.getDataDirectory();

    //String[] currency;
    int[] currencySymb;
    AlertDialog.Builder builderSingle;
    ArrayAdapter<String> currencyAdapter,mUnitAdapter;
    String name,email,password,mobileNo,profileType,usrCurrency="",usrConsumptionUnit,pProtectionVl="0";

    RadioGroup usrProfileTypeRG;
    RadioButton usrProfileTypeRB;
    EditText usrNameET,usrEmailIdET,usrPasswordET,usrMobileET;
    CheckBox ppCheckBox;
    TextView usrCurrencyTV,measurementUnitTV,resultTv;//,pageSubTitle;
    DBhandler dbHandler;
    private static final int FILE_SELECT_CODE = 1;
    private static final int ZIP_FILE_SELECT_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        dbHandler = new DBhandler(this, null, null, 1);

        usrProfileTypeRG = (RadioGroup) findViewById(R.id.usrProfileTypeRG);

        usrNameET = (EditText) findViewById(R.id.usrNameET);
        usrEmailIdET = (EditText) findViewById(R.id.usrEmailIdET);
        usrPasswordET = (EditText) findViewById(R.id.usrPasswordET);
        usrMobileET = (EditText) findViewById(R.id.usrMobileET);

        ppCheckBox = (CheckBox) findViewById(R.id.ppCheckBox);

        usrCurrencyTV = (TextView) findViewById(R.id.usrCurrencyTV);
        measurementUnitTV = (TextView) findViewById(R.id.measurementUnitTV);
        resultTv = (TextView) findViewById(R.id.resultTv);

        currencySymb=new int[6];
        currencySymb[0]=R.string.INR;
        currencySymb[1]=R.string.USD;
        currencySymb[2]=R.string.GBP;
        currencySymb[3]=R.string.JPY;
        currencySymb[4]=R.string.CAD;
        currencySymb[5]=R.string.EUR;

        currencyAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);
        currencyAdapter.add("INR"); currencyAdapter.add("USD"); currencyAdapter.add("GBP");
        currencyAdapter.add("JPY"); currencyAdapter.add("CAD"); currencyAdapter.add("EUR");

        mUnitAdapter=new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);
        mUnitAdapter.add("KM"); mUnitAdapter.add("MILE");
    }

    public void uploadPreviousData(View view){
/*
        Intent intent = new Intent();

        intent.setType("application/zip");
        //intent.setType("file/ZIP");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select"), ZIP_FILE_SELECT_CODE);
*/

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("application/zip");
        //intent.setType("application/ZIP");

/*      intent.setType("file/zip");
        intent.setType("file/ZIP");
*/
        //intent.setType("*/*");
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a ZIP File to Upload"),ZIP_FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",Toast.LENGTH_SHORT).show();
        }

    }

    public void processUploadPreviousData(Uri uri){

        //unzip(uri.toString(),sd + "/"+"test");
        try {
            new ZipUtils().unZip(new File(uri.getPath()),new File(sd + "/"+appDataFolder));;
            //unZip(new File(uri.getPath()),new File(sd + "/"+".AutoMobileManager"));
            File file = new File(sd + "/"+appDataFolder+"/"+DBhandler.DATABASE_NAME);
            if(file.exists()){
                Toast.makeText(this, "DB File Exist",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "DB File Does Not Exist",Toast.LENGTH_SHORT).show();
                resultTv.setText("DB File Does Not Exist");
            }
            processImportDb(Uri.parse(sd + "/"+appDataFolder+"/"+DBhandler.DATABASE_NAME));

        } catch (IOException e) {
            e.printStackTrace();
            resultTv.setText(e.getMessage());
        }
        //String resultStrng = dbHandler.importDB(new File(uri.getPath()));
    }

    /*
    public void importDb(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a DB File to Import"),FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",Toast.LENGTH_SHORT).show();
        }
    }*/

    public void processImportDb(Uri uri){
        String resultStrng = dbHandler.importDB(new File(uri.getPath()));

        if(resultStrng.equals("ok")){
            resultTv.setText("DataBase Imported !!!");
            Toast.makeText(getApplicationContext(), "DB Imported !!!", Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else{
            resultTv.setText(resultStrng);
            Toast.makeText(getApplicationContext(), "DB Imported Result: \n " +resultStrng, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getApplicationContext(), "onActivityResult", Toast.LENGTH_LONG).show();
        // TODO Auto-generated method stub
        switch(requestCode){
            case FILE_SELECT_CODE:
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    //String FilePath = data.getData().getPath();
                    String FilePath =uri.getPath();

                    //Toast.makeText(getApplicationContext(), "Selected File: \n " +FilePath, Toast.LENGTH_LONG).show();
                    //resultTv.setText("Selected File: \n " +FilePath);
                    //textFile.setText(FilePath);
                    //processImportDb(FilePath);
                    //processImportDb(uri);

                    String filenameArray[] = FilePath.split("\\.");
                    String extension = filenameArray[filenameArray.length-1];
                    if(extension.equals("DB") || extension.equals("db")){
                        Toast.makeText(getApplicationContext(), "Selected File: \n " +FilePath, Toast.LENGTH_LONG).show();
                        resultTv.setText("Selected File: \n " +FilePath);
                        processImportDb(uri);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Selected File: \n " +FilePath+ " is not DataBase File", Toast.LENGTH_LONG).show();
                        resultTv.setText("Selected File: \n " +FilePath+ " is not DataBase File");
                    }

                }//else{Toast.makeText(getApplicationContext(), "Selected File To  \n " +FilePath, Toast.LENGTH_LONG).show();}
            case ZIP_FILE_SELECT_CODE:
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    String FilePath = data.getData().getPath();
                    //Toast.makeText(getApplicationContext(), "Selected File: \n " +FilePath, Toast.LENGTH_LONG).show();
                    resultTv.setText("Selected File: \n " +FilePath);
                    //textFile.setText(FilePath);
                    //processImportDb(FilePath);
                    processUploadPreviousData(uri);
                }
            break;
        }
    }

    public void submitUserDetail(View view){
        Toast.makeText(this, "submitUserDetail", Toast.LENGTH_LONG).show();

        //String name,email,password,mobileNo,profileType,usrCurrency="",usrConsumptionUnit;

        int selectedId = usrProfileTypeRG.getCheckedRadioButtonId();  // find the radiobutton by returned id
        usrProfileTypeRB = (RadioButton) findViewById(selectedId);
        profileType = usrProfileTypeRB.getText().toString();

        name=usrNameET.getText().toString();
        email=usrEmailIdET.getText().toString();
        password=usrPasswordET.getText().toString();
        mobileNo=usrMobileET.getText().toString();

        if(ppCheckBox.isChecked()){pProtectionVl="1"; }

        if (name.equals("")) {
            usrNameET.setError("Enter Name !");
            usrNameET.setHint("Your Name");
        }else if (email.equals("")) {
            usrEmailIdET.setError("Enter eMail-ID!");
            usrEmailIdET.setHint("Your e-MailID");
        }else if (password.equals("")) {
            usrEmailIdET.setError("Enter Password!");
            usrEmailIdET.setHint("Password for This App");
        }else if (mobileNo.equals("")) {
            usrMobileET.setError("Enter Mobile No!");
            usrMobileET.setHint("Your Mobile Number");
        }else if(usrConsumptionUnit==null || usrConsumptionUnit.equals("")){
            resultTv.setText("Select Consumption Unit !");
            Toast.makeText(this, " Select Consumption Unit ! ", Toast.LENGTH_SHORT).show();
        }else if(usrCurrency==null || usrCurrency.equals("")){
            resultTv.setText("Select Currency !!! ");
            Toast.makeText(this, " Select Currency !!!  ", Toast.LENGTH_SHORT).show();
        }else{

            UserData object=new UserData(name,email,password,mobileNo,profileType,usrCurrency,usrConsumptionUnit,pProtectionVl);

            String rtn=dbHandler.putUserData(object);

            if(rtn.equals("1")){
                UserData object2=dbHandler.getUserData();
                resultTv.setText("Result: Name: "+object2.getName()+" \n Email:"+object2.getEmail()+" \n Password: "+object2.getPassword()
                        +" \n Mobile No: "+object2.getMobileNo()+"\n Profile Type: " +object2.getPrfileType()
                        +"\n Currency:" +object2.getCurrency()+"\n CUnit "+object2.getConsumptionUnit()+"\n PP "+object2.getpProtection());

                //Intent intent = new Intent(getBaseContext(), MenuPage.class);
                Intent intent = new Intent(getBaseContext(), AddVehicle.class);
                startActivity(intent);
                finish();
            }else{
                resultTv.setText("Error Try Again : "+ rtn);
            }

        }

    }

    public void selectConsumptionUnit(View view){
        builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.measurement_unit);
        builderSingle.setTitle("Select Consumption Unit");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        measurementUnitTV.setText("Select Measurement Unit(Tap Here)");
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(mUnitAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usrConsumptionUnit= mUnitAdapter.getItem(which);
                        measurementUnitTV.setText("Selected Measurement Unit: "+ usrConsumptionUnit+" (Tap To Change)");
                    }
                });
        builderSingle.show();
    }

    public void selectCurrency(View view){
        //Toast.makeText(this, "You Set: ", Toast.LENGTH_LONG).show();
        //usrCurrencyTxt.setText("Rs hhgfd");
        builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Currency");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usrCurrencyTV.setText("Select Currency(Tap Here)");
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(currencyAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = currencyAdapter.getItem(which);
                        usrCurrency=getResources().getString(currencySymb[which]);
                        usrCurrencyTV.setText("Selected Currency: " + strName+" ("+ usrCurrency+") (Tap To Change)");
                    }
                });
        builderSingle.show();
    }
}
