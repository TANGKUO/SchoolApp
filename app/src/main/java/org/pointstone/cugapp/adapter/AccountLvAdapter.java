package org.pointstone.cugapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.bean.ConsumeData;

import java.util.List;

/**
 * Created by keyboy on 2016/12/20.
 */

public class AccountLvAdapter extends BaseAdapter {
    private Context context;                        //运行上下文
    private List<ConsumeData> listItems;            //数据集合
    private LayoutInflater listContainer;           //视图容器


    public AccountLvAdapter(List<ConsumeData> listItems,Context context) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    public int getCount() {
        return listItems.size();
    }

    public Object getItem(int position) {
        return listItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.account_lv_item,parent,false);
            holder = new ViewHolder();
            holder.img_icon = (ImageView) convertView.findViewById(R.id.recharge_account_img);
            holder.money_content = (TextView) convertView.findViewById(R.id.recharge_account_money_tv);
            holder.time_content = (TextView) convertView.findViewById(R.id.recharge_account_time_tv);
            holder.use_content = (TextView) convertView.findViewById(R.id.recharge_account_use_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img_icon.setImageResource(listItems.get(position).getImgId());
        holder.money_content.setText(listItems.get(position).getContentMoney());
        holder.time_content.setText(listItems.get(position).getContentTime());
        holder.use_content.setText(listItems.get(position).getContentUse());
        return convertView;
    }
    private class ViewHolder{
        ImageView img_icon;
        TextView money_content;
        TextView time_content;
        TextView use_content;
    }
//    public void add(ConsumeData data) {
//        if (listItems == null) {
//            listItems = new LinkedList<>();
//        }
//        listItems.add(data);
//        notifyDataSetChanged();
//    }

}
