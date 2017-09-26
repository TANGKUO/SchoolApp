package org.pointstone.cugapp.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.Course;
import org.pointstone.cugapp.utils.CourseArray;
import org.pointstone.cugapp.utils.DBCourseManager;
import org.pointstone.cugapp.utils.DBCourseNewManager;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.ToastUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/2/12.
 */

public class CourseWidget extends AppWidgetProvider {

    public static final String COLLECTION_VIEW_ACTION = "org.pointstone.cugapp.COLLECTION_VIEW_ACTION";
    public static final String COLLECTION_VIEW_EXTRA = "org.pointstone.cugapp.COLLECTION_VIEW_EXTRA";
    public static String[][] con = new String[6][7];
    private static CourseArray contents[][];
    private DBCourseManager mgr;
    private DBCourseNewManager mgr2;
    private static int changeWeek = 0;

    //给按钮返回PendingIntent
    private PendingIntent getClickIntent(Context context, int widgetId, int viewId, int requestCode, String action) {
        //拿到管理对象
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //pendingintent中需要的intent，绑定这个类和当前context
        Intent i = new Intent(context, CourseWidget.class);
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

    /*
  * 到达指定的时间，或者用户第一次创建Appwidget所调用的方法
  */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        int status= InformationShared.getInt("status");
        int login=InformationShared.getInt("login");
        if(status==1&&login==1){
            changeWeek=0;
            for (int appWidgetId : appWidgetIds) {
                // 获取AppWidget对应的视图
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_course);


                // 设置 “GridView(gridview)” 的adapter。
                // (01) intent: 对应启动 GridWidgetService(RemoteViewsService) 的intent
                // (02) setRemoteAdapter: 设置 gridview的适配器
                //    通过setRemoteAdapter将gridview和GridWidgetService关联起来，
                //    以达到通过 GridWidgetService 更新 gridview 的目的
                setDownUp(context, rv);
                getCourse(context, rv,0);
                rv.setOnClickPendingIntent(R.id.next_tv, getClickIntent(context, appWidgetId, R.id.next_tv, 0, "org.pointstone.cugapp.Widget.Button.Update"));
                rv.setOnClickPendingIntent(R.id.before_tv, getClickIntent(context, appWidgetId, R.id.before_tv, 1, "org.pointstone.cugapp.Widget.Button.Update"));
                rv.setOnClickPendingIntent(R.id.week_tv, getClickIntent(context, appWidgetId, R.id.week_tv, 2, "org.pointstone.cugapp.Widget.Button.Update"));
                rv.setOnClickPendingIntent(R.id.up_tv, getClickIntent(context, appWidgetId, R.id.up_tv, 3, "org.pointstone.cugapp.Widget.Button.Update"));
                rv.setOnClickPendingIntent(R.id.down_tv, getClickIntent(context, appWidgetId, R.id.down_tv, 4, "org.pointstone.cugapp.Widget.Button.Update"));

                // 设置响应 “GridView(gridview)” 的intent模板
                // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素，如GridView包含很多格子。
                //     它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
                //        (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
                //        (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
                Intent gridIntent = new Intent();
                gridIntent.setAction(COLLECTION_VIEW_ACTION);
                gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gridIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                // 设置intent模板
                rv.setPendingIntentTemplate(R.id.grid_course, pendingIntent);

                // 调用集合管理器对集合进行更新
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.grid_course);
                appWidgetManager.updateAppWidget(appWidgetId, rv);
            }
        }else{
            Toast.makeText(context, "请打开APP查看是否登录成功", Toast.LENGTH_LONG).show();
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    private void setDownUp(Context context, RemoteViews view) {
        int current_class = GradeYear.getCurrentClass();
        if (current_class < 6) {
            InformationShared.setInt("isDown", 0);
            view.setTextColor(R.id.up_tv, context.getResources().getColor(R.color.widget_down));
            view.setTextColor(R.id.down_tv, context.getResources().getColor(R.color.widget_up_black));
        } else {
            InformationShared.setInt("isDown", 1);
            view.setTextColor(R.id.down_tv, context.getResources().getColor(R.color.widget_down));
            view.setTextColor(R.id.up_tv, context.getResources().getColor(R.color.widget_up_black));
        }

    }

    private void setClassTime(Context context, RemoteViews rv) {


        rv.setTextColor(R.id.class1_tv, context.getResources().getColor(R.color.black));
        rv.setTextColor(R.id.class2_tv, context.getResources().getColor(R.color.black));
        rv.setTextColor(R.id.class3_tv, context.getResources().getColor(R.color.black));
        rv.setTextColor(R.id.class4_tv, context.getResources().getColor(R.color.black));
        rv.setTextColor(R.id.class5_tv, context.getResources().getColor(R.color.black));
        rv.setTextColor(R.id.class6_tv, context.getResources().getColor(R.color.black));

        if (InformationShared.getInt("isDown") == 0) {
            rv.setTextViewText(R.id.class1_tv, "08:00\n" + "1");
            rv.setTextViewText(R.id.class2_tv, "2\n" + "09:35");

            rv.setTextViewText(R.id.class3_tv, "10:05\n" + "3");
            rv.setTextViewText(R.id.class4_tv, "4\n" + "11:40");
            if (InformationShared.getInt("SummerWinter") == 0) {
                rv.setTextViewText(R.id.class5_tv, "14:00\n" + "5");
                rv.setTextViewText(R.id.class6_tv, "6\n" + "15:35");

            } else {
                rv.setTextViewText(R.id.class5_tv, "14:30\n" + "5");
                rv.setTextViewText(R.id.class6_tv, "6\n" + "16:05");

            }
            int currentclass = GradeYear.getCurrentClass();
            if (currentclass != 0) {
                switch (currentclass) {
                    case 1:
                        rv.setTextColor(R.id.class1_tv, context.getResources().getColor(R.color.currentdate));
                        rv.setTextColor(R.id.class2_tv, context.getResources().getColor(R.color.currentdate));
                        break;
                    case 3:
                        rv.setTextColor(R.id.class3_tv, context.getResources().getColor(R.color.currentdate));
                        rv.setTextColor(R.id.class4_tv, context.getResources().getColor(R.color.currentdate));
                        break;
                    case 5:
                        rv.setTextColor(R.id.class5_tv, context.getResources().getColor(R.color.currentdate));
                        rv.setTextColor(R.id.class6_tv, context.getResources().getColor(R.color.currentdate));
                        break;
                    default:
                        break;
                }

            }
        } else {
            if (InformationShared.getInt("SummerWinter") == 0) {
                rv.setTextViewText(R.id.class1_tv, "16:00\n" + "7");
                rv.setTextViewText(R.id.class2_tv, "8\n" + "17:35");
                rv.setTextViewText(R.id.class3_tv, "19:00\n" + "9");
                rv.setTextViewText(R.id.class4_tv, "10\n" + "20:35");
                rv.setTextViewText(R.id.class5_tv, "21:05\n" + "11");
                rv.setTextViewText(R.id.class6_tv, "12\n" + "22:40");

            } else {
                rv.setTextViewText(R.id.class1_tv, "16:25\n" + "7");
                rv.setTextViewText(R.id.class2_tv, "8\n" + "18:00");
                rv.setTextViewText(R.id.class3_tv, "19:30\n" + "9");
                rv.setTextViewText(R.id.class4_tv, "10\n" + "21:05");
                rv.setTextViewText(R.id.class5_tv, "21:35\n" + "11");
                rv.setTextViewText(R.id.class6_tv, "12\n" + "23:10");

            }
            int currentclass = GradeYear.getCurrentClass();
            if (currentclass != 0) {
                switch (currentclass) {

                    case 7:
                        rv.setTextColor(R.id.class1_tv, context.getResources().getColor(R.color.currentdate));
                        rv.setTextColor(R.id.class2_tv, context.getResources().getColor(R.color.currentdate));
                        break;
                    case 9:
                        rv.setTextColor(R.id.class3_tv, context.getResources().getColor(R.color.currentdate));
                        rv.setTextColor(R.id.class4_tv, context.getResources().getColor(R.color.currentdate));
                        break;
                    case 11:
                        rv.setTextColor(R.id.class5_tv, context.getResources().getColor(R.color.currentdate));
                        rv.setTextColor(R.id.class6_tv, context.getResources().getColor(R.color.currentdate));
                        break;
                    default:
                        break;
                }

            }
        }


    }

    public void setWeekTime(Context context, RemoteViews rv, int week, int grade) {
        String MonToSun[] = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};



        int date[] = GradeYear.getMonMonthDay(grade, week);
        int month = date[0];
        if (month >= 5 && month <= 9) {
            InformationShared.setInt("SummerWinter", 1);
        } else {
            InformationShared.setInt("SummerWinter", 0);
        }


        rv.setTextViewText(R.id.month_tv, date[0] + "月 ");
        rv.setTextColor(R.id.month_tv, context.getResources().getColor(R.color.black));
        int day = 0;
        rv.setTextColor(R.id.mon_tv, context.getResources().getColor(R.color.black));
        rv.setTextViewText(R.id.mon_tv, date[day + 1] + "\n" + MonToSun[day]);
        day++;
        rv.setTextColor(R.id.tue_tv, context.getResources().getColor(R.color.black));
        rv.setTextViewText(R.id.tue_tv, date[day + 1] + "\n" + MonToSun[day]);
        day++;
        rv.setTextColor(R.id.wed_tv, context.getResources().getColor(R.color.black));
        rv.setTextViewText(R.id.wed_tv, date[day + 1] + "\n" + MonToSun[day]);
        day++;
        rv.setTextColor(R.id.ths_tv, context.getResources().getColor(R.color.black));
        rv.setTextViewText(R.id.ths_tv, date[day + 1] + "\n" + MonToSun[day]);
        day++;
        rv.setTextColor(R.id.fri_tv, context.getResources().getColor(R.color.black));
        rv.setTextViewText(R.id.fri_tv, date[day + 1] + "\n" + MonToSun[day]);
        day++;
        rv.setTextColor(R.id.sat_tv, context.getResources().getColor(R.color.black));
        rv.setTextViewText(R.id.sat_tv, date[day + 1] + "\n" + MonToSun[day]);
        day++;
        rv.setTextColor(R.id.sun_tv, context.getResources().getColor(R.color.black));
        rv.setTextViewText(R.id.sun_tv, date[day + 1] + "\n" + MonToSun[day]);

        Calendar cl = Calendar.getInstance();
        boolean isFirstSunday = (cl.getFirstDayOfWeek() == Calendar.SUNDAY);
        int xing = cl.get(Calendar.DAY_OF_WEEK);

        if (isFirstSunday) {
            xing -= 1;
            if (xing == 0) {
                xing = 7;
            }
        }


        int current_week = InformationShared.getInt("currentweek" + GradeYear.getCurrentGrade());
        if(current_week==week){
            switch (xing) {
                case 1:
                    rv.setTextColor(R.id.mon_tv, context.getResources().getColor(R.color.currentdate));
                    break;
                case 2:
                    rv.setTextColor(R.id.tue_tv, context.getResources().getColor(R.color.currentdate));
                    break;
                case 3:
                    rv.setTextColor(R.id.wed_tv, context.getResources().getColor(R.color.currentdate));
                    break;
                case 4:
                    rv.setTextColor(R.id.ths_tv, context.getResources().getColor(R.color.currentdate));
                    break;
                case 5:
                    rv.setTextColor(R.id.fri_tv, context.getResources().getColor(R.color.currentdate));
                    break;
                case 6:
                    rv.setTextColor(R.id.sat_tv, context.getResources().getColor(R.color.currentdate));
                    break;
                case 7:
                    rv.setTextColor(R.id.sun_tv, context.getResources().getColor(R.color.currentdate));
                    break;
                default:
                    break;

            }
        }

       /* for (int i = 0; i < 7; i++) {

            WeeksTv[i].setTextColor(getResources().getColor(R.color.black));
            if (current_day == date[i + 1] && current_month == date[0] && InformationShared.getInt("currentweek" + GradeYear.getCurrentGrade()) == WeekSpinner.getSelectedItemPosition() - 1)
                WeeksTv[i].setTextColor(getResources().getColor(R.color.currentdate));
            WeeksTv[i].setText(date[i + 1] + "\n" + MonToSun[i]);
        }*/

    }

    public void getCourse(Context context, RemoteViews rv,int cw) {
        int grade = GradeYear.getCurrentGrade();

        String xn = GradeYear.getCurrentXN(grade);
        String xq = GradeYear.getCurrentXQ(grade);

        int week = InformationShared.getInt("currentweek" + grade)+cw;


        setWeekTime(context, rv, week, grade);
        setClassTime(context, rv);


        if(week>0){
            rv.setTextViewText(R.id.week_tv, "第 " + week + " 周");
        }else{
            rv.setTextViewText(R.id.week_tv, "放假了");
        }


        mgr2 = new DBCourseNewManager(context);
        mgr = new DBCourseManager(context);
        contents = new CourseArray[6][];
        for (int i = 0; i < 6; i++) {
            contents[i] = new CourseArray[7];
            for (int j = 0; j < 7; j++) {
                contents[i][j] = new CourseArray();
            }
        }

        List<Course> courses = mgr.query(xn, xq);
        int course_num = -1;


        for (Course course : courses) {
            int qsz = Integer.parseInt(course.qsz);
            int jsz = Integer.parseInt(course.jsz);
            if (qsz <= week && jsz >= week) {
                int find = 0;
                int xqj = Integer.parseInt(course.xqj);
                int djj = Integer.parseInt(course.djj);
                String[] c = course.kcb.split("<br>");
                String course2 = " ";
                try {
                    course2 = c[3] + "\n" + c[0] + "\n\n\n\n\n\n" + c[2] + "\n" + c[1];
                } catch (Exception e) {
                    course2 = "   " + "\n" + c[0] + "\n\n\n\n\n\n" + c[2] + "\n" + c[1];
                }
                for (int i = 0; i < 6; i++) {
                    if (find == 1) {
                        break;
                    }
                    for (int j = 0; j < 7; j++) {
                        if (find == 1) {
                            break;
                        }
                        int size = contents[i][j].getSize();
                        for (int m = 0; m < size; m++) {
                            String aa[] = contents[i][j].get(m).split(":");
                            String bb[] = aa[1].split("\n");
                            if (c[0].equals(bb[1])) {
                                course2 = aa[0] + ":" + course2;
                                find = 1;
                                break;
                            }
                        }
                    }
                }
                if (find != 1) {
                    course_num++;
                    course2 = course_num + ":" + course2;
                }
                if (course.dsz.equals("单")) {
                    if (week % 2 != 0) {

                        if (course.skcd.equals("2") || course.skcd.equals("1")) {
                            contents[djj / 2][xqj - 1].add(course2);
                        } else {
                            contents[djj / 2][xqj - 1].add(course2);
                            contents[djj / 2 + 1][xqj - 1].add(course2);
                        }

                    }

                } else if (course.dsz.equals("双")) {
                    if (week % 2 == 0) {
                        if (course.skcd.equals("2") || course.skcd.equals("1")) {
                            contents[djj / 2][xqj - 1].add(course2);
                        } else {
                            contents[djj / 2][xqj - 1].add(course2);
                            contents[djj / 2 + 1][xqj - 1].add(course2);
                        }

                    }

                } else {
                    if (course.skcd.equals("2") || course.skcd.equals("1")) {
                        contents[djj / 2][xqj - 1].add(course2);
                    } else {
                        contents[djj / 2][xqj - 1].add(course2);
                        contents[djj / 2 + 1][xqj - 1].add(course2);
                    }
                }

            }
        }


        courses = mgr2.query(xn, xq);
        for (Course course : courses) {
            int qsz = Integer.parseInt(course.qsz);
            int jsz = Integer.parseInt(course.jsz);
            if (qsz <= week && jsz >= week) {
                int find = 0;
                int xqj = Integer.parseInt(course.xqj);
                int djj = Integer.parseInt(course.djj);
                String[] c = course.kcb.split("<br>");
                String course2 = " ";
                try {
                    course2 = c[3] + "\n" + c[0] + "\n\n\n\n\n\n" + c[2] + "\n" + c[1];
                } catch (Exception e) {
                    course2 = "   " + "\n" + c[0] + "\n\n\n\n\n\n" + c[2] + "\n" + c[1];
                }
                for (int i = 0; i < 6; i++) {
                    if (find == 1) {
                        break;
                    }
                    for (int j = 0; j < 7; j++) {
                        if (find == 1) {
                            break;
                        }
                        int size = contents[i][j].getSize();
                        for (int m = 0; m < size; m++) {
                            String aa[] = contents[i][j].get(m).split(":");
                            String bb[] = aa[1].split("\n");
                            if (c[0].equals(bb[1])) {
                                course2 = aa[0] + ":" + course2;
                                find = 1;
                                break;
                            }
                        }
                    }
                }
                if (find != 1) {
                    course_num++;
                    course2 = course_num + ":" + course2;
                }
                if (course.dsz.equals("单")) {
                    if (week % 2 != 0) {

                        if (course.skcd.equals("2") || course.skcd.equals("1")) {
                            contents[djj / 2][xqj - 1].add(course2);
                        } else {
                            contents[djj / 2][xqj - 1].add(course2);
                            contents[djj / 2 + 1][xqj - 1].add(course2);
                        }

                    }

                } else if (course.dsz.equals("双")) {
                    if (week % 2 == 0) {
                        if (course.skcd.equals("2") || course.skcd.equals("1")) {
                            contents[djj / 2][xqj - 1].add(course2);
                        } else {
                            contents[djj / 2][xqj - 1].add(course2);
                            contents[djj / 2 + 1][xqj - 1].add(course2);
                        }

                    }

                } else {
                    if (course.skcd.equals("2") || course.skcd.equals("1")) {
                        contents[djj / 2][xqj - 1].add(course2);
                    } else {
                        contents[djj / 2][xqj - 1].add(course2);
                        contents[djj / 2 + 1][xqj - 1].add(course2);
                    }
                }

            }
        }
        con = new String[6][7];
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++)
                con[i][j] = week + "";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                int size = contents[i][j].getSize();
                for (int m = 0; m < size; m++) {
                    con[i][j] = con[i][j] + contents[i][j].get(m) + "<br>";
                }
            }
        }

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++) {
                String day = "";
                switch (j) {
                    case 0:
                        day = "周一";
                        break;
                    case 1:
                        day = "周二";
                        break;
                    case 2:
                        day = "周三";
                        break;
                    case 3:
                        day = "周四";
                        break;
                    case 4:
                        day = "周五";
                        break;
                    case 5:
                        day = "周六";
                        break;
                    case 6:
                        day = "周日";
                        break;
                }
                if (con[i][j].equals("")) {

                    con[i][j] = "\n\n\n\n\n\n\n\n\n\n" + day + "第" + (i * 2 + 1) + "," + (i * 2 + 2) + "节" + grade;
                } else {
                    con[i][j] = con[i][j] + "\n\n\n\n\n" + day + "第" + (i * 2 + 1) + "," + (i * 2 + 2) + "节" + "\n\n\n\n\n\n点击添加课程" + grade;
                }
            }
        Intent serviceIntent = new Intent(context, GridWidgetService.class);
        // serviceIntent.putExtra("a",con[0][0]);
        rv.setRemoteAdapter(R.id.grid_course, serviceIntent);
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
        String action = intent.getAction();

        if (action.equals(COLLECTION_VIEW_ACTION)) {
            // 接受“gridview”的点击事件的广播

            int viewIndex = intent.getIntExtra(COLLECTION_VIEW_EXTRA, 0);
            //求余得到二维索引
            if(InformationShared.getInt("isDown")!=0)
            {
                viewIndex=viewIndex+21;
            }
            int column = viewIndex % 7;
            //求商得到二维索引
            int row = viewIndex / 7;

            String data = con[row][column];
            if(data!=null){
                if (data.charAt(5) != '\n') {

                    String spl[] = data.split(":");
                    if (spl.length == 2) {
                        data = data.replace("\n\n", "");
                        String[] c = data.split("\n");
                        String Place = c[0];
                        String[] p = Place.split(":");
                        String CourseName = c[1];
                        String TeacherName = c[2];

                        ToastUtil.showToast(context,p[1] + "\n" + CourseName + "\n" + TeacherName.substring(0, TeacherName.length() - 4),Toast.LENGTH_LONG);
                    } else {
                        ToastUtil.showToast(context,"有重课，请打开APP查看详情",Toast.LENGTH_LONG);


                    }
                }
            }



        } else if (action.equals("org.pointstone.cugapp.Widget.Button.Update")) {
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_course);
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
            //根据获得到的viewid进行相应view的操作(这里我是用了一个flipper)InformationShared.setInt("isDown",1);
            int grade = GradeYear.getCurrentGrade();

            int week = InformationShared.getInt("currentweek" + grade);
            switch (viewId) {
                case R.id.next_tv:
                    changeWeek+=1;
                    if(week+changeWeek<21)
                    {
                        getCourse(context, view,changeWeek);
                    }else{
                        changeWeek-=1;
                        Toast.makeText(context, "大概是没了吧", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.before_tv:
                    changeWeek-=1;
                    if(week+changeWeek>0)
                    {
                        getCourse(context, view,changeWeek);
                    }else{
                        changeWeek+=1;
                        Toast.makeText(context, "大概是没了吧", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.week_tv:
                    changeWeek=0;
                    getCourse(context, view,changeWeek);
                    break;
                case R.id.down_tv:
                    InformationShared.setInt("isDown", 1);
                    getCourse(context, view,changeWeek);
                    view.setTextColor(R.id.down_tv, context.getResources().getColor(R.color.widget_down));
                    view.setTextColor(R.id.up_tv, context.getResources().getColor(R.color.widget_up_black));
                    break;
                case R.id.up_tv:
                    InformationShared.setInt("isDown", 0);
                    view.setTextColor(R.id.up_tv, context.getResources().getColor(R.color.widget_down));
                    view.setTextColor(R.id.down_tv, context.getResources().getColor(R.color.widget_up_black));
                    getCourse(context, view,changeWeek);
                    break;
                default:
                    return;
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.grid_course);
            appWidgetManager.updateAppWidget(widgetId, view);
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

}
