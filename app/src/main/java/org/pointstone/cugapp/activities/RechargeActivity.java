package org.pointstone.cugapp.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.GuiUtils;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;

import java.util.HashMap;
import java.util.Map;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvClose;
    private TextView mFabCircle;
    private ProgressDialog pd;
    private boolean flag=true;
    private LinearLayout linearLayout;
    private Button leftButton,rightButton,Button_money10,Button_money30,Button_money50,Button_money100,Button_money150,Button_money200;
    private CheckBox rememberCheckBox;
    private EditText moneyEditText;
    private EditText passwordEditText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        context=RechargeActivity.this;
        init();
        if(flag){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setupEnterAnimation(); // 入场动画
                setupExitAnimation(); // 退场动画
            } else {
                initViews();
            }
        }
    }

    // 动画展示
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        GuiUtils.animateRevealShow(
                this, linearLayout,
                mFabCircle.getWidth() / 2, R.color.colorRecharge,
                new GuiUtils.OnRevealAnimationListener() {
                    @Override public void onRevealHide() {

                    }

                    @Override public void onRevealShow() {
                        init();
                    }
                });
    }

    private void init() {
        linearLayout= (LinearLayout) findViewById(R.id.recharge_layout);
        mFabCircle= (TextView) findViewById(R.id.other_fab_circle);
        mIvClose= (ImageView) findViewById(R.id.recharge_close);
        leftButton = (Button)findViewById(R.id.btn_cancel);
        rightButton = (Button)findViewById(R.id.btn_sure);
        rememberCheckBox= (CheckBox)findViewById(R.id.remember);
        moneyEditText = (EditText)findViewById(R.id.edit_money);
        passwordEditText = (EditText)findViewById(R.id.edit_password);
        Button_money10= (Button) findViewById(R.id.btn_money10);
        Button_money30= (Button) findViewById(R.id.btn_money30);
        Button_money50= (Button) findViewById(R.id.btn_money50);
        Button_money100= (Button) findViewById(R.id.btn_money100);
        Button_money150= (Button) findViewById(R.id.btn_money150);
        Button_money200= (Button) findViewById(R.id.btn_money200);
        Button_money10.setOnClickListener(this);
        Button_money30.setOnClickListener(this);
        Button_money50.setOnClickListener(this);
        Button_money100.setOnClickListener(this);
        Button_money150.setOnClickListener(this);
        Button_money200.setOnClickListener(this);

        if(!InformationShared.getString("password").equals("0")) {
            rememberCheckBox.setChecked(true);
            passwordEditText.setText(InformationShared.getString("password"));
        }
        //记住密码复选框的点击事件
        rememberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    InformationShared.setString("password", passwordEditText.getText().toString());
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    onBackPressed();
                } else {
                    defaultBackPressed();
                }
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=passwordEditText.getText().toString();
                String money=moneyEditText.getText().toString();
                if(money.length()==0)
                    OwnToast.Short("金额不能为空哦！");
                else if(Integer.parseInt(money)==0)
                    OwnToast.Short("金额不能为0哦！");
                else if(password.length()==0)
                    OwnToast.Short("密码不能为空哦！");
                else if(Integer.parseInt(money)>500)
                    OwnToast.Short("一次充值金额不超过500元哦！");
                else if(password.length()!=6)
                    OwnToast.Short("请输入正确的密码格式！");
                else {
                    pd = ProgressDialog.show(context, "充值中", "请稍后......");
                    String temp= String.valueOf(Integer.parseInt(moneyEditText.getText().toString()));
                    postRecharge(passwordEditText.getText().toString(), temp);
                }

            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                onBackPressed();
            } else {
                defaultBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 初始化视图
    private void initViews() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                animation.setDuration(300);

                mIvClose.setAnimation(animation);

                mIvClose.setVisibility(View.VISIBLE);
            }
        });
    }



    // 入场动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {

        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override public void onTransitionStart(Transition transition) {

            }

            @Override public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override public void onTransitionCancel(Transition transition) {

            }

            @Override public void onTransitionPause(Transition transition) {

            }

            @Override public void onTransitionResume(Transition transition) {

            }
        });
    }

    // 退出动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimation() {
        Fade fade = new Fade();
        getWindow().setReturnTransition(fade);
        fade.setDuration(300);
    }



    // 退出按钮
    public void backActivity(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        } else {
            defaultBackPressed();
        }
    }

    // 退出事件
    @Override public void onBackPressed() {
        GuiUtils.animateRevealHide(
                this, linearLayout,
                mFabCircle.getWidth() / 2, R.color.colorRecharge,
                new GuiUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        defaultBackPressed();
                    }

                    @Override
                    public void onRevealShow() {

                    }
                });
    }

    // 默认回退
    private void defaultBackPressed() {
        super.onBackPressed();
    }




    // 充值服务的Volley
    private void postRecharge(String password,String amount) {
        CugApplication.getInstance().getRequestQueue().cancelAll("My Tag");
        String url = "https://api.cugapp.com/public_api/CugApp/recharge_oneCard";
        HashMap<String, String> hashMap = new HashMap<String,String>();
        hashMap.put("unionid", InformationShared.getString("unionid"));
        hashMap.put("password",password);
        hashMap.put("amount",amount);
        System.out.println(amount);
        JSONObject jsonObject = new JSONObject(hashMap);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean status = response.getBoolean("status");
                            if(status) {
                                System.out.println(true + " " + response.getString("message"));
                                pd.cancel();
                                OwnToast.Short("充值成功");
                            }
                            else {
                                System.out.println(response.getString("message"));
                                pd.cancel();
                                OwnToast.Short(response.getString("message"));
                                if(response.getString("message").equals("密码错误"))
                                    InformationShared.setString("password","0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if( error instanceof NetworkError) {
                            //网络异常
                            OwnToast.Short("网络异常");
                        } else if( error instanceof ServerError) {
                            //服务器响应错误
                            OwnToast.Short("服务器响应错误");
                        } else if( error instanceof AuthFailureError) {
                            //请求时验证凭证失败
                            OwnToast.Short("请求时验证凭证失败");
                        } else if( error instanceof ParseError) {
                            //解析服务器返回数据错误
                            OwnToast.Short("解析服务器返回数据错误");
                        } else if( error instanceof NoConnectionError) {
                            //网络请求无任何连接
                            OwnToast.Short("网络请求无任何连接");
                        } else if( error instanceof TimeoutError) {
                            //超时
                            OwnToast.Short("请求超时");
                        }else{
                            //成功
                            OwnToast.Short("充值成功");
                        }
                    }
                }) {
            // Volley请求类提供了一个 getHeaders（）的方法，重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-API-KEY","s0048ksww8ck8kc8kg4wc8ooskwsgc00k4kg0kkk");
                return headers;
            }


            @Override
            protected void onFinish() {
                super.onFinish();
                pd.dismiss();
            }

        };
           /* 解决重复请求问题 */
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 0, 1f) {
            @Override
            public int getCurrentRetryCount() {
                //表示当前已经重复请求一次，就不会再第二次重复请求，从而屏蔽掉Volley的自动重复请求功能
                return 1;
            }
        });
        jsObjRequest.setTag("My Tag");
        CugApplication.getInstance().addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id)
        {
            case R.id.btn_money10:
                moneyEditText.setText("10");
                break;
            case R.id.btn_money30:
                moneyEditText.setText("30");
                break;
            case R.id.btn_money50:
                moneyEditText.setText("50");
                break;
            case R.id.btn_money100:
                moneyEditText.setText("100");
                break;
            case R.id.btn_money150:
                moneyEditText.setText("150");
                break;
            case R.id.btn_money200:
                moneyEditText.setText("200");
                break;
            default:
                break;
        }
    }
}
