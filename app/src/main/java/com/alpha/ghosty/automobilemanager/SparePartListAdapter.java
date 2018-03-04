package com.alpha.ghosty.automobilemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by iPramodSinghRawat on 07-11-2016.
 */

public class SparePartListAdapter extends BaseAdapter{

    String[] ids,vehicleDtl,partNos,partNames,partDetailss,quantitys,images,addedOns;

    Context context;

    //int [] imageId;

    DBhandler dbHandler;

    private static LayoutInflater inflater=null;

    AlertDialog.Builder builderSingle;

    public SparePartListAdapter(Context mainActivity, String[] idList, String[] vehicleIdList,
                                String[] partNoList, String[] partNameList, String[] partDetailsList,
                                String[] quantityList,String[] imageList,String[] addedOnList) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        ids=idList;
        vehicleDtl=vehicleIdList;
        partNos=partNoList;
        partNames=partNameList;
        partDetailss=partDetailsList;
        quantitys=quantityList;
        images=imageList;
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

    public class Holder
    {
        TextView vehicleDetailTV,partNameTV,partDetailTV,addedOnTV;
        //ImageView partImageView;
        ImageView editIV,deleteIV;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        SparePartListAdapter.Holder holder=new SparePartListAdapter.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.spare_part_lists, null);

        holder.vehicleDetailTV=(TextView) rowView.findViewById(R.id.vehicleDetailTV);
        holder.partNameTV=(TextView) rowView.findViewById(R.id.partNameTV);
        holder.partDetailTV=(TextView) rowView.findViewById(R.id.partDetailTV);
        holder.addedOnTV=(TextView) rowView.findViewById(R.id.addedOnTV);
        holder.editIV=(ImageView) rowView.findViewById(R.id.editIV);
        holder.deleteIV=(ImageView) rowView.findViewById(R.id.deleteIV);

        holder.vehicleDetailTV.setText("Vehicle: " + vehicleDtl[position]);
        holder.partNameTV.setText(partNames[position]+" ("+partNos[position]+")"+" ("+quantitys[position]+")");
        holder.partDetailTV.setText(partDetailss[position]);
        holder.addedOnTV.setText(addedOns[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked "+partNames[position], Toast.LENGTH_SHORT).show();

                // TODO: Go TO Spare Part Detail Activity

                Intent intent = new Intent(context,SparePartDetail.class);
                intent.putExtra("sparePartRecordId",ids[position]);
                context.startActivity(intent);
            }
        });

        holder.editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Tap to Edit Record", Toast.LENGTH_SHORT).show();
                //goToEditSparePartRecord(ids[position]);

                Intent intent = new Intent(context,EditSparePartRecord.class);
                intent.putExtra("sparePartRecordId",ids[position]);
                context.startActivity(intent);
            }
        });

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Tap to Delete "+partNames[position], Toast.LENGTH_SHORT).show();
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

                        if(dbHandler.deleteSparePartData(itemId)==true){

                            context.startActivity(new Intent(context, SpareParts.class));

                        }else{
                            Toast.makeText(context,"Error Try Again",Toast.LENGTH_SHORT).show();
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
