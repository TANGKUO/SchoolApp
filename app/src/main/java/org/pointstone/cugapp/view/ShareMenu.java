package org.pointstone.cugapp.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.AboutActivity;
import org.pointstone.cugapp.activities.DrawerActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import razerdp.basepopup.BasePopupWindow;

import static org.pointstone.cugapp.CugApplication.api;
import static org.pointstone.cugapp.CugApplication.mTencent;


/**
 * Created by Administrator on 2017/2/28.
 */

public class ShareMenu extends BasePopupWindow implements View.OnClickListener {
    private View popupView;

    public ShareMenu(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.share_menu, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
            popupView.findViewById(R.id.tx_4).setOnClickListener(this);
            popupView.findViewById(R.id.tx_5).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1: {
                final ProgressDialog pd = ProgressDialog.show(DrawerActivity.getInstannce(), "", "请求中......");
                pd.setCancelable(true);
                pd.show();
                String infourl = "https://api.cugapp.com/public_api/CugApp/check_update?platform=android";
                JsonObjectRequest getInfo = new JsonObjectRequest(infourl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                String data = response.getString("data");
                                Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                                Matcher matcher = pattern.matcher(data);
                                int i = 0;
                                while (matcher.find()) {
                                    if (i == 2) {
                                        String json = "{" + matcher.group(1) + "}";
                                        JSONObject d = new JSONObject(json);
                                        final String title = d.getString("title");
                                        final String description = d.getString("description");
                                        final String img = d.getString("thumbnail");
                                        final String link = d.getString("link");
                                        ImageRequest imageRequest = new ImageRequest(
                                                img,
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        WXWebpageObject webpage = new WXWebpageObject();
                                                        webpage.webpageUrl = link;
                                                        WXMediaMessage msg = new WXMediaMessage(webpage);
                                                        msg.title = title;
                                                        msg.description = description;
                                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                        response.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                                                        byte[] b = stream.toByteArray();
                                                        msg.thumbData = b;

                                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                                        req.transaction = buildTransaction("webpage");
                                                        req.message = msg;
                                                        req.scene = SendMessageToWX.Req.WXSceneTimeline;

                                                        api.sendReq(req);
                                                        pd.dismiss();

                                                    }
                                                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        });
                                        CugApplication.getInstance().addToRequestQueue(imageRequest);

                                    }
                                    // Finance_jz jz = new Finance_jz(d.getString("酬金类型"), d.getString("应发数"), d.getString("实际发放"), d.getString("酬金说明"));
                                    //dbgradeManager.delete(grade);
                                    i++;
                                    //GradeList.add(grade);
                                }

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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

                    }
                };
                CugApplication.getInstance().addToRequestQueue(getInfo);
                // 初始化一个WXTextObject对象，并填写分享的内容
                this.dismiss();
                break;
            }

            case R.id.tx_2: {
                final ProgressDialog pd = ProgressDialog.show(DrawerActivity.getInstannce(), "", "请求中......");
                pd.setCancelable(true);
                pd.show();
                String infourl = "https://api.cugapp.com/public_api/CugApp/check_update?platform=android";
                JsonObjectRequest getInfo = new JsonObjectRequest(infourl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                String data = response.getString("data");
                                Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                                Matcher matcher = pattern.matcher(data);
                                int i = 0;
                                while (matcher.find()) {
                                    if (i == 1) {
                                        String json = "{" + matcher.group(1) + "}";
                                        json = json.substring(json.indexOf(':') + 1);
                                        JSONObject d = new JSONObject(json);
                                        final String title = d.getString("title");
                                        final String description = d.getString("description");
                                        final String img = d.getString("thumbnail");
                                        final String link = d.getString("link");
                                        ImageRequest imageRequest = new ImageRequest(
                                                img,
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        WXWebpageObject webpage = new WXWebpageObject();
                                                        webpage.webpageUrl = link;
                                                        WXMediaMessage msg = new WXMediaMessage(webpage);
                                                        msg.title = title;
                                                        msg.description = description;
                                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                        response.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                                                        byte[] b = stream.toByteArray();
                                                        msg.thumbData = b;

                                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                                        req.transaction = buildTransaction("webpage");
                                                        req.message = msg;
                                                        req.scene = SendMessageToWX.Req.WXSceneSession;

                                                        api.sendReq(req);
                                                        pd.dismiss();

                                                    }
                                                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        });
                                        CugApplication.getInstance().addToRequestQueue(imageRequest);

                                    }
                                    // Finance_jz jz = new Finance_jz(d.getString("酬金类型"), d.getString("应发数"), d.getString("实际发放"), d.getString("酬金说明"));
                                    //dbgradeManager.delete(grade);
                                    i++;
                                    //GradeList.add(grade);
                                }

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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

                    }
                };
                CugApplication.getInstance().addToRequestQueue(getInfo);
                // 初始化一个WXTextObject对象，并填写分享的内容
                this.dismiss();
                break;
            }

            case R.id.tx_3:
                this.dismiss();
                break;
            case R.id.tx_4:
                final ProgressDialog pd = ProgressDialog.show(DrawerActivity.getInstannce(), "", "请求中......");
                pd.setCancelable(true);
                pd.show();
                String infourl = "https://api.cugapp.com/public_api/CugApp/check_update?platform=android";
                JsonObjectRequest getInfo = new JsonObjectRequest(infourl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                String data = response.getString("data");
                                Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                                Matcher matcher = pattern.matcher(data);
                                int i = 0;
                                while (matcher.find()) {
                                    if (i == 2) {
                                        String json = "{" + matcher.group(1) + "}";
                                        JSONObject d = new JSONObject(json);
                                        final String title = d.getString("title");
                                        final String description = d.getString("description");
                                        final String img = d.getString("thumbnail");
                                        final String link = d.getString("link");
                                        final Bundle params;
                                        params = new Bundle();
                                        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                                        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 标题
                                        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);// 摘要
                                        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,link);// 内容地址
                                        ArrayList<String> imgUrlList = new ArrayList<>();
                                        imgUrlList.add(img);
                                        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imgUrlList);// 图片地址
                                        // 　// 分享操作要在主线程中完成
                                        // 分享操作要在主线程中完成
                                        ThreadManager.getMainHandler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mTencent.shareToQzone(AboutActivity.getInstance(), params, new BaseUiListener());
                                                pd.dismiss();
                                            }
                                        });
                                    }
                                    // Finance_jz jz = new Finance_jz(d.getString("酬金类型"), d.getString("应发数"), d.getString("实际发放"), d.getString("酬金说明"));
                                    //dbgradeManager.delete(grade);
                                    i++;
                                    //GradeList.add(grade);
                                }

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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

                    }
                };
                CugApplication.getInstance().addToRequestQueue(getInfo);
                this.dismiss();
                break;


            case R.id.tx_5:
            final ProgressDialog pd2 = ProgressDialog.show(DrawerActivity.getInstannce(), "", "请求中......");
            pd2.setCancelable(true);
            pd2.show();
            String infourl2 = "https://api.cugapp.com/public_api/CugApp/check_update?platform=android";
            JsonObjectRequest getInfo2 = new JsonObjectRequest(infourl2, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean status = response.getBoolean("status");
                        if (status) {
                            String data = response.getString("data");
                            Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                            Matcher matcher = pattern.matcher(data);
                            int i = 0;
                            while (matcher.find()) {
                                if (i == 1) {
                                    String json = "{" + matcher.group(1) + "}";
                                    json = json.substring(json.indexOf(':') + 1);
                                    JSONObject d = new JSONObject(json);
                                    final String title = d.getString("title");
                                    final String description = d.getString("description");
                                    final String img = d.getString("thumbnail");
                                    final String link = d.getString("link");
                                    final Bundle params;
                                    params = new Bundle();
                                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                                    params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 标题
                                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);// 摘要
                                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, link);// 内容地址　　
                                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "我的地质锤");// 应用名称
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, img);
                                    params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
                                    // 分享操作要在主线程中完成
                                    ThreadManager.getMainHandler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTencent.shareToQQ(AboutActivity.getInstance(), params, new BaseUiListener());
                                            pd2.dismiss();
                                        }
                                    });
                                }
                                // Finance_jz jz = new Finance_jz(d.getString("酬金类型"), d.getString("应发数"), d.getString("实际发放"), d.getString("酬金说明"));
                                //dbgradeManager.delete(grade);
                                i++;
                                //GradeList.add(grade);
                            }

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


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

                }
            };
            CugApplication.getInstance().addToRequestQueue(getInfo2);
            this.dismiss();
            break;

            default:
                break;
        }

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void shareToQQ() {

    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError e) {


        }

        @Override
        public void onCancel() {


        }


    }
}
