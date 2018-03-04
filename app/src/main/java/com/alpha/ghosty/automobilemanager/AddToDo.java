package com.alpha.ghosty.automobilemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddToDo extends SuperActivity {

    DBhandler dbHandler;

    TextView selectVehicle,resultTv;

    EditText titleET,detailET;

    ArrayAdapter<String> userVehicleLists;
    VehicleData[] objectAry;
    AlertDialog.Builder builderSingle;

    String vehicleId,title,detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        dbHandler = new DBhandler(this, null, null, 1);
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        titleET = (EditText) findViewById(R.id.titleET);
        detailET = (EditText) findViewById(R.id.detailET);

        selectVehicle = (TextView) findViewById(R.id.selectVehicle);
        resultTv = (TextView) findViewById(R.id.resultTv);

        objectAry=dbHandler.userVehicleDetails();
        int dtLen=objectAry.length;
        userVehicleLists = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);

        for(int l=0;l<dtLen;l++){
            userVehicleLists.add(objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+") Type:"+objectAry[l].getType());
        }

        if(dtLen==1){
            vehicleId = objectAry[0].getId();
            selectVehicle.setText(userVehicleLists.getItem(0) + "");
        }else{selectVehicle(null);}
    }

    /* Function: To Select Vehicle For Which Fuel Filling Data Filling */
    public void selectVehicle(View view){
        builderSingle = new AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Vehicle");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //selectVehicle.setText("Select Vehicle (Tap Here)");
                        vehicleId = null;
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(userVehicleLists,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vehicleId = objectAry[which].getId();
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                        //Toast.makeText(getBaseContext(),"vDateOfPurchase"+vDateOfPurchase,Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.show();
    }


    public void addToDo(View view){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processAddToDo();
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

    public void processAddToDo(){

        title=titleET.getText().toString();
        detail=detailET.getText().toString();

        if(sharedpreferences.getString("vehicleId", null)!=null){
            vehicleId = sharedpreferences.getString("vehicleId", null);
        }

        if(vehicleId==null || vehicleId.equals("")){
            Toast.makeText(this, "Select A Vehicle ",Toast.LENGTH_SHORT).show();
        }else if (title==null || title.equals("")) {
            titleET.setError("Enter Title !");
            titleET.setHint("Title of ToDo");
        }else if (detail==null || detail.equals("")) {
            detailET.setError("Enter Part Name !");
            detailET.setHint("Part Name");
        }else{
            ToDo object=new ToDo(vehicleId,title,detail);

            if(dbHandler.putToDoData(object)==true){
                Toast.makeText(this, "ToDo Added",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ToDos.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ToDos.class));
        //finish();
    }
}
