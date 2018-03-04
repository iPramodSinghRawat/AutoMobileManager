package com.alpha.ghosty.automobilemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by iPramodSinghRawat on 27-04-2016.
 */
public class ReminderListAdapter extends BaseAdapter {

    String[] id;
    String[] title;
    String[] detail;
    String[] date;
    String[] time;

    Context context;

    //int [] imageId;

    private static LayoutInflater inflater=null;

    public ReminderListAdapter(Context mainActivity, String[] idList, String[] titleList,
                               String[] detailList, String[] dateList, String[] timeList) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        id=idList;
        title=titleList;
        detail=detailList;
        date=dateList;
        time=timeList;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
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
        TextView titleTV,detailTV,dateTV,timeTV;
        //ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.reminder_lists, null);

        holder.titleTV=(TextView) rowView.findViewById(R.id.titleTV);
        holder.detailTV=(TextView) rowView.findViewById(R.id.detailTV);
        holder.dateTV=(TextView) rowView.findViewById(R.id.dateTV);
        holder.timeTV=(TextView) rowView.findViewById(R.id.timeTV);

        holder.titleTV.setText(title[position]);
        holder.detailTV.setText(detail[position]);
        holder.dateTV.setText(date[position]);
        holder.timeTV.setText(time[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+title[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
