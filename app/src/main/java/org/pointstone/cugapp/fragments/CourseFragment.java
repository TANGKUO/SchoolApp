package org.pointstone.cugapp.fragments;


//import android.app.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.baoyz.widget.PullRefreshLayout;
import com.tendcloud.tenddata.TCAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.adapter.CourseGridAdapter;
import org.pointstone.cugapp.utils.Course;
import org.pointstone.cugapp.utils.CourseArray;
import org.pointstone.cugapp.utils.DBCourseManager;
import org.pointstone.cugapp.utils.DBCourseNewManager;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.view.CourseMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static org.pointstone.cugapp.R.id.course_background;
import static org.pointstone.cugapp.utils.GradeYear.getCurrentXN;


//import com.umeng.analytics.MobclickAgent;



/**
 * Created by Administrator on 2016/12/5.
 */

public class CourseFragment extends Fragment {
    View courseView;
    private Spinner WeekSpinner;
    private Spinner TermSpinner;
    private GridView detailCource;
    private CourseArray contents[][] ;
    private DBCourseManager mgr;
    private DBCourseNewManager mgr2;
    private ProgressDialog pd;

    private CourseGridAdapter contentAdapter;
    private List<String> spinnerWeekDataList;
    private ArrayAdapter<String> spinnerWeekAdapter;
    private List<String> spinnerTermDataList;
    private ArrayAdapter<String> spinnerTermAdapter;

    private TextView MonthTv;//月日星期
    private TextView WeeksTv[]=new TextView[7];
    private TextView ClassesTv[]=new TextView[12];

    private AlertDialog.Builder builder;


    private PullRefreshLayout layout;

    private ImageView headCourseIv;
    private ImageButton MoreBtn;

    private  ScrollView courseSV;
    private LinearLayout CourseBackground;
    private static CourseFragment courseFragment; //Activity实例.

    private LinearLayout ScreenTimeLy;
    private LinearLayout ScreenWeekLy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        courseView=inflater.inflate(R.layout.fragment_course, container, false);
        if(InformationShared.getInt("timeTable")!=0&&InformationShared.getInt("status")==1)
        {
            InformationShared.setInt("timeTable",1);
        }
        mgr = new DBCourseManager(getActivity());
        mgr2 = new DBCourseNewManager(getActivity());
        initView();
        setTime();

        courseFragment=this;
        courseSV= (ScrollView) courseView.findViewById(R.id.course_sv);
        layout = (PullRefreshLayout) courseView.findViewById(R.id.swipeRefreshLayout);

        headCourseIv= (ImageView) courseView.findViewById(R.id.head_iv);
        MoreBtn= (ImageButton) courseView.findViewById(R.id.more_btn);

        ScreenTimeLy= (LinearLayout) courseView.findViewById(R.id.screen_time_ly);
        ScreenWeekLy= (LinearLayout) courseView.findViewById(R.id.screen_week_ly);
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

        CourseBackground= (LinearLayout) courseView.findViewById(course_background);
        String bg=InformationShared.getString("course_background");
        if(!bg.equals("0"))
        {
            byte[] bytes = Base64.decode(bg, Base64.DEFAULT);
            Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            BitmapDrawable bd=new BitmapDrawable(getResources(),dBitmap);
            CourseBackground.setBackground(bd);
        }

// listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNetStartWeek();
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

