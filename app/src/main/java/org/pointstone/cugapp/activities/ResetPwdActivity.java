package org.pointstone.cugapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;
import org.pointstone.cugapp.view.Drawl;
import org.pointstone.cugapp.view.GestureLockView;


public class ResetPwdActivity extends Activity{

    Context context;

    private FrameLayout mFrameLayout;
    private GestureLockView mGuestureLockView;
    private SharedPreferences sp;
    private String pwd;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);

        context=this;

        initView();
    }

    private void initView() {

        //系统保存的密码
        pwd = InformationShared.getString("hand_pwd");

        mTextView= (TextView) findViewById(R.id.lock_tv);
        mTextView.setText("重置密码，请输入密码校验");

        mFrameLayout= (FrameLayout) findViewById(R.id.framelayout);
        mGuestureLockView=new GestureLockView(context,  new Drawl.GestureCallBack() {
            @Override
            public void checkedSuccess(String password) {
                //password是用户输入的密码，如果两个和系统保存的密码一样直接重置
                if(pwd.equals(password)){
                    InformationShared.setString("hand_pwd","0");
                    Intent intent=new Intent(ResetPwdActivity.this, GestureLockActivity.class);
                    startActivity(intent);
                    OwnToast.Short("密码重置成功，请设置新密码");
                    finish();
                }else {
                    OwnToast.Short("密码不正确，重置失败");
                    refresh();
                }
            }

            @Override
            public void checkedFail() {

            }
        });

        mGuestureLockView.setParentView(mFrameLayout);
    }

    public void refresh() {
        onCreate(null);
    }
}
