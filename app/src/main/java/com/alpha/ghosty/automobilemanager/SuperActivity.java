package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by iPramodSinghRawat on 09-12-2015.
 */
public class SuperActivity extends AppCompatActivity {

    final public String packageName = this.getClass().getPackage().getName();

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String appDataFolder=".AutoMobileManager";

    SharedPreferences sharedpreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_us) {
            redirectaboutUs(item.getActionView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        this.finish();

        String crrntActvty=this.getClass().getSimpleName();

        //Toast.makeText(getApplicationContext(), "current: "+crrntActvty+" lnchrActivity: "+lnchrActvty,Toast.LENGTH_LONG).show();

        /*Close if Current and Launcher activity is same */
        //if(current.equals("MainActivity")){
        if(crrntActvty.equals("MenuPage")
                || crrntActvty.equals("AppLock")
                || crrntActvty.equals("UserRegistration")
                || crrntActvty.equals("ForgetPassword")){

            AppExit();

        }else if(crrntActvty.equals("DataBackupRestore")
                || crrntActvty.equals("UserSetting")
                || crrntActvty.equals("SpareParts")
                || crrntActvty.equals("ToDos")){

            startActivity(new Intent(this,OtherOptions.class));
            //finish();

        }else{

            Intent intent = new Intent(getBaseContext(),MenuPage.class);
            startActivity(intent);
            finish();
        }
        //AppExit();
        //super.onBackPressed();
    }

    public void AppExit(){
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void appExitBtn(View view){
        this.finish();
        AppExit();
    }

    public void redirectaboutUs(View view){
        //Toast.makeText(getApplicationContext(), " redirectaboutUs ", Toast.LENGTH_SHORT).show();
        String crrntActvty=this.getClass().getSimpleName();
        Intent intent = new Intent(this, AboutUs.class);
        intent.putExtra("caller", crrntActvty);
        startActivity(intent);

        //startActivity(new Intent(this,AboutUs.class));

    }

    /* Function to Show Images */
    public void show_image_file(ImageView imageBox, File imageFile){
        try {
            FileInputStream in = new FileInputStream(imageFile);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 10;

            //String imagePath = imageFile.getAbsolutePath();
            //resultTv.setText(imagePath);
            Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
            imageBox.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            //resultTv.setText(e.getMessage());
        }
    }

    public void moveImageFile(String fileName, ImageView receiptView, TextView resultTv){
        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);
        String fileUri = sharedpreferences.getString("file_path", null);

        File imgFile=new File(Uri.parse(fileUri).getPath());

        String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+appDataFolder+"/"+fileName+".jpg";
        File destination = new File(destinationPath);

        String rsltRtn=moveFileToProjectFolder(imgFile, destination);
        if(rsltRtn.equals("1")){
            show_image_file(receiptView, destination);
        }else{
            resultTv.setText(rsltRtn+" "+destinationPath);
        }
    }

    /* Function to Move Image Files */
    public String moveFileToProjectFolder(File sourceFile,File destinationFile){
        /* Create Project data folder if not exist */
        File dataFolder = new File(Environment.getExternalStorageDirectory() + "/"+appDataFolder);
        boolean success = true;
        boolean move = true;
        if (!dataFolder.exists()){
            success = dataFolder.mkdir();
            if (success) {
                // Do something on success
                move = true;
            } else {
                // Do something else on failure
                move = false;
            }
        }
 /*       else {
            // Do something if Exist
            move = true;
        }
*/
        if(!move){
            return "Directory Does not Exist and not able to Create";
        }else{
            InputStream inStream = null;
            OutputStream outStream = null;
            try{
                inStream = new FileInputStream(sourceFile);
                outStream = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];
                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0){
                    outStream.write(buffer, 0, length);
                }
                inStream.close();
                outStream.close();
                //delete the original file
                //sourceFile.delete(); /*Uncomment Ot Delete Source File */
                /*
                if(delete){
                    //sourceFile.delete();
                }
                */
                return "1";
                //return "File is copied successful!";
            }catch(IOException e){
                e.printStackTrace();
                //tvPath.setText(e.getMessage());
                return e.getMessage();
            }
        }
    }

    /* Function: Get a Pic Using Camera */
    public void getShowImageFromCamera(Intent data,ImageView iImageView){
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        //Uri uri=new URI(Environment.getExternalStorageDirectory(),"DCIM/"+System.currentTimeMillis() + ".jpg");
        File destination = new File(Environment.getExternalStorageDirectory(),"DCIM/"+System.currentTimeMillis() + ".jpg");
        //File destination = new File(Environment.getExternalStorageDirectory(),"DCIM/test.jpg");

        Uri uri=Uri.fromFile(destination);
        String file_path=uri.toString();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        iImageView.setImageBitmap(thumbnail);

        //resultTv.setText(file_path);

        //SharedPreferences sharedpreferences = getSharedPreferences("text_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("file_path", file_path);
        editor.commit();
    }

    /* Function: Select a Pic from Gallery */
    public void selectGetShowImageFromGallery(Intent data,ImageView iImageView){
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(
                selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String selectedImagePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        iImageView.setImageBitmap(bm);

        //resultTv.setText(selectedImagePath);

        //SharedPreferences sharedpreferences = getSharedPreferences("text_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("file_path", selectedImagePath);
        editor.commit();
    }

    // Calculate dateDiffrence
    public long dateDiffrence(String dateB,String dateS){

        dateB = dateB.replaceAll("\\s+","");
        dateS = dateS.replaceAll("\\s+","");

        //Toast.makeText(getApplicationContext(), dateB+" - "+dateS,Toast.LENGTH_LONG).show();
        long diff=0;
        //String dateStart = "01/14/2012";//month//day//year
        //String dateStop = "01/15/2012";//month//day//year
        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateS);
            d2 = format.parse(dateB);
            //in milliseconds
            diff = d2.getTime() - d1.getTime();

            //Toast.makeText(getApplicationContext(), d2.getTime()+" - "+d1.getTime(),Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diff;
    }

    public String date2Show(String preDate){
        String[] date2Show = preDate.split("-");
        if(date2Show.length<2){ return preDate;}
        else{return date2Show[2]+"/"+date2Show[1]+"/"+date2Show[0];}
    }

    public String time2Show(String preTime){
        String[] time2Show = preTime.split(":");
        if(time2Show.length<2){return preTime;}
        else{
            if (Integer.parseInt(time2Show[0])>12){return pad(Integer.parseInt(time2Show[0])-12)+":"+pad(Integer.parseInt(time2Show[1]))+" PM";}
            else{
                return pad(Integer.parseInt(time2Show[0]))+":"+pad(Integer.parseInt(time2Show[1]))+" AM";
            }

        }
    }

    public String pad(int input){
        String str = "";
        if (input > 10) {
            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);
        }
        return str;
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}