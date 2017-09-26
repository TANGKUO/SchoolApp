package org.pointstone.cugapp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.avos.avoscloud.signature.Base64Encoder;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.pointstone.cugapp.fragments.LoginFragment.pd;

/**
 * Created by Administrator on 2016/12/1.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private String CODE;
    private String urlData;
    private String TOKEN;

    public static final String TAG = WXEntryActivity.class
            .getSimpleName();

    private void handleIntent(Intent paramIntent) {
        CugApplication.api.handleIntent(paramIntent, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    public void onReq(BaseReq arg0) {
        finish();
    }

    @Override

    public void onResp(BaseResp baseResp) {
        finish();
     //   new NukeSSLCerts().nuke();
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            CODE = ((SendAuth.Resp) baseResp).code;
            urlData = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=" + CugApplication.APP_ID +
                    "&secret=" + CugApplication.SECRET +
                    "&code=" + CODE +
                    "&grant_type=authorization_code";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(urlData, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                TOKEN = response.getString("access_token");
                                urlData = "https://api.weixin.qq.com/sns/userinfo?" +
                                        "access_token=" + TOKEN +
                                        "&openid=" + CugApplication.APP_ID;
                                JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(urlData, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    String name = response.getString("nickname");
                                                    String unionid="";
                                                    try{
                                                        unionid = response.getString("unionid");
                                                        InformationShared.setString("unionid", unionid);
                                                    }catch (Exception e)
                                                    {

                                                    }

                                                   // OwnToast.Long(uinonid);
                                                    InformationShared.setString("nickname", name);
                                                    InformationShared.setInt("login", 1);
                                                    String imageurl = response.getString("headimgurl");
                                                    InformationShared.setString("headimgurl", imageurl);
                                                    ImageRequest imageRequest = new ImageRequest(
                                                            imageurl,
                                                            new Response.Listener<Bitmap>() {
                                                                @Override
                                                                public void onResponse(Bitmap response) {

                                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                                    response.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                                                                    byte[] b = stream.toByteArray();
                                                                    // 将图片流以字符串形式存储下来
                                                                    String tp = new String(Base64Encoder.encode(b));
                                                                    InformationShared.setString("headimage", tp);
                                                                    String infourl="https://api.cugapp.com/public_api/CugApp/userinfo_for_wechat_unionid?unionid="+InformationShared.getString("unionid");

                                                                  //  HttpsTrustManager.allowAllSSL();
                                                                //    new NukeSSLCerts().nuke();

                                                                 //   JsonObjectRequest getInfo=new JsonObjectRequest(infourl,null, new Response.Listener<JSONObject>() {
                                                                    JsonObjectRequest getInfo=new JsonObjectRequest(Request.Method.GET,infourl, new Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {

                                                                            try {
                                                                                boolean status = response.getBoolean("status");
                                                                                if(status)
                                                                                {

                                                                                    String data=response.getString("data");
                                                                                    JSONObject d=new JSONObject(data.substring(1,data.length()-1));
                                                                                    String id=d.getString("student_or_staff_id");
                                                                                    String phone=d.getString("mobile_phone_number");
                                                                                    InformationShared.setString("mobile_phone_number",phone);
                                                                                    String name=d.getString("name");
                                                                                    InformationShared.setString("id",id);
                                                                                    InformationShared.setString("name",name);
                                                                                    InformationShared.setInt("status",1);


                                                                                    boolean is_team_member=d.getBoolean("is_team_member");
                                                                                    if(is_team_member){
                                                                                        InformationShared.setInt("is_team_member",1);
                                                                                    }

                                                                                    boolean is_betatest_member=d.getBoolean("is_betatest_member");

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
                                                                                OwnToast.Long("连接失败，请稍后重试");
                                                                            }
                                                                        }
                                                                    }){
                                                                        @Override
                                                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                                            HashMap<String, String> headers = new HashMap<String, String>();
                                                                            headers.put("X-API-KEY", CugApplication.X_API_KEY);
                                                                            return  headers;
                                                                        }
                                                                    };
                                                                    CugApplication.getInstance().addToRequestQueue(getInfo);

                                                                }
                                                            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                        }
                                                    });
                                                    CugApplication.getInstance().addToRequestQueue(imageRequest);


                                                } catch (Exception e) {

                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                    }
                                });
                                CugApplication.getInstance().addToRequestQueue(jsonObjReq2);

                            } catch (Exception e) {
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });
            CugApplication.getInstance().addToRequestQueue(jsonObjReq);
        }else
        {
            DrawerActivity.getInstannce().refresh();
        }

    }
}
