package org.pointstone.cugapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.adapter.Finance_jz_ListAdapter;
import org.pointstone.cugapp.utils.Finance_jz;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luckysonge on 2016/12/20.
 */

public class Finance_jz_Fragment extends Fragment {

    private ProgressDialog pd;
    private ListView jzListView;
    private final List<Finance_jz> jzListData = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View jzView = inflater.inflate(R.layout.fragment_finance_jz, null);
        jzListView = (ListView) jzView.findViewById(R.id.finance_jz_lv);
        pd = ProgressDialog.show(getActivity(), "查询中", "请稍后......");
        getjz();
        return jzView;
    }

    public void getjz() {
        String getjzUrl = "https://api.cugapp.com/public_api/CugApp/reward_money?unionid=" + InformationShared.getString("unionid") + "&password=" + InformationShared.getString("id");
        JsonObjectRequest getJZJson = new JsonObjectRequest(getjzUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {

                        String data = response.getString("data");
                        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                        Matcher matcher = pattern.matcher(data);
                        while (matcher.find()) {
                            String json = "{" + matcher.group(1) + "}";
                            JSONObject d = new JSONObject(json);
                            //OwnToast.Short(d.getString("酬金类型"));
                            Finance_jz jz = new Finance_jz(d.getString("酬金类型"), d.getString("应发数"), d.getString("实际发放"), d.getString("酬金说明"));
                            //dbgradeManager.delete(grade);
                            jzListData.add(jz);
                            //GradeList.add(grade);
                        }
                        //OwnToast.Short("奖助get√");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    //网络异常
                    OwnToast.Short("网络异常");
                } else if (error instanceof ServerError) {
                    //服务器响应错误
                    //OwnToast.Short("服务器响应错误");
                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("查询失败，请稍后再试")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }).show();
                } else if (error instanceof AuthFailureError) {
                    //请求时验证凭证失败
                    OwnToast.Short("请求时验证凭证失败");
                } else if (error instanceof ParseError) {
                    //解析服务器返回数据错误
                    OwnToast.Short("解析服务器返回数据错误");
                } else if (error instanceof NoConnectionError) {
                    //网络请求无任何连接
                    OwnToast.Short("网络请求无任何连接");
                } else if (error instanceof TimeoutError) {
                    //超时
                    OwnToast.Short("查询超时,网络连接较慢");
                }else {
                    //成功
                    OwnToast.Short("error");
                }

            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-API-KEY", CugApplication.X_API_KEY);
                return headers;
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                if (pd != null) {
                    setjz();
                    pd.dismiss();
                }
            }
        };
        //因为获取奖助金时间太长，需要修改超时检测
        int socketTimeout = 5000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getJZJson.setRetryPolicy(policy);
        CugApplication.getInstance().addToRequestQueue(getJZJson);
    }

    public void setjz() {
        Finance_jz_ListAdapter jz_listAdapter=new Finance_jz_ListAdapter(getActivity(),jzListData);
        jzListView.setAdapter(jz_listAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // MobclickAgent.onPageStart("Grade"); //统计页面，"MainScreen"为页面名称，可自定义

        TCAgent.onPageStart(getActivity(), "jzFragment");

    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageStart("Grade"); //统计页面，"MainScreen"为页面名称，可自定义

        TCAgent.onPageEnd(getActivity(), "jzFragment");

    }
}

