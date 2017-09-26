package org.pointstone.cugapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.bean.CourseDay;
import org.pointstone.cugapp.utils.Course;
import org.pointstone.cugapp.utils.DBCourseManager;
import org.pointstone.cugapp.utils.DBCourseNewManager;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/1/30.
 */

public class CourseDayWidget extends AppWidgetProvider {


    private static int currentweek;
    private static int current_xing;
    private DBCourseManager mgr;
    private DBCourseNewManager mgr2;
    private static int changeDay = 0;
    private static int changeWeek = 0;
    public static ArrayList<CourseDay> data = new ArrayList<CourseDay>();

    /*
   * 到达指定的时间，或者用户第一次创建Appwidget所调用的方法
   */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int status=InformationShared.getInt("status");
        int login=InformationShared.getInt("login");
        if(status==1&&login==1){
            final int N = appWidgetIds.length;
            changeDay=0;
            changeWeek=0;
            for (int i = 0; i < N; i++) {

                RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_course_day);
                //给两个按钮设置点击监听
                view.setOnClickPendingIntent(R.id.next_tv, getClickIntent(context, appWidgetIds[i], R.id.next_tv, 0, "org.pointstone.cugapp.Widget.Button.Update"));
                view.setOnClickPendingIntent(R.id.before_tv, getClickIntent(context, appWidgetIds[i], R.id.before_tv, 1, "org.pointstone.cugapp.Widget.Button.Update"));
                view.setOnClickPendingIntent(R.id.daytime_tv, getClickIntent(context, appWidgetIds[i], R.id.daytime_tv, 2, "org.pointstone.cugapp.Widget.Button.Update"));
                initData(view,context);
                //RemoteViews Service needed to provide adapter for ListView
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i],R.id.listViewWidget);
                appWidgetManager.updateAppWidget(appWidgetIds[i], view);
            }
        }else{
            Toast.makeText(context, "请打开APP查看是否登录成功", Toast.LENGTH_LONG).show();
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }



    /*
     * 删除一个AppWidget所调用的方法
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);


    }

    /*
     * 接收广播事件
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //这里用一个switch实现了
        final String choice = intent.getAction();


        switch (choice) {
            case "org.pointstone.cugapp.Widget.Button.Update":
                //因为遥操作view所以拿到remotevie用来操作
                RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_course_day);
                //bundle中会由系统存好下面两个属性,下面是提取出来
                Bundle bundle = intent.getExtras();
                int widgetId = -1;
                int viewId = -1;
                try {
                    widgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                    Log.d("widgetId", String.valueOf(widgetId));
                    viewId = bundle.getInt("Button");
                    Log.d("viewId", String.valueOf(viewId));
                } catch (NullPointerException e) {
                    return;
                }
                //根据获得到的viewid进行相应view的操作(这里我是用了一个flipper)
                switch (viewId) {
                    case R.id.next_tv:
                        if (currentweek != -1) {
                            if (changeWeek <= 3) {
                                changeDay += 1;
                                if (changeDay + current_xing == 8) {
                                    changeWeek += 1;
                                    if (changeWeek + currentweek != 0) {
                                        changeDay = -1 * current_xing + 1;
                                        changeDate(view,context);
                                    } else {
                                        changeWeek -= 1;
                                        Toast.makeText(context, "未来的事以后说吧", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    changeDate(view,context);
                                }
                            } else {
                                Toast.makeText(context, "请到全周课表查看更多", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "请到全周课表查看更多", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.before_tv:
                        if (currentweek != -1) {
                            if (-3 <= changeWeek) {
                                changeDay -= 1;
                                if (changeDay + current_xing == 0) {
                                    changeWeek -= 1;
                                    if (changeWeek + currentweek != 0) {
                                        changeDay = 7 - current_xing;
                                        changeDate(view,context);
                                    } else {
                                        changeWeek += 1;
                                        Toast.makeText(context, "太远了，回不去了", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    changeDate(view,context);
                                }

                            } else {
                                Toast.makeText(context, "请到全周课表查看更多", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "请到全周课表查看更多", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.daytime_tv:
                        initData(view,context);
                        break;
                    default:
                        return;
                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.notifyAppWidgetViewDataChanged(widgetId,R.id.listViewWidget);
                appWidgetManager.updateAppWidget(widgetId, view);
                break;
        }

    }

    /*
     * 创建第一个AppWiget实例所调用的方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /*
     * 删除最后一个Appwidget所调用的方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    //给按钮返回PendingIntent
    private PendingIntent getClickIntent(Context context, int widgetId, int viewId, int requestCode, String action) {
        //拿到管理对象
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //pendingintent中需要的intent，绑定这个类和当前context
        Intent i = new Intent(context, CourseDayWidget.class);
        //设置action
        i.setAction(action); //设置更新动作
        //设置bundle
        Bundle bundle = new Bundle();
        //将widgetId放进bundle
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        //放进需要设置的viewId
        bundle.putInt("Button", viewId);
        i.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void initData(RemoteViews view,Context context) {

        changeWeek = 0;
        changeDay = 0;
        Calendar cl = Calendar.getInstance();
        int xing = cl.get(Calendar.DAY_OF_WEEK);
        currentweek = InformationShared.getInt("currentweek" + GradeYear.getCurrentGrade(), -1);
        boolean isFirstSunday = (cl.getFirstDayOfWeek() == Calendar.SUNDAY);
        if (isFirstSunday) {
            xing -= 1;
            if (xing == 0) {
                current_xing = 7;
            } else {
                current_xing = xing;
            }
        } else {
            current_xing = xing;
        }

        if (currentweek != -1) {
            switch (current_xing) {
                case 1:
                    view.setTextViewText(R.id.daytime_tv, "第" + currentweek + "周-.-" + "星期一");
                    break;
                case 2:
                    view.setTextViewText(R.id.daytime_tv, "第" + currentweek + "周-.-" + "星期二");
                    break;
                case 3:
                    view.setTextViewText(R.id.daytime_tv, "第" + currentweek + "周-.-" + "星期三");
                    break;
                case 4:
                    view.setTextViewText(R.id.daytime_tv, "第" + currentweek + "周-.-" + "星期四");
                    break;
                case 5:
                    view.setTextViewText(R.id.daytime_tv, "第" + currentweek + "周-.-" + "星期五");
                    break;
                case 6:
                    view.setTextViewText(R.id.daytime_tv, "第" + currentweek + "周-.-" + "星期六");
                    break;
                case 7:
                    view.setTextViewText(R.id.daytime_tv, "第" + currentweek + "周-.-" + "星期日");
                    break;


            }
        } else {
            view.setTextViewText(R.id.daytime_tv, "放假啦~~~~~~~");
        }


        //passing app widget id to that RemoteViews Service

        data.clear();
        mgr2 = new DBCourseNewManager(context);
        mgr = new DBCourseManager(context);
        int course_num=0;
        if(currentweek!=-1)
        {
            List<Course> courses = mgr.query(GradeYear.getCurrentXN(GradeYear.getCurrentGrade()),GradeYear.getCurrentXQ(GradeYear.getCurrentGrade()));

            for(Course course : courses)
            {
                //if(Integer.parseInt(course.djj)==currentweek&&Integer.parseInt(course.xqj)==current_xing)
                // if(Integer.parseInt(course.qsz)<=currentweek&&currentweek<=Integer.parseInt(course.jsz))
                if(Integer.parseInt(course.qsz)<=(currentweek+changeWeek)&&(currentweek+changeWeek)<=Integer.parseInt(course.jsz)&&Integer.parseInt(course.xqj)==current_xing)
                {
                    String []c=course.kcb.split("<br>");
                    if(course.dsz.equals("单"))
                    {
                        if(currentweek%2!=0)
                        {

                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else if(course.dsz.equals("双"))
                    {
                        if(currentweek%2==0)
                        {
                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else
                    {
                        try{
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                            data.add(cd);
                        }catch (Exception e)
                        {
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                            data.add(cd);
                        }
                        course_num++;
                    }
                }
            }

            List<Course> courses2 = mgr2.query(GradeYear.getCurrentXN(GradeYear.getCurrentGrade()),GradeYear.getCurrentXQ(GradeYear.getCurrentGrade()));

            for(Course course : courses2)
            {
                //if(Integer.parseInt(course.djj)==currentweek&&Integer.parseInt(course.xqj)==current_xing)
                // if(Integer.parseInt(course.qsz)<=currentweek&&currentweek<=Integer.parseInt(course.jsz))
                if(Integer.parseInt(course.qsz)<=(currentweek+changeWeek)&&(currentweek+changeWeek)<=Integer.parseInt(course.jsz)&&Integer.parseInt(course.xqj)==current_xing)
                {
                    String []c=course.kcb.split("<br>");
                    if(course.dsz.equals("单"))
                    {
                        if(currentweek%2!=0)
                        {

                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else if(course.dsz.equals("双"))
                    {
                        if(currentweek%2==0)
                        {
                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else
                    {
                        try{
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                            data.add(cd);
                        }catch (Exception e)
                        {
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                            data.add(cd);
                        }
                        course_num++;
                    }
                }
            }
        }
        if(course_num==0)
        {
            CourseDay cd=new CourseDay("","","今天没有课哦","啦啦啦~~~");
            data.add(cd);
        }
        Collections.sort(data,new Comparator<CourseDay>(){
            public int compare(CourseDay arg0, CourseDay arg1) {
                if(arg0.getTime().length()==5)
                    return 10;
                else if(arg1.getTime().length()==5)
                    return -10;
                else
                    return arg0.getTime().compareTo(arg1.getTime());
            }
        });
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        Intent svcIntent = new Intent(context, ListWidgetService.class);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        //svcIntent.putExtra("a","a");
        view.setRemoteAdapter( R.id.listViewWidget,
                svcIntent);

        view.setEmptyView(R.id.listViewWidget, R.id.empty_view);
    }

    private void changeDate(RemoteViews view,Context context) {
        Calendar cl = Calendar.getInstance();
        int xing = cl.get(Calendar.DAY_OF_WEEK);
        currentweek = InformationShared.getInt("currentweek" + GradeYear.getCurrentGrade(), -1);
        boolean isFirstSunday = (cl.getFirstDayOfWeek() == Calendar.SUNDAY);
        if (isFirstSunday) {
            xing -= 1;
            if (xing == 0) {
                current_xing = 7;
            } else {
                current_xing = xing;
            }
        } else {
            current_xing = xing;
        }
        if (currentweek != -1) {
            switch (current_xing + changeDay) {

                case 1:
                    view.setTextViewText(R.id.daytime_tv, "第" + (currentweek + changeWeek) + "周-.-" + "星期一");
                    break;
                case 2:
                    view.setTextViewText(R.id.daytime_tv, "第" + (currentweek + changeWeek) + "周-.-" + "星期二");
                    break;
                case 3:
                    view.setTextViewText(R.id.daytime_tv, "第" + (currentweek + changeWeek) + "周-.-" + "星期三");
                    break;
                case 4:
                    view.setTextViewText(R.id.daytime_tv, "第" + (currentweek + changeWeek) + "周-.-" + "星期四");
                    break;
                case 5:
                    view.setTextViewText(R.id.daytime_tv, "第" + (currentweek + changeWeek) + "周-.-" + "星期五");
                    break;
                case 6:
                    view.setTextViewText(R.id.daytime_tv, "第" + (currentweek + changeWeek) + "周-.-" + "星期六");
                    break;
                case 7:
                    view.setTextViewText(R.id.daytime_tv, "第" + (currentweek + changeWeek) + "周-.-" + "星期日");
                    break;
            }
        } else {
            view.setTextViewText(R.id.daytime_tv, "放假啦~~~~~~~");
        }

        mgr2 = new DBCourseNewManager(context);
        mgr = new DBCourseManager(context);
        data.clear();
        int course_num=0;
        if(currentweek!=-1)
        {
            List<Course> courses = mgr.query(GradeYear.getCurrentXN(GradeYear.getCurrentGrade()),GradeYear.getCurrentXQ(GradeYear.getCurrentGrade()));

            for(Course course : courses)
            {
                //if(Integer.parseInt(course.djj)==currentweek&&Integer.parseInt(course.xqj)==current_xing)
                // if(Integer.parseInt(course.qsz)<=currentweek&&currentweek<=Integer.parseInt(course.jsz))
                int week=current_xing+changeDay;
                int z=currentweek+changeWeek; //周
                if(Integer.parseInt(course.qsz)<=(currentweek+changeWeek)&&(currentweek+changeWeek)<=Integer.parseInt(course.jsz)&&Integer.parseInt(course.xqj)==week)
                {
                    String []c=course.kcb.split("<br>");
                    if(course.dsz.equals("单"))
                    {
                        if(z%2!=0)
                        {

                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else if(course.dsz.equals("双"))
                    {
                        if(z%2==0)
                        {
                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else
                    {
                        try{
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                            data.add(cd);
                        }catch (Exception e)
                        {
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                            data.add(cd);
                        }
                        course_num++;
                    }
                }
            }

            List<Course> courses2= mgr2.query(GradeYear.getCurrentXN(GradeYear.getCurrentGrade()),GradeYear.getCurrentXQ(GradeYear.getCurrentGrade()));

            for(Course course : courses2)
            {
                //if(Integer.parseInt(course.djj)==currentweek&&Integer.parseInt(course.xqj)==current_xing)
                // if(Integer.parseInt(course.qsz)<=currentweek&&currentweek<=Integer.parseInt(course.jsz))
                int week=current_xing+changeDay;
                int z=currentweek+changeWeek; //周
                if(Integer.parseInt(course.qsz)<=(currentweek+changeWeek)&&(currentweek+changeWeek)<=Integer.parseInt(course.jsz)&&Integer.parseInt(course.xqj)==week)
                {
                    String []c=course.kcb.split("<br>");
                    if(course.dsz.equals("单"))
                    {
                        if(z%2!=0)
                        {

                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else if(course.dsz.equals("双"))
                    {
                        if(z%2==0)
                        {
                            try{
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                                data.add(cd);
                            }catch (Exception e)
                            {
                                CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                                data.add(cd);
                            }
                            course_num++;
                        }

                    }else
                    {
                        try{
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1),c[3],c[0],c[2]);
                            data.add(cd);
                        }catch (Exception e)
                        {
                            CourseDay cd=new CourseDay(course.djj+"-"+(Integer.parseInt(course.djj)+1)," ",c[0],c[2]);
                            data.add(cd);
                        }
                        course_num++;
                    }
                }
            }

        }
        if(course_num==0)
        {
            CourseDay cd=new CourseDay("","","今天没有课哦","啦啦啦~~~");
            data.add(cd);
        }

        Collections.sort(data,new Comparator<CourseDay>(){
            public int compare(CourseDay arg0, CourseDay arg1) {
                if(arg0.getTime().length()==5)
                    return 10;
                else if(arg1.getTime().length()==5)
                    return -10;
                else
                    return arg0.getTime().compareTo(arg1.getTime());
            }
        });
        Intent svcIntent = new Intent(context, ListWidgetService.class);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //svcIntent.putExtra("a","b");
        view.setRemoteAdapter( R.id.listViewWidget,
                svcIntent);

        view.setEmptyView(R.id.listViewWidget, R.id.empty_view);
    }



}
