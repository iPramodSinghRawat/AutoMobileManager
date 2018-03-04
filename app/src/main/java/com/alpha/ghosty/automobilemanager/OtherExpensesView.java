package com.alpha.ghosty.automobilemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class OtherExpensesView extends SuperActivity
        implements SimpleGestureFilter.SimpleGestureListener {

    TextView vehicleDetailTv,typeOfExpenseTv,odoMtrRdingTv,dateTv,priceTv,receiptOptTv,addedOnTv;
    ImageView receiptView;

    ExpenseData[] object;
    int dtLen=0,i=0;
    DBhandler dbHandler;
    private SimpleGestureFilter detector;
    String vehicleId;
    String crntRecordId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_expenses_view);

        dbHandler= new DBhandler(this, null, null, 1);

        vehicleDetailTv = (TextView) findViewById(R.id.vehicleDetailTv);
        typeOfExpenseTv = (TextView) findViewById(R.id.typeOfExpenseTv);
        odoMtrRdingTv = (TextView) findViewById(R.id.odoMtrRdingTv);
        dateTv = (TextView) findViewById(R.id.dateTv);
        priceTv = (TextView) findViewById(R.id.priceTv);
        receiptOptTv = (TextView) findViewById(R.id.receiptOptTv);
        addedOnTv = (TextView) findViewById(R.id.addedOnTv);
        receiptView = (ImageView) findViewById(R.id.receiptView);

        vehicleId=getIntent().getStringExtra("vehicleId");
        object=dbHandler.vehicleExpenseData(vehicleId);
        dtLen=object.length;

        VehicleData object2=dbHandler.getVehicleData(vehicleId);
        vehicleDetailTv.setText(object2.getBrand()+" "+object2.getModel()+" ("+object2.getRegNumber()+")");
        detector = new SimpleGestureFilter(this,this);

        showOExpenseData(i);//here i=0;
    }

    public void showOExpenseData (int index){
        crntRecordId=object[index].getId();
        typeOfExpenseTv.setText("Expense Type: "+ object[index].getExpenseType());
        odoMtrRdingTv.setText("Expense Done at Meter Reading: "+ object[index].getMeterReading());
        dateTv.setText("Expense On : "+ date2Show(object[index].getDate()));
        priceTv.setText("Paid: "+ object[index].getPrice());
        receiptOptTv.setText("Receipt: "+ object[index].getReceipt());
        addedOnTv.setText("Data Added On : " + object[index].getAddedOn());

        if (object[index].getReceipt().equals("0")) {
            receiptOptTv.setText("Receipt: N/A ");
            receiptView.setImageDrawable(null);
        }else {
            receiptOptTv.setText("Receipt: Yes");
            String fileName = "oexpenses_receipt_" + object[index].getVehicleId() + "_" + object[index].getId()+".jpg";
            //String filename="amm_"+file_name+".jpg";
            String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+appDataFolder+"/"+fileName;
            File destination = new File(destinationPath);
            show_image_file(receiptView, destination);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //View v = getCurrentFocus();
        //Toast.makeText(MainActivity.this, "I am Touched", Toast.LENGTH_LONG).show();
        this.detector.onTouchEvent(event);
        boolean ret = super.dispatchTouchEvent(event);
        return ret;
    }

    @Override
    public void onSwipe(int direction) {
        // String str = "";
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT :
                showPre();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :
                showNext();
                break;
            /*
            case SimpleGestureFilter.SWIPE_DOWN :
                //str = "Swipe Down";
                //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case SimpleGestureFilter.SWIPE_UP :
                //str = "Swipe Up";
                //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            */
        }
    }

    public void showPre(){
        int j=i-1;
        if(j<0){
            Toast.makeText(this, "Start of List", Toast.LENGTH_SHORT).show();
            i=0;
        }else{
            showOExpenseData(j);
            i--;
        }
    }
    public void showNext(){
        int j=i+1;
        if(j>= dtLen){
            Toast.makeText(this, "End of List", Toast.LENGTH_SHORT).show();
            i= dtLen-1;
        }else{
            showOExpenseData(j);
            i++;
        }
    }

    @Override
    public void onDoubleTap() {

        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
        //processSubmitInsuranceData();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        processDeleteData("otherExpense");
                        //Toast.makeText(getBaseContext(),"Yes Clicked",Toast.LENGTH_LONG).show();
                        //processSubmitInsuranceData();
                        //Toast.makeText(getBaseContext(), " DataId :", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        //Toast.makeText(getBaseContext(), "No Clicked",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Confirm To Delete This Record ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void processDeleteData(String recordType){

        String returnString=dbHandler.deleteData(recordType,crntRecordId);
        if(returnString.equals("1")){
            Toast.makeText(getBaseContext(),"Record Deleted",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(),ShowRecords.class);
            intent.putExtra("vehicleId",vehicleId);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getBaseContext(),returnString,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        if(getIntent().getExtras().containsKey("caller")) {
            Intent intent = new Intent(getBaseContext(),ShowRecords.class);
            intent.putExtra("vehicleId",vehicleId);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(getBaseContext(),MenuPage.class);
            startActivity(intent);
            finish();
        }
    }
}
