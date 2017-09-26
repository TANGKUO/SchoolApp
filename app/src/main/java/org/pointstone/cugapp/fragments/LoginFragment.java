package org.pointstone.cugapp.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;
import org.pointstone.cugapp.wxapi.WXEntryActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by Administrator on 2016/12/1.
 */

public class LoginFragment extends Fragment {
    Button loginBtn;
    public static ProgressDialog pd;
    Button loginCommitBtn;
    EditText usernameEt;
    EditText passwordEt;
    String username;
    String password;
    public static final String TAG = WXEntryActivity.class
            .getSimpleName();
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      //  new NukeSSLCerts().nuke();
        if(!isWeixinAvilible(getActivity())|| InformationShared.getInt("isWeixin")==1){
            InformationShared.setInt("isWeixin",0);
            view = inflater.inflate(R.layout.fragment_login_commit, container, false);
            loginCommitBtn= (Button) view.findViewById(R.id.login_commit_btn);
            usernameEt= (EditText) view.findViewById(R.id.et_username);
            passwordEt= (EditText) view.findViewById(R.id.et_password);
            loginCommitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username=usernameEt.getText().toString();
                    password=passwordEt.getText().toString();
                    if(username.equals("")||password.equals("")){
                        OwnToast.Long("请填写用户名或密码！");
                    }else{
                        pd=ProgressDialog.show(getActivity(),"正在请求","请稍后......");

                        String infourl="https://api.cugapp.com/public_api/CugApp/userinfo_for_password?username="+username+"&password="+password;
                        JsonObjectRequest getInfo=new JsonObjectRequest(infourl, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    boolean status = response.getBoolean("status");
                                    if(status)
                                    {
                                        InformationShared.setInt("login", 1);
                                        String data=response.getString("data");
                                        JSONObject d=new JSONObject(data.substring(1,data.length()-1));
                                        String id=d.getString("student_or_staff_id");
                                        String name=d.getString("name");
                                        String unionid=d.getString("wechat_unionid");
                                        InformationShared.setString("id",id);
                                        InformationShared.setString("name",name);
                                        String phone=d.getString("mobile_phone_number");
                                        InformationShared.setString("mobile_phone_number",phone);
                                        InformationShared.setInt("status",1);
                                        InformationShared.setString("unionid", unionid);
                                        //  if(InformationShared.getInt("Alias")==0&&!InformationShared.getString("unionid").equals("0")) {
                                        if(!InformationShared.getString("unionid").equals("0")) {
                                            String md5=DrawerActivity.Md5(InformationShared.getString("unionid"));
                                            if(md5!=null)
                                            {
                                                YunBaManager.setAlias(DrawerActivity.getInstannce(), md5,
                                                        new IMqttActionListener() {
                                                            @Override
                                                            public void onSuccess(IMqttToken asyncActionToken) {
                                                                //  DemoUtil.showToast("success", getApplicationContext());
                                                                //  InformationShared.setInt("Alias", 1);
                                                            }

                                                            @Override
                                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                                                if (exception instanceof MqttException) {
                                                                    MqttException ex = (MqttException) exception;
                                                                    // String msg =  "setAlias failed with error code : " + ex.getReasonCode();
                                                                    //  DemoUtil.showToast(msg, getApplicationContext());
                                                                }
                                                            }
                                                        }
                                                );
                                            }

                                        }
                                        boolean is_team_member=d.getBoolean("is_team_member");

                                        if(is_team_member){
                                            InformationShared.setInt("is_team_member",1);
                                        }

                                        boolean is_betatest_member=d.getBoolean("is_betatest_member");

                                      /*  if (is_betatest_member){
                                            String  group=d.getString("betatest_group");
                                            group=group.substring(1,group.length()-1);
                                            String []G=group.split(",");

                                            for(String g :G){
                                                if(g.equals("\"Android\"")){
                                                    InformationShared.setInt("is_betatest_member",1);
                                                }
                                            }

                                        }*/
                                        if (is_betatest_member){
                                            InformationShared.setInt("is_betatest_member",1);
                                        }
                                        DrawerActivity.getInstannce().refresh();
                                    }else
                                    {
                                        DrawerActivity.getInstannce().refresh();
                                    }
                                } catch (JSONException e) {
                                    DrawerActivity.getInstannce().refresh();
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();
                                if(error instanceof ServerError){
                                    DrawerActivity.getInstannce().refresh();
                                }else{
                                    String str=error.getMessage();
                                    OwnToast.Long("连接失败，请稍后重试");
                                }
                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws
                                    AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("X-API-KEY", CugApplication.X_API_KEY);
                                return  headers;
                            }
                        };

                        CugApplication.getInstance().addToRequestQueue(getInfo);
                    }
                }
            });
        }else{
            view = inflater.inflate(R.layout.fragment_login, container, false);
            loginBtn= (Button) view.findViewById(R.id.login_btn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pd=ProgressDialog.show(getActivity(),"正在请求","请稍后......");
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "carjob_wx_login";
                    CugApplication.api.sendReq(req);


                }
            });
            loginBtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    InformationShared.setInt("isWeixin",1);
                    DrawerActivity.getInstannce().refresh();InformationShared.setInt("isWeixin",1);
                    return true;
                }
            });

        }

        return view;
    }
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
}
