package org.pointstone.cugapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.Grade;

import java.util.List;

/**
 * Created by Luckysonge on 2016/12/20.
 */

public class GradeListAdapter extends BaseAdapter {
    private List<Grade> list;
    private LayoutInflater inflater;

    public GradeListAdapter(Context context, List<Grade> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    public int getCount() {
        int ret = 0;
        if(list!=null){
            ret = list.size();
        }
        return ret;
    }


    public Object getItem(int position) {
        return list.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Grade grade = (Grade) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.grade_list_item, null);
            viewHolder.kcmc = (TextView) convertView.findViewById(R.id.kcmcTv);
            viewHolder.kcxz = (TextView) convertView.findViewById(R.id.kcxzTv);
            viewHolder.xf = (TextView) convertView.findViewById(R.id.xfTv);
            viewHolder.jd = (TextView) convertView.findViewById(R.id.jdTv);
            viewHolder.cj = (TextView) convertView.findViewById(R.id.cjTv);


            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.kcmc.setText(grade.getKcmc());
        viewHolder.kcmc.setTextSize(13);
        viewHolder.kcxz.setText(grade.getKcxz());
        viewHolder.kcxz.setTextSize(13);
        viewHolder.xf.setText(grade.getXf());
        viewHolder.xf.setTextSize(13);
        viewHolder.jd.setText(grade.getJd());
        viewHolder.jd.setTextSize(13);
        viewHolder.cj.setText(grade.getCj());
        viewHolder.cj.setTextSize(13);


        return convertView;
    }

    public static class ViewHolder{
        public TextView kcmc;
        public TextView kcxz;
        public TextView xf;
        public TextView jd;
        public TextView cj;
    }
}
