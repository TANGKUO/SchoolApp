package org.pointstone.cugapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.fragments.LoginFragment;
import org.pointstone.cugapp.utils.Version;
import org.pointstone.cugapp.view.ShareMenu;

import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static org.pointstone.cugapp.CugApplication.mTencent;

/**
 * Created by Administrator on 2017/1/6.
 */

public class AboutActivity extends SwipeBackAppCompatActivity {

    LinearLayout linearLayout;
    Toolbar mToolbar;
    private static AboutActivity aboutActivity; //Activity实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        linearLayout = (LinearLayout) findViewById(R.id.about_layout);
        aboutActivity=this;
        Element update = new Element();
        update.setTitle("检查更新(v"+ Version.getVersionName(this)+")");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd;
                pd=ProgressDialog.show(AboutActivity.this,"","请稍后......");
                pd.show();
                String infourl="https://api.cugapp.com/public_api/CugApp/check_update?platform=android";
                JsonObjectRequest getInfo=new JsonObjectRequest(infourl,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status)
                            {
                                String data=response.getString("data");
                                JSONObject d=new JSONObject(data.substring(1,data.length()-1));
                                final String url=d.getString("url");
                                String description=d.getString("description");
                                int version=Integer.parseInt(d.getString("version_code"));
                                if(Integer.parseInt(Version.getVersionCode(AboutActivity.this))<version)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                                    builder.setMessage(description);

                                    builder.setTitle("有新版本，是否现在去更新？");

                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Uri uri = Uri.parse(url);
                                            startActivity(new Intent(Intent.ACTION_VIEW,uri));
                                        }
                                    });

                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    builder.create().show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                                    builder.setMessage(description);

                                    builder.setTitle("您的版本已经是最新了哦");

                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });



                                    builder.create().show();
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
                CugApplication.getInstance().addToRequestQueue(getInfo);

            }

        });

        final Element AboutUs = new Element();
        AboutUs.setTitle("关于点石");
        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("url",getString(R.string.pointstone_html));

                startActivity(intent);
            }
        });
        Element function = new Element();
        function.setTitle("功能介绍");
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("url",getString(R.string.function_html));

                startActivity(intent);
            }
        });

        Element people=new Element();
        people.setTitle("参与人员");
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("url",getString(R.string.people_html));

                startActivity(intent);
            }
        });
        Element share = new Element();
        share.setTitle("分享应用");
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareMenu popup = new ShareMenu(AboutActivity.this);
                popup.showPopupWindow();
            }
        });
        View aboutPage;
        if(LoginFragment.isWeixinAvilible(this)){
           aboutPage = new AboutPage(this)
                    .isRTL(false)
                    .setImage(R.mipmap.ic_launcher)
                    .addItem(function)
                    .addItem(people)
                    .addItem(AboutUs)
                    .addItem(share)
                    .addItem(update)

                    .setDescription(getString(R.string.app_name) )
                    .create();
        }else{
            aboutPage = new AboutPage(this)
                    .isRTL(false)
                    .setImage(R.mipmap.ic_launcher)
                    .addItem(function)
                    .addItem(people)
                    .addItem(AboutUs)

                    .addItem(update)
                    .setDescription(getString(R.string.app_name) )
                    .create();
        }

        linearLayout.addView(aboutPage);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        initToolbar();


    }

    /**
     * 选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar.setTitle("关于我们");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.setting_back);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static Activity getInstance(){
        return aboutActivity;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }
}
