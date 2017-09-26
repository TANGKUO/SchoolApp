package org.pointstone.cugapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by keyboy on 2017/1/2.
 */

public class LibraryPw extends Activity {
    private Button leftButton, rightButton;
    private CheckBox rememberCheckBox;
    private EditText passwordEditText;
    private static int rorf = 0;
    private static int library_pass_flag = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_password_dialog);

        leftButton = (Button) findViewById(R.id.library_btn_cancel);
        rightButton = (Button) findViewById(R.id.library_btn_sure);
        rememberCheckBox = (CheckBox) findViewById(R.id.library_remember);
        passwordEditText = (EditText) findViewById(R.id.library_edit_password);
        rememberCheckBox.setChecked(true);
        if (!InformationShared.getString("password").equals("0")) {
            rememberCheckBox.setChecked(true);
            passwordEditText.setText(InformationShared.getString("password"));
        }
        //记住密码复选框的点击事件
        rememberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    library_pass_flag = 1;
//                    InformationShared.setString("password", passwordEditText.getText().toString());
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                build.dismiss();
                Intent intent = new Intent(LibraryPw.this,DrawerActivity.class);
//                intent.putExtra("userloginflag", 0);
                startActivity(intent);
                finish();
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBookNow();
//                build.dismiss();

            }
        });
    }

    //查询图书当前借阅函数
    public void getBookNow()
    {
        CugApplication.getInstance().getRequestQueue().cancelAll("My Tag_BookNow");
        String url_balance="https://api.cugapp.com/public_api/CugApp/now_books?unionid="+ InformationShared.getString("unionid")+"&password="+passwordEditText.getText().toString();
        System.out.println(url_balance);
        JsonObjectRequest balance=new JsonObjectRequest(url_balance,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if(status) {
                        InformationShared.setString("library_password", passwordEditText.getText().toString());
//                getFragmentManager().beginTransaction().replace(R.id.content_layout, new LibraryFragment()).commit();//初始界面
                        Intent intent = new Intent(LibraryPw.this,DrawerActivity.class);
                        intent.putExtra("userloginflag", 1);
                        startActivity(intent);
                        finish();
                    }

                    else{
                        OwnToast.Short("密码错误，请重试");
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
                    OwnToast.Short("密码错误,请重新登录");

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
                    OwnToast.Short("查询当前借阅超时");
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
            }

        };
        balance.setTag("My Tag_BookNow");
        CugApplication.getInstance().addToRequestQueue(balance);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(LibraryPw.this,DrawerActivity.class);
            intent.putExtra("userloginflag", 0);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
