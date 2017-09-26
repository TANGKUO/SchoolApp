package org.pointstone.cugapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.bean.CourseDay;
import org.pointstone.cugapp.utils.InformationShared;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public class CourseDayAdapter extends BaseAdapter {
    private List<CourseDay> data;
    private Context context;
    private LayoutInflater mInflater;

    public CourseDayAdapter(List<CourseDay> data, Context context) {
        this.data = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.course_day_item, null);
            holder = new ViewHolder();
            holder.cname_tv = (TextView) convertView.findViewById(R.id.cname_tv);
            holder.tname_tv = (TextView) convertView.findViewById(R.id.tname_tv);
            holder.place_tv = (TextView) convertView.findViewById(R.id.place_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.start_tv = (TextView) convertView.findViewById(R.id.start_tv);
            holder.end_tv = (TextView) convertView.findViewById(R.id.end_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CourseDay c = data.get(position);
        holder.cname_tv.setText(c.getCname());
        holder.tname_tv.setText(c.getTname());
        holder.place_tv.setText(c.getPlace());
        String t=c.getTime();
        if(t.length()==3)
        {
            t="0"+t.substring(0,2)+"0"+t.substring(2);
        }else if(t.length()==4)
        {
            t="0"+t;
        }
        holder.time_tv.setText(t);

        String ClassTime[]=new String[13];
        ClassTime[1]="08:00";
        ClassTime[2]="09:35";

        ClassTime[3]="10:05";
        ClassTime[4]="11:40";


        if (InformationShared.getInt("SummerWinter")==0)
        {
            ClassTime[5]="14:00";
            ClassTime[6]="15:35";

            ClassTime[7]="16:00";
            ClassTime[8]="17:35";

            ClassTime[9]="19:00";
            ClassTime[10]="20:35";

            ClassTime[11]="21:05";
            ClassTime[12]="22:40";
        }else{
            ClassTime[5]="14:30";
            ClassTime[6]="16:05";

            ClassTime[7]="16:25";
            ClassTime[8]="18:00";

            ClassTime[9]="19:30";
            ClassTime[10]="21:05";

            ClassTime[11]="21:35";
            ClassTime[12]="23:10";
        }

        String time=c.getTime();
        if(!time.equals(""))
        {
            holder.start_tv.setText(ClassTime[Integer.parseInt(time.substring(0,time.indexOf("-")))]);
            holder.end_tv.setText(ClassTime[Integer.parseInt(time.substring(time.indexOf("-")+1))]);
        }else
        {
            holder.start_tv.setText("");
            holder.end_tv.setText("");
        }

        return convertView;
    }

    static class ViewHolder{
        TextView cname_tv;
        TextView tname_tv;
        TextView place_tv;
        TextView start_tv;
        TextView end_tv;
        TextView time_tv;

    }
}
