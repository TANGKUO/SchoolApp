package org.pointstone.cugapp.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.adapter.CourseDayAdapter;
import org.pointstone.cugapp.bean.CourseDay;
import org.pointstone.cugapp.utils.Course;
import org.pointstone.cugapp.utils.DBCourseManager;
import org.pointstone.cugapp.utils.DBCourseNewManager;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;
import org.pointstone.cugapp.view.CourseDayMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public class CourseDayFragment extends Fragment {
    private ImageView headCourseIv;
    private ImageButton MoreBtn;
    private List<CourseDay> data = new ArrayList<CourseDay>();
    private TextView WeekTimeTv;
    private TextView DayTimeTv;
    private int currentweek;
    private  int current_xing;
    private DBCourseManager mgr;
    private DBCourseNewManager mgr2;
    private TextView BeforeTv;
    private TextView NextTv;
    private int changeDay=0;
    private int changeWeek=0;
    private CourseDayAdapter mAdapter;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_day, container, false);

        mListView = (ListView) view.findViewById(R.id.course_day_lv);
        mAdapter = new CourseDayAdapter(data, DrawerActivity.getInstannce());
        mListView.setAdapter(mAdapter);
        headCourseIv= (ImageView) view.findViewById(R.id.head_iv);
        MoreBtn= (ImageButton) view.findViewById(R.id.more_btn);
        WeekTimeTv= (TextView) view.findViewById(R.id.weektime_tv);
        DayTimeTv= (TextView) view.findViewById(R.id.daytime_tv);
        BeforeTv= (TextView) view.findViewById(R.id.before_tv);
        NextTv= (TextView) view.findViewById(R.id.next_tv);
        mgr2 = new DBCourseNewManager(getActivity());
        mgr = new DBCourseManager(getActivity());
        currentweek= InformationShared.getInt("currentweek"+ GradeYear.getCurrentGrade(),-1);
        if(currentweek!=-1)
        {
            WeekTimeTv.setText("第"+currentweek+"周"+"（今天）");
        }else{
            WeekTimeTv.setText("放假中");
        }
        initData();
        DayTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
                changeDay=0;
                changeWeek=0;
                currentweek= InformationShared.getInt("currentweek"+ GradeYear.getCurrentGrade(),-1);
                if(currentweek!=-1)
                {
                    WeekTimeTv.setText("第"+currentweek+"周"+"（今天）");
                }else{
                    WeekTimeTv.setText("放假中");
                }
            }
        });

        BeforeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentweek!=-1)
                {
                    if(-3<=changeWeek)
                    {
                        changeDay-=1;
                        if(changeDay+current_xing==0)
                        {
                            changeWeek-=1;
                            if(changeWeek+currentweek!=0)
                            {
                                changeDay=7-current_xing;
                                changeDate();
                            }else
                            {
                                changeWeek+=1;
                                OwnToast.Long("太远了，回不去了");
                            }
                        }else
                        {
                            changeDate();
                        }

                    }else{
                        OwnToast.Long("更多课程请到周视图查看哦");
                    }
                }else{
                    OwnToast.Short("请到全周课表查看下学期课表");
                }



            }
        });

        NextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentweek!=-1)
                {
                    if(changeWeek<=3)
                    {
                        changeDay+=1;
                        if(changeDay+current_xing==8)
                        {
                            changeWeek+=1;
                            if(changeWeek+currentweek!=0)
                            {
                                changeDay=-1*current_xing+1;
                                changeDate();
                            }else
                            {
                                changeWeek-=1;
                                OwnToast.Long("未来的事以后说吧");
                            }
                        }else
                        {
                            changeDate();
                        }
                    }else{
                        OwnToast.Long("更多课程请到周视图查看哦");
                    }
                }else{
                    OwnToast.Short("请到全周课表查看下学期课表");
                }



            }
        });

        String tp = InformationShared.getString("headimage");
        if (!tp.equals("0")) {
            byte[] bytes = Base64.decode(tp, Base64.DEFAULT);
            Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            headCourseIv.setImageBitmap(dBitmap);
        }
        headCourseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerActivity.getInstannce().openDrawer();
            }
        });

        MoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseDayMenu popup = new  CourseDayMenu(getActivity());
                popup.showPopupWindow(view);
            }
        });



        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {

                        final int SWIPE_MIN_DISTANCE = 60;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 100;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                                if(changeWeek<=3)
                                {
                                    changeDay+=1;
                                    if(changeDay+current_xing==8)
                                    {
                                        changeWeek+=1;
                                        if(changeWeek+currentweek!=0)
                                        {
                                            changeDay=-1*current_xing+1;
                                            changeDate();
                                        }else
                                        {
                                            changeWeek-=1;
                                            OwnToast.Long("未来的事以后说吧");
                                        }
                                    }else
                                    {
                                        changeDate();
                                    }
                                }else{
                                    OwnToast.Long("更多课程请到周视图查看哦");
                                }

                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                                if(changeWeek>=-3)
                                {
                                    changeDay-=1;
                                    if(changeDay+current_xing==0)
                                    {
                                        changeWeek-=1;
                                        if(changeWeek+currentweek!=0)
                                        {
                                            changeDay=7-current_xing;
                                            changeDate();
                                        }else
                                        {
                                            changeWeek+=1;
                                            OwnToast.Long("太远了，回不去了");
                                        }
                                    }else
                                    {
                                        changeDate();
                                    }

                                }else{
                                    OwnToast.Long("更多课程请到周视图查看哦");
                                }
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return view;
    }

    private void initData() {
        Calendar cl = Calendar.getInstance();
        int current_month = cl.get(Calendar.MONTH) + 1;
        int current_day = cl.get(Calendar.DAY_OF_MONTH);
        int xing=cl.get(Calendar.DAY_OF_WEEK);
        boolean isFirstSunday = (cl.getFirstDayOfWeek() == Calendar.SUNDAY);
        if(isFirstSunday)
        {
            xing-=1;
            if(xing==0)
            {
                current_xing=7;
            }else
            {
                current_xing=xing;
            }
        }else
        {
            current_xing=xing;
        }

        switch (current_xing)
        {
            case 1:
                DayTimeTv.setText(current_month+"月"+current_day+"日"+"星期一");
                break;
            case 2:
                DayTimeTv.setText(current_month+"月"+current_day+"日"+"星期二");
                break;
            case 3:
                DayTimeTv.setText(current_month+"月"+current_day+"日"+"星期三");
                break;
            case 4:
                DayTimeTv.setText(current_month+"月"+current_day+"日"+"星期四");
                break;
            case 5:
                DayTimeTv.setText(current_month+"月"+current_day+"日"+"星期五");
                break;
            case 6:
                DayTimeTv.setText(current_month+"月"+current_day+"日"+"星期六");
                break;
            case 7:
                DayTimeTv.setText(current_month+"月"+current_day+"日"+"星期日");
                break;

        }

        data.clear();
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
        mListView.setAdapter(mAdapter);
    }

    private void changeDate() {

        if(changeDay==0&&changeWeek==0)
        {
            currentweek= InformationShared.getInt("currentweek"+ GradeYear.getCurrentGrade(),-1);
            if(currentweek!=-1)
            {
                WeekTimeTv.setText("第"+currentweek+"周"+"（今天）");
            }else{
                WeekTimeTv.setText("放假中");
            }
        }else{
            currentweek= InformationShared.getInt("currentweek"+ GradeYear.getCurrentGrade(),-1);
            if(currentweek!=-1)
            {
                WeekTimeTv.setText("第"+(currentweek+changeWeek)+"周"+"（非今天）");
            }else{
                WeekTimeTv.setText("放假中");
            }
        }
        DayTimeTv.setText(GradeYear.getDate(changeDay,changeWeek));
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
        mListView.setAdapter(mAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mgr.closeDB();
        mgr2.closeDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("CourseDay"); //统计页面，"MainScreen"为页面名称，可自定义
         TCAgent.onPageStart(getActivity(), "CourseDay");
    }

    @Override
    public void onPause() {
        super.onPause();
     //   MobclickAgent.onPageStart("CourseDay"); //统计页面，"MainScreen"为页面名称，可自定义
         TCAgent.onPageEnd(getActivity(), "CourseDay");
    }
}
