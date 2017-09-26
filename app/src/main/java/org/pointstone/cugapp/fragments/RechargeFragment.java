package org.pointstone.cugapp.fragments;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.ConsumeRecording;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.activities.RechargeActivity;
import org.pointstone.cugapp.activities.RechargeSet;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by keyboy on 2016/12/6.
 */
public class RechargeFragment extends Fragment implements View.OnClickListener{

    private Button btn_recharge,btn_accountBook,btn_person,btn_help,btn_agree,btn_lose;
    private TextView text_query,text_what;
    private String id=null ,cardbalance=null,tmpbalance=null;
    private ProgressDialog pd;
    private CheckBox agreeCheckBox;

    private ImageView headRechargeIv,motionIv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recharge, container, false);
        text_query= (TextView) view.findViewById(R.id.txt_query);
        text_what= (TextView) view.findViewById(R.id.txt_what);
        btn_recharge= (Button) view.findViewById(R.id.button_recharge);
        btn_lose= (Button) view.findViewById(R.id.button_lose);
        btn_accountBook= (Button) view.findViewById(R.id.button_accountBook);
        btn_person= (Button) view.findViewById(R.id.button_person);
        btn_help= (Button) view.findViewById(R.id.button_help);
        btn_recharge.setOnClickListener(this);
        btn_lose.setOnClickListener(this);
        btn_accountBook.setOnClickListener(this);
        btn_person.setOnClickListener(this);
        btn_help.setOnClickListener(this);
        headRechargeIv= (ImageView)view.findViewById(R.id.recharge_head_iv);
        motionIv= (ImageView) view.findViewById(R.id.img_chui);
        String tp = InformationShared.getString("headimage");
        if (!tp.equals("0")) {
            byte[] bytes = Base64.decode(tp, Base64.DEFAULT);
            Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            headRechargeIv.setImageBitmap(dBitmap);
        }
        headRechargeIv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DrawerActivity.getInstannce().openDrawer();
            }
        });


        //查询
        pd= ProgressDialog.show(getActivity(),"查询中","请稍后......");
        getBalance();
        return view;
    }



    //点击事件的函数
    @Override
    public void onClick(View view) {
        int a=view.getId();
        switch (a){
            //充值
            case R.id.button_recharge:
                if(InformationShared.getInt("is_betatest_member")==1||InformationShared.getInt("is_team_member")==1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(getActivity(), btn_recharge, btn_recharge.getTransitionName());
                        startActivity(new Intent(getActivity(), RechargeActivity.class), options.toBundle());
                    } else {
                        startActivity(new Intent(getActivity(), RechargeActivity.class));
                    }
//                rechargeDialog();
                }else{
                    OwnToast.Long("暂未开放");
                }

                break;
            //明细
            case R.id.button_accountBook:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(getActivity(), btn_accountBook, btn_accountBook.getTransitionName());
                    startActivity(new Intent(getActivity(), ConsumeRecording.class), options.toBundle());
                } else {
                    startActivity(new Intent(getActivity(), ConsumeRecording.class));
                }
//                Intent intent = new Intent(getActivity(), ConsumeRecording.class);
//                startActivity(intent);
                break;
            //帮助
            case R.id.button_help:
                useDialog();
                break;
            //个人设置
            case R.id.button_person:
                Intent intent = new Intent(getActivity(), RechargeSet.class);
                startActivity(intent);
                break;
            //个人设置
            case R.id.button_lose:
                OwnToast.Short("该功能尚未实现，敬请期待！");
                break;
            default:
                break;

        }
    }



    //显示余额函数
    public void inquire(String cardmoney, String dangmoney) {
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.logo)
                .setTitle("账户余额")
                .setMessage(
                        "我的学号:  " + InformationShared.getString("id") + "\n\n"
                                + "卡内余额:  " + cardmoney+" 元" + "\n\n"
                                + "过度余额:  " + dangmoney+" 元")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setNegativeButton("取消", null).create().show();
    }



    //显示用户须知
    private void useDialog() {
        final AlertDialog build = new AlertDialog.Builder(getActivity()).create();
        //自定义布局
        View view = getActivity().getLayoutInflater().inflate(R.layout.recharge_dialog_use, null);
        //把自定义的布局设置到dialog中，注意，布局设置一定要在show之前。从第二个参数分别填充内容与边框之间左、上、右、下、的像素
        build.setView(view, 0, 0, 0, 0);
        //一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了
        build.show();
//        //得到当前显示设备的宽度，单位是像素
//        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
//        //得到这个dialog界面的参数对象
//        WindowManager.LayoutParams params = build.getWindow().getAttributes();
//        //设置dialog的界面宽度
//        params.width = width - (width / 6);
//        //设置dialog高度为包裹内容
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        //设置dialog的重心
//        params.gravity = Gravity.CENTER;
//        //dialog.getWindow().setLayout(width-(width/6), LayoutParams.WRAP_CONTENT);
//        //用这个方法设置dialog大小也可以，但是这个方法不能设置重心之类的参数，推荐用Attributes设置
//        //最后把这个参数对象设置进去，即与dialog绑定
//        build.getWindow().setAttributes(params);
        btn_agree = (Button) view.findViewById(R.id.agree_btn);
        agreeCheckBox= (CheckBox) view.findViewById(R.id.agree_checkbox);
        btn_agree.setEnabled(false);
        agreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    btn_agree.setBackgroundColor(0xFF2DF945);
                    btn_agree.setEnabled(true);
                    btn_agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            build.dismiss();
                        }
                    });
                }
                else {
                    btn_agree.setBackgroundColor(0xFFFFFFFF);
                    btn_agree.setEnabled(false);
                }
            }
        });
