package org.pointstone.cugapp.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.bean.CourseDay;
import org.pointstone.cugapp.utils.InformationShared;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/8.
 */

public class ListRemoteViewFactory  implements RemoteViewsService.RemoteViewsFactory {

    private Context context = null;
    private int appWidgetId;
    private static ArrayList<CourseDay> data;
    public String a;
    public ListRemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

      /*    data = (ArrayList<CourseDay>)
                CourseDayWidget.data
                        .clone();*/

   //  a=intent.getStringExtra("a");

    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_course_day_item);

         CourseDay  course = data.get(position);
    /*   remoteView.setTextViewText(R.id.cname_tv, "微机接口课程设计");
        remoteView.setTextViewText(R.id.tname_tv, "蔷薇");
        remoteView.setTextViewText(R.id.place_tv, "教三楼");
        remoteView.setTextViewText(R.id.start_tv, a);
        remoteView.setTextViewText(R.id.end_tv, "11:00");
        remoteView.setTextViewText(R.id.time_tv, "09-11");*/
       remoteView.setTextViewText(R.id.cname_tv, course.getCname());
        remoteView.setTextViewText(R.id.tname_tv, course.getTname());
        remoteView.setTextViewText(R.id.place_tv, course.getPlace());
        remoteView.setTextViewText(R.id.time_tv, course.getTime());


        String t=course.getTime();
        if(t.length()==3)
        {
            t="0"+t.substring(0,2)+"0"+t.substring(2);
        }else if(t.length()==4)
        {
            t="0"+t;
        }
        remoteView.setTextViewText(R.id.time_tv, t);

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

        String time=course.getTime();
        if(!time.equals(""))
        {

            remoteView.setTextViewText(R.id.start_tv, ClassTime[Integer.parseInt(time.substring(0,time.indexOf("-")))]);
            remoteView.setTextViewText(R.id.end_tv, ClassTime[Integer.parseInt(time.substring(time.indexOf("-")+1))]);
        }else
        {
            remoteView.setTextViewText(R.id.start_tv, "");
            remoteView.setTextViewText(R.id.end_tv, "");
        }

        return remoteView;
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
         data = (ArrayList<CourseDay>)
                CourseDayWidget.data
                        .clone();
    }

    @Override
    public void onDataSetChanged() {
        data = (ArrayList<CourseDay>)
                CourseDayWidget.data
                        .clone();
    }

    @Override
    public void onDestroy() {
    }

}
