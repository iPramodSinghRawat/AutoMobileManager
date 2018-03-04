package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.Random;

public class Statistics extends SuperActivity implements OnChartValueSelectedListener {//OnChartGestureListener,
    //private static int[] COLORS;// = new int[] { Color.GREEN, Color.BLUE,Color.MAGENTA, Color.CYAN };
    //private static double[] VALUES;// = new double[] { 100.09, 110.05, 120.10, 130.30 };
    //private static String[] NAME_LIST;// = new String[] { "A", "B", "C", "D" };

    private static double[] VALUES= new double[] { 100.09, 110.05, 120.10, 130.30 };
    private static String[] NAME_LIST= new String[] { "A", "B", "C", "D" };

    VehicleData[] objectAry;
    ArrayAdapter<String> statisticsType,userVehicleLists,chartType;
    android.app.AlertDialog.Builder builderSingle;

    DBhandler dbHandler;// = new DBhandler(this, null, null, 1);
    SharedPreferences sharedpreferences;
    int noOfUserVehicle;
    //int randomColors[];

    String[] userVehicleId;
    String[] userVehicleList;

    //private static String[] userVehicleId;
    //private static String[] userVehicleList;
    //private static double[] totalExpense;
    //private static double[] fuelExpense;
    //private static double[] serviceExpense;
    //private static double[] otherExpense;
    //private static double[] tripExpense;

    //private CategorySeries mSeries;// = new CategorySeries("");
    //private DefaultRenderer mRenderer; //= new DefaultRenderer();
    //XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    //XYSeriesRenderer renderer = new XYSeriesRenderer();
    //private GraphicalView mChartView;

    LinearLayout layout;
    String vId,sType,sChartType;
    TextView staticsTypeTv,selectVehicle,chartTypeTV;

    String chartDescription;
    PieChart pieChart;
    BarChart barChart;
    LineChart lineChart;

    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        dbHandler= new DBhandler(this, null, null, 1);

        sharedpreferences = getSharedPreferences("automobilemanager", Context.MODE_PRIVATE);

        //layout = (LinearLayout) findViewById(R.id.chartLayout);
        pieChart = (PieChart)findViewById(R.id.pieChart);
        //pieChart.setOnChartGestureListener(this);
        pieChart.setOnChartValueSelectedListener(this);
        barChart = (BarChart)findViewById(R.id.barChart);
        //barChart.setOnChartGestureListener(this);
        barChart.setOnChartValueSelectedListener(this);

        lineChart = (LineChart)findViewById(R.id.lineChart);
        //lineChart.setOnChartGestureListener(this);
        lineChart.setOnChartValueSelectedListener(this);

        entries = new ArrayList<>();
        barEntries = new ArrayList<>();
        labels = new ArrayList<String>();

        staticsTypeTv = (TextView) findViewById(R.id.staticsTypeTv);
        selectVehicle = (TextView) findViewById(R.id.selectVehicle);
        chartTypeTV = (TextView) findViewById(R.id.chartTypeTV);

        statisticsType = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        statisticsType.add("All");
        statisticsType.add("Combined");
        statisticsType.add("Fuel");
        statisticsType.add("Service");
        statisticsType.add("Other Expense");
        statisticsType.add("Trip");
        statisticsType.add("Insurance");

        chartType = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        chartType.add("PieChart");
        chartType.add("BarChart");
        chartType.add("LineChart");

