package org.pointstone.cugapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.Finance_jz;

import java.util.List;

/**
 * Created by Luckysonge on 2016/12/20.
 */

public class Finance_jz_ListAdapter extends BaseAdapter {
    private List<Finance_jz> list;
    private LayoutInflater inflater;

    public Finance_jz_ListAdapter(Context context, List<Finance_jz> list){
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

        Finance_jz jz = (Finance_jz) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.jz_list_item, null);
            viewHolder.jelx = (TextView) convertView.findViewById(R.id.jelxTv);
            viewHolder.yff = (TextView) convertView.findViewById(R.id.yffTv);
            viewHolder.sjff = (TextView) convertView.findViewById(R.id.sjffTv);
            viewHolder.jesm = (TextView) convertView.findViewById(R.id.jesmTv);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.jelx.setText(jz.getJelx());
        viewHolder.jelx.setTextSize(13);
        viewHolder.yff.setText(jz.getYff());
        viewHolder.yff.setTextSize(13);
        viewHolder.sjff.setText(jz.getSjff());
        viewHolder.sjff.setTextSize(13);
        viewHolder.jesm.setText(jz.getJesm());
        viewHolder.jesm.setTextSize(13);
//        viewHolder.kssj.setTextSize(13);
//        viewHolder.jsmc.setText(exam.getJsmc());
//        viewHolder.jsmc.setTextSize(13);
//        viewHolder.zwh.setText(exam.getZwh());
//        viewHolder.zwh.setTextSize(13);

        return convertView;
    }

    public static class ViewHolder{
        public TextView jelx;
        public TextView yff;
        public TextView sjff;
        public TextView jesm;
    }
}
