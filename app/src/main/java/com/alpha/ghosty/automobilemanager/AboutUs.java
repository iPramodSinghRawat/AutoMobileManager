package com.alpha.ghosty.automobilemanager;

import android.content.Intent;
import android.os.Bundle;

public class AboutUs extends SuperActivity{

    String caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        caller= getIntent().getStringExtra("caller");
        //Toast.makeText(getApplicationContext(), "caller "+caller, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        if(caller.equals("AppLock")){
            Intent intent = new Intent(getBaseContext(),AppLock.class);
            startActivity(intent);
            finish();
        }else{
            Class callerClass = null;
            try {
                callerClass = Class.forName(packageName + "." + caller);
                startActivity(new Intent(this, callerClass));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Intent intent = new Intent(getBaseContext(),MenuPage.class);
                startActivity(intent);
                finish();
            }
        }

    }
}