        objectAry=dbHandler.userVehicleDetails();
        if(objectAry==null){
            Toast.makeText(getApplicationContext(), " No Vehicle Data For Statistics ", Toast.LENGTH_SHORT).show();
        }else{
            noOfUserVehicle=objectAry.length;

            //Toast.makeText(getApplicationContext(), "noOfUserVehicle "+noOfUserVehicle, Toast.LENGTH_SHORT).show();

            if(noOfUserVehicle>0){

                userVehicleLists = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);
                // userVehicleLists.add("TVS"); userVehicleLists.add("Hindustan Motor");
                userVehicleList=new String[noOfUserVehicle];
                userVehicleId=new String[noOfUserVehicle];
                for(int l=0;l<noOfUserVehicle;l++){
                    userVehicleLists.add(objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+")");//+") Type:"+objectAry[l].getType());
                    userVehicleId[l]=objectAry[l].getId();
                    userVehicleList[l]=objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+")";//+") Type:"+ objectAry[l].getType();
                }

                vId="All";
                sType="All";
                sChartType="PieChart";

                processChartValues(vId,sType);
                selectVehicle.setText("Vehicle: " + vId + " (Tap To Change)");
                staticsTypeTv.setText("Statics Type: "+sType+" (Tap To Change)");

            }else{
                Toast.makeText(getApplicationContext(), " No Data For Statistics", Toast.LENGTH_SHORT).show();
            }
        }

        //printPieChart();
    }

    protected void processChartValues(String vId,String sType){
        labels.clear();
        entries.clear();
        barEntries.clear();

        //Toast.makeText(this, "sType "+sType, Toast.LENGTH_SHORT).show();

        if(sType.equals("Combined") && vId.equals("All")){

            double fuelExpense=0;
            double serviceExpense=0;
            double otherExpense=0;
            double tripExpense=0;
            double totalExpense=0;
            double insuranceExpense=0;

            for(int l=0;l<noOfUserVehicle;l++){
                //userVehicleLists.add(objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+") Type:"+objectAry[l].getType());
                userVehicleId[l]=objectAry[l].getId();

                fuelExpense=dbHandler.getTotalExpense(userVehicleId[l],"Fuel");
                serviceExpense=dbHandler.getTotalExpense(userVehicleId[l],"Service");
                otherExpense=dbHandler.getTotalExpense(userVehicleId[l], "Other Expense");
                tripExpense=dbHandler.getTotalExpense(userVehicleId[l],"Trip");
                insuranceExpense=dbHandler.getTotalExpense(userVehicleId[l], "Insurance");

                totalExpense=fuelExpense+serviceExpense+otherExpense+tripExpense+insuranceExpense;
                //userVehicleList[l]=objectAry[l].getBrand()+" "+objectAry[l].getModel()+" ("+objectAry[l].getRegNumber()+") Type:"+ objectAry[l].getType();

                //labels.add(objectAry[l].getRegNumber());
                labels.add(userVehicleList[l]);
                entries.add(new Entry((float) totalExpense, l));
                barEntries.add(new BarEntry((float) totalExpense, l));
            }

            chartDescription="Vehicle Wise Combined Data";
        }else if(vId.equals("All")){
            if(sType.equals("All")){
                double fuelExpense=dbHandler.getTotalExpense("all","Fuel");
                double serviceExpense=dbHandler.getTotalExpense("all","Service");
                double otherExpense=dbHandler.getTotalExpense("all", "Other Expense");
                double tripExpense=dbHandler.getTotalExpense("all", "Trip");
                double insuranceExpense=dbHandler.getTotalExpense("all", "Insurance");
                //double tripExpense=dbHandler.getAllTripExpense("all");
                NAME_LIST= new String[] { "Fuel", "Service", "Other Expense", "Trip","Insurance"};
                VALUES = new double[] { fuelExpense, serviceExpense, otherExpense, tripExpense};

                labels.add("Fuel");
                labels.add("Service");
                labels.add("Other Expense");
                labels.add("Trip");
                labels.add("Insurance");

                entries.add(new Entry((float) fuelExpense, 0));
                entries.add(new Entry((float) serviceExpense, 1));
                entries.add(new Entry((float) otherExpense, 2));
                entries.add(new Entry((float) tripExpense, 3));
                entries.add(new Entry((float) insuranceExpense, 4));

                //BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
                barEntries.add(new BarEntry((float) fuelExpense, 0));
                barEntries.add(new BarEntry((float) serviceExpense, 1));
                barEntries.add(new BarEntry((float) otherExpense, 2));
                barEntries.add(new BarEntry((float) tripExpense, 3));
                barEntries.add(new BarEntry((float) insuranceExpense, 4));

                chartDescription="All Vehicle Sum Data";
            }
            /*
            else if(sType.equals("Trip")){
                VALUES = new double[noOfUserVehicle];
                for(int l=0;l<noOfUserVehicle;l++){
                    NAME_LIST=userVehicleList;
                    double expense=dbHandler.getAllTripExpense(userVehicleId[l]);
                    VALUES[l]=expense;

                    labels.add(NAME_LIST[l]);
                    entries.add(new Entry((float) VALUES[l], l));
                    barEntries.add(new BarEntry((float) VALUES[l], l));
                }
                chartDescription="All Vehicle Trip Data";
            }
            */
            else{
                VALUES = new double[noOfUserVehicle];
                for(int l=0;l<noOfUserVehicle;l++){
                    NAME_LIST=userVehicleList;
                    double expense=dbHandler.getTotalExpense(userVehicleId[l],sType);
                    VALUES[l]=expense;
                    labels.add(NAME_LIST[l]);
                    entries.add(new Entry((float) VALUES[l], l));
                    barEntries.add(new BarEntry((float) VALUES[l], l));
                }
                chartDescription="All Vehicle "+sType+" Data";
            }
        }else{
            double fuelExpense=dbHandler.getTotalExpense(vId,"Fuel");
            double serviceExpense=dbHandler.getTotalExpense(vId,"Service");
            double otherExpense=dbHandler.getTotalExpense(vId, "Other Expense");
            double tripExpense=dbHandler.getTotalExpense(vId, "Trip");
            //double tripExpense=dbHandler.getAllTripExpense(vId);
            double insuranceExpense=dbHandler.getTotalExpense(vId, "Insurance");

            NAME_LIST= new String[] { "Fuel", "Service", "Other Expense", "Trip","Insurance"};
            VALUES = new double[] { fuelExpense, serviceExpense, otherExpense, tripExpense};

            labels.add("Fuel");
            labels.add("Service");
            labels.add("Other Expense");
            labels.add("Trip");
            labels.add("Insurance");

            entries.add(new Entry((float) fuelExpense, 0));
            entries.add(new Entry((float) serviceExpense, 1));
            entries.add(new Entry((float) otherExpense, 2));
            entries.add(new Entry((float) tripExpense, 3));
            entries.add(new Entry((float) insuranceExpense, 4));

            barEntries.add(new BarEntry((float) fuelExpense, 0));
            barEntries.add(new BarEntry((float) serviceExpense, 1));
            barEntries.add(new BarEntry((float) otherExpense, 2));
            barEntries.add(new BarEntry((float) tripExpense, 3));
            barEntries.add(new BarEntry((float) insuranceExpense, 4));

            chartDescription="Selected Vehicle Sum Data";
        }

        //printPieChart();
        if(sChartType.equals("PieChart")){drawPieChart();
        }else if(sChartType.equals("BarChart")){drawBarChart();
        }else{
            drawLineChart();
        }
    }

    public void drawPieChart(){
        barChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.VISIBLE);

        pieChart.clear();
        pieChart.setData(null);

        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color

        PieData data = new PieData(labels, dataset); // initialize Piedata
        pieChart.setData(data); //set data into chart
        pieChart.setDescription("Description: "+chartDescription);  // set the description
        pieChart.setDescriptionTextSize(16);
        pieChart.animateY(5000);
        pieChart.setDrawHoleEnabled(false);

        /// get the legend (only possible after setting data)
        Legend lgnd = pieChart.getLegend();
        // modify the legend ... by default it is on the left
        lgnd.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        lgnd.setForm(Legend.LegendForm.CIRCLE);
        lgnd.setFormSize(20);
        lgnd.setTextSize(18);
    }

    public void drawBarChart(){
        pieChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.INVISIBLE);
        barChart.setVisibility(View.VISIBLE);

        barChart.clear();
        barChart.setData(null); //set data into chart
        //pieChart
        //PieDataSet dataset = new PieDataSet(entries, "# of Calls");
        BarDataSet dataset = new BarDataSet(barEntries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color
        dataset.setValueTextSize(16);
        //dataset.entryT

        BarData data = new BarData(labels, dataset); // initialize Piedata
        barChart.setData(data); //set data into chart
        barChart.setDescription("Description: "+chartDescription);  // set the description
        barChart.setDescriptionTextSize(16);
        barChart.animateY(5000);
        //barChart.
        //barChart.setDrawHoleEnabled(false);

        /// get the legend (only possible after setting data)
        Legend lgnd = barChart.getLegend();
        //lgnd.setEnabled(true);
        // modify the legend ... by default it is on the left
        lgnd.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        lgnd.setForm(Legend.LegendForm.SQUARE);
        lgnd.setFormSize(20);
        lgnd.setTextSize(18);
    }

    public void drawLineChart(){
        pieChart.setVisibility(View.INVISIBLE);
        barChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);

        lineChart.clear();
        lineChart.setData(null); //set data into chart

        LineDataSet dataset = new LineDataSet(entries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color

        LineData data = new LineData(labels, dataset); // initialize Piedata
        lineChart.setData(data); //set data into chart
        lineChart.setDescription("Description: "+chartDescription);  // set the description
        lineChart.setDescriptionTextSize(16);
        lineChart.animateY(5000);

        /// get the legend (only possible after setting data)
        Legend lgnd = lineChart.getLegend();
        // modify the legend ... by default it is on the left
        lgnd.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        lgnd.setForm(Legend.LegendForm.LINE);
        lgnd.setFormSize(20);
        lgnd.setTextSize(18);
    }
