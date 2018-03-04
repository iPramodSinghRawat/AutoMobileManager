package com.alpha.ghosty.automobilemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class DataBackupRestore extends SuperActivity {
    TextView resultTv;
    DBhandler dbHandler;
    //private static final int FILE_SELECT_CODE = 1;

    String packageName;//="com.alpha.ghosty.automobilemanager";

    File sd = Environment.getExternalStorageDirectory();
    File data = Environment.getDataDirectory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_backup_restore);

        dbHandler = new DBhandler(this, null, null, 1);

        packageName=this.getPackageName();

        resultTv = (TextView) findViewById(R.id.resultTv);

        int uChk=dbHandler.check_user();
        //Toast.makeText(this, Integer.toString(uChk), Toast.LENGTH_SHORT).show();
        Intent intent;
        if(uChk==0){
            intent = new Intent(getApplicationContext(),UserRegistration.class);
            startActivity(intent);
            finish();
        }

    }

    public void doCompleteBackUp(View view){
        String backupToFolder="autoMobileManager_BackUp.zip";

        //String backupFromFolder = ".AutoMobileManager";
        String backupFromFolder = appDataFolder;

        String resultStrng = dbHandler.exportDB(backupFromFolder);
        resultTv.setText(resultStrng);
        //Toast.makeText(getApplicationContext(), "DB Export Result: \n " +resultStrng, Toast.LENGTH_SHORT).show();

        //File backupToDir = new File(sd + "/"+backupToFolder);
        //File backupFromDir = new File(sd + "/"+backupFromFolder);

        ZipUtils appZip = new ZipUtils(sd+"/"+backupFromFolder);
        appZip.generateFileList(new File(sd + "/"+backupFromFolder));
        String result=appZip.zipIt(sd + "/"+backupToFolder, this);

        resultTv.setText(result);
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }
/*
    public void importDb(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a DB File to Import"),FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }

    }

    public void processImportDb(Uri uri){
        String resultStrng = dbHandler.importDB(new File(uri.getPath()));

        if(resultStrng.equals("ok")){
            resultTv.setText("DataBase Imported !!!");
            Toast.makeText(getApplicationContext(), "DB Imported !!!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, OtherOptions.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
                }
                break;
        }
    }

    public void exportDb(View view){
        //exportDB
        String resultStrng = dbHandler.exportDB("autoMobileManagerDB");// Exporting Db to autoMobileManagerDB Directory
        resultTv.setText(resultStrng);
        //Toast.makeText(getApplicationContext(), "DB Export Result: \n " +resultStrng, Toast.LENGTH_LONG).show();
    }
*/
}
