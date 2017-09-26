package org.pointstone.cugapp;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVOSCloud;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tendcloud.tenddata.TCAgent;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by Administrator on 2016/12/1.
 */

public class CugApplication extends MultiDexApplication {

    //有微信开发平台提供
    public static final String APP_ID="wx132b798d022ff3d9";
    public static String SECRET="eb6e0e2f1d185cc747f66e8fe7b68f8d";
    public static String hand_pass;
    public static  String X_API_KEY="k0kk44kccw4k04kos8w0s8gw88g84wwk04ocsgs4";
    public static CugApplication application;
    public static IWXAPI api;

    public static Tencent mTencent;
    private RequestQueue mRequestQueue;
    public static final String TAG = CugApplication.class
            .getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        TCAgent.LOG_ON=true;
        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(true); //开启自动上传错误

        application=this;
        api= WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);

        if(InformationShared.getInt("status")==1)
            GradeYear.setCurrentWeek(GradeYear.getCurrentGrade());


        GradeYear.setSummerWinter();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"Sjs3i37sI2vsgr3JUrqJF3Wq-gzGzoHsz","0PhCLwNQWOlisurMiTEqxKd9");
      //  InformationShared.setInt("version",10);

        //友盟
      //  MobclickAgent.openActivityDurationTrack(false);
      //  MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);




        //阿里百川用户反馈
        FeedbackAPI.init(this, "23590122");


        //云巴推送
        YunBaManager.setThirdPartyEnable(getApplicationContext(), true);
        YunBaManager.setXMRegister("2882303761517551551","5701755169551");
        YunBaManager.start(getApplicationContext());
      //  if(InformationShared.getInt("Topic")==0){
            YunBaManager.subscribe(getApplicationContext(), new String[]{"CUGAPP_PUBLIC","CUGAPP_PUBLIC_ANDROID"}, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken arg0) {
                    Log.d(TAG, "Subscribe topic succeed");
           //         InformationShared.setInt("Topic",1);
                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    Log.d(TAG, "Subscribe topic failed") ;
                }
            });

     //   }


        MQConfig.init(this, "6159b2ce48262af6600ec908dd82c10c", new OnInitCallback() {
            @Override
            public void onSuccess(String clientId) {
                MQConfig.isShowClientAvatar=true;
            }

            @Override
            public void onFailure(int code, String message) {

            }
        });


    mTencent=Tencent.createInstance("1105528829", this.getApplicationContext());
    }
    public static CugApplication getInstance() {
        return application;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
