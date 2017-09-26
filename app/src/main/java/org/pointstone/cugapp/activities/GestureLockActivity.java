package org.pointstone.cugapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;
import org.pointstone.cugapp.view.Drawl;
import org.pointstone.cugapp.view.GestureLockView;


public class GestureLockActivity extends Activity {

    Context context;

    private FrameLayout mFrameLayout;
    private GestureLockView mGuestureLockView;
    private SharedPreferences sp;
    private TextView tv_tittle;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        context=this;
        //从sp中获取保存的密码，判断用户是否已经设置密码
        pwd=InformationShared.getString("hand_pwd");
        tv_tittle= (TextView) findViewById(R.id.lock_tv);
        if(!pwd.equals("0"))
            tv_tittle.setText("输入手势密码");
        initView();
    }

    private void initView() {

        mFrameLayout= (FrameLayout) findViewById(R.id.framelayout);

        mGuestureLockView=new GestureLockView(context, new Drawl.GestureCallBack() {
            @Override
            public void checkedSuccess(String password) {

                //首先判断一下用户是否已经设置密码
                if(pwd.equals("0")){
                    //如果为空，代码没有设置密码，需要设置新的密码；
                    // 设置新密码需要设置两遍，防止用户误操作；
                    // 第一遍设置的新密码保存在Variate类的一个变量中，这个变量默认为null
                    if(TextUtils.isEmpty(CugApplication.hand_pass)){
                        //如果这个变量为null，第一次将密码保存在Variate.PASSWORD提示再次输入密码，
                        CugApplication.hand_pass=password;
                        OwnToast.Short("请再次输入密码");
                        // 并且刷新当前页面
                        refresh();
                    }else {
                        //如果Variate.PASSWORD不为空代表是第二次输入新密码，判断两次输入密码是否相同
                        if(password.equals(CugApplication.hand_pass)){
                            //如果相同，将密码保存在当地sp中
                            InformationShared.setString("hand_pwd",password);
                            // 进入主页面，点击输入密码，输入设置的密码进入“搏击爱好者”页面
                            OwnToast.Short("密码设置成功，请输入新密码");
                            refresh();
                        }else {
                            //如果两次输入密码不一样，将Variate.PASSWORD设为null,提示密码设置失败
                            CugApplication.hand_pass=null;
                            OwnToast.Short("密码设置失败");
                            // 跳回主页面需重新设置密码
                            Intent intent=new Intent(GestureLockActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                }else{
                    //如果已经设置密码，判断输入密码和保存密码是否相同

                    if(pwd.equals(password)){
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", 0);
                        //设置返回数据
                        setResult(RESULT_OK, intent);
                        //关闭Activity
                        finish();
                    }else {
                        //如果不相同，密码错误，刷新当前activity，需重新输入密码
                        Toast.makeText(context,"密码错误",Toast.LENGTH_SHORT).show();
                        refresh();
                    }
                }
            }

            @Override
            public void checkedFail() {

            }
        });

        mGuestureLockView.setParentView(mFrameLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当前页面关闭时将Variate.PASSWORD设为null；防止用户第二次输入密码的时候退出当前activity
        CugApplication.hand_pass=null;
    }

    public void refresh() {
        onCreate(null);
    }


    /**
     * 屏蔽物理返回按钮
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //数据是使用Intent返回
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("result", 1);
            //设置返回数据
            setResult(RESULT_OK, intent);
            //关闭Activity
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
