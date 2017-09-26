package org.pointstone.cugapp.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.adapter.GradeCardViewAdapter;
import org.pointstone.cugapp.utils.DBGradeManager;
import org.pointstone.cugapp.utils.Grade;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;
import org.pointstone.cugapp.view.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/1.
 */

public class GradeFragmentc extends Fragment {
    View gradeView;
    private Spinner TermSpinner;
    private ProgressDialog pd;
    private DBGradeManager dbgradeManager;
    private SwipeRecyclerView GradeCardView;
    private List<String> spinnerTermDataList;
    private List<Grade> GradeList;
    private List<Grade> CjList;
    private View lineview;
    private ArrayAdapter<String> spinnerTermAdapter;
    private PullRefreshLayout layout;
    private ImageView headGradeIv;
    private ImageButton MoreBtn;
//    private TextView lastTv;
    private GradeCardViewAdapter gradeCardViewAdapter;
    private static GradeFragmentc gradeFragmentc;
    private static String year;
    private static String term;
    private View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gradeView = inflater.inflate(R.layout.fragment_gradec, container, false);
        if (InformationShared.getInt("timeTable") != 0 && InformationShared.getInt("status") == 1) {
            InformationShared.setInt("timeTable", 1);
        }
        dbgradeManager=new DBGradeManager(getActivity());
        gradeFragmentc=this;
        emptyView= gradeView.findViewById(R.id.id_empty_view);
        GradeCardView=(SwipeRecyclerView) gradeView.findViewById(R.id.rlv);
        ((SimpleItemAnimator)GradeCardView.getItemAnimator()).setSupportsChangeAnimations(false); //屏蔽删除动画避免闪烁
        layout = (PullRefreshLayout) gradeView.findViewById(R.id.swipeRefreshLayout);
        headGradeIv= (ImageView) gradeView.findViewById(R.id.head_iv);
        MoreBtn= (ImageButton) gradeView.findViewById(R.id.more_btn);
//        lastTv=(TextView)gradeView.findViewById(lastTv);
        lineview=gradeView.findViewById(R.id.linev);
        GradeList = new ArrayList<Grade>();
        CjList = new ArrayList<Grade>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        GradeCardView.setHasFixedSize(true);
        GradeCardView.setLayoutManager(layoutManager);

        String tp = InformationShared.getString("headimage");
        if (!tp.equals("0")) {
            byte[] bytes = Base64.decode(tp, Base64.DEFAULT);
            Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            headGradeIv.setImageBitmap(dBitmap);
        }
        headGradeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerActivity.getInstannce().openDrawer();
            }
        });

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String year=GradeYear.getCurrentXN(TermSpinner.getSelectedItemPosition());
                String term=GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition());
