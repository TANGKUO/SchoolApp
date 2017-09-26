package org.pointstone.cugapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.fragments.CourseDayFragment;
import org.pointstone.cugapp.fragments.CourseFragment;
import org.pointstone.cugapp.fragments.ExamFragmentc;
import org.pointstone.cugapp.fragments.FinanceFragment;
import org.pointstone.cugapp.fragments.GradeFragmentc;
import org.pointstone.cugapp.fragments.HintFragment;
import org.pointstone.cugapp.fragments.LibraryFragment;
import org.pointstone.cugapp.fragments.LoginFragment;
import org.pointstone.cugapp.fragments.RechargeFragment;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.LibraryPw;
import org.pointstone.cugapp.utils.OwnToast;
import org.pointstone.cugapp.utils.Version;

import java.util.HashMap;
import java.util.Map;

import io.yunba.android.manager.YunBaManager;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //   private Toolbar toolbar;
    private TextView nameTV;
    private ImageView headIv;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private long mExitTime = 0;
    private static DrawerActivity drawerActivity; //Activity实例
    public static final String TAG = DrawerActivity.class
            .getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerActivity = this;

        //setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        String ben=InformationShared.getString("id");
        if(!ben.equals("0")){
            String  Ben = ben.substring(0, 2);
            if (!Ben.equals("20")) {
                MenuItem menuItem3 = navigationView.getMenu().findItem(R.id.schedule_nav);
                menuItem3.setVisible(false);	// true 为显示，false 为隐藏
                MenuItem menuItem4 = navigationView.getMenu().findItem(R.id.grade_nav);
                menuItem4.setVisible(false);	// true 为显示，false 为隐藏
                MenuItem menuItem5 = navigationView.getMenu().findItem(R.id.exam_nav);
                menuItem5.setVisible(false);	// true 为显示，false 为隐藏
            }
        }


        View headView = navigationView.getHeaderView(0);
        nameTV = (TextView) headView.findViewById(R.id.name_tv);
        headIv = (ImageView) headView.findViewById(R.id.head_iv);


        //根据登录状态初始化界面
        int login = InformationShared.getInt("login");
        if (login == 0) {
            getFragmentManager().beginTransaction().replace(R.id.content_layout, new LoginFragment())
                    .commit();//初始界面
        } else {
          //  if(InformationShared.getInt("Alias")==0&&!InformationShared.getString("unionid").equals("0")) {
            if(!InformationShared.getString("unionid").equals("0")) {
                String md5=Md5(InformationShared.getString("unionid"));
                if(md5!=null)
                {
                    YunBaManager.setAlias(getApplicationContext(), md5,
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
            int status = InformationShared.getInt("status");
            if (status == 1) {
                String isBen = InformationShared.getString("id").substring(0, 2);
                if (isBen.equals("20")) {

                    if (InformationShared.getInt("isDay") == 0) {
                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new CourseFragment())
                                .commit();//初始界面
                    } else {
                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new CourseDayFragment())
                                .commit();//初始界面
                    }

                    //        toolbar.setTitle("课程表");

                } else {
                    getFragmentManager().beginTransaction().replace(R.id.content_layout, new LibraryFragment())
                            .commit();//初始界面
                }
                //       toolbar.setTitle("课程表");
                nameTV.setText(InformationShared.getString("name"));
                String tp = InformationShared.getString("headimage");
                if (!tp.equals("0")) {
                    byte[] bytes = Base64.decode(tp, Base64.DEFAULT);
                    Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    headIv.setImageBitmap(dBitmap);
                }
            } else {
                Intent intent=new Intent(DrawerActivity.this,WebActivity.class);
                intent.putExtra("url",getString(R.string.hint_html));

                startActivity(intent);
                nameTV.setText(InformationShared.getString("nickname"));
                String tp = InformationShared.getString("headimage");
                if (!tp.equals("0")) {
                    byte[] bytes = Base64.decode(tp, Base64.DEFAULT);
                    Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    headIv.setImageBitmap(dBitmap);
                }

            }

        }

        /**设置MenuItem的字体,图标颜色**/

        navigationView.setNavigationItemSelectedListener(this);
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.nav_menu_text_color);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList((ColorStateList) resource.getColorStateList(R.color.nav_menu_icon_color));
        // navigationView.setBackgroundColor(getResources().getColor(R.color.black)); //设置背景色


        /**设置MenuItem默认选中项**/
        navigationView.getMenu().getItem(0).setChecked(true);

       // if (InformationShared.getInt("login") == 1)
          //  drawer.openDrawer(GravityCompat.START);
        //图片本身颜色
        // navigationView.setItemIconTintList(null);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this) ;
        if(prefs.getBoolean("new",false) ){
            String infourl = "https://api.cugapp.com/public_api/CugApp/check_update?platform=android";
            JsonObjectRequest getInfo = new JsonObjectRequest(infourl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean status = response.getBoolean("status");
                        if (status) {
                            String data = response.getString("data");
                            JSONObject d = new JSONObject(data.substring(1, data.length() - 1));
                            final String url = d.getString("url");
                            String description = d.getString("description");
                            int version = Integer.parseInt(d.getString("version_code"));
                            int nowversion=Integer.parseInt(Version.getVersionCode(DrawerActivity.this));
                            if (nowversion< version) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
                                builder.setMessage(description);

                                builder.setTitle("有新版本，是否现在去更新？");

                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Uri uri = Uri.parse(url);
                                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                                    }
                                });

                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
                    VolleyLog.d(TAG, "Error: " + error.getMessage());


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
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
      /*  int id2 = item.getItemId();
        if (id2 == R.id.root_nav) {
            InformationShared.setString("id", "20141002570");
            InformationShared.setString("id","20161003192");
            InformationShared.setString("name", "王亮");
            InformationShared.setString("nickname", "随风丶亮");
            InformationShared.setInt("status", 1);
            InformationShared.setInt("login", 1);
            InformationShared.setString("unionid", "otrNdt4WzWYhfp7CJlErU4M6mH24");  //王亮
            //     InformationShared.setString("unionid", "otrNdtxLanVAFRJs7g8xAQuFC17k");  //尤悦
              InformationShared.setString("unionid", "otrNdt8RIw2f7fDZDzVrA3gFilsE");  //杨靖仪
            refresh();
            return true;
        }*/
        boolean isActivity = false;

        if (InformationShared.getInt("login") == 0) {
            int ID = item.getItemId();
             if(ID==R.id.about_nav) {
                Intent intent = new Intent(DrawerActivity.this, AboutActivity.class);
                startActivity(intent);
                isActivity = true;
                //       toolbar.set
            }
        }

        if (InformationShared.getInt("login") == 1) {
            int id = item.getItemId();

            if (id == R.id.grade_nav) {
                if (InformationShared.getInt("status") == 1) {
                    getFragmentManager().beginTransaction().replace(R.id.content_layout, new GradeFragmentc())
                            .commit();//初始界面
                    //       toolbar.setTitle("成绩");
                } else {
               //     getFragmentManager().beginTransaction().replace(R.id.content_layout, new HintFragment())
              //              .commit();//初始界面
                    Intent intent=new Intent(DrawerActivity.this,WebActivity.class);
                    intent.putExtra("url",getString(R.string.hint_html));

                    startActivity(intent);
                    //       toolbar.setTitle("成绩");
                }

                // Handle the camera action
//            }else if(id == R.id.gradetest){
//                if (InformationShared.getInt("status") == 1) {
//                    getFragmentManager().beginTransaction().replace(R.id.content_layout, new GradeFragment())
//                            .commit();//初始界面
//                    //       toolbar.setTitle("成绩");
//                } else {
//                    getFragmentManager().beginTransaction().replace(R.id.content_layout, new HintFragment())
//                            .commit();//初始界面
//                    //       toolbar.setTitle("成绩");
//                }

            } else if (id == R.id.schedule_nav) {
                if (InformationShared.getInt("status") == 1) {
                    String isBen = InformationShared.getString("id").substring(0, 2);
                    if (isBen.equals("20")) {

                        if (InformationShared.getInt("isDay") == 0) {
                            getFragmentManager().beginTransaction().replace(R.id.content_layout, new CourseFragment())
                                    .commit();//初始界面
                        } else {
                            getFragmentManager().beginTransaction().replace(R.id.content_layout, new CourseDayFragment())
                                    .commit();//初始界面
                        }

                        //        toolbar.setTitle("课程表");

                    } else {
                        OwnToast.Long("对不起，课表只支持本科生");
                    }

                } else {
                    Intent intent=new Intent(DrawerActivity.this,WebActivity.class);
                    intent.putExtra("url",getString(R.string.hint_html));

                    startActivity(intent);
                    //   toolbar.setTitle("课程表");
                }


            }
          else if (id == R.id.charge_nav) {

                if (InformationShared.getInt("status") == 1) {
                    if (InformationShared.getString("set_hand_pass").equals("1")) {
                        Intent intent = new Intent(this, GestureLockActivity.class);
                        startActivityForResult(intent, 0);

                    } else {
                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new RechargeFragment())
                                .commit();//初始界面
                    }

                    //  toolbar.setTitle("充值");
                } else {

                    getFragmentManager().beginTransaction().replace(R.id.content_layout, new HintFragment())
                            .commit();//初始界面
                }

                //  toolbar.setTitle("充值");


            }
            else if (id == R.id.exam_nav) {
                if (InformationShared.getInt("status") == 1) {
                    getFragmentManager().beginTransaction().replace(R.id.content_layout, new ExamFragmentc())
                            .commit();//初始界面
                    //   toolbar.setTitle("考试安排");
                } else {
                    Intent intent=new Intent(DrawerActivity.this,WebActivity.class);
                    intent.putExtra("url",getString(R.string.hint_html));

                    startActivity(intent);
                    //   toolbar.setTitle("考试安排");
                }
            } else if (id == R.id.library_nav) {

                if (InformationShared.getInt("status") == 1) {
                    if (InformationShared.getString("library_password").equals("0")) {
                        Intent intent = new Intent(DrawerActivity.this, LibraryPw.class);
                        startActivity(intent);
                        finish();

//                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new LibraryFragment()).commit();//初始界面
                    } else
                        getFragmentManager().beginTransaction().replace(R.id.content_layout, new LibraryFragment()).commit();//初始界面

                } else {
                    Intent intent=new Intent(DrawerActivity.this,WebActivity.class);
                    intent.putExtra("url",getString(R.string.hint_html));

                    startActivity(intent);
                }
            } else if (id == R.id.finance_nav) {
                if (InformationShared.getInt("status") == 1) {
                    getFragmentManager().beginTransaction().replace(R.id.content_layout, new FinanceFragment())
                            .commit();//初始界面
                    //   toolbar.setTitle("财务明细");
                } else {
                    Intent intent=new Intent(DrawerActivity.this,WebActivity.class);
                    intent.putExtra("url",getString(R.string.hint_html));

                    startActivity(intent);
                }
            } else if (id == R.id.setting_nav) {


                    Intent intent = new Intent(DrawerActivity.this, SettingActivity.class);
                    startActivity(intent);
                    isActivity = true;
                    //       toolbar.setTitle("成绩");




            } else if (id == R.id.send_nav) {
                //FeedbackAPI.openFeedbackActivity();
                String unionid=InformationShared.getString("unionid");
                String md5=Md5(InformationShared.getString("unionid"));
                if(!unionid.equals("0")&&md5!=null){
                    HashMap<String, String> clientInfo = new HashMap<>();
                    clientInfo.put("name", InformationShared.getString("name"));
                    String phone=InformationShared.getString("mobile_phone_number");
                    clientInfo.put("tel", phone);
                    clientInfo.put("avatar",InformationShared.getString("headimgurl"));
                    clientInfo.put("unionID",unionid);
                    clientInfo.put("version","v"+ Version.getVersionName(this)+"."+Version.getVersionCode(DrawerActivity.this));
                    Intent intent = new MQIntentBuilder(this)
                            .setCustomizedId(md5) // 相同的 id 会被识别为同一个顾客
                            .setClientInfo(clientInfo)
                            .build();
                    startActivity(intent);
                }else{
                    Intent intent = new MQIntentBuilder(this).build();
                    startActivity(intent);
                }




            } else if (id == R.id.about_nav) {

                    Intent intent = new Intent(DrawerActivity.this, AboutActivity.class);
                    startActivity(intent);
                    isActivity = true;
                    //       toolbar.setTitle("成绩");

            } /*else if (id == R.id.unionid_nav) {
                getFragmentManager().beginTransaction().replace(R.id.content_layout, new UnionidFragment())
                        .commit();//初始
            }*/
        } else {
            getFragmentManager().beginTransaction().replace(R.id.content_layout, new LoginFragment())
                    .commit();//初始界面

        }
        if (!isActivity) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }

        return true;
    }

    public static DrawerActivity getInstannce() {
        return drawerActivity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // super.onSaveInstanceState(outState, outPersistentState);
    }

    //重新加载
    public void refresh() {
        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);
//close this activity
        finish();
    }


    //按两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {//
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                //MobclickAgent.onKillProcess(this);
                finish();

                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // this.overridePendingTransition(R.anim.activity_close,0);
    }


    @Override
    protected void onResume() {
//        int id = getIntent().getIntExtra("userloginflag", 0);
//        if (id == 1) {
//            drawer.closeDrawers();
//            getFragmentManager().beginTransaction().replace(R.id.content_layout, new LibraryFragment()).commit();//跳转到图书馆fragment
//
//        }
        super.onResume();
        //MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  MobclickAgent.onPause(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int isBack = data.getExtras().getInt("result");//得到新Activity 关闭后返回的数据
        if (isBack == 1) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getFragmentManager().beginTransaction().replace(R.id.content_layout, new RechargeFragment())
                    .commit();//初始界面
        }

    }

    public static String Md5(String md5){
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