                                if(WeekSpinner.getSelectedItemPosition()<=20)
                                {
                                    setTime();
                                    WeekSpinner.setSelection(WeekSpinner.getSelectedItemPosition()+1);
                                   // setCurrentCourseContent(WeekSpinner.getSelectedItemPosition(), getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));//课程表内容初始化
                                }

                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                if(WeekSpinner.getSelectedItemPosition()>=3)
                                {
                                    setTime();
                                    WeekSpinner.setSelection(WeekSpinner.getSelectedItemPosition()-1);
                                   // setCurrentCourseContent(WeekSpinner.getSelectedItemPosition()-1, getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));//课程表内容初始化
                                }

                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        courseView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        GridView courseGV= (GridView) courseView.findViewById(R.id.courceDetail);
        courseGV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });


        MoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CourseMenu popup = new  CourseMenu(getActivity());
                popup.showPopupWindow(view);
            }
        });

        if(AndPermission.hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 有权限，直接do anything.
        } else {
            // 申请权限。
            AndPermission.with(getActivity())
                    .requestCode(100)
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE )
                    .send();
        }
        return courseView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if(requestCode == 100) {
                // TODO 相应代码。
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。

            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
                // 第一种：用默认的提示语。
                //AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

                // 第二种：用自定义的提示语。
                 AndPermission.defaultSettingDialog(getActivity(), 100)
                 .setTitle("权限申请失败")
                 .setMessage("如果不开启存储权限课表背景将无法设置")
                 .setPositiveButton("好，去设置")
                 .show();

                // 第三种：自定义dialog样式。
                // SettingService settingService =
                //    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
                // 你的dialog点击了确定调用：
                // settingService.execute();
                // 你的dialog点击了取消调用：
                // settingService.cancel();
            }
        }
    };
    //获取时间
    private void setTime(){

        String MonToSun[]={"周一","周二","周三","周四","周五","周六","周日"};
        Calendar c = Calendar.getInstance();
        int current_month = c.get(Calendar.MONTH) + 1;
        int current_day = c.get(Calendar.DAY_OF_MONTH);
        int month;
        if(WeekSpinner.getSelectedItemPosition()!=0&&InformationShared.getInt("currentweek"+TermSpinner.getSelectedItemPosition())!=0)
        {
            int date[]= GradeYear.getMonMonthDay(TermSpinner.getSelectedItemPosition(),WeekSpinner.getSelectedItemPosition()-1);

            month=date[0];
            if(month>=5&& month<=9)
            {
                InformationShared.setInt("SummerWinter",1);
            }else
            {
                InformationShared.setInt("SummerWinter",0);
            }


            MonthTv.setText(date[0]+"月 ");
            int day;
            for(int i=0;i<7;i++)
            {
                MonthTv.setTextColor(getResources().getColor(R.color.black));
                WeeksTv[i].setTextColor(getResources().getColor(R.color.black));
                if(current_day==date[i+1]&&InformationShared.getInt("currentweek"+GradeYear.getCurrentGrade())==WeekSpinner.getSelectedItemPosition()-1)
                    WeeksTv[i].setTextColor(getResources().getColor(R.color.currentdate));
                WeeksTv[i].setText(date[i+1]+"\n"+MonToSun[i]);
            }
        }else{
            for (int i=0;i<7;i++)
            {
                MonthTv.setTextColor(getResources().getColor(R.color.black));
                WeeksTv[i].setTextColor(getResources().getColor(R.color.black));
                MonthTv.setText("");
                WeeksTv[i].setText( MonToSun[i]);
            }
        }


        setClassTime();




    }

    private void setClassTime() {
        ClassesTv[0].setText("08:00\n"+"1");
        ClassesTv[1].setText("2\n"+"09:35");

        ClassesTv[2].setText("10:05\n"+"3");
        ClassesTv[3].setText("4\n"+"11:40");


        if (InformationShared.getInt("SummerWinter")==0)
        {
            ClassesTv[4].setText("14:00\n"+"5");
            ClassesTv[5].setText("6\n"+"15:35");

            ClassesTv[6].setText("16:00\n"+"7");
            ClassesTv[7].setText("8\n"+"17:35");

            ClassesTv[8].setText("19:00\n"+"9");
            ClassesTv[9].setText("10\n"+"20:35");

            ClassesTv[10].setText("21:05\n"+"11");
            ClassesTv[11].setText("12\n"+"22:40");
        }else{
            ClassesTv[4].setText("14:30\n"+"5");
            ClassesTv[5].setText("6\n"+"16:05");

            ClassesTv[6].setText("16:25\n"+"7");
            ClassesTv[7].setText("8\n"+"18:00");

            ClassesTv[8].setText("19:30\n"+"9");
            ClassesTv[9].setText("10\n"+"21:05");

            ClassesTv[10].setText("21:35\n"+"11");
            ClassesTv[11].setText("12\n"+"23:10");
        }

        for(int i=0;i<12;i++)
        {
            ClassesTv[i].setTextColor(getResources().getColor(R.color.black));
        }
        int currentclass=GradeYear.getCurrentClass();
        if(currentclass!=0)
        {
            ClassesTv[currentclass-1].setTextColor(getResources().getColor(R.color.currentdate));
            ClassesTv[currentclass].setTextColor(getResources().getColor(R.color.currentdate));
        }



    }

    //初始化函数
    private void initView(){

      //
        Calendar c = Calendar.getInstance();
        int current_year=c.get(Calendar.YEAR);
       int mon=c.get(Calendar.MONTH)+1;
        DataList();      //spinner 下拉框内容初始化
        WeekSpinner = (Spinner)courseView.findViewById(R.id.switchWeek);
        TermSpinner= (Spinner) courseView.findViewById(R.id.termSpi);
        detailCource = (GridView)courseView.findViewById(R.id.courceDetail);
        MonthTv= (TextView) courseView.findViewById(R.id.month_tv);

        WeeksTv[0]= (TextView) courseView.findViewById(R.id.mon_tv);
        WeeksTv[1]= (TextView) courseView.findViewById(R.id.tue_tv);
        WeeksTv[2]= (TextView) courseView.findViewById(R.id.wed_tv);
        WeeksTv[3]= (TextView) courseView.findViewById(R.id.ths_tv);
        WeeksTv[4]= (TextView) courseView.findViewById(R.id.fri_tv);
        WeeksTv[5]= (TextView) courseView.findViewById(R.id.sat_tv);
        WeeksTv[6]= (TextView) courseView.findViewById(R.id.sun_tv);

        ClassesTv[0]= (TextView) courseView.findViewById(R.id.class1_tv);
        ClassesTv[1]= (TextView) courseView.findViewById(R.id.class2_tv);
        ClassesTv[2]= (TextView) courseView.findViewById(R.id.class3_tv);
        ClassesTv[3]= (TextView) courseView.findViewById(R.id.class4_tv);
        ClassesTv[4]= (TextView) courseView.findViewById(R.id.class5_tv);
        ClassesTv[5]= (TextView) courseView.findViewById(R.id.class6_tv);
        ClassesTv[6]= (TextView) courseView.findViewById(R.id.class7_tv);
        ClassesTv[7]= (TextView) courseView.findViewById(R.id.class8_tv);
        ClassesTv[8]= (TextView) courseView.findViewById(R.id.class9_tv);
        ClassesTv[9]= (TextView) courseView.findViewById(R.id.class10_tv);
        ClassesTv[10]= (TextView) courseView.findViewById(R.id.class11_tv);
        ClassesTv[11]= (TextView) courseView.findViewById(R.id.class12_tv);

        /*
        //课程表内容适配器
         */

        contentAdapter = new CourseGridAdapter(getActivity());

        /*
        //创建Spinner数据
         */
        spinnerWeekAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerWeekDataList);
        spinnerWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        WeekSpinner.setAdapter(spinnerWeekAdapter);

        spinnerTermAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerTermDataList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TermSpinner.setAdapter(spinnerTermAdapter);
        int currentweek= InformationShared.getInt("currentweek"+GradeYear.getCurrentGrade(),-1);
        WeekSpinner.setSelection(currentweek+1);


        WeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i==1)
                {
                    String []week=new String[20];
                    for(int w = 0; w < 20; w++) {
                        week[w]="第 "+(w+1)+" 周";
                    }
                    builder=new AlertDialog.Builder(getActivity());builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle("设置当前周");

                    /**
                     * 设置内容区域为单选列表项
                     */
                    builder.setItems(week,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            InformationShared.setInt("currentweek"+TermSpinner.getSelectedItemPosition(),i+1);
                            int currentweek= InformationShared.getInt("currentweek"+TermSpinner.getSelectedItemPosition(),-1);
                            WeekSpinner.setSelection(currentweek+1);
                            Calendar c = Calendar.getInstance();
                            int day=c.get(Calendar.DAY_OF_MONTH);
                            int week=i+1;
                            int w=c.get(Calendar.DAY_OF_WEEK);
                            boolean isFirstSunday = (c.getFirstDayOfWeek() == Calendar.SUNDAY);
                            if(!isFirstSunday)
                            {
                                w+=1;
                                if(w==7)
                                    w=1;
                            }
                            //找到星期一的日期
                            if(day>6)
                            {
                                if(w==1)
                                {
                                    day=day-6;
                                }else
                                {
                                    day=day-w+2;
                                }
                            }else
                            {
                                if(w==1)
                                {
                                    day=day+1;
                                    week=week+1;
                                }else
                                {
                                    day=day+7-w+2;
                                    week=week+1;
                                }
                            }

                            InformationShared.setInt("start_month"+TermSpinner.getSelectedItemPosition(),c.get(Calendar.MONTH)+1);
                            InformationShared.setInt("start_day"+TermSpinner.getSelectedItemPosition(),day);
                            InformationShared.setInt("start_week"+TermSpinner.getSelectedItemPosition(),week);
                            setWeekSpinner();
                            setTime();
                        }
                    });

                    builder.setCancelable(true);
                    AlertDialog dialog=builder.create();
                    dialog.show();
                } else if(i==0)
                {
                    setTime();
                    setAlltCourseContent(getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));
                }else {
                    setTime();
                    setCurrentCourseContent(i-1, getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));//课程表内容初始化
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TermSpinner.setSelection(GradeYear.getCurrentGrade());
        TermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    int isDown=InformationShared.getInt("course"+i);
                    if(isDown==0)
                    {
                        pd=ProgressDialog.show(getActivity(),"正在加载","请稍后......");
                        getNetStartWeek();
                    }

                if(i==GradeYear.getCurrentGrade())
                     GradeYear.setCurrentWeek(i);
                int currentweek= InformationShared.getInt("currentweek"+i,-1);
                WeekSpinner.setSelection(currentweek+1);
                setCurrentCourseContent(InformationShared.getInt("currentweek"+i,-1), getCurrentXN(i),GradeYear.getCurrentXQ(i));
                setWeekSpinner();
                setTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setWeekSpinner();
       /* int current=GradeYear.getCurrentGrade();
        int isDown=InformationShared.getInt("course"+current);
        if(isDown==0)
        {
            pd=ProgressDialog.show(getActivity(),"正在加载","请稍后......");
            getCourse(GradeYear.getCurrentXN(current),GradeYear.getCurrentXQ(current));
        }*/
    }

    public void setWeekSpinner()
    {
        int XN=Integer.parseInt(getCurrentXN(TermSpinner.getSelectedItemPosition()).substring(0,4))+1;
        int days[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (XN% 4 == 0 && XN % 100 != 0 || XN% 400 == 0)
        {
            days[2]=29;
        }
        if(InformationShared.getInt("currentweek"+TermSpinner.getSelectedItemPosition())!=0)
        {

            for(int i = 1; i < 21; i++) {
                int date[]=GradeYear.getMonMonthDay(TermSpinner.getSelectedItemPosition(),i);
                if(i==InformationShared.getInt("currentweek"+TermSpinner.getSelectedItemPosition()))
                {
                    spinnerWeekDataList.set(i+1,"第" + i + "周 (当前周）");
                }else{
                    if(days[date[0]]-date[1]<6)
                    {
                        int month=date[0]+1;
                        if(month==13)
                            month=1;
                        spinnerWeekDataList.set(i+1,"第" + i + "周("+date[0]+"."+date[1]+"~"+month+"."+date[7]+")");
                    }else
                        spinnerWeekDataList.set(i+1,"第" + i + "周("+date[0]+"."+date[1]+"~"+date[0]+"."+date[7]+")");
                }

            }
        }
    }
    public void  DataList() {
        spinnerWeekDataList = new ArrayList<>();
        spinnerWeekDataList.add("全部课程");
        spinnerWeekDataList.add("设置当前周");

        for(int i = 1; i < 21; i++) {
            spinnerWeekDataList.add("第" + i + "周");
        }



        spinnerTermDataList = new ArrayList<>();
        spinnerTermDataList.add("大一上");
        spinnerTermDataList.add("大一下");
        spinnerTermDataList.add("大二上");
        spinnerTermDataList.add("大二下");
        spinnerTermDataList.add("大三上");
        spinnerTermDataList.add("大三下");
        spinnerTermDataList.add("大四上");
        spinnerTermDataList.add("大四下");
    }
    //设置当前周的课程
    public void setCurrentCourseContent(int week,String xn,String xq) {
        if(week==-1)
        {
            setAlltCourseContent(xn,xq);
            return;
        }
        contents=new CourseArray[6][];
        for (int i=0;i<6;i++)
        {
            contents[i]=new CourseArray[7];
            for(int j=0;j<7;j++)
            {
                contents[i][j]=new CourseArray();
            }
        }

        List<Course> courses = mgr.query(xn,xq);
        int course_num=-1;

        for(Course course :courses)
        {
            int qsz=Integer.parseInt(course.qsz);
            int jsz=Integer.parseInt(course.jsz);
            if(qsz<=week&&jsz>=week)
            {
                int find=0;
                int xqj=Integer.parseInt(course.xqj);
                int djj=Integer.parseInt(course.djj);
                String []c=course.kcb.split("<br>");
                String course2=" ";
                try{
                    course2=c[3]+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
                }catch (Exception e)
                {
                    course2="   "+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
                }
                for (int i=0;i<6;i++)
                {
                    if(find==1)
                    {
                        break;
                    }
                    for(int j=0;j<7;j++)
                    {
                        if(find==1)
                        {
                            break;
                        }
                        int size=contents[i][j].getSize();
                        for(int m=0;m<size;m++)
                        {
                            String aa[]=contents[i][j].get(m).split(":");
                            String bb[]=aa[1].split("\n");
                            if(c[0].equals(bb[1])){
                                course2=aa[0]+":"+course2;
                                find=1;
                                break;
                            }
                        }
                    }
                }
                if(find!=1)
                {
                    course_num++;
                    course2=course_num+":"+course2;
                }
                if(course.dsz.equals("单"))
                {
                    if((WeekSpinner.getSelectedItemPosition()-1)%2!=0)
                    {

                            if(course.skcd.equals("2")||course.skcd.equals("1")){
                                contents[djj/2][xqj-1].add(course2);
                            }else
                            {
                                contents[djj/2][xqj-1].add(course2);
                                contents[djj/2+1][xqj-1].add(course2);
                            }

                        }

                }else if(course.dsz.equals("双"))
                {
                    if((WeekSpinner.getSelectedItemPosition()-1)%2==0)
                    {
                        if(course.skcd.equals("2")||course.skcd.equals("1")){
                            contents[djj/2][xqj-1].add(course2);
                        }else{
                            contents[djj/2][xqj-1].add(course2);
                            contents[djj/2+1][xqj-1].add(course2);
                        }

                    }

                }else
                {
                    if(course.skcd.equals("2")||course.skcd.equals("1"))
                    {
                        contents[djj/2][xqj-1].add(course2);
                    }else{
                        contents[djj/2][xqj-1].add(course2);
                        contents[djj/2+1][xqj-1].add(course2);
                    }
                }

            }
        }


        courses = mgr2.query(xn,xq);
        for(Course course :courses)
        {
            int qsz=Integer.parseInt(course.qsz);
            int jsz=Integer.parseInt(course.jsz);
            if(qsz<=week&&jsz>=week)
            {
                int find=0;
                int xqj=Integer.parseInt(course.xqj);
                int djj=Integer.parseInt(course.djj);
                String []c=course.kcb.split("<br>");
                String course2=" ";
                try{
                    course2=c[3]+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
                }catch (Exception e)
                {
                    course2="   "+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
                }
                for (int i=0;i<6;i++)
                {
                    if(find==1)
                    {
                        break;
                    }
                    for(int j=0;j<7;j++)
                    {
                        if(find==1)
                        {
                            break;
                        }
                        int size=contents[i][j].getSize();
                        for(int m=0;m<size;m++)
                        {
                            String aa[]=contents[i][j].get(m).split(":");
                            String bb[]=aa[1].split("\n");
                            if(c[0].equals(bb[1])){
                                course2=aa[0]+":"+course2;
                                find=1;
                                break;
                            }
                        }
                    }
                }
                if(find!=1)
                {
                    course_num++;
                    course2=course_num+":"+course2;
                }
                if(course.dsz.equals("单"))
                {
                    if((WeekSpinner.getSelectedItemPosition()-1)%2!=0)
                    {

                        if(course.skcd.equals("2")||course.skcd.equals("1")){
                            contents[djj/2][xqj-1].add(course2);
                        }else
                        {
                            contents[djj/2][xqj-1].add(course2);
                            contents[djj/2+1][xqj-1].add(course2);
                        }

                    }

                }else if(course.dsz.equals("双"))
                {
                    if((WeekSpinner.getSelectedItemPosition()-1)%2==0)
                    {
                        if(course.skcd.equals("2")||course.skcd.equals("1")){
                            contents[djj/2][xqj-1].add(course2);
                        }else{
                            contents[djj/2][xqj-1].add(course2);
                            contents[djj/2+1][xqj-1].add(course2);
                        }

                    }

                }else
                {
                    if(course.skcd.equals("2")||course.skcd.equals("1"))
                    {
                        contents[djj/2][xqj-1].add(course2);
                    }else{
                        contents[djj/2][xqj-1].add(course2);
                        contents[djj/2+1][xqj-1].add(course2);
                    }
                }

            }
        }
        String [][]con = new String[6][7];
        for (int i=0;i<6;i++)
            for (int j=0;j<7;j++)
                con[i][j]="";
        for (int i=0;i<6;i++)
        {
            for(int j=0;j<7;j++)
            {
                int size=contents[i][j].getSize();
                for(int m=0;m<size;m++)
                {
                    con[i][j]=con[i][j]+contents[i][j].get(m)+"<br>";
                }
            }
        }

        for (int i=0;i<6;i++)
            for (int j=0;j<7;j++)
            {
                String day="";
                switch (j)
                {
                    case 0:
                        day="周一";
                        break;
                    case 1:
                        day="周二";
                        break;
                    case 2:
                        day="周三";
                        break;
                    case 3:
                        day="周四";
                        break;
                    case 4:
                        day="周五";
                        break;
                    case 5:
                        day="周六";
                        break;
                    case 6:
                        day="周日";
                        break;
                }
                if(con[i][j].equals("")) {

                    con[i][j]="\n\n\n\n\n\n\n\n\n\n"+day+"第"+(i*2+1)+","+(i*2+2)+"节"+TermSpinner.getSelectedItemPosition();
                }else
                {
                    con[i][j]=con[i][j]+"\n\n\n\n\n"+day+"第"+(i*2+1)+","+(i*2+2)+"节"+"\n\n\n\n\n\n点击添加课程"+TermSpinner.getSelectedItemPosition();
                }
            }

        contentAdapter.setContent(con, 6, 7);
        detailCource.setAdapter(contentAdapter);
    }
    //设置全部的课程
    public void setAlltCourseContent(String xn,String xq) {
        contents=new CourseArray[6][];
        for (int i=0;i<6;i++)
        {
            contents[i]=new CourseArray[7];
            for(int j=0;j<7;j++)
            {
                contents[i][j]=new CourseArray();
            }
        }
        List<Course> courses = mgr.query(xn,xq);
        int course_num=0;
        for(Course course :courses)
        {
            int find=0;
            int xqj=Integer.parseInt(course.xqj);
            int djj=Integer.parseInt(course.djj);
            String []c=course.kcb.split("<br>");
            String course2;
            try{
                course2=c[3]+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
            }catch (Exception e)
            {
                course2="   "+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
            }

            for (int i=0;i<6;i++)
            {
                if(find==1)
                {
                    break;
                }
                for(int j=0;j<7;j++)
                {
                    if(find==1)
                    {
                        break;
                    }
                    int size=contents[i][j].getSize();
                    for(int m=0;m<size;m++)
                    {
                        String aa[]=contents[i][j].get(m).split(":");
                        String bb[]=aa[1].split("\n");
                        if(c[0].equals(bb[1])){
                            course2=aa[0]+":"+course2;
                            find=1;
                            break;
                        }
                    }
                }
            }
            if(find!=1)
            {
                course_num++;
                course2=course_num+":"+course2;
            }
            if(course.skcd.equals("2")||course.skcd.equals("1"))
            {
                contents[djj/2][xqj-1].add(course2);
            }else if(course.skcd.equals("4"))
            {

                contents[djj/2][xqj-1].add(course2);
                contents[djj/2+1][xqj-1].add(course2);
            }
        }

        courses = mgr2.query(xn,xq);
        for(Course course :courses)
        {
            int find=0;
            int xqj=Integer.parseInt(course.xqj);
            int djj=Integer.parseInt(course.djj);
            String []c=course.kcb.split("<br>");
            String course2;
            try{
                course2=c[3]+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
            }catch (Exception e)
            {
                course2="   "+"\n"+c[0]+"\n\n\n\n\n\n"+c[2]+"\n"+c[1];
            }

            for (int i=0;i<6;i++)
            {
                if(find==1)
                {
                    break;
                }
                for(int j=0;j<7;j++)
                {
                    if(find==1)
                    {
                        break;
                    }
                    int size=contents[i][j].getSize();
                    for(int m=0;m<size;m++)
                    {
                        String aa[]=contents[i][j].get(m).split(":");
                        String bb[]=aa[1].split("\n");
                        if(c[0].equals(bb[1])){
                            course2=aa[0]+":"+course2;
                            find=1;
                            break;
                        }
                    }
                }
            }
            if(find!=1)
            {
                course_num++;
                course2=course_num+":"+course2;
            }
            if(course.skcd.equals("2")||course.skcd.equals("1"))
            {
                contents[djj/2][xqj-1].add(course2);
            }else if(course.skcd.equals("4"))
            {

                contents[djj/2][xqj-1].add(course2);
                contents[djj/2+1][xqj-1].add(course2);
            }
        }
        String [][]con = new String[6][7];
        for (int i=0;i<6;i++)
            for (int j=0;j<7;j++)
                con[i][j]="";
        for (int i=0;i<6;i++)
        {
            for(int j=0;j<7;j++)
            {
                int size=contents[i][j].getSize();
                for(int m=0;m<size;m++)
                {
                    con[i][j]=con[i][j]+contents[i][j].get(m)+"<br>";
                }
            }
        }
        for (int i=0;i<6;i++)
            for (int j=0;j<7;j++)
            {
                String day="";
                switch (j)
                {
                    case 0:
                        day="周一";
                        break;
                    case 1:
                        day="周二";
                        break;
                    case 2:
                        day="周三";
                        break;
                    case 3:
                        day="周四";
                        break;
                    case 4:
                        day="周五";
                        break;
                    case 5:
                        day="周六";
                        break;
                    case 6:
                        day="周日";
                        break;
                }
                if(con[i][j].equals("")) {

                    con[i][j]="\n\n\n\n\n\n\n\n\n\n"+day+"第"+(i*2+1)+","+(i*2+2)+"节"+TermSpinner.getSelectedItemPosition();
                }else
                {
                    con[i][j]=con[i][j]+"\n\n\n\n\n"+day+"第"+(i*2+1)+","+(i*2+2)+"节"+"\n\n\n\n\n\n点击添加课程"+TermSpinner.getSelectedItemPosition();
                }
            }





        contentAdapter.setContent(con, 6, 7);
        detailCource.setAdapter(contentAdapter);
    }


    void getNetStartWeek(){
            AVQuery<AVObject> query = new AVQuery<>("TimeTable");
            query.whereEqualTo("XN", getCurrentXN(TermSpinner.getSelectedItemPosition()));
            query.whereEqualTo("XQ", GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(list!=null)
                    if(list.size()!=0)
                    {
                        AVObject l=list.get(0);
                        if(l!=null){
                            InformationShared.setInt("start_month"+TermSpinner.getSelectedItemPosition(),Integer.parseInt(l.getString("Month")));
                            InformationShared.setInt("start_day"+TermSpinner.getSelectedItemPosition(),Integer.parseInt(l.getString("Day")));
                            InformationShared.setInt("start_week"+TermSpinner.getSelectedItemPosition(),1);
                            InformationShared.setInt("currentweek"+TermSpinner.getSelectedItemPosition(),-1);
                            if(GradeYear.getCurrentGrade()==TermSpinner.getSelectedItemPosition())
                                GradeYear.setCurrentWeek(TermSpinner.getSelectedItemPosition());
                        }

                    }
                    getCourse(getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));

                }
            });


    }


    public void getCourse(String year,String term)
    {
        String infourl="https://api.cugapp.com/public_api/CugApp/curriculum_schedules_for_wechat_unionid?unionid="+ InformationShared.getString("unionid")+"&academic_year="+year+"&term="+term;
        JsonObjectRequest getInfo=new JsonObjectRequest(infourl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean status = response.getBoolean("status");
                    if(status)
                    {
                        mgr.delete(getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));
                        String data=response.getString("data");
                        Pattern pattern = Pattern.compile("\\{(.*?\\{.*?\\}.*?)\\}");
                        Matcher matcher = pattern.matcher(data);
                        while(matcher.find()){
                            String json="{"+matcher.group(1)+"}";
                            JSONObject d=new JSONObject(json);
                            Course cos=new Course(d.getString("XN"),d.getString("XQ"),d.getString("XQJ"),d.getString("DJJ"),d.getString("QSZ"),d.getString("JSZ"),d.getString("KCB"),d.getString("DSZ"),d.getString("SKCD"));
                            mgr.add(cos);
                        }
                        InformationShared.setInt("course"+TermSpinner.getSelectedItemPosition(),1);
                    }
                    layout.setRefreshing(false);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-API-KEY", CugApplication.X_API_KEY);
                return  headers;
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                if(pd!=null)
                    pd.dismiss();
                layout.setRefreshing(false);
                int currentweek= InformationShared.getInt("currentweek"+TermSpinner.getSelectedItemPosition(),-1);
                WeekSpinner.setSelection(currentweek+1);
                setCurrentCourseContent(InformationShared.getInt("currentweek"+TermSpinner.getSelectedItemPosition(),-1), getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));
                setWeekSpinner();
                setTime();
            }
        };
        CugApplication.getInstance().addToRequestQueue(getInfo);
    }

    public static CourseFragment getInstannce() {
        return courseFragment;
    }
    public void setCourse()
    {
        setTime();
        int i=WeekSpinner.getSelectedItemPosition();
        if(i==0)
        {
            setAlltCourseContent(getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));
        }else
            setCurrentCourseContent(i-1, getCurrentXN(TermSpinner.getSelectedItemPosition()),GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition()));//课程表内容初始化
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
        mgr2.closeDB();
    }

    /**
     * 截取scrollview的屏幕
     * **/
    /*public void getBitmapByView( ) {
        ProgressDialog pd;
        pd=ProgressDialog.show(getActivity(),"正在截取","请稍后......");
        pd.show();
        String fileName= ScreenShot.shoot(courseSV,ScreenTimeLy,ScreenWeekLy);
        if(!fileName.equals("")){
            pd.dismiss();
            OwnToast.Short("截图已保存到相册！");
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            File file = new File(fileName);
            Uri uri = Uri.fromFile(file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(shareIntent, "请选择"));
        }else{
            pd.dismiss();
            OwnToast.Short("截取失败");
        }
    }*/
    public LinearLayout getCourseBackground(){
        return CourseBackground;
    }

    @Override
    public void onResume() {
        super.onResume();
      //  MobclickAgent.onPageStart("Course"); //统计页面，"MainScreen"为页面名称，可自定义
         TCAgent.onPageStart(getActivity(), "Course");
    }

    @Override
    public void onPause() {
        super.onPause();
      //  MobclickAgent.onPageStart("Course"); //统计页面，"MainScreen"为页面名称，可自定义
        TCAgent.onPageEnd(getActivity(), "Course");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null) {

            Uri uri = data.getData();

          /*  int weight= CourseBackground.getHeight();
            int height=CourseBackground.getWidth();
            Bitmap photo=getSmallBitmap(getPathByUri4kitkat(getActivity(),uri),weight,height);
            if(photo==null)
            {
                OwnToast.Long("请检查存储权限是否开启");
                return;
            }
            BitmapDrawable bd=new BitmapDrawable(getResources(),photo);
            if(bd==null)
            {
                OwnToast.Long("请检查存储权限是否开启");
                return;
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] b = stream.toByteArray();
            // 将图片流以字符串形式存储下来
            String tp = new String(Base64Encoder.encode(b));
            InformationShared.setString("course_background", tp);
            CourseBackground.setBackground(bd);*/
        }
    }

    public  int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
 /*   private    String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }*/


}