/*
    @Override
    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
        Toast.makeText(getApplicationContext(), "onChartGestureStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
        Toast.makeText(getApplicationContext(), "onChartGestureEnd", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
        Toast.makeText(getApplicationContext(), "onChartLongPressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
        Toast.makeText(getApplicationContext(), "onChartDoubleTapped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
        Toast.makeText(getApplicationContext(), "onChartSingleTapped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
        Toast.makeText(getApplicationContext(), "onChartFling", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartScale(MotionEvent motionEvent, float v, float v1) {
        Toast.makeText(getApplicationContext(), "onChartScale", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {
        Toast.makeText(getApplicationContext(), "onChartTranslate", Toast.LENGTH_SHORT).show();
    }
    */

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        Toast.makeText(getApplicationContext(), "Value Selected: "+i+" Entry: "+entry, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    /* Function: To Select Chart Type*/
    public void selectChart(View view){
        builderSingle = new android.app.AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.presentation);
        builderSingle.setTitle("Select Chart Type");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chartTypeTV.setText("Default Chart: PieChart (Tap Here)");
                        sChartType = "PieChart";
                        processChartValues(vId, sType);
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(chartType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sChartType=chartType.getItem(which);//.getItem(which);
                        processChartValues(vId, sType);
                        chartTypeTV.setText(chartType.getItem(which) + " (Tap To Change)");
                    }
                });
        builderSingle.show();
    }

    /* Function: To Select Vehicle For Which Service Done */
    public void selectVehicle(View view){
        builderSingle = new android.app.AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Vehicle");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectVehicle.setText("All Vehicle (Tap Here)");
                        //staticsTypeTv.setText("Stat Type: " + sType + " (Tap To Change)");
                        vId = "All";
                        processChartValues(vId, sType);
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(userVehicleLists,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vId = objectAry[which].getId();//.getItem(which);
                        //slctVId = vehicleIdAry[which];//.getItem(which);
                        //layout.removeView(mChartView);
                        //mChartView=null;
                        processChartValues(vId, sType);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText(userVehicleLists.getItem(which) + " (Tap To Change)");
                        staticsTypeTv.setText("Stat Type: All (Tap To Change)");
                    }
                });
        builderSingle.show();
    }

    public void selectStatType(final View view){

        //Toast.makeText(this, "You Set: ", Toast.LENGTH_LONG).show();
        //usrCurrencyTxt.setText("Rs hhgfd");
        builderSingle = new android.app.AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.indian_rupee);
        builderSingle.setTitle("Select Expense Type");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        staticsTypeTv.setText("Select Expense Type (Tap Here)");
                        selectVehicle.setText("All Vehicle (Tap Here)");
                        sType ="All";
                        processChartValues("All", sType);
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(statisticsType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sType = statisticsType.getItem(which);
                        //usrCurrency=getResources().getString(currencySymb[which]);
                        selectVehicle.setText("All Vehicle (Tap Here)");
                        staticsTypeTv.setText("Stat Type: " + sType + " (Tap To Change)");
                        processChartValues("All", sType);
                        //SharedPreferences.Editor editor = sharedpreferences.edit();
                        //editor.putString("typeOfExpense", typeOfExpense); ///*Saving Data To Protect Change on Camera Intent/
                        //editor.commit();

                    }
                });
        builderSingle.show();
    }

    public int[] generateRandomColor(int n){
        Random rand = new Random();
        int randomColors[] = new int[n];

        for(int i=0;i<n;i++){
            int r,g,b;

            r = rand.nextInt(255);
            g = rand.nextInt(255);
            b = rand.nextInt(255);

            if(r==g || g==b ||b==r){
                //r = rand.nextInt(255);
                //g = rand.nextInt(255);
                //b = rand.nextInt(255);
                generateRandomColor(n);
            }
            else{
                randomColors[i]= Color.rgb(r, g, b);
            }

            //randomColors[i]= Color.rgb(r, g, b);
        }
        return randomColors;
    }
}
