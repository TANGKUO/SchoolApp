package org.pointstone.cugapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.InformationShared;

/**
 * Created by Administrator on 2017/2/14.
 */

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private String[][] contents;

    public GridRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        contents=CourseWidget.con.clone();


    }
    @Override
    public void onCreate() {
        contents=CourseWidget.con.clone();
    }

    @Override
    public void onDataSetChanged() {
        contents=CourseWidget.con.clone();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 21;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        // 获取 grid_view_item.xml 对应的RemoteViews
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_item);

        String course=(String)getItem(i);
        rv.setTextViewText(R.id.grid_text, "");
        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_transparent));
        if(course!=null)
        {
            //如果有课,那么添加数
            if( course.charAt(5)!='\n'){

                if (course.charAt(1)==':')
                {
                    rv.setTextViewText(R.id.grid_text, course.substring(2,course.length()-1));
                }else{
                    rv.setTextViewText(R.id.grid_text, course.substring(3,course.length()-1));
                }

                rv.setTextColor(R.id.grid_text, Color.WHITE);


                String spl[]=course.split(":");
                if(spl.length==2){
                    rv.setTextViewText(R.id.grid_text, spl[1]);
                }

                int color=Integer.parseInt(spl[0]);
                //  if(spl.length==2){
                switch( color%10 ) {
                    case 0:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color0));

                        break;
                    case 1:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color1));
                        break;
                    case 2:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color2));
                        break;
                    case 3:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color3));
                        break;
                    case 4:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color4));
                        break;
                    case 5:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color5));
                        break;
                    case 6:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color6));
                        break;
                    case 7:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color7));
                        break;
                    case 8:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color8));
                        break;
                    case 9:
                        rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color9));
                        break;
                }
                //  }else
          /*  {
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
                }*/
            }
        }

        // 设置 第position位的“视图”对应的响应事件
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(CourseWidget.COLLECTION_VIEW_EXTRA, i);
        rv.setOnClickFillInIntent(R.id.widgetCourseLayout, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public Object getItem(int position) {
        if(InformationShared.getInt("isDown")!=0)
        {
            position=position+21;
        }
        //求余得到二维索引
        int column = position % 7;
        //求商得到二维索引
        int row = position / 7;
        return contents[row][column];
    }
}