//        new android.support.v7.app.AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.logo)
//                .setTitle("今日流水信息")
//
//                .setMessage(
//                        "提示：从你的和一卡通绑定的银行卡给一卡通充值\n\n提示：如提示充值成功后请去查询余额信息以确认\n\n提示：一卡通充值成功最终以银行卡扣款成功为准\n\n提示：消费密码默认为身份证号不包括 X 的后六位\n\n提示：可能会有其他异常，请截图并告诉开发人员\n\n提示：所有曾经掉过银行卡的同学都无法在线充值成功！因为你的新卡号没有和一卡通绑定！请去一卡通管理中心解决绑定！一般出现「未知错误 59」就是这个问题！\n\n提示：提示充值成功后，钱以「过渡余额」形式存在你的一卡通帐户中，需要去任意一处联网的刷卡机（如食堂，超市）刷一次卡进行领取（无需消费，贴卡然后听到「嘀」的一声就完成了余额同步）\n\n")
//                .setPositiveButton("确定",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//                            }
//                        }).show();

    }



    //查询余额函数
    public void getBalance()
    {
        CugApplication.getInstance().getRequestQueue().cancelAll("My Tag_Balance");
        String url_balance="https://api.cugapp.com/public_api/CugApp/onecard_balance_for_wechat_unionid?unionid="+InformationShared.getString("unionid");
        System.out.println(url_balance);
        JsonObjectRequest balance=new JsonObjectRequest(url_balance,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if(status)
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        cardbalance = jsonObject.getString("cardbalance");
                        tmpbalance = jsonObject.getString("tmpbalance");
                        //查询
                        pd.cancel();
                        id=InformationShared.getString("nickname");
                        Float a = Float.parseFloat(cardbalance)+Float.parseFloat(tmpbalance);
                        System.out.println(a);
                        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
                        String money=df.format(a);
                        System.out.println(money);
                        Log.i("zzq",String.valueOf(a));
                        if(a>=0&&a<30) {
                            text_query.setText("饭卡里只剩" + money + "元了");
                            text_what.setText("再忙也要吃饭啊！");
                            motionIv.setImageDrawable(getActivity().getResources().getDrawable((R.drawable.chui03)));
                        }
                        else if(a>=30&&a<50) {
                            text_query.setText("饭卡里只有" + money + "元");
                            text_what.setText("要对自己好一点！");
                            motionIv.setImageDrawable(getActivity().getResources().getDrawable((R.drawable.chui02)));
                        }
                        else{
                            text_query.setText("饭卡里还有"+ money+"元");
                            text_what.setText("拿去尽情刷吧！");
                            motionIv.setImageDrawable(getActivity().getResources().getDrawable((R.drawable.chui01)));
                        }

                    }
                    else{
                        OwnToast.Short("获取信息失败");
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
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
                    OwnToast.Short("超时");
                }else{
                    //成功
                    OwnToast.Short("error");
                }
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
                pd.dismiss();
            }

        };
        balance.setTag("My Tag_Balance");
        CugApplication.getInstance().addToRequestQueue(balance);
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("Recharge"); //统计页面，"MainScreen"为页面名称，可自定义
         TCAgent.onPageStart(getActivity(), "Recharge");
    }

    @Override
    public void onPause() {
        super.onPause();
      //  MobclickAgent.onPageStart("Recharge"); //统计页面，"MainScreen"为页面名称，可自定义
         TCAgent.onPageEnd(getActivity(), "Recharge");
    }
}

