package org.pointstone.cugapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.fragments.ExamFragmentc;
import org.pointstone.cugapp.utils.DBExamManager;

import java.util.Calendar;


public class ExamAlterActivity extends AppCompatActivity {
    private TextView kcmctv;
    private EditText ExamjsEt;
    private EditText ExamzhEt;
    private DBExamManager dbExamManager;
    private Button OkBtn;
    private Button CancleBtn;
    private EditText showDate = null;
    private Button pickDate = null;
    private EditText showStartTime = null;
    private EditText showEndtime = null;
    private Button pickStartTime = null;
    private Button pickEndTime = null;

    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_STARTPICK = 2;
    private static final int START_DIALOG_ID = 3;
    private static final int SHOW_ENDPICK = 4;
    private static final int END_DIALOG_ID = 5;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private static ExamAlterActivity examAlterActivity;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_alter);


        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String usedata = data.split("\n")[0];
        final String xn = usedata.substring(0, 9);
        final String xq = usedata.substring(10, 13).equals("上学期")?"1":"2";
        final String kcmc = data.split("\n")[1];
        final String kssj=data.split("\n")[3];
        final String jsmc=data.split("\n")[4].substring(5,data.split("\n")[4].length());
        final String zwh=data.split("\n")[5].substring(4,data.split("\n")[5].length());
        examAlterActivity = this;
        dbExamManager = new DBExamManager(this);
        kcmctv = (TextView) findViewById(R.id.kcmcTv);
        kcmctv.setText(kcmc);
        ExamjsEt = (EditText) findViewById(R.id.jsEt);
        ExamjsEt.setText(jsmc);
        ExamzhEt = (EditText) findViewById(R.id.zhEt);
        ExamzhEt.setText(zwh);
        showDate = (EditText) findViewById(R.id.showdate);
        showDate.setText(kssj.split("u0029")[0]);
        pickDate = (Button) findViewById(R.id.pickdate);
        showStartTime = (EditText) findViewById(R.id.showStarttime);
        showEndtime = (EditText) findViewById(R.id.showEndtime);
        pickStartTime = (Button) findViewById(R.id.pickStrattime);
        pickEndTime = (Button) findViewById(R.id.pickEndtime);
        OkBtn = (Button) findViewById(R.id.ok_btn);
        CancleBtn = (Button) findViewById(R.id.cancle_btn);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        initializeViews();
        setDateTime();
        setTimeOfDay();
        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jsmc = ExamjsEt.getText().toString();
                String zwh = ExamzhEt.getText().toString();
                String kssj=getTime();
                dbExamManager.update(xn, xq, kcmc, kssj,jsmc, zwh);
                //OwnToast.Short(xn+";"+xq+";"+kcmc);
                finish();
                ExamFragmentc.getInstannce().setExam(xn,xq);
            }
        });
        CancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initializeViews() {


        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickDate.equals((Button) v)) {
                    msg.what = ExamAlterActivity.SHOW_DATAPICK;
                }
                ExamAlterActivity.this.dateandtimeHandler.sendMessage(msg);
            }
        });

        pickStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickStartTime.equals((Button) v)) {
                    msg.what = ExamAlterActivity.SHOW_STARTPICK;
                }
                ExamAlterActivity.this.dateandtimeHandler.sendMessage(msg);
            }
        });

        pickEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickEndTime.equals((Button) v)) {
                    msg.what = ExamAlterActivity.SHOW_ENDPICK;
                }
                ExamAlterActivity.this.dateandtimeHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 设置日期
     */
    private void setDateTime() {
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();
    }

    /**
     * 更新日期显示
     */
    private void updateDateDisplay() {
        showDate.setText(new StringBuilder().append(mYear).append("年")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("月")
                .append((mDay < 10) ? "0" + mDay : mDay).append("日"));
    }

    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDateDisplay();
        }
    };

    /**
     * 设置时间
     */
    private void setTimeOfDay() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        //updateTimeDisplay();
    }

    /**
     * 更新时间显示
     */
    private void updateTimeDisplay() {
        showStartTime.setText(new StringBuilder().append(mHour).append(":")
                .append((mMinute < 10) ? "0" + mMinute : mMinute));
        showEndtime.setText(new StringBuilder().append(mHour).append(":")
                .append((mMinute < 10) ? "0" + mMinute : mMinute));
    }

    /**
     * 时间控件事件
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

            showStartTime.setText(new StringBuilder().append(mHour).append(":")
                    .append((mMinute < 10) ? "0" + mMinute : mMinute));
        }
    };
    private TimePickerDialog.OnTimeSetListener eTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

            showEndtime.setText(new StringBuilder().append(mHour).append(":")
                    .append((mMinute < 10) ? "0" + mMinute : mMinute));
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
            case START_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
            case END_DIALOG_ID:
                return new TimePickerDialog(this, eTimeSetListener, mHour, mMinute, true);
        }

        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case START_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                break;
            case END_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
        }
    }

    /**
     * 处理日期和时间控件的Handler
     */
    Handler dateandtimeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ExamAlterActivity.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case ExamAlterActivity.SHOW_STARTPICK:
                    showDialog(START_DIALOG_ID);
                    break;
                case ExamAlterActivity.SHOW_ENDPICK:
                    showDialog(END_DIALOG_ID);
                    break;
            }
        }

    };

    public String getTime(){
        return showDate.getText().toString()+"("+showStartTime.getText().toString()+"-"+showEndtime.getText().toString()+")";
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ExamAlter Page") // TODO: Define a title for the content shown.
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
        dbExamManager.closeDB();
    }
}
