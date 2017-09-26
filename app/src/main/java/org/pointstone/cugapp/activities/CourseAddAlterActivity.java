package org.pointstone.cugapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.fragments.CourseFragment;
import org.pointstone.cugapp.utils.Course;
import org.pointstone.cugapp.utils.DBCourseNewManager;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;

import java.util.ArrayList;
import java.util.List;


public class CourseAddAlterActivity extends AppCompatActivity {
    private EditText TeacherNameEt;
    private EditText CourseNameEt;
    private EditText PlaceEt;
    private TextView TimeTv;

    private RadioGroup WeekGroup;
    private Button OkBtn;
    private Button CancleBtn;

    private Spinner StartSpinner;
    private Spinner EndSpinner;
    private List<String> spinnerWeekDataList;
    private ArrayAdapter<String> spinnerWeekAdapter;

    private DBCourseNewManager mgr;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private  String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_alter);

        TeacherNameEt = (EditText) findViewById(R.id.teacher_name_et);
        CourseNameEt = (EditText) findViewById(R.id.course_name_et);
        PlaceEt = (EditText) findViewById(R.id.place_et);
        TimeTv = (TextView) findViewById(R.id.time_tv);
        StartSpinner = (Spinner) findViewById(R.id.start_spi);
        EndSpinner= (Spinner) findViewById(R.id.end_spi);
        WeekGroup= (RadioGroup) findViewById(R.id.week_group);
        OkBtn= (Button) findViewById(R.id.ok_btn);
        CancleBtn= (Button) findViewById(R.id.cancle_btn);
        mgr = new DBCourseNewManager(this);
        CancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StartSpinner.getSelectedItemPosition()<=EndSpinner.getSelectedItemPosition())
                {
                    String radioB="";
                    for (int i = 0; i < WeekGroup.getChildCount(); i++) {
                        RadioButton rd = (RadioButton)WeekGroup.getChildAt(i);
                        if (rd.isChecked()) {
                            radioB= rd.getText().toString();
                            break;
                        }
                    }

                    String xn=GradeYear.getCurrentXN(Integer.parseInt(time));
                    String xq=GradeYear.getCurrentXQ(Integer.parseInt(time));
                    String xqj="";
                    String date=TimeTv.getText().toString();
                    if(date.indexOf("一")!=-1){xqj="1";}
                    else  if(date.indexOf("二")!=-1){xqj="2";}
                    else  if(date.indexOf("三")!=-1){xqj="3";}
                    else  if(date.indexOf("四")!=-1){xqj="4";}
                    else  if(date.indexOf("五")!=-1){xqj="5";}
                    else  if(date.indexOf("六")!=-1){xqj="6";}
                    else  if(date.indexOf("日")!=-1){xqj="7";}
                    String djj=date.substring(date.indexOf("第")+1,date.indexOf(","));
                    String qsz=StartSpinner.getSelectedItemPosition()+1+"";
                    String jsz=EndSpinner.getSelectedItemPosition()+1+"";
                    String dsz="";
                    if(radioB.equals("全"))
                    {
                        dsz=" ";
                    }else
                    {
                        dsz=radioB.substring(0,1);
                    }
                    if(date.indexOf("-")!=-1)
                        date=date.substring(0,date.indexOf("{"));
                    String courseName="   ";
                    String TeacherName="   ";
                    String Place="   ";
                    if(!CourseNameEt.getText().toString().equals(""))
                        courseName=CourseNameEt.getText().toString();
                    if(!TeacherNameEt.getText().toString().equals(""))
                        TeacherName=TeacherNameEt.getText().toString();
                    if(!PlaceEt.getText().toString().equals(""))
                        Place=PlaceEt.getText().toString();
                    String kcb=courseName+"<br>"+date+"{第"+qsz+"-"+jsz+"周}<br>"+TeacherName +"<br>"+Place ;
                    Course cos=new Course(xn,xq,xqj,djj,qsz,jsz,kcb,dsz,"2");
                    mgr.add(cos);
                    finish();
                    CourseFragment.getInstannce().setCourse();
                }else{
                    OwnToast.Long("亲，开始周不能大于结束周哦");
                }

            }
        });



        spinnerWeekDataList = new ArrayList<>();
        for(int i = 1; i < 21; i++) {
            spinnerWeekDataList.add("第" + i + "周");
        }

        spinnerWeekAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerWeekDataList);
        spinnerWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StartSpinner.setAdapter(spinnerWeekAdapter);
        EndSpinner.setAdapter(spinnerWeekAdapter);
        int currentweek= InformationShared.getInt("currentweek"+ GradeYear.getCurrentGrade(),-1);
        if(currentweek!=-1)
        {
            StartSpinner.setSelection(currentweek-1);
            EndSpinner.setSelection(currentweek-1);
        }else
        {
            StartSpinner.setSelection(9);
            EndSpinner.setSelection(9);
        }

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        time=intent.getStringExtra("time");

        if (data.charAt(0) != '\n') {
            data = data.replace("\n\n", "");
            String[] c = data.split("\n");
            PlaceEt.setText(c[0]);
            CourseNameEt.setText(c[1]);
            TeacherNameEt.setText(c[2]);
            TimeTv.setText(c[3] );
            if(c[3].indexOf("单")!=-1)
            {
                WeekGroup.check(R.id.single_rb);
            }else if(c[3].indexOf("双")!=-1)
            {
                WeekGroup.check(R.id.double_rb);
            }else{
                WeekGroup.check(R.id.full_rb);
            }
            StartSpinner.setSelection(Integer.parseInt(c[3].substring(c[3].indexOf("{")+2,c[3].indexOf("-")))-1);
            String t=c[3].substring(c[3].indexOf("{")+1,c[3].indexOf("}"));
            EndSpinner.setSelection(Integer.parseInt(t.substring(t.indexOf("-")+1,t.indexOf("周")))-1);
        }else{
            data = data.replace("\n\n", "");
            String[] c = data.split("\n");
            for(int i=0;i<c.length;i++)
            {
                if(!c[i].equals(""))
                {
                    TimeTv.setText(c[i]);
                    break;
                }
            }
            WeekGroup.check(R.id.full_rb);
        }

        int XN=Integer.parseInt(GradeYear.getCurrentXN(Integer.parseInt(time)).substring(0,4));
       int days[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (XN% 4 == 0 && XN % 100 != 0 || XN% 400 == 0)
        {
            days[2]=29;
        }
        if(InformationShared.getInt("currentweek"+time)!=0)
        {

            for(int i = 1; i < 21; i++) {
                int date[]= GradeYear.getMonMonthDay(Integer.parseInt(time),i);

                    if(days[date[0]]-date[1]<7)
                    {
                        int month=date[0]+1;
                        if(month==13)
                            month=1;
                        spinnerWeekDataList.set(i-1,"第" + i + "周("+date[0]+"."+date[1]+"~"+month+"."+date[7]+")");
                    }else
                        spinnerWeekDataList.set(i-1,"第" + i + "周("+date[0]+"."+date[1]+"~"+date[0]+"."+date[7]+")");
            }
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CourseAddAlter Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }
}
