package org.pointstone.cugapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import org.pointstone.cugapp.adapter.LibraryCardAdapter;
import org.pointstone.cugapp.bean.Book;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keyboy on 2017/1/2.
 */

public class LibraryHistoryFragment extends Fragment {

    private ProgressDialog pd;
    private RecyclerView lv_history;
    private List<Book> data = new ArrayList<Book>();
    private LibraryCardAdapter libraryCardAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_library_history, null);
        lv_history= (RecyclerView) rootView.findViewById(R.id.library_history_lv);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        lv_history.setHasFixedSize(true);
        lv_history.setLayoutManager(layoutManager);
        pd= ProgressDialog.show(getActivity(),"查询中","请稍后......");
        getBookHistory();
        return rootView;
    }

    //查询图书历史借阅函数
    public void getBookHistory()
    {
        CugApplication.getInstance().getRequestQueue().cancelAll("My Tag_BookHistory");
        String url_balance="https://api.cugapp.com/public_api/CugApp/history_books?unionid="+ InformationShared.getString("unionid")+"&password="+InformationShared.getString("library_password");
        System.out.println(url_balance);
        JsonObjectRequest balance=new JsonObjectRequest(url_balance,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if(status) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            response = (JSONObject) jsonArray.get(i);
                            Book book=new Book();
                            book.setName(((String)response.getString("书名")));
                            book.setId(((String)response.getString("条码号")));
                            book.setLend_time(((String)response.getString("借阅日期")));
                            book.setReturn_time(((String)response.getString("归还日期")));
                            data.add(book);

                        }

                    }

                    else{
                        if(response.getString("message").equals("密码错误")||response.getString("message").equals("无法登录")) {
                            OwnToast.Short("密码错误，请重新登录");

                        }
                        OwnToast.Short("获取历史借阅图书信息失败");
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
                    OwnToast.Short("查询历史借阅超时");
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
                libraryCardAdapter =new LibraryCardAdapter(data,getActivity(),2);
                lv_history.setAdapter(libraryCardAdapter);
            }

        };
        balance.setTag("My Tag_BookHistory");
        CugApplication.getInstance().addToRequestQueue(balance);
    }

    //显示借阅历史详情函数
    public void historyDetail(String data) {

        String[] temp=data.split("\n");
        String bookName=temp[0];
        String borrowTime=temp[1];
        String returnTime=temp[2];
        String code=temp[3];
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.logo)
                .setTitle("借阅详情")
                .setMessage(
                        "借阅书名:  " + bookName + "\n\n"
                                + "借阅时间:  " + borrowTime + "\n\n"
                                + "归还时间:  " + returnTime+ "\n\n"
                                + "条行码号:  " + code+ "\n\n")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setNegativeButton("取消", null).create().show();
    }
}
