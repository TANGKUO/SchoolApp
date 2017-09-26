package org.pointstone.cugapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.Exam;

import java.util.List;

/**
 * Created by Luckysonge on 2016/12/20.
 */

public class ExamListAdapter extends BaseAdapter {
    private List<Exam> list;
    private LayoutInflater inflater;

    public ExamListAdapter(Context context, List<Exam> list){
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

        Exam exam = (Exam) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.exam_list_item, null);
            viewHolder.kczwmc = (TextView) convertView.findViewById(R.id.kczwmcTv);
            viewHolder.kssj = (TextView) convertView.findViewById(R.id.kssjTv);
//            viewHolder.jsmc = (TextView) convertView.findViewById(R.id.jsmcTv);
//            viewHolder.zwh = (TextView) convertView.findViewById(R.id.zwhTv);



            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.kczwmc.setText(exam.getKczwmc());
        viewHolder.kczwmc.setTextSize(13);
        viewHolder.kssj.setText(exam.getKssj());
        viewHolder.kssj.setTextSize(13);
//        viewHolder.jsmc.setText(exam.getJsmc());
//        viewHolder.jsmc.setTextSize(13);
//        viewHolder.zwh.setText(exam.getZwh());
//        viewHolder.zwh.setTextSize(13);

        return convertView;
    }

    public static class ViewHolder{
        public TextView kczwmc;
        public TextView kssj;
        public TextView jsmc;
        public TextView zwh;
    }
}
