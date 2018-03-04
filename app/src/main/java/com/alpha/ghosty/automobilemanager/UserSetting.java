package com.alpha.ghosty.automobilemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UserSetting extends SuperActivity {
    EditText usrNameET,usrEmailIdET,usrPasswordET,usrMobileET;

    //RadioGroup usr_currency,usr_consumption_unit;
    //RadioButton usr_currency_btn,usr_consumption_unit_btn;
    CheckBox ppCheckBox;
    TextView crncyTxtVw,cnsmptnUntTv,result_textView;
    String usrCrncy,usrCnsptn;

    RadioGroup usrProfileTypeRG;
    RadioButton usrProfileTypeRB;
    String name,email,password,mobileNo,profileType,pProtectionVl="0";
    DBhandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        dbHandler = new DBhandler(this, null, null, 1);

        usrProfileTypeRG = (RadioGroup) findViewById(R.id.usrProfileTypeRG);
        /*name,email,password,mobile_no,currency,consumption_unit(Mile or Kilometer)*/
        usrNameET = (EditText) findViewById(R.id.usrNameET);
        usrEmailIdET = (EditText) findViewById(R.id.usrEmailIdET);
        usrPasswordET = (EditText) findViewById(R.id.usrPasswordET);
        usrMobileET = (EditText) findViewById(R.id.usrMobileET);

        //usr_currency = (RadioGroup)findViewById(R.id.usr_currency);
        //usr_consumption_unit = (RadioGroup)findViewById(R.id.usr_consumption_unit);

        ppCheckBox = (CheckBox) findViewById(R.id.ppCheckBox);

        crncyTxtVw = (TextView) findViewById(R.id.crncyTxtVw);
        cnsmptnUntTv = (TextView) findViewById(R.id.cnsmptnUntTv);

        result_textView = (TextView) findViewById(R.id.result_textView);

        //DBhandler dbHandler = new DBhandler(this, null, null, 1);
        UserData uData = dbHandler.getUserData();

        profileType = uData.getPrfileType();
        usrNameET.setText(uData.getName());
        usrEmailIdET.setText(uData.getEmail());
        usrPasswordET.setText(uData.getPassword());
        usrMobileET.setText(uData.getMobileNo());

        usrCrncy = uData.getCurrency();
        crncyTxtVw.setText(usrCrncy);
        usrCnsptn = uData.getConsumptionUnit();
        cnsmptnUntTv.setText(usrCnsptn);
        String ppChk = uData.getpProtection();

        //Toast.makeText(getBaseContext(), "profileType: "+profileType, Toast.LENGTH_LONG).show();

        if (ppChk.equals("1")) {
            ppCheckBox.setChecked(!ppCheckBox.isChecked());
        }
        int count = usrProfileTypeRG.getChildCount();
        for (int i=0;i<count;i++) {
            View o = usrProfileTypeRG.getChildAt(i);
            int rbId=o.getId();
            RadioButton rBtn = (RadioButton) findViewById(rbId);
            String pType = rBtn.getText().toString();

            if(pType.equals(profileType)){
                rBtn.setChecked(true);
            }
        }
    }

    public void updateUserDetail(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processUpdateUserDetail();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        //Toast.makeText(getBaseContext(), "No Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void processUpdateUserDetail(){
        //Toast.makeText(this, "processUpdateUserDetail", Toast.LENGTH_LONG).show();

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
            usrPasswordET.setError("Enter Password!");
            usrPasswordET.setHint("Password for This App");
        }else if (mobileNo.equals("")) {
            usrMobileET.setError("Enter Mobile No!");
            usrMobileET.setHint("Your Mobile Number");
        }else {

            UserData object = new UserData(name, email, password, mobileNo, profileType, usrCrncy, usrCnsptn, pProtectionVl);
            String rtn = dbHandler.updateUserData(object);
            if(rtn.equals("1")){
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Updated ");
                alertDialog.setMessage("Data Updated");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                    /* Redirecting to main page After User Registration */
                                //Intent intent = new Intent(getBaseContext(),Main_Page.class);
                                    /* Redirecting to add_vehicles_detail After User Registration to add First Vehicle detail */
                                Intent intent = new Intent(getBaseContext(), MenuPage.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDialog.show();
            }else{
                result_textView.setText("Error Try Again : "+ rtn);
                Toast.makeText(getBaseContext(), "Error Try Again : "+ rtn, Toast.LENGTH_LONG).show();
            }
        }

    }
}
