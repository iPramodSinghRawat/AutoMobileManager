package com.alpha.ghosty.automobilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OtherOptions extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_options);
    }

    public void redirect2DataBackupRestore(View view){
        startActivity(new Intent(this,DataBackupRestore.class));
        finish();
    }

    public void redirect2UserSetting(View view){
        startActivity(new Intent(this,UserSetting.class));
        finish();
    }

    public void redirect2SpareParts(View view){
        startActivity(new Intent(this,SpareParts.class));
        finish();
    }

    public void redirect2ToDo(View view){
        startActivity(new Intent(this,ToDos.class));
        finish();
    }
}
