package com.alpha.ghosty.automobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddSparePart extends SuperActivity {

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);

    TextView selectVehicle,resultTv;

    EditText partNoET,partNameET,partDetailsET,quantityET;

    ImageView receiptView;

    ArrayAdapter<String> userVehicleLists;
    VehicleData[] objectAry;
    AlertDialog.Builder builderSingle;

    private String vehicleId,partNo,partName,partDetails,quantity,picOptn="0";//,image,addedOn;

    RadioGroup receiptOptRG;
    RadioButton picOption,takePhoto;//noNeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spare_part);

        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        partNoET = (EditText) findViewById(R.id.partNoET);
        partNameET = (EditText) findViewById(R.id.partNameET);
        partDetailsET = (EditText) findViewById(R.id.partDetailsET);
        quantityET = (EditText) findViewById(R.id.quantityET);

        selectVehicle = (TextView) findViewById(R.id.selectVehicle);
        resultTv = (TextView) findViewById(R.id.resultTv);

        receiptView = (ImageView) findViewById(R.id.receiptView);

        receiptOptRG = (RadioGroup) findViewById(R.id.receiptOptRG);
        takePhoto = (RadioButton) findViewById(R.id.takePhoto);

        objectAry=dbHandler.userVehicleDetails();
        int dtLen=objectAry.length;
        userVehicleLists = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item);

        for(int l=0;l<dtLen;l++){
            userVehicleLists.add(objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+") Type:"+objectAry[l].getType());
        }

        if(dtLen==1){
            vehicleId = objectAry[0].getId();//.getItem(which);
            //vDateOfPurchase = objectAry[0].getDateOfPurchase();//.getItem(which);
            //fuelCapacity=objectAry[0].getFuelTankCapacity();//.getItem(which);
            //slctVId = vehicleIdAry[which];//.getItem(which);
            //fetechFillingDetails(vehicleId);
            //usrCurrency=getResources().getString(currencySymb[which]);
            selectVehicle.setText(userVehicleLists.getItem(0) + "");
        }else{selectVehicle(null);}
    }

    /* Function: To Select Vehicle For Which Fuel Filling Data Filling */
    public void selectVehicle(View view){
        builderSingle = new Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Vehicle");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //selectVehicle.setText("Select Vehicle (Tap Here)");
                        vehicleId = null;
                        //vDateOfPurchase = null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(userVehicleLists,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vehicleId = objectAry[which].getId();//.getItem(which);
                        //vDateOfPurchase = objectAry[which].getDateOfPurchase();//.getItem(which);
                        //fuelCapacity=objectAry[which].getFuelTankCapacity();//.getItem(which);
                        //slctVId = vehicleIdAry[which];//.getItem(which);
                        //fetechFillingDetails(vehicleId);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                        //Toast.makeText(getBaseContext(),"vDateOfPurchase"+vDateOfPurchase,Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.show();
    }

    public void submitSparePartData(View view){

        //processSubmitFuelFillData();
        ///*
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processSubmitSparePartData();
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

    public void processSubmitSparePartData(){

        InputMethodManager inputManager =(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        partNo=partNoET.getText().toString();
        partName=partNameET.getText().toString();
        partDetails=partDetailsET.getText().toString();
        quantity=quantityET.getText().toString();

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

        //Toast.makeText(this, "picOptn "+picOptn,Toast.LENGTH_SHORT).show();

        if(sharedpreferences.getString("vehicleId", null)!=null){
            vehicleId = sharedpreferences.getString("vehicleId", null);
        }

        if(vehicleId==null || vehicleId.equals("")){
            Toast.makeText(this, "Select A Vehicle ",Toast.LENGTH_SHORT).show();
        }else if (partNo==null || partNo.equals("")) {
            partNoET.setError("Enter Part No !");
            partNoET.setHint("Part No/Sno");
        }else if (partName==null || partName.equals("")) {
            partNameET.setError("Enter Part Name !");
            partNameET.setHint("Part Name");
        }else if (partDetails==null || partDetails.equals("")) {
            partDetailsET.setError("Enter Details !");
            partDetailsET.setHint("Part Details");
        }else if (quantity==null || quantity.equals("")) {
            quantityET.setError("Enter Quantity !");
            quantityET.setHint("Quantity");
        }else{

            SparePart object=new SparePart(vehicleId,partNo,partName,partDetails,quantity,picOptn);

            boolean rtn=dbHandler.putSparePartData(object);

            if(rtn==true){
                resultTv.setText("Data OK To Enter ");


                SparePart object2=dbHandler.getSparePartDetail("0");
                if(picOptn.equals("1")){
                    String fileName = "spare_part_image_" + vehicleId + "_" + object2.getId();
                    moveImageFile(fileName,receiptView,resultTv);
                    // Submit and Redirect To Preview Page with ID
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("file_path");
                    editor.commit();
                }

                Intent intent = new Intent(this, SpareParts.class);
                //intent.putExtra("vehicleId",vehicleId);
                startActivity(intent);
                finish();

            }else{
                resultTv.setText("Error Try Again : " + rtn);

            }
            removeSharedPreferences();
        }
    }

    public void removeSharedPreferences(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("vehicleId");
        editor.commit();
    }

    /* Function: to select or take Image from camera */
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SpareParts.class));
        //finish();
    }
}
