package org.pointstone.cugapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.adapter.AccountLvAdapter;
import org.pointstone.cugapp.bean.ConsumeData;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumeRecording extends Activity {
    private ListView lv_account;

    private AccountLvAdapter listViewAdapter;
    private List<ConsumeData> mData;
    private Integer[] imgeIDs = {R.drawable.recharge_account_plus, R.drawable.recharge_account_reduce};
    private ProgressDialog pd;
    private ImageView img_back;
    ConsumeData bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_listview);
        init();
        pd= ProgressDialog.show(this,"查询中","请稍后......");
        getAccountBook();

    }

    //查询今日流水
    public void getAccountBook()
    {
        CugApplication.getInstance().getRequestQueue().cancelAll("My Tag_AccountBook");
        String url_accountBook="https://api.cugapp.com/public_api/CugApp/tran_today?unionid="+ InformationShared.getString("unionid");
        Log.e("TAG2", url_accountBook);
        JsonObjectRequest getRecord=new JsonObjectRequest(url_accountBook,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    Log.e("TAG2", String.valueOf(status));
                    if(status)
                    {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            response = (JSONObject) jsonArray.get(i);
                            //将数据装到集合中去
                            if( Float.parseFloat(response.getString("交易数额"))<0) {
                                if (response.getString("交易商户").equals(""))
                                    bean = new ConsumeData(imgeIDs[1], response.getString("交易时间"), "¥" + response.getString("交易数额"), response.getString("交易方式"));
                                else
                                    bean = new ConsumeData(imgeIDs[1], response.getString("交易时间"), "¥" + response.getString("交易数额"), response.getString("交易商户"));
                            }
                            else {
                                if (response.getString("交易商户").equals(""))
                                    bean = new ConsumeData(imgeIDs[0], response.getString("交易时间"), "¥" + response.getString("交易数额"), response.getString("交易方式"));
                                else
                                    bean = new ConsumeData(imgeIDs[0], response.getString("交易时间"), "¥" + response.getString("交易数额"), response.getString("交易商户"));
                            }
                            mData.add(bean);
                        }
                        if(jsonArray.length()==0) {
                            //为数据绑定适配器
                            bean = new ConsumeData(imgeIDs[1], "", "", "暂无消费记录");
                            mData.add(bean);
                            listViewAdapter = new AccountLvAdapter(mData, ConsumeRecording.this);
                            lv_account.setAdapter(listViewAdapter);
                        }
                        else {

                            listViewAdapter = new AccountLvAdapter(mData, ConsumeRecording.this);
                            lv_account.setAdapter(listViewAdapter);
                        }
                    }
                    else{
                        OwnToast.Short(response.getString("message"));
                        Log.e("TAG2","获取今日流水失败");
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
                    OwnToast.Short("你可能还没有消费哦！");
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
                bean = new ConsumeData(imgeIDs[1], "", "", "暂无消费记录");
                mData.add(bean);
                listViewAdapter = new AccountLvAdapter(mData, ConsumeRecording.this);
                lv_account.setAdapter(listViewAdapter);
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
        getRecord.setTag("My Tag_AccountBook");
        CugApplication.getInstance().addToRequestQueue(getRecord);
    }


    private void init() {
        lv_account=(ListView)findViewById(R.id.recharge_account_lv);
        mData = new ArrayList<ConsumeData>();
        img_back= (ImageView) findViewById(R.id.consume_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
