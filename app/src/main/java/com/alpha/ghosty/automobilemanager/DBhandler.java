/**
 * Created by iPramodSinghRawat on 09-06-2016.
 * File to Handle Database Functions
 */

package com.alpha.ghosty.automobilemanager;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DBhandler extends SQLiteOpenHelper {
    //BufferedReader reader;
    //String line;
    InputStream in;

    File sd = Environment.getExternalStorageDirectory();
    File data = Environment.getDataDirectory();

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "autoMobileManager.db";
    String packageName;

    private Context mCtx; //<-- declare a Context reference

    public DBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mCtx = context; //<-- fill it with the Context you are passed
        packageName=mCtx.getPackageName();
        //packageName=mCtx.getPackageName();
        //Toast.makeText(context,"From DB packageName: "+packageName,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String userOptionTbl = "CREATE TABLE userOption(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name VARCHAR(20)," +
                " email VARCHAR(50) NOT NULL UNIQUE," +
                " password VARCHAR(64)," +
                " mobileNo VARCHAR(14) NOT NULL UNIQUE," +
                " prfileType VARCHAR(8)," +
                " currency VARCHAR(4)," +
                " pProtection VARCHAR(2)," +
                " consumptionUnit VARCHAR(4))";
        db.execSQL(userOptionTbl);

        //userVehicles
        String userVehiclesTbl = "CREATE TABLE userVehicles(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " regNumber VARCHAR(20) NOT NULL UNIQUE," +
                " brand VARCHAR(20)," +
                " model VARCHAR(50)," +
                " dateOfPurchase date," +
                " fuelTankCapacity VARCHAR(10)," +
                " type VARCHAR(20)," +
                " color VARCHAR(10)," +
                " fuelType VARCHAR(20)," +
                " comment VARCHAR(200)," +
                " addedOn VARCHAR(64))";
        db.execSQL(userVehiclesTbl);

        String fuelFillingsTbl = "CREATE TABLE fuelFillings(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " dateOfFill DATE," +
                " type_of_fuel VARCHAR(20)," +
                " meterReading number," +
                " volume DECIMAL(10,5)," +
                " price DECIMAL(10,5)," +
                " receipt integer," +
                " addedOn VARCHAR(64))";
        db.execSQL(fuelFillingsTbl);

        String serviceRecordTbl = "CREATE TABLE serviceRecord(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " typeOfService VARCHAR(64)," +
                " meterReading number," +
                " setNxtService VARCHAR(64)," +
                " dateOfService DATE," +
                " price DECIMAL(10,5)," +
                " receipt integer," +
                " addedOn VARCHAR(64))";
        db.execSQL(serviceRecordTbl);

        String otherExpensesTbl = "CREATE TABLE otherExpenses(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " type VARCHAR(120)," +
                " meterReading number," +
                " dateOfExpense DATE," +
                " price DECIMAL(10,5)," +
                " receipt integer," +
                " addedOn VARCHAR(64))";
        db.execSQL(otherExpensesTbl);

        String tripRecordTbl = "CREATE TABLE tripRecord(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " typeOfTrip VARCHAR(64)," +
                " locationDeparture VARCHAR(64)," +
                " locationReturn VARCHAR(64)," +
                " dateOfDeparture DATE," +
                " dateOfReturn DATE," +
                " timeOfDeparture TIME," +
                " timeOfReturn TIME," +
                " meterReadingDeparture number," +
                " meterReadingReturn number," +
                " costOfTrip DECIMAL(10,5)," +
                " receipt integer," +
                " addedOn VARCHAR(64))";
        db.execSQL(tripRecordTbl);
        /**id, vehicleId typeOfTrip locationDeparture locationReturn dateOfDeparture dateOfReturn timeOfDeparture timeOfReturn
         * meterReadingDeparture  meterReadingReturn costOfTrip receipt addedOn
         */

        //insurance table
        //provider
        String insuranceRecordTbl = "CREATE TABLE insuranceRecord(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " provider VARCHAR(80)," +
                " type VARCHAR(64)," +
                " dateBegin DATE," +
                " dateEnd DATE," +
                " meterReading number," +
                " cost DECIMAL(10,5)," +
                " detail VARCHAR(124)," +
                " receipt INTEGER," +
                " addedOn VARCHAR(64))";
        db.execSQL(insuranceRecordTbl);

        String sparePartsTbl = "CREATE TABLE spareParts(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " partNo VARCHAR(20)," +
                " partName VARCHAR(40)," +
                " partDetails VARCHAR(200)," +
                " quantity INTEGER," +
                " image integer," +
                " addedOn VARCHAR(64))";
        db.execSQL(sparePartsTbl);

        String reminderTbl = "CREATE TABLE reminders(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title VARCHAR(64)," +
                " detail text," +
                " date DATE," +
                " time TIME," +
                " addedOn VARCHAR(64))";
        db.execSQL(reminderTbl);

        String toDosTbl = "CREATE TABLE toDos(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " title VARCHAR(20)," +
                " details VARCHAR(200)," +
                " addedOn VARCHAR(64))";
        db.execSQL(toDosTbl);

        createInsertFuelType(db);

/*
        String vehicleBrandsTbl = "CREATE TABLE vehicleBrands(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name VARCHAR(50) NOT NULL UNIQUE," +
                " established VARCHAR(5) NOT NULL," +
                " countryOfOrigin VARCHAR(64))";
        db.execSQL(vehicleBrandsTbl);
*/
        //bike, truck, bus , car
 /*       String vehicleTypeTbl = "CREATE TABLE vehicleType(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " type VARCHAR(10) NOT NULL UNIQUE," +
                " countryOfOrigin VARCHAR(64))";
        db.execSQL(vehicleTypeTbl);
*/
 /*       String serviceTypeTbl = "CREATE TABLE serviceType(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " type VARCHAR(20) NOT NULL UNIQUE," +
                " details VARCHAR(128))";
        db.execSQL(serviceTypeTbl);

        String expenseTypeTbl = "CREATE TABLE expenseType(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " type VARCHAR(20) NOT NULL UNIQUE," +
                " details VARCHAR(128))";
        db.execSQL(expenseTypeTbl);

        String tripTypeTbl = "CREATE TABLE tripType(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " type VARCHAR(20) NOT NULL UNIQUE," +
                " details VARCHAR(128))";
        db.execSQL(tripTypeTbl);

        String insuranceTypeTbl = "CREATE TABLE insuranceType(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " type VARCHAR(20) NOT NULL UNIQUE," +
                " details VARCHAR(128))";
        db.execSQL(insuranceTypeTbl);
*/

        //Petrol,CNG,Diesel
 /*       String fuelTypeTbl = "CREATE TABLE fuelType (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " type VARCHAR(20) NOT NULL UNIQUE," +
                " details VARCHAR(128))";
        db.execSQL(fuelTypeTbl);
*/

        /* Creating and Inserting Fuel Type Data*/

    }

    public void createInsertFuelType(SQLiteDatabase db){

        //SQLiteDatabase db = this.getWritableDatabase();
        String fuelTypeTbl = "CREATE TABLE fuelType (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " type VARCHAR(20) NOT NULL UNIQUE," +
                " details VARCHAR(128))";
        db.execSQL(fuelTypeTbl);

        ContentValues contentValues = new ContentValues();

        contentValues.put("type", "Petrol");
        contentValues.put("details","Petrol Detail");
        db.insert("fuelType", null, contentValues);

        contentValues.put("type", "Diesel");
        contentValues.put("details","Diesel Detail");
        db.insert("fuelType", null, contentValues);

        contentValues.put("type", "CNG");
        contentValues.put("details","CNG Detail");
        db.insert("fuelType", null, contentValues);

        contentValues.put("type", "Hydrogen");
        contentValues.put("details","Hydrogen Detail");
        db.insert("fuelType", null, contentValues);

        //Toast.makeText(mCtx, "createInsertFuelType", Toast.LENGTH_LONG).show();

    }

    /* Function:*/
    public String[] getFuelType() {

        ArrayList<String> flTypeAry = new ArrayList<String>();

        String query = "Select * FROM fuelType ORDER BY type DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst() || cursor.getCount() != 0) {
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    flTypeAry.add(cursor.getString(cursor.getColumnIndex("type")));
                    cursor.moveToNext();
                }
            } else {
                flTypeAry.add("No Data");
            }
        } else {
            flTypeAry.add("No Data");
        }
        cursor.close();

        return flTypeAry.toArray(new String[flTypeAry.size()]);
    }


    public void reCreateFuelFilling_Table(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS fuelFillings");

        Toast.makeText(mCtx, "1. Deleted fuelFillings", Toast.LENGTH_LONG).show();
        String fuelFillingsTbl = "CREATE TABLE fuelFillings (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " vehicleId INTEGER," +
                " dateOfFill DATE," +
                " type_of_fuel VARCHAR(64)," +
                " meterReading number," +
                " volume DECIMAL(10,5)," +
                " price DECIMAL(10,5)," +
                " receipt integer," +
                " addedOn VARCHAR(64))";
        db.execSQL(fuelFillingsTbl);
        Toast.makeText(mCtx, "2. reCreateFuelFilling_Table", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
            db.execSQL("DROP TABLE IF EXISTS insuranceRecord");
            onCreate(db);
        */
    }

    //public void exportDB(){
    //public String exportDB(String backUpToFolder){
    public String exportDB(String backUpFolder){
        //Toast.makeText(mCtx, "backUpFolder: \n " +backUpFolder, Toast.LENGTH_LONG).show();
        FileChannel source=null;
        FileChannel destination=null;
        //String backupFolder;

        /*
        if(backUpToFolder.equals("")){backupFolder="autoMobileManager";}
        else{backupFolder=backUpToFolder;}
        */

        //if(backUpFolder.equals("")){backUpFolder="autoMobileManager";}
        //else{backUpFolder=".AutoMobileManager";}

        //backUpFolder=".AutoMobileManagerd";

        String currentDBPath = "/data/"+ packageName +"/databases/"+DATABASE_NAME;
        //Toast.makeText(mCtx, packageName,Toast.LENGTH_LONG).show();
        //String currentDBPath = "/data/"+ "com.alpha.ghosty.automobilemanager" +"/databases/"+DATABASE_NAME;

        String backupDBTo = "/"+backUpFolder+"/"+DATABASE_NAME;

        File backupDir = new File(sd + "/"+backUpFolder);
        boolean success = true;
        boolean move = true;

        if (!backupDir.exists()){
            success = backupDir.mkdir();
            if (success) {
                // Do something on success
                move = true;
            } else {
                // Do something else on failure
                move = false;
            }
        }
        /*
        else{
            // Do something if Exist
            move = true;
        }
        */

        if(!move){
            ///Toast.makeText(mCtx.getApplicationContext(), "Backup Directory Does not Exist & Not able to Create", Toast.LENGTH_LONG).show();
            return "Backup Directory Does not Exist & Not able to Create";
        }
        else{

            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBTo);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                ///Toast.makeText(mCtx.getApplicationContext(), "DB Exported To 'autoMobileManager' folder !", Toast.LENGTH_LONG).show();
                return "DB Exported To '"+backUpFolder+"' folder !";

            } catch(IOException e) {
                e.printStackTrace();
                ///Toast.makeText(mCtx.getApplicationContext(), "DB IOException: "+e.getMessage(), Toast.LENGTH_LONG).show();
                return "Input Error: "+e.getMessage();
            }

        }

    }

    /**
     * Copies the database file at the specified location
     * over the current internal application database.
     * */
    //public String importDB(){// throws IOException {
    public String importDB(File dbPath){// throws IOException {
        String DB_FILEPATH = "/data/"+ packageName +"/databases/"+DATABASE_NAME;
        //String DB_FILEPATH = "/data/"+ "com.alpha.ghosty.automobilemanager" +"/databases/"+DATABASE_NAME;
        //String dbPath="/autoMobileManager/"+DATABASE_NAME;
        // Close the SQLiteOpenHelper so it will
        // commit the created empty database to internal storage.
        close();
        //File newDb = new File(sd, dbPath);
        File newDb = dbPath;
        File oldDb = new File(data, DB_FILEPATH);
        if (newDb.exists()) {
        ///Toast.makeText(mCtx.getApplicationContext(), "newDb.exists()", Toast.LENGTH_LONG).show();
            try {
                copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
                // Access the copied database so SQLiteHelper
                // will cache it and mark it as created.
                getWritableDatabase().close();
                //Toast.makeText(mCtx.getApplicationContext(), "DB Imported!", Toast.LENGTH_LONG).show();
                return "ok";
                //return true;
            } catch(IOException e) {
                e.printStackTrace();
                //Toast.makeText(mCtx.getApplicationContext(), "DB IOException! "+e.getMessage(), Toast.LENGTH_LONG).show();
                return "DB IOException! "+e.getMessage();
                //return false;
            }
        }
        else{
            //Toast.makeText(mCtx.getApplicationContext(), "Database File To Import Not Exist, Place It on Internal SD !!!", Toast.LENGTH_LONG).show();
            return "Database File To Import Not Exist, Place It on Internal SD inside 'autoMobileManager' folder !!!";
        }
        //return false;
        //return "Error, TryAgain";
    }
    public void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
    /* Function: To Collect Ids*/
    public String[] getDataIds(String type) {
        //String[] ids;
        String query;
        ArrayList<String> dataIdAry = new ArrayList<String>();

        if (type.equals("vehicle")) {
            query = "Select _id FROM userVehicles ORDER BY _id DESC";
        } else if (type.equals("fuel")) {
            query = "Select _id FROM fuelFillings ORDER BY _id DESC";
        } else if (type.equals("service")) {
            query = "Select _id FROM serviceRecord ORDER BY _id DESC";
        } else if (type.equals("trip")) {
            query = "Select _id FROM tripRecord ORDER BY _id DESC";
        }else if (type.equals("insurance")) {
            query = "Select _id FROM insuranceRecord ORDER BY _id DESC";
        } else {
            query = "Select _id FROM otherExpenses ORDER BY _id DESC";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst() || cursor.getCount() != 0) {
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    dataIdAry.add(cursor.getString(cursor.getColumnIndex("_id")));
                    cursor.moveToNext();
                }
            } else {
                dataIdAry.add("No Data");
            }
        } else {
            dataIdAry.add("No Data");
        }
        cursor.close();

        return dataIdAry.toArray(new String[dataIdAry.size()]);
    }

    /* Function: To Collect Ids*/
    public String[] getDataIdsViaVehicle(String type,String vehicleId) {
        //String[] ids;
        String query;
        ArrayList<String> dataIdAry = new ArrayList<String>();

        if (type.equals("vehicle")) {
            query = "Select _id FROM userVehicles ORDER BY _id DESC";
        } else if (type.equals("fuel")) {
            query = "Select _id FROM fuelFillings ORDER BY _id DESC";
        } else if (type.equals("service")) {
            query = "Select _id FROM serviceRecord ORDER BY _id DESC";
        } else if (type.equals("trip")) {
            query = "Select _id FROM tripRecord ORDER BY _id DESC";
        }
        else if (type.equals("insurance")) {
            query = "Select _id FROM insuranceRecord WHERE vehicleId='"+vehicleId+"' ORDER BY _id DESC";
        } else {
            query = "Select _id FROM otherExpenses ORDER BY _id DESC";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst() || cursor.getCount() != 0) {
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    dataIdAry.add(cursor.getString(cursor.getColumnIndex("_id")));
                    cursor.moveToNext();
                }
            } else {
                dataIdAry.add("No Data");
            }
        } else {
            dataIdAry.add("No Data");
        }
        cursor.close();

        return dataIdAry.toArray(new String[dataIdAry.size()]);
    }

    /* Function: put User data in DataBase */
    public String putUserData(UserData object) {
        /*Check here For Previous Similar data */
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", object.getName());
        contentValues.put("email", object.getEmail());
        contentValues.put("password", object.getPassword());
        contentValues.put("mobileNo", object.getMobileNo());
        contentValues.put("prfileType", object.getPrfileType());
        contentValues.put("currency", object.getCurrency());
        contentValues.put("consumptionUnit", object.getConsumptionUnit());
        contentValues.put("pProtection", object.getpProtection());
        //db.insert("insuranceRecord", null, contentValues);

        try {
            db.insert("userOption", null, contentValues);
            return "1";//+insert_qry;
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    /* Function: Get User data From DataBase */
    public UserData getUserData() {

        SQLiteDatabase db = this.getReadableDatabase();
        UserData object2 = new UserData();

        String query = "Select * FROM userOption ORDER BY _id DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            object2.setName(cursor.getString(cursor.getColumnIndex("name")));
            object2.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            object2.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            object2.setMobileNo(cursor.getString(cursor.getColumnIndex("mobileNo")));
            object2.setPrfileType(cursor.getString(cursor.getColumnIndex("prfileType")));
            object2.setCurrency(cursor.getString(cursor.getColumnIndex("currency")));
            object2.setConsumptionUnit(cursor.getString(cursor.getColumnIndex("consumptionUnit")));
            object2.setpProtection(cursor.getString(cursor.getColumnIndex("pProtection")));
            cursor.close();
            db.close();
        } else {
            object2.setName("N/A");
            object2.setEmail("N/A");
            object2.setPassword("N/A");
            object2.setMobileNo("N/A");
            object2.setPrfileType("N/A");
            object2.setCurrency("N/A");
            object2.setConsumptionUnit("N/A");
            object2.setpProtection("N/A");
        }
        return object2;
    }

    /* Update User Details */
    public String updateUserData(UserData object) {

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", object.getName());
        contentValues.put("email", object.getEmail());
        contentValues.put("password", object.getPassword());
        contentValues.put("mobileNo", object.getMobileNo());
        contentValues.put("prfileType", object.getPrfileType());
        contentValues.put("pProtection", object.getpProtection());

        try {
            db.update("userOption", contentValues, "_id = ? ", new String[] {"1"} );
            return "1";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    /* Function: Check User Exist or Not */
    public int check_user() {
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM userOption limit 1";// WHERE mobile_no='"+mobile_no+"' AND password='"+password+"'";

        Cursor cursor = db.rawQuery(query, null);
        count = cursor.getCount();
        if (count == 0) {
            return 0;
        } else {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                String pp_optn = cursor.getString(cursor.getColumnIndex("pProtection"));
                cursor.close();
                db.close();
                if (pp_optn.equals("1")) {
                    return 1;
                } else {
                    return 2;
                }
                //return Integer.toString(lst_rcrd);
            } else {
                return 0;
                //return e.getMessage();
            }
        }
        //return count;
    }

    /* Function: User login */
    public int log_in(String password) {
        int count = 0;
        String query = "Select * FROM userOption WHERE password='" + password + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count = cursor.getCount();
        return count;
    }

    public boolean putToDoData(ToDo object){
        SQLiteDatabase db = this.getWritableDatabase();

        /*todo: Check here For Previous Similar data */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        ////regNumber,brand,model,dateOfPurchase,fuelTankCapacity,type,color,fuelType,comment,addedOn

        ContentValues contentValues = new ContentValues();
        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("title", object.getTitle());
        contentValues.put("details", object.getDetail());
        contentValues.put("addedOn", addedOn);

        db.insert("toDos", null, contentValues);
        return true;
    }

    public ToDo getToDoDetail(String id){
        int count=0;//,i=0;

        String query;
        if(id.equals("0")){
            query = "Select * FROM toDos ORDER BY _id DESC LIMIT 1";
        }else{
            query = "Select * FROM toDos WHERE _id='" + id + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        ToDo object=null;//=new VehicleData[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    object=new ToDo(
                            cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("vehicleId")),
                            cursor.getString(cursor.getColumnIndex("title")),
                            cursor.getString(cursor.getColumnIndex("details")),
                            cursor.getString(cursor.getColumnIndex("addedOn"))
                    );
                    cursor.moveToNext();
                }
            } else {
                object=null;
            }
        }else {
            object=null;
        }
        cursor.close();
        return object;
    }

    public String[] getToDoRecordIds(){
        int count=0,i=0;

        String query = "Select * FROM toDos ORDER BY _id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        String[] objectAry=new String[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    objectAry[i]=cursor.getString(cursor.getColumnIndex("_id"));
                    i++;
                    cursor.moveToNext();
                }
            } else {
                objectAry[i]=null;
            }
        }else {
            objectAry=null;
        }
        cursor.close();
        return objectAry;
    }

    public boolean deleteToDoData(String recordId){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("DELETE FROM toDos WHERE _id=" + recordId + ";");
            db.close();
            return true;
        } catch (SQLException e) {
            //returnString=e.getMessage();
            db.close();
            return false;
        }
        //return true;
    }

    /* Function: put Spare Parts Data */
    public boolean putSparePartData(SparePart object) {
        SQLiteDatabase db = this.getWritableDatabase();


        /*Check here For Previous Similar data */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        ////regNumber,brand,model,dateOfPurchase,fuelTankCapacity,type,color,fuelType,comment,addedOn

        ContentValues contentValues = new ContentValues();
        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("partNo", object.getPartNo());
        contentValues.put("partName", object.getPartName());
        contentValues.put("partDetails", object.getPartDetail());
        contentValues.put("quantity", object.getQuantity());
        contentValues.put("image", object.getImage());
        contentValues.put("addedOn", addedOn);

        db.insert("spareParts", null, contentValues);
        return true;
    }

    public boolean editSparePartData(String recordId, SparePart object){


        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("partNo", object.getPartNo());
        contentValues.put("partName", object.getPartName());
        contentValues.put("partDetails", object.getPartDetail());
        contentValues.put("quantity", object.getQuantity());
        contentValues.put("image", object.getImage());

        try {
            db.update("spareParts", contentValues, "_id = ? ", new String[] {recordId} );
            return true;
        } catch (SQLException e) {
            //return e.getMessage();
            return false;
        }

        //return true;
    }

    public SparePart getSparePartDetail(String id){
        int count=0;//,i=0;

        String query;
        if(id.equals("0")){
            query = "Select * FROM spareParts ORDER BY _id DESC LIMIT 1";
        }else{
            //query = "Select * FROM insuranceRecord WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
            query = "Select * FROM spareParts WHERE _id='" + id + "'";
        }

        //String query = "Select * FROM spareParts WHERE _id='" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        SparePart object=null;//=new VehicleData[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    object=new SparePart(
                            cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("vehicleId")),
                            cursor.getString(cursor.getColumnIndex("partNo")),
                            cursor.getString(cursor.getColumnIndex("partName")),
                            cursor.getString(cursor.getColumnIndex("partDetails")),
                            cursor.getString(cursor.getColumnIndex("quantity")),
                            cursor.getString(cursor.getColumnIndex("image")),
                            cursor.getString(cursor.getColumnIndex("addedOn"))
                    );
                    //i++;
                    cursor.moveToNext();
                }
            } else {
                object=null;
            }
        }else {
            object=null;
        }
        cursor.close();
        return object;
    }

    public String[] getSparePartRecordIds(){
        int count=0,i=0;

        String query = "Select * FROM spareParts ORDER BY _id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        String[] objectAry=new String[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    objectAry[i]=cursor.getString(cursor.getColumnIndex("_id"));
                    i++;
                    cursor.moveToNext();
                }
            } else {
                objectAry[i]=null;
            }
        }else {
            objectAry=null;
        }
        cursor.close();
        return objectAry;
    }


    public boolean deleteSparePartData(String recordId){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("DELETE FROM spareParts WHERE _id=" + recordId + ";");
            db.close();
            return true;
        } catch (SQLException e) {
            db.close();
            return false;
        }
        //return true;
    }

    /* Function: put Insurance Data */
    //public String putVehicleData(VehicleData object) {
    public boolean putInsuranceData(InsuranceData object) {
        /*Check here For Previous Similar data */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        SQLiteDatabase db = this.getWritableDatabase();
        ////regNumber,brand,model,dateOfPurchase,fuelTankCapacity,type,color,fuelType,comment,addedOn

        ContentValues contentValues = new ContentValues();

        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("provider", object.getProvider());
        contentValues.put("type", object.getType());
        contentValues.put("dateBegin", object.getDateBegin());
        contentValues.put("dateEnd", object.getDateEnd());
        contentValues.put("meterReading", object.getMeterReading());
        contentValues.put("cost", object.getCost());
        contentValues.put("detail", object.getDetails());
        contentValues.put("receipt", object.getReceipt());
        contentValues.put("addedOn", addedOn);

        db.insert("insuranceRecord", null, contentValues);
        return true;
    }

    /* Function: Get Insurance Data */
    public InsuranceData getLastInsuranceData(String vehicleId){
        //vehicleId,dateOfFill,meterReading,volume,price,receipt,addedOn
        SQLiteDatabase db = this.getReadableDatabase();
        InsuranceData object2;//=new FillingData();
        String query;

        if(vehicleId.equals("0")){
            query = "Select * FROM insuranceRecord ORDER BY _id DESC LIMIT 1";
        }else{
            query = "Select * FROM insuranceRecord WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
        }

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            object2=new InsuranceData(
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("vehicleId")),
                    cursor.getString(cursor.getColumnIndex("provider")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("dateBegin")),
                    cursor.getString(cursor.getColumnIndex("dateEnd")),
                    cursor.getString(cursor.getColumnIndex("meterReading")),
                    cursor.getString(cursor.getColumnIndex("cost")),
                    cursor.getString(cursor.getColumnIndex("detail")),
                    cursor.getString(cursor.getColumnIndex("receipt")),
                    cursor.getString(cursor.getColumnIndex("addedOn"))
            );
            cursor.close();
            db.close();
        } else {
            object2=new InsuranceData("NA","NA","NA","NA","0/0/0","0/0/0","0","NA","NA","NA","NA");
        }
        return object2;
    }

    /* Function: Get Insurance Data */
    public InsuranceData getInsuranceDataViaId(String id,String vehicleId){
        //vehicleId,dateOfFill,meterReading,volume,price,receipt,addedOn
        SQLiteDatabase db = this.getReadableDatabase();
        InsuranceData object2;//=new FillingData();
        String query;
        if(vehicleId.equals("0")){
            query = "Select * FROM insuranceRecord ORDER BY _id DESC LIMIT 1";
        }
        else{
            query = "Select * FROM insuranceRecord WHERE vehicleId='"+vehicleId+"' AND _id='"+id+"' ORDER BY meterReading DESC LIMIT 1";
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            object2=new InsuranceData(
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("vehicleId")),
                    cursor.getString(cursor.getColumnIndex("provider")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("dateBegin")),
                    cursor.getString(cursor.getColumnIndex("dateEnd")),
                    cursor.getString(cursor.getColumnIndex("meterReading")),
                    cursor.getString(cursor.getColumnIndex("cost")),
                    cursor.getString(cursor.getColumnIndex("detail")),
                    cursor.getString(cursor.getColumnIndex("receipt")),
                    cursor.getString(cursor.getColumnIndex("addedOn"))
            );
            cursor.close();
            db.close();
        } else {
            object2=new InsuranceData("NA","NA","NA","NA","0/0/0","0/0/0","0","NA","NA","NA","NA");
        }
        return object2;
    }

    /* Function: put Vehicle Data */
    //public String putVehicleData(VehicleData object) {
    public boolean putVehicleData(VehicleData object) {
        /*Check here For Previous Similar data */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        SQLiteDatabase db = this.getWritableDatabase();

        ////regNumber,brand,model,dateOfPurchase,fuelTankCapacity,type,color,fuelType,comment,addedOn

        ContentValues contentValues = new ContentValues();

        contentValues.put("regNumber", object.getRegNumber());
        contentValues.put("brand", object.getBrand());
        contentValues.put("model", object.getModel());
        contentValues.put("dateOfPurchase", object.getDateOfPurchase());
        contentValues.put("fuelTankCapacity", object.getFuelTankCapacity());
        contentValues.put("type", object.getType());
        contentValues.put("color", object.getColor());
        contentValues.put("fuelType", object.getFuelType());
        contentValues.put("comment", object.getComment());
        contentValues.put("addedOn", addedOn);

        db.insert("userVehicles", null, contentValues);
        return true;

    }

    /* Function: put Vehicle Data */
    //public String updateVehicleData(VehicleData object, String dataId) {
    public boolean updateVehicleData(VehicleData object){//}, String dataId) {
        /*Check here For Previous Similar data */

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("regNumber",  object.getRegNumber());
        contentValues.put("brand",  object.getBrand());
        contentValues.put("model",  object.getModel());
        contentValues.put("dateOfPurchase",  object.getDateOfPurchase());
        contentValues.put("fuelTankCapacity",  object.getFuelTankCapacity());
        contentValues.put("type",  object.getType());
        contentValues.put("color",  object.getColor());
        contentValues.put("fuelType",  object.getFuelType());
        contentValues.put("comment",  object.getComment());

        db.update("userVehicles", contentValues, "_id = ? ", new String[] {object.getId()} );
        return true;
    }

    public String deleteVehicleData(String dataId){
        String returnString="sfdfsdf"+dataId;
        /*todo:Check here For Previous Similar data */
        //String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        SQLiteDatabase db = this.getReadableDatabase();
        //vehicleId,dateOfFill,meterReading,volume,price,receipt,addedOn

        try {
            //db.execSQL(insert_qry);
            db.execSQL("DELETE FROM userVehicles WHERE _id=" + dataId + ";");
            db.execSQL("DELETE FROM fuelFillings WHERE vehicleId=" + dataId + ";");
            db.execSQL("DELETE FROM serviceRecord WHERE vehicleId=" + dataId + ";");
            db.execSQL("DELETE FROM otherExpenses WHERE vehicleId=" + dataId + ";");
            db.execSQL("DELETE FROM tripRecord WHERE vehicleId=" + dataId + ";");
            db.execSQL("DELETE FROM insuranceRecord WHERE vehicleId=" + dataId + ";");
            returnString="1";
        } catch (SQLException e) {
            returnString=e.getMessage();
        }
        return returnString;
    }

    public String deleteData(String type, String dataId){
        String returnString;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            //db.execSQL(insert_qry);
            returnString="1";

            if(type.equals("Vehicles")){
                db.execSQL("DELETE FROM userVehicles WHERE _id=" + dataId + ";");
            }
            else if(type.equals("fuel")){
                db.execSQL("DELETE FROM fuelFillings WHERE _id=" + dataId + ";");
            }
            else if(type.equals("service")){
                db.execSQL("DELETE FROM serviceRecord WHERE _id=" + dataId + ";");
            }
            else if(type.equals("otherExpense")){
                db.execSQL("DELETE FROM otherExpenses WHERE _id=" + dataId + ";");
            }
            else if(type.equals("trip")){
                db.execSQL("DELETE FROM tripRecord WHERE _id=" + dataId + ";");
            }
            else if(type.equals("insurance")){
                db.execSQL("DELETE FROM insuranceRecord WHERE _id=" + dataId + ";");
            }
            else{
                returnString="0";
            }
        } catch (SQLException e) {
            returnString=e.getMessage();
        }
        return returnString;
    }


    /* Function: Get Vehicle Data */
    public VehicleData getVehicleData(String id) {
        //regNumber,brand,model,dateOfPurchase,fuelTankCapacity,type,color,fuelType, comment,addedOn
        SQLiteDatabase db = this.getReadableDatabase();
        VehicleData object2;//=new VehicleData();
        String query;

        if (id.equals("0")) {
            query = "Select * FROM userVehicles ORDER BY _id DESC LIMIT 1";
        } else {
            query = "Select * FROM userVehicles WHERE _id='" + id + "'";
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            object2 = new VehicleData(
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("regNumber")),
                    cursor.getString(cursor.getColumnIndex("brand")),
                    cursor.getString(cursor.getColumnIndex("model")),
                    cursor.getString(cursor.getColumnIndex("dateOfPurchase")),
                    cursor.getString(cursor.getColumnIndex("fuelTankCapacity")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("color")),
                    cursor.getString(cursor.getColumnIndex("fuelType")),
                    cursor.getString(cursor.getColumnIndex("comment")),
                    cursor.getString(cursor.getColumnIndex("addedOn"))
            );
            cursor.close();
            db.close();
        } else {
            object2 = new VehicleData("N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A");
        }
        return object2;
    }

    /* Function: To Return VehicleData Object Array */
    public VehicleData[] userVehicleDetails(){
        int count=0,i=0;

        String query = "Select * FROM userVehicles ORDER BY _id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        VehicleData[] objectAry=new VehicleData[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    objectAry[i]=new VehicleData(cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("regNumber")),
                            cursor.getString(cursor.getColumnIndex("brand")),
                            cursor.getString(cursor.getColumnIndex("model")),
                            cursor.getString(cursor.getColumnIndex("dateOfPurchase")),
                            cursor.getString(cursor.getColumnIndex("fuelTankCapacity")),
                            cursor.getString(cursor.getColumnIndex("type")),
                            cursor.getString(cursor.getColumnIndex("color")),
                            cursor.getString(cursor.getColumnIndex("fuelType")),
                            cursor.getString(cursor.getColumnIndex("comment")),
                            cursor.getString(cursor.getColumnIndex("addedOn"))
                    );
                    i++;
                    cursor.moveToNext();
                }
            } else {
                objectAry[i]=new VehicleData("id","regNumber","brand","model","dateOfPurchase","fuelTankCapacity","type","color","fuelType","comment","addedOn");
            }
        }
        else {
            objectAry=null;
           // objectAry[i]=new VehicleData("id","regNumber","brand","model","dateOfPurchase","fuelTankCapacity","type","color","fuelType","comment","addedOn");
        }
        cursor.close();
        return objectAry;
    }

    /* Function: Put Fuel Filling Data */
    //public String putFuelFillData(FillingData object){
    public Boolean putFuelFillData(FillingData object){
        /*Check here For Previous Similar data */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("type_of_fuel", object.getFuelType());
        contentValues.put("dateOfFill", object.getDate());
        contentValues.put("meterReading", object.getMeterReading());
        contentValues.put("volume", object.getVolume());
        contentValues.put("price", object.getPrice());
        contentValues.put("receipt", object.getReceipt());
        contentValues.put("addedOn", addedOn);

        db.insert("fuelFillings", null, contentValues);
        return true;
    }

    /* Function: Put Fuel Filling Data */
    //public String putServiceData(ServiceData object){
    public boolean putServiceData(ServiceData object){
        /*Check here For Previous Similar data */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("typeOfService", object.getServiceType());
        contentValues.put("meterReading", object.getMeterReading());
        //contentValues.put("setNxtService", object.getNextService());
        contentValues.put("dateOfService", object.getDate());
        contentValues.put("price", object.getPrice());
        contentValues.put("receipt", object.getReceipt());
        contentValues.put("addedOn", addedOn);

        db.insert("serviceRecord", null, contentValues);
        return true;

    }

    /* Function: Put Fuel Filling Data */
    //public String putOExpenseData(ExpenseData object){
    public boolean putOExpenseData(ExpenseData object){
        //ExpenseData otherExpenses
        /*Db table otherExpenses: id,vehicleId,type,meterReading,dateOfExpense,price,receipt,addedOn */
        /*Check here For Previous Similar data */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("type", object.getExpenseType());
        contentValues.put("meterReading", object.getMeterReading());
        contentValues.put("dateOfExpense", object.getDate());
        contentValues.put("price", object.getPrice());
        contentValues.put("receipt", object.getReceipt());
        contentValues.put("addedOn", addedOn);

        db.insert("otherExpenses", null, contentValues);
        return true;

    }

    /* Function: Put Fuel Filling Data */
    //public String putTripData(TripData object)
    public boolean putTripData(TripData object){
        /**vehicleId,typeOfTrip,locationDeparture,locationReturn,dateOfDeparture,dateOfReturn,timeOfDeparture,timeOfReturn,
         meterReadingDeparture,meterReadingReturn,costOfTrip,receipt,addedOn
         */
        String addedOn = new Timestamp(System.currentTimeMillis()).toString();
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("vehicleId", object.getVehicleId());
        contentValues.put("typeOfTrip", object.getTypeOfTrip());
        contentValues.put("locationDeparture", object.getLocationDeparture());
        contentValues.put("locationReturn", object.getLocationReturn());
        contentValues.put("dateOfDeparture", object.getDateOfDeparture());
        contentValues.put("dateOfReturn", object.getDateOfReturn());
        contentValues.put("timeOfDeparture", object.getTimeOfDeparture());
        contentValues.put("timeOfReturn", object.getTimeOfReturn());
        contentValues.put("meterReadingDeparture", object.getMeterReadingDeparture());
        contentValues.put("meterReadingReturn", object.getMeterReadingReturn());
        contentValues.put("costOfTrip", object.getCostOfTrip());
        contentValues.put("receipt", object.getReceipt());
        contentValues.put("addedOn", addedOn);

        db.insert("tripRecord", null, contentValues);
        return true;

    }

    public String[] getLastData(String vehicleId){

        String[] data= new String[4]; //record Type,ododmeter,date,id

        SQLiteDatabase db = this.getReadableDatabase();

        String query1,query2,query3,query4,query5;

        String[] fuelData= new String[4]; //record Type,ododmeter,date,id
        String[] serviceData= new String[4]; //record Type,ododmeter,date,id
        String[] otherExpenseData= new String[4]; //record Type,ododmeter,date,id
        String[] tripData= new String[4]; //record Type,ododmeter,date,id
        String[] insuranceData= new String[4]; //record Type,ododmeter,date,id

        if(vehicleId.equals("0")){
            query1 = "Select * FROM fuelFillings ORDER BY meterReading DESC LIMIT 1";
            query2 = "Select * FROM serviceRecord ORDER BY meterReading DESC LIMIT 1";
            query3 = "Select * FROM otherExpenses ORDER BY meterReading DESC LIMIT 1";
            query4 = "Select * FROM tripRecord ORDER BY meterReadingReturn DESC LIMIT 1";
            query5 = "Select * FROM insuranceRecord ORDER BY meterReading DESC LIMIT 1";
        }else{
            query1 = "Select * FROM fuelFillings WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
            query2 = "Select * FROM serviceRecord WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
            query3 = "Select * FROM otherExpenses WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
            query4 = "Select * FROM tripRecord WHERE vehicleId='"+vehicleId+"' ORDER BY meterReadingReturn DESC LIMIT 1";
            query5 = "Select * FROM insuranceRecord WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
        }

        Cursor cursor = db.rawQuery(query1, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            fuelData[0]="fuel";
            fuelData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            fuelData[2]=cursor.getString(cursor.getColumnIndex("dateOfFill"));
            fuelData[3]=cursor.getString(cursor.getColumnIndex("price"));

            cursor.close();
            //db.close();
        } else {
            fuelData[0]=fuelData[1]=fuelData[3]="0";
            fuelData[2]="0/0/0";
        }

        cursor = db.rawQuery(query2, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            serviceData[0]="service";
            serviceData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            serviceData[2]=cursor.getString(cursor.getColumnIndex("dateOfService"));
            serviceData[3]=cursor.getString(cursor.getColumnIndex("price"));

            cursor.close();
            //db.close();
        } else {
            serviceData[0]=serviceData[1]=serviceData[2]=serviceData[3]="0";
            serviceData[2]="0/0/0";
        }

        cursor = db.rawQuery(query3, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            otherExpenseData[0]="otherExpense";
            otherExpenseData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            otherExpenseData[2]=cursor.getString(cursor.getColumnIndex("dateOfExpense"));
            otherExpenseData[3]=cursor.getString(cursor.getColumnIndex("price"));

            cursor.close();
            //db.close();
        } else {
            otherExpenseData[0]=otherExpenseData[1]=otherExpenseData[2]=otherExpenseData[3]="0";
            otherExpenseData[2]="0/0/0";
        }

        cursor = db.rawQuery(query4, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            tripData[0]="Trip";
            //meterReadingDeparture//dateOfDeparture
            //tripData[1]=cursor.getString(cursor.getColumnIndex("meterReadingDeparture"));
            //tripData[2]=cursor.getString(cursor.getColumnIndex("dateOfDeparture"));
            tripData[1]=cursor.getString(cursor.getColumnIndex("meterReadingReturn"));
            tripData[2]=cursor.getString(cursor.getColumnIndex("dateOfReturn"));
            tripData[3]=cursor.getString(cursor.getColumnIndex("costOfTrip"));
            cursor.close();
        } else {
            tripData[0]=tripData[1]=tripData[2]=tripData[3]="0";
            tripData[2]="0/0/0";
        }

        cursor = db.rawQuery(query5, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            insuranceData[0]="Insurance";
            insuranceData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            insuranceData[2]=cursor.getString(cursor.getColumnIndex("dateBegin"));
            insuranceData[3]=cursor.getString(cursor.getColumnIndex("cost"));

            cursor.close();
            //db.close();
        } else {
            insuranceData[0]=insuranceData[1]=insuranceData[2]=insuranceData[3]="0";
            insuranceData[2]="0/0/0";
        }

        db.close();

        int a=Integer.parseInt(fuelData[1]);
        int b=Integer.parseInt(serviceData[1]);
        int c=Integer.parseInt(otherExpenseData[1]);
        int d=Integer.parseInt(tripData[1]);
        int e=Integer.parseInt(insuranceData[1]);

        //int max = c > (a > b ? a : b) ? c : ((a > b) ? a : b);

        int[] numbers = {a,b,c,d,e};
        int largest = Integer.MIN_VALUE;

        for(int i =0;i<numbers.length;i++) {
            if(numbers[i] > largest) {
                largest = numbers[i];
            }
        }

        //int max=((a>b)&&(a>c)&&(a>d))?a:(((b>c)&&(b>d))?b:(c>d)?c:d);

        if(largest==a){data=fuelData;}
        else if(largest==b){data=serviceData;}
        else if(largest==c){data=otherExpenseData;}
        else if(largest==d){data=tripData;}
        else{data=insuranceData;}

/*
        System.out.println("Largest number in array is : " +largest);

        int max=((a>b)&&(a>c)&&(a>d))?a:(((b>c)&&(b>d))?b:(c>d)?c:d);

        if(max==a){data=fuelData;}
        else if(max==b){data=serviceData;}
        //else {data=otherExpenseData;}
        else if(max==c){data=otherExpenseData;}
        else{data=tripData;}
*/
        return data;
    }

    public String[] getLastDataByReading(String vehicleId,String mtrReading){

        String[] data= new String[4]; //record Type,ododmeter,date,id

        SQLiteDatabase db = this.getReadableDatabase();

        String query1,query2,query3,query4,query5;

        String[] fuelData= new String[4]; //record Type,ododmeter,date,id
        String[] serviceData= new String[4]; //record Type,ododmeter,date,id
        String[] otherExpenseData= new String[4]; //record Type,ododmeter,date,id
        String[] tripData= new String[4]; //record Type,ododmeter,date,id
        String[] insuranceData= new String[4]; //record Type,ododmeter,date,id

        if(vehicleId.equals("0")){
            query1 = "Select * FROM fuelFillings WHERE meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
            query2 = "Select * FROM serviceRecord WHERE meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
            query3 = "Select * FROM otherExpenses WHERE meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
            query4 = "Select * FROM tripRecord WHERE meterReadingReturn<="+mtrReading+" ORDER BY meterReadingReturn DESC LIMIT 1";
            query5 = "Select * FROM insuranceRecord WHERE meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
        }else{
            query1 = "Select * FROM fuelFillings WHERE vehicleId='"+vehicleId+"' AND meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
            query2 = "Select * FROM serviceRecord WHERE vehicleId='"+vehicleId+"' AND meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
            query3 = "Select * FROM otherExpenses WHERE vehicleId='"+vehicleId+"' AND meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
            query4 = "Select * FROM tripRecord WHERE vehicleId='"+vehicleId+"' AND meterReadingReturn<="+mtrReading+" ORDER BY meterReadingReturn DESC LIMIT 1";
            query5 = "Select * FROM insuranceRecord WHERE vehicleId='"+vehicleId+"' AND meterReading<="+mtrReading+" ORDER BY meterReading DESC LIMIT 1";
        }

        Cursor cursor = db.rawQuery(query1, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            fuelData[0]="fuel";
            fuelData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            fuelData[2]=cursor.getString(cursor.getColumnIndex("dateOfFill"));
            fuelData[3]=cursor.getString(cursor.getColumnIndex("price"));

            cursor.close();
            //db.close();
        } else {
            fuelData[0]=fuelData[1]=fuelData[3]="0";
            fuelData[2]="0/0/0";
        }

        cursor = db.rawQuery(query2, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            serviceData[0]="service";
            serviceData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            serviceData[2]=cursor.getString(cursor.getColumnIndex("dateOfService"));
            serviceData[3]=cursor.getString(cursor.getColumnIndex("price"));

            cursor.close();
            //db.close();
        } else {
            serviceData[0]=serviceData[1]=serviceData[2]=serviceData[3]="0";
            serviceData[2]="0/0/0";
        }

        cursor = db.rawQuery(query3, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            otherExpenseData[0]="otherExpense";
            otherExpenseData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            otherExpenseData[2]=cursor.getString(cursor.getColumnIndex("dateOfExpense"));
            otherExpenseData[3]=cursor.getString(cursor.getColumnIndex("price"));

            cursor.close();
            //db.close();
        } else {
            otherExpenseData[0]=otherExpenseData[1]=otherExpenseData[2]=otherExpenseData[3]="0";
            otherExpenseData[2]="0/0/0";
        }

        cursor = db.rawQuery(query4, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            tripData[0]="Trip";
            //meterReadingDeparture//dateOfDeparture
            //tripData[1]=cursor.getString(cursor.getColumnIndex("meterReadingDeparture"));
            //tripData[2]=cursor.getString(cursor.getColumnIndex("dateOfDeparture"));
            tripData[1]=cursor.getString(cursor.getColumnIndex("meterReadingReturn"));
            tripData[2]=cursor.getString(cursor.getColumnIndex("dateOfReturn"));
            tripData[3]=cursor.getString(cursor.getColumnIndex("costOfTrip"));
            cursor.close();
        } else {
            tripData[0]=tripData[1]=tripData[2]=tripData[3]="0";
            tripData[2]="0/0/0";
        }

        cursor = db.rawQuery(query5, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
            insuranceData[0]="Insurance";
            insuranceData[1]=cursor.getString(cursor.getColumnIndex("meterReading"));
            insuranceData[2]=cursor.getString(cursor.getColumnIndex("dateBegin"));
            insuranceData[3]=cursor.getString(cursor.getColumnIndex("cost"));

            cursor.close();
            //db.close();
        } else {
            insuranceData[0]=insuranceData[1]=insuranceData[2]=insuranceData[3]="0";
            insuranceData[2]="0/0/0";
        }

        db.close();

        int a=Integer.parseInt(fuelData[1]);
        int b=Integer.parseInt(serviceData[1]);
        int c=Integer.parseInt(otherExpenseData[1]);
        int d=Integer.parseInt(tripData[1]);
        int e=Integer.parseInt(insuranceData[1]);

        //int max = c > (a > b ? a : b) ? c : ((a > b) ? a : b);

        int[] numbers = {a,b,c,d,e};
        int largest = Integer.MIN_VALUE;

        for(int i =0;i<numbers.length;i++) {
            if(numbers[i] > largest) {
                largest = numbers[i];
            }
        }

        //int max=((a>b)&&(a>c)&&(a>d))?a:(((b>c)&&(b>d))?b:(c>d)?c:d);

        if(largest==a){data=fuelData;}
        else if(largest==b){data=serviceData;}
        else if(largest==c){data=otherExpenseData;}
        else if(largest==d){data=tripData;}
        else{data=insuranceData;}
		
        return data;
    }

    /* Function: Get Fuel Filling Data */
    public FillingData getLastFillingData(String vehicleId){
        //vehicleId,dateOfFill,meterReading,volume,price,receipt,addedOn
        SQLiteDatabase db = this.getReadableDatabase();
        FillingData object2;//=new FillingData();
        String query;
        if(vehicleId.equals("0")){
            query = "Select * FROM fuelFillings ORDER BY _id DESC LIMIT 1";
        }else{
            query = "Select * FROM fuelFillings WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
                object2=new FillingData(
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("vehicleId")),
                        cursor.getString(cursor.getColumnIndex("type_of_fuel")),
                        cursor.getString(cursor.getColumnIndex("meterReading")),
                        cursor.getString(cursor.getColumnIndex("dateOfFill")),
                        cursor.getString(cursor.getColumnIndex("volume")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getString(cursor.getColumnIndex("receipt")),
                        cursor.getString(cursor.getColumnIndex("addedOn"))
                );
            cursor.close();
            db.close();
        } else {
            object2=new FillingData("NA","NA","NA","0","0/0/0","NA","NA","NA","NA");
        }
        return object2;
    }

    /* Function: Get Fuel Filling Data By Last reading */
    public FillingData getLastFillingDataByReading(String vehicleId,String mtrReading,String method){
        //vehicleId,dateOfFill,meterReading,volume,price,receipt,addedOn
        SQLiteDatabase db = this.getReadableDatabase();
        FillingData object2;//=new FillingData();
        String query;
        if(vehicleId.equals("0")){
            query = "Select * FROM fuelFillings ORDER BY _id "+method+" LIMIT 1";
        }else{
            if(method.equals("DESC")){
                query = "Select * FROM fuelFillings WHERE vehicleId='"+vehicleId+"' AND meterReading<='"+mtrReading+"' ORDER BY meterReading "+method+" LIMIT 1";
            }else{
                query = "Select * FROM fuelFillings WHERE vehicleId='"+vehicleId+"' AND meterReading>='"+mtrReading+"' ORDER BY meterReading "+method+" LIMIT 1";
            }
        }

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            //int lst_rcrd =Integer.parseInt(cursor.getString(0));
                object2=new FillingData(
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("vehicleId")),
                        cursor.getString(cursor.getColumnIndex("type_of_fuel")),
                        cursor.getString(cursor.getColumnIndex("meterReading")),
                        cursor.getString(cursor.getColumnIndex("dateOfFill")),
                        cursor.getString(cursor.getColumnIndex("volume")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getString(cursor.getColumnIndex("receipt")),
                        cursor.getString(cursor.getColumnIndex("addedOn"))
                );
            cursor.close();
            db.close();
        } else {
            object2=new FillingData("NA","NA","NA","0","0/0/0","NA","NA","NA","NA");
        }
        return object2;
    }

    /* Function: To Return FillingData(FuelFilling Data) Object Array */
    public FillingData[] fuelFillingData(String vehicleId){
        //vehicleId,dateOfFill,meterReading,volume,price,receipt,addedOn
        int count=0,i=0;
        String query = "Select * FROM fuelFillings WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        FillingData[] objectAry=new FillingData[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    objectAry[i]=new FillingData(
                            cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("vehicleId")),
                            cursor.getString(cursor.getColumnIndex("type_of_fuel")),
                            cursor.getString(cursor.getColumnIndex("meterReading")),
                            cursor.getString(cursor.getColumnIndex("dateOfFill")),
                            cursor.getString(cursor.getColumnIndex("volume")),
                            cursor.getString(cursor.getColumnIndex("price")),
                            cursor.getString(cursor.getColumnIndex("receipt")),
                            cursor.getString(cursor.getColumnIndex("addedOn"))
                    );
                    i++;
                    cursor.moveToNext();
                }
            } else {
                objectAry[i]=new FillingData("id","vehicleId","type_of_fuel","meterReading","dateOfFill",
                        "volume","price","receipt","addedOn");
            }
        }
        else {
            objectAry[i]=new FillingData("id","vehicleId","type_of_fuel","meterReading","dateOfFill",
                    "volume","price","receipt","addedOn");
        }
        cursor.close();
        return objectAry;
    }

    /* Function: Get Service Data */
    public ServiceData getLastServiceData(String vehicleId){
        SQLiteDatabase db = this.getReadableDatabase();
        ServiceData object;

        String query;
        if(vehicleId.equals("0")){
            query = "Select * FROM serviceRecord ORDER BY _id DESC LIMIT 1";
        }
        else {
            query = "Select * FROM serviceRecord WHERE vehicleId='"+vehicleId+"' ORDER BY meterReading DESC LIMIT 1";
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            object=new ServiceData(
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("vehicleId")),
                    cursor.getString(cursor.getColumnIndex("typeOfService")),
                    cursor.getString(cursor.getColumnIndex("meterReading")),
                    cursor.getString(cursor.getColumnIndex("dateOfService")),
                    cursor.getString(cursor.getColumnIndex("price")),
                    cursor.getString(cursor.getColumnIndex("receipt")),
                    cursor.getString(cursor.getColumnIndex("addedOn"))
            );//cursor.getString(cursor.getColumnIndex("setNxtService")),
            cursor.close();
            db.close();
        } else {
            object=new ServiceData("NA","NA","NA","0","0","NA","NA","NA");
        }

        //object=new ServiceData("NA",vehicleId,"NA","0","NA","NA","NA","NA","NA");
        ////vehicleId,typeOfService,meterReading,setNxtService,dateOfService,price,receipt,addedOn
        //classSequence: vehicleId,typeOfService,odoMeterReading,date,price,picOptn,nxtService
        return object;
    }
    /* Function: Get other Expenses Data */
    public ExpenseData getLastOExpenseData(String vehicleId){
        SQLiteDatabase db = this.getReadableDatabase();
        ExpenseData object;

        String query;
        if(vehicleId.equals("0")){
            query = "Select * FROM otherExpenses ORDER BY _id DESC LIMIT 1";
        }else {
            query = "Select * FROM otherExpenses WHERE vehicleId='" + vehicleId + "' ORDER BY meterReading DESC LIMIT 1";
        }

        //String query = "Select * FROM otherExpenses WHERE vehicleId='"+vehicleId+"' ORDER BY _id DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            object=new ExpenseData(
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("vehicleId")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("meterReading")),
                    cursor.getString(cursor.getColumnIndex("dateOfExpense")),
                    cursor.getString(cursor.getColumnIndex("price")),
                    cursor.getString(cursor.getColumnIndex("receipt")),
                    cursor.getString(cursor.getColumnIndex("addedOn"))
            );
            cursor.close();
            db.close();
        } else {
            object=new ExpenseData("NA","NA","NA","0","0","NA","NA","NA");
        }
        /*Db table otherExpenses: id,vehicleId,type,meterReading,dateOfExpense,price,receipt,addedOn */
        return object;
    }

    /* Function: Get Trip Data */
    public TripData getTripData(String vehicleId){
        SQLiteDatabase db = this.getReadableDatabase();
        TripData object;


        String query;
        if(vehicleId.equals("0")){
            query = "Select * FROM tripRecord ORDER BY _id DESC LIMIT 1";
        }else {
            query = "Select * FROM tripRecord WHERE vehicleId='" + vehicleId + "' ORDER BY meterReadingReturn DESC LIMIT 1";
        }

        //String query = "Select * FROM tripRecord WHERE vehicleId='"+vehicleId+"' ORDER BY _id DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            object=new TripData(
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("vehicleId")),
                    cursor.getString(cursor.getColumnIndex("typeOfTrip")),

                    cursor.getString(cursor.getColumnIndex("locationDeparture")),
                    cursor.getString(cursor.getColumnIndex("locationReturn")),

                    cursor.getString(cursor.getColumnIndex("dateOfDeparture")),
                    cursor.getString(cursor.getColumnIndex("dateOfReturn")),

                    cursor.getString(cursor.getColumnIndex("timeOfDeparture")),
                    cursor.getString(cursor.getColumnIndex("timeOfReturn")),

                    cursor.getString(cursor.getColumnIndex("meterReadingDeparture")),
                    cursor.getString(cursor.getColumnIndex("meterReadingReturn")),

                    cursor.getString(cursor.getColumnIndex("costOfTrip")),

                    cursor.getString(cursor.getColumnIndex("receipt")),
                    cursor.getString(cursor.getColumnIndex("addedOn"))
            );
            cursor.close();
            db.close();
        } else {
            object=new TripData("NA","NA","NA","NA","NA","0","0","0","0","0","0","NA","NA","NA");
        }

        /**vehicleId,typeOfTrip,locationDeparture,locationReturn,dateOfDeparture,dateOfReturn,timeOfDeparture,timeOfReturn,
         meterReadingDeparture,meterReadingReturn,costOfTrip,receipt,addedOn
         */

        return object;
    }

    /* Function: To Return FillingData(FuelFilling Data) Object Array */
    public ServiceData[] vehicleServiceData(String vehicleId){
        ////vehicleId,typeOfService,meterReading,setNxtService,dateOfService,price,receipt,addedOn
        //classSequence: vehicleId,typeOfService,odoMeterReading,date,price,picOptn,nxtService
        int count=0,i=0;
        String query = "Select * FROM serviceRecord WHERE vehicleId='"+vehicleId+"' ORDER BY _id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        ServiceData[] objectAry=new ServiceData[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    objectAry[i]=new ServiceData(
                            cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("vehicleId")),
                            cursor.getString(cursor.getColumnIndex("typeOfService")),
                            cursor.getString(cursor.getColumnIndex("meterReading")),
                            cursor.getString(cursor.getColumnIndex("dateOfService")),
                            cursor.getString(cursor.getColumnIndex("price")),
                            cursor.getString(cursor.getColumnIndex("receipt")),
                            cursor.getString(cursor.getColumnIndex("addedOn"))
                    );//                            cursor.getString(cursor.getColumnIndex("setNxtService")),
                    i++;
                    cursor.moveToNext();
                }
            } else {
                objectAry[i]=new ServiceData("NA","NA","NA","0","NA","NA","NA","NA");
            }
        }
        else {
            objectAry[i]=new ServiceData("NA","NA","NA","0","NA","NA","NA","NA");
        }
        cursor.close();
        return objectAry;
    }

    /* Function: To Return FillingData(FuelFilling Data) Object Array */
    public ExpenseData[] vehicleExpenseData(String vehicleId){
        /*Db table otherExpenses: id,vehicleId,type,meterReading,dateOfExpense,price,receipt,addedOn */
        int count=0,i=0;
        String query = "Select * FROM otherExpenses WHERE vehicleId='"+vehicleId+"' ORDER BY _id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        ExpenseData[] objectAry=new ExpenseData[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    objectAry[i]=new ExpenseData(
                            cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("vehicleId")),
                            cursor.getString(cursor.getColumnIndex("type")),
                            cursor.getString(cursor.getColumnIndex("meterReading")),
                            cursor.getString(cursor.getColumnIndex("dateOfExpense")),
                            cursor.getString(cursor.getColumnIndex("price")),
                            cursor.getString(cursor.getColumnIndex("receipt")),
                            cursor.getString(cursor.getColumnIndex("addedOn"))
                    );
                    i++;
                    cursor.moveToNext();
                }
            } else {
                objectAry[i]=new ExpenseData("NA","NA","NA","0","NA","NA","NA","NA");
            }
        }else {
            objectAry[i]=new ExpenseData("NA","NA","NA","0","NA","NA","NA");
        }
        cursor.close();
        return objectAry;
    }

    /* Function: To Return FillingData(FuelFilling Data) Object Array */
    public TripData[] vehicleTripsData(String vehicleId){
        /*Db table otherExpenses: id,vehicleId,type,meterReading,dateOfExpense,price,receipt,addedOn */
        int count=0,i=0;
        String query = "Select * FROM tripRecord WHERE vehicleId='"+vehicleId+"' ORDER BY _id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        TripData[] objectAry=new TripData[count];
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    objectAry[i]=new TripData(
                            cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("vehicleId")),
                            cursor.getString(cursor.getColumnIndex("typeOfTrip")),

                            cursor.getString(cursor.getColumnIndex("locationDeparture")),
                            cursor.getString(cursor.getColumnIndex("locationReturn")),

                            cursor.getString(cursor.getColumnIndex("dateOfDeparture")),
                            cursor.getString(cursor.getColumnIndex("dateOfReturn")),

                            cursor.getString(cursor.getColumnIndex("timeOfDeparture")),
                            cursor.getString(cursor.getColumnIndex("timeOfReturn")),

                            cursor.getString(cursor.getColumnIndex("meterReadingDeparture")),
                            cursor.getString(cursor.getColumnIndex("meterReadingReturn")),

                            cursor.getString(cursor.getColumnIndex("costOfTrip")),

                            cursor.getString(cursor.getColumnIndex("receipt")),
                            cursor.getString(cursor.getColumnIndex("addedOn"))
                    );
                    i++;
                    cursor.moveToNext();
                }
            } else {
                objectAry[i]=new TripData("NA","NA","NA","NA","NA","NA","NA","NA","NA","0","0","NA","NA","NA");
            }
        }else {
            objectAry[i]=new TripData("NA","NA","NA","NA","NA","NA","NA","NA","NA","0","0","NA","NA","NA");
        }
        cursor.close();
        return objectAry;
        /**vehicleId,typeOfTrip,locationDeparture,locationReturn,dateOfDeparture,dateOfReturn,timeOfDeparture,timeOfReturn,
         meterReadingDeparture,meterReadingReturn,costOfTrip,receipt,addedOn
         */
    }

    public double getTotalExpense(String vId,String type){
        String query,table,costClmn="price";
        int count=0,i=0;
        double expense=0;

        if(type.equals("Fuel")){
            table="fuelFillings";

        }else if(type.equals("Service")){
            table="serviceRecord";
        }else if(type.equals("Other Expense")){
            table="otherExpenses";
        }else if(type.equals("Trip")){
            table="tripRecord";
            costClmn="costOfTrip";
        }else if(type.equals("Insurance")){
            table="insuranceRecord";
            costClmn="cost";
        }else{
            table="fuelFillings";
        }

        if(vId.equals("all")){
            query = "Select "+costClmn+" FROM "+table+" ORDER BY _id DESC";
        }else{
            query = "Select "+costClmn+" FROM "+table+" WHERE vehicleId='"+vId+"' ORDER BY _id DESC";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    expense=expense+Double.parseDouble(cursor.getString(cursor.getColumnIndex(costClmn)));
                    i++;
                    cursor.moveToNext();
                }
            } else {
                expense=0;
            }
        }else {
            expense=0;
        }
        cursor.close();

        return expense;
    }

    public double getAllTripExpense(String vId){
        String query;
        int count=0,i=0;
        double expense=0;
        if(vId.equals("all")){
            query = "Select costOfTrip FROM tripRecord ORDER BY _id DESC";
        }else{
            query = "Select costOfTrip FROM tripRecord WHERE vehicleId='"+vId+"' ORDER BY _id DESC";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        count=cursor.getCount();
        if (cursor.moveToFirst() || count!=0){
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    expense=expense+Double.parseDouble(cursor.getString(cursor.getColumnIndex("costOfTrip")));
                    i++;
                    cursor.moveToNext();
                }
            } else {
                expense=0;
            }
        }else {
            expense=0;
        }
        cursor.close();

        return expense;
    }

    public boolean putReminderData(ReminderData object) {
        /*Check here For Previous Similar data */
        //title detail date time addedOn

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("title", object.getTitle());
        contentValues.put("detail", object.getDetails());
        contentValues.put("date", object.getDate());
        contentValues.put("time", object.getTime());
        contentValues.put("addedOn", new Timestamp(System.currentTimeMillis()).toString());

        if(db.insert("reminders", null, contentValues)>0){return true;}
        else{return false;}
    }

    public Cursor getReminderData(String id){
        String query;
        if(id==null){
            query = "Select * FROM reminders ORDER BY _id DESC";
        }else{
            query = "Select * FROM reminders WHERE _id='"+id+"'";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        return res;
    }

}