//                if(isNetworkAvailable(getActivity())){
//                    if(ping()){
//                        getGrade(year,term);
//                    }else{
//                        OwnToast.Short("网络异常,请检查连接");
//                        layout.setRefreshing(false);
//                    }
//
//                }else{
//                    layout.setRefreshing(false);
//                    new AlertDialog.Builder(getActivity())
//                            .setTitle("网络错误")
//                            .setMessage("网络未打开,是否打开网络设置？")
//
//                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    //android.os.Process.killProcess(android.os.Process.myPid());
//                                    layout.setRefreshing(false);
//                                }
//                            })
//                            .setNegativeButton("确认",new DialogInterface.OnClickListener(){
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    Intent intent = null;
//                                    // 先判断当前系统版本
//                                    if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
//                                        intent = new Intent(Settings.ACTION_SETTINGS);
//                                    }else{
//                                        intent = new Intent();
//                                        intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
//                                    }
//                                    GradeFragmentc.this.startActivity(intent);
//                                }
//                            }).show();
//                }
//
                getGrade(year,term);
            }
        });
        initView();
        return gradeView;
    }

    public void DataList() {
        spinnerTermDataList = new ArrayList<>();
        spinnerTermDataList.add("大一上");
        spinnerTermDataList.add("大一下");
        spinnerTermDataList.add("大二上");
        spinnerTermDataList.add("大二下");
        spinnerTermDataList.add("大三上");
        spinnerTermDataList.add("大三下");
        spinnerTermDataList.add("大四上");
        spinnerTermDataList.add("大四下");
    }


    //初始化函数
    private void initView(){

        TermSpinner= (Spinner) gradeView.findViewById(R.id.termSpi);
        DataList();      //spinner 下拉框内容初始化

        /*
        //创建Spinner数据
        */
        spinnerTermAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerTermDataList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TermSpinner.setAdapter(spinnerTermAdapter);
        //设置当前学期
        final int position = GradeYear.getCurrentGrade();
        year= GradeYear.getCurrentXN(position);
        term= GradeYear.getCurrentXQ(position);
        if(position>1){
            if(dbgradeManager.query(year,term).isEmpty()){
                TermSpinner.setSelection(GradeYear.getCurrentGrade()-1);
            }
        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    setGrade(year,term);
                }
            }
        };

        TermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //根据用户所选的spinner容器位置获取学年、学期
                String year= GradeYear.getCurrentXN(position);
                String term=GradeYear.getCurrentXQ(position);

                //获取数据库grade表数据并装填成绩列表容器
                int visit=InformationShared.getInt("grade"+position);
                if(visit==0)
                {
                    GradeList.removeAll(GradeList);
                    gradeCardViewAdapter=new GradeCardViewAdapter(GradeList,getActivity());
                    GradeCardView.setAdapter(gradeCardViewAdapter);
                    GradeCardView.setEmptyView(emptyView);
                    //                    setListViewHeightBasedOnChildren(GradeListView);
//                    if(isNetworkAvailable(getActivity())){
//                        if(ping()){
//                           //pd=ProgressDialog.show(getActivity(),"正在加载","请稍后......");
//                            layout.setRefreshing(true);
//                            getGrade(year,term);
//                        }else{
//                            layout.setRefreshing(false);
//                            OwnToast.Short("网络异常,请检查连接");
//
//                        }
//                    }else{
//                        new AlertDialog.Builder(getActivity())
//                                .setTitle("网络错误")
//                                .setMessage("网络未打开,是否打开网络设置？")
//
//                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface arg0, int arg1) {
//                                        //android.os.Process.killProcess(android.os.Process.myPid());
//                                        layout.setRefreshing(false);
//                                    }
//                                })
//                                .setNegativeButton("确认",new DialogInterface.OnClickListener(){
//                                            @Override
//                                            public void onClick(DialogInterface arg0, int arg1) {
//                                                Intent intent = null;
//                                                // 先判断当前系统版本
//                                                if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
//                                                    intent = new Intent(Settings.ACTION_SETTINGS);
//                                                }else{
//                                                    intent = new Intent();
//                                                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
//                                                }
//                                                GradeFragment.this.startActivity(intent);
//                                            }
//                                }).show();
//
//
//                    }
//                    pd=ProgressDialog.show(getActivity(),"正在加载","请稍后......");
                    layout.setRefreshing(true);
                    getGrade(year,term);
                }else{
                    setGrade(year,term);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter=listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    public static GradeFragmentc getInstance(){
        return gradeFragmentc;
    }

    //设置成绩列表
    public void setGrade(String year, final String term){
        GradeList=dbgradeManager.query(year,term);
      //  CjList=dbgradeManager.Cjquery(year, term);
        final String xqxn=year+"\n"+term;
        gradeCardViewAdapter =new GradeCardViewAdapter(GradeList,getActivity());
//        GradeListAdapter adapter=new GradeListAdapter(getActivity(),GradeList);
//        lastTv.setText("下拉刷新,点击可查看成绩详情");

        GradeCardView.setAdapter(gradeCardViewAdapter);
        GradeCardView.setEmptyView(emptyView);
//        GradeListView.setAdapter(adapter);
//        GradeCardView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
//
//                Intent intent=new Intent(mContext, GradeDetailActivity.class);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("data", "" + CjList.get(i).kcmc + "\n\n"
//                        + "最后成绩：" + CjList.get(i).cj + "\n"
//                        + "期末成绩：" + returnCj(CjList.get(i).qmcj) + "\n"
//                        + "平时成绩：" + returnCj(CjList.get(i).pscj) + "\n"
//                        + "实验成绩：" + returnCj(CjList.get(i).sycj));
//                intent.putExtra("xqxn",xqxn);
//                mContext.startActivity(intent);
//            }
//        });
        //lineview.setBackgroundColor(Color.parseColor("#ccccccc"));
        // setListViewHeightBasedOnChildren(GradeListView);

    }
    public String returnCj(String cj){
        if(cj.equals("null")){
            return "";
        }else{
            return cj;
        }
    }

    //通过接口获取成绩存入数据库grade表
    public void getGrade(String year,String term) {

        String getGradeUrl = "https://api.cugapp.com/public_api/CugApp/grades_for_wechat_unionid?unionid=" + InformationShared.getString("unionid") + "&academic_year=" + year + "&term=" + term;
        JsonObjectRequest getGradeJson = new JsonObjectRequest(getGradeUrl, null, new Response.Listener<JSONObject>() {

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
                            //OwnToast.Short(d.getString("KCMC"));
                            Grade grade=new Grade(d.getString("XN"),d.getString("XQ"),d.getString("KCMC"), d.getString("KCXZ"),d.getString("XF"),Grade.getJD(d.getString("CJ")), d.getString("CJ") , d.getString("PSCJ") , d.getString("QMCJ") , d.getString("SYCJ"));
                            //dbgradeManager.delete(grade);
                            dbgradeManager.delete(grade);
                            dbgradeManager.add(grade);
                            //GradeList.add(grade);
                        }
                        OwnToast.Short("成绩get√");
                        InformationShared.setInt("grade"+TermSpinner.getSelectedItemPosition(),1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                layout.setRefreshing(false);
                if( error instanceof NetworkError) {
                    //网络异常
                    OwnToast.Short("网络异常");
                } else if( error instanceof ServerError) {
                    //服务器响应错误
                    //OwnToast.Short("服务器响应错误");
                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("本学期成绩还没出哦,再等等吧")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }).show();
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
                    OwnToast.Short("查询超时,网络连接较慢");
                }else{
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
                if(pd!=null){
                    pd.dismiss();
                }
                layout.setRefreshing(false);
                String year=GradeYear.getCurrentXN(TermSpinner.getSelectedItemPosition());
                String term=GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition());
                setGrade(year,term);
            }
        };CugApplication.getInstance().addToRequestQueue(getGradeJson);

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.i("NetWorkState", "Unavailabel");
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.i("NetWorkState", "Availabel");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dbgradeManager.closeDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        // MobclickAgent.onPageStart("Grade"); //统计页面，"MainScreen"为页面名称，可自定义
        TCAgent.onPageStart(getActivity(), "Grade");
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageStart("Grade"); //统计页面，"MainScreen"为页面名称，可自定义
        TCAgent.onPageEnd(getActivity(), "Grade");
    }
}