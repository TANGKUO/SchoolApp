package org.pointstone.cugapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.Finance_xf;

import java.util.List;

/**
 * Created by Luckysonge on 2016/12/20.
 */

public class Finance_xf_ListAdapter extends BaseAdapter {
    private List<Finance_xf> list;
    private LayoutInflater inflater;

    public Finance_xf_ListAdapter(Context context, List<Finance_xf> list){
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

        Finance_xf xf = (Finance_xf) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.xf_list_item, null);
            viewHolder.jfxm = (TextView) convertView.findViewById(R.id.jfxmTv);
            viewHolder.jfn = (TextView) convertView.findViewById(R.id.jfnTv);
            viewHolder.yjje = (TextView) convertView.findViewById(R.id.yjjeTv);
            viewHolder.sjje = (TextView) convertView.findViewById(R.id.sjjeTv);
            viewHolder.qf = (TextView) convertView.findViewById(R.id.qfTv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.jfxm.setText(xf.getJfxm());
        viewHolder.jfxm.setTextSize(13);
        viewHolder.jfn.setText(xf.getJfn());
        viewHolder.jfn.setTextSize(13);
        viewHolder.yjje.setText(xf.getYjje());
        viewHolder.yjje.setTextSize(13);
        viewHolder.sjje.setText(xf.getSjje());
        viewHolder.sjje.setTextSize(13);
        viewHolder.qf.setText(xf.getQf());
        viewHolder.qf.setTextSize(13);
        return convertView;
    }

    public static class ViewHolder{
        public TextView jfxm;
        public TextView jfn;
        public TextView yjje;
        public TextView sjje;
        public TextView qf;
    }
}
