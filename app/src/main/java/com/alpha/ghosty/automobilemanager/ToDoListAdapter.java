package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by iPramodSinghRawat on 29-11-2016.
 */

public class ToDoListAdapter extends BaseAdapter{

    String[] ids,vehicles,titles,details,addedOns;

    Context context;

    //int [] imageId;

    private static LayoutInflater inflater=null;

    DBhandler dbHandler;

    public ToDoListAdapter(){

    }

    public ToDoListAdapter(Context mainActivity, String[] idList,String[] vehicleList,
                           String[] titleList,String[] detailList,String[] addedOnList) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        ids=idList;
        vehicles=vehicleList;
        titles=titleList;
        details=detailList;
        addedOns=addedOnList;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHandler = new DBhandler(context, null, null, 1);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ids.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder{
        TextView vehicleDetailTV,titleTV,detailTV,addedOnTV;
        //ImageView partImageView;
        ImageView deleteIV;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ToDoListAdapter.Holder holder=new ToDoListAdapter.Holder();

        View rowView;
        rowView = inflater.inflate(R.layout.to_do_list, null);

        holder.vehicleDetailTV=(TextView) rowView.findViewById(R.id.vehicleDetailTV);
        holder.titleTV=(TextView) rowView.findViewById(R.id.titleTV);
        holder.detailTV=(TextView) rowView.findViewById(R.id.detailTV);
        holder.addedOnTV=(TextView) rowView.findViewById(R.id.addedOnTV);
        holder.deleteIV=(ImageView) rowView.findViewById(R.id.deleteIV);

        holder.vehicleDetailTV.setText("Vehicle: " + vehicles[position]);
        holder.titleTV.setText("Title: " + titles[position]);
        holder.detailTV.setText("ToDo: "+ details[position]);
        holder.addedOnTV.setText(addedOns[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Auto-generated method stub
                Toast.makeText(context, "You Clicked "+titles[position], Toast.LENGTH_LONG).show();
            }
        });

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Auto-generated method stub
                //Toast.makeText(context, "You Tap to Delete "+titles[position], Toast.LENGTH_LONG).show();
                deleteListItem(ids[position]);
            }
        });

        return rowView;
    }

    public void deleteListItem(final String itemId){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        if(dbHandler.deleteToDoData(itemId)==true){

                            context.startActivity(new Intent(context, ToDos.class));

                        }else{
                            Toast.makeText(context,"Error Try Again",Toast.LENGTH_LONG).show();
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage("Sure to Delete ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}
