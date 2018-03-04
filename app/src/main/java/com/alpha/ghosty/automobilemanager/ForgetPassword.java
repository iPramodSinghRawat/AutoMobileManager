package com.alpha.ghosty.automobilemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassword extends SuperActivity {
    EditText usrEmailIdET,usrMobileET;
    String email,mobileNo;
    TextView resultTV;
    DBhandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        dbHandler = new DBhandler(this, null, null, 1);

        usrEmailIdET = (EditText) findViewById(R.id.usrEmailIdET);
        usrMobileET = (EditText) findViewById(R.id.usrMobileET);
        resultTV = (TextView) findViewById(R.id.resultTv);
    }

    public void checkUser(View view){

        email=usrEmailIdET.getText().toString();
        mobileNo=usrMobileET.getText().toString();

        if (email==null || email.equals("")) {
            usrEmailIdET.setError("Enter eMail-ID!");
            usrEmailIdET.setHint("Your e-MailID");
        }
        else if (mobileNo==null || mobileNo.equals("")) {
            usrMobileET.setError("Enter Mobile No!");
            usrMobileET.setHint("Your Mobile Number");
        }
        else{
            UserData uData = dbHandler.getUserData();
            if(email.equals(uData.getEmail()) && mobileNo.equals(uData.getMobileNo())){
                redirect2UserSetting();
            }
            else{
                Toast.makeText(getBaseContext(),"Incorrect Your eMail id OR Mobile Number !!!", Toast.LENGTH_LONG).show();
                resultTV.setText("Incorrect Your eMail id OR Mobile Number !!!");
            }
        }

    }

    public void redirect2UserSetting(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Redirecting ");
        alertDialog.setMessage("Redirecting to Setting Page to Change Password ");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),UserSetting.class));
                        finish();
                    }
                });
        alertDialog.show();
    }

}
