package org.pointstone.cugapp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.CourseDetailActivity;

/**
 * Created by Administrator on 2016/12/5.
 */

public class CourseGridAdapter extends BaseAdapter {
    private Context mContext;

    private String[][] contents;

    private int rowTotal;

    private int columnTotal;

    private int positionTotal;

    public CourseGridAdapter(Context context) {
        this.mContext = context;

    }

    public int getCount() {
        return positionTotal;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        //求余得到二维索引
        int column = position % columnTotal;
        //求商得到二维索引
        int row = position / columnTotal;
        return contents[row][column];
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(final int position, View convertView, ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grib_item, null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.text);
        String course=(String)getItem(position);
        //如果有课,那么添加数据
        if( course.charAt(0)!='\n'){
            if (course.charAt(1)==':')
            {
                textView.setText(course.substring(2,course.length()-1));
            }else{
                textView.setText(course.substring(3,course.length()-1));
            }

            textView.setTextColor(Color.WHITE);
            //变换颜色
            String spl[]=course.split(":");
            if(spl.length==2){
                textView.setText(spl[1]);
            }

            int color=Integer.parseInt(spl[0]);
            if(spl.length==2){
                switch( color%10 ) {
                    case 0:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color0));
                        break;
                    case 1:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color1));
                        break;
                    case 2:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color2));
                        break;
                    case 3:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color3));
                        break;
                    case 4:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color4));
                        break;
                    case 5:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color5));
                        break;
                    case 6:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color6));
                        break;
                    case 7:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color7));
                        break;
                    case 8:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color8));
                        break;
                    case 9:
                        textView.setBackground(mContext.getResources().getDrawable(R.color.course_grid_color9));
                        break;
                }
            }else
            {
                switch( color%10 ) {
                    case 0:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_0));

                        break;
                    case 1:
                       textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_1));

                        break;
                    case 2:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_2));
                        break;
                    case 3:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_3));
                        break;
                    case 4:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_4));
                        break;
                    case 5:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_5));
                        break;
                    case 6:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_6));
                        break;
                    case 7:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_7));
                        break;
                    case 8:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_8));
                        break;
                    case 9:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_9));
                        break;
                }
            }


        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row = position / columnTotal;
                int column = position % columnTotal;
                String con =contents[row][column] ;
                Intent intent=new Intent(mContext, CourseDetailActivity.class);
                intent.putExtra("data", con.substring(0,con.length()-1));
                intent.putExtra("time", con.charAt(con.length()-1)+"");
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * 设置内容、行数、列数
     */
    public void setContent(String[][] contents, int row, int column) {
        this.contents = contents;
        this.rowTotal = row;
        this.columnTotal = column;
        positionTotal = rowTotal * columnTotal;
    }
}
