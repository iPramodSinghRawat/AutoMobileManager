package com.alpha.ghosty.automobilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends SuperActivity {

    DBhandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBhandler dbHandler = new DBhandler(getApplicationContext(), null, null, 1);

        dbHandler = new DBhandler(this, null, null, 1);
        int uChk=dbHandler.check_user();
        //Toast.makeText(this, Integer.toString(uChk), Toast.LENGTH_SHORT).show();
        Intent intent;
        if(uChk==0){
            intent = new Intent(getApplicationContext(),UserRegistration.class);
        }else if(uChk==1){
            intent = new Intent(getApplicationContext(),AppLock.class);
        }else {
            intent = new Intent(getApplicationContext(),MenuPage.class);
        }

        //Intent intent = new Intent(getBaseContext(),MenuPage.class);
        //Intent intent = new Intent(getBaseContext(),UserRegistration.class);
        startActivity(intent);
        finish();
    }

}
