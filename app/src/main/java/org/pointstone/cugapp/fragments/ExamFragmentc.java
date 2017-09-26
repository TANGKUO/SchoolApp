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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import org.pointstone.cugapp.adapter.ExamCardViewAdapter;
import org.pointstone.cugapp.utils.DBExamManager;
import org.pointstone.cugapp.utils.Exam;
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

//import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ExamFragmentc extends Fragment {
    View ExamView;
    private Spinner TermSpinner;
    private ProgressDialog pd;
    private DBExamManager dbexamManager;
    private SwipeRecyclerView ExamCardView;
    private List<String> spinnerTermDataList;
    private List<Exam> ExamList;
    private List<Exam> XxList;
    private ArrayAdapter<String> spinnerTermAdapter;
    private PullRefreshLayout layout;
    private ImageView headExamIv;
    private ImageButton MoreBtn;
    private View emptyView;
    private ExamCardViewAdapter examCardViewAdapter;
    private static ExamFragmentc examFragmentc; //Activity实例.


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ExamView = inflater.inflate(R.layout.fragment_examc, container, false);
        if (InformationShared.getInt("timeTable") != 0 && InformationShared.getInt("status") == 1) {
            InformationShared.setInt("timeTable", 1);
        }

        dbexamManager=new DBExamManager(getActivity());
        TermSpinner= (Spinner) ExamView.findViewById(R.id.termSpi);
        ExamCardView=(SwipeRecyclerView) ExamView.findViewById(R.id.rlv);
        emptyView=ExamView.findViewById(R.id.exam_empty_view);
        layout = (PullRefreshLayout) ExamView.findViewById(R.id.swipeRefreshLayout);
        headExamIv= (ImageView) ExamView.findViewById(R.id.head_iv);
        MoreBtn= (ImageButton) ExamView.findViewById(R.id.more_btn);
//        lastTv=(TextView)ExamView.findViewById(lastTv);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        ExamCardView.setHasFixedSize(true);
        ExamCardView.setLayoutManager(layoutManager);
        examFragmentc=this;
        ExamList = new ArrayList<Exam>();
        XxList = new ArrayList<Exam>();
        initView();
        String tp = InformationShared.getString("headimage");
        if (!tp.equals("0")) {
            byte[] bytes = Base64.decode(tp, Base64.DEFAULT);
            Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            headExamIv.setImageBitmap(dBitmap);
        }
        headExamIv.setOnClickListener(new View.OnClickListener() {
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
//                        dbexamManager.delete(year,term);
//                        getExam(year,term);
//                    }else{
//                        OwnToast.Short("网络异常,请检查连接");
//                        layout.setRefreshing(false);
//                    }
//
//                }else{
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
//                                    ExamFragmentc.this.startActivity(intent);
//                                }
//                            }).show();
//                }

                getExam(year,term);
            }
        });
        return ExamView;
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


        DataList();      //spinner 下拉框内容初始化

        /*
        //创建Spinner数据
         */
        spinnerTermAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerTermDataList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TermSpinner.setAdapter(spinnerTermAdapter);
        TermSpinner.setSelection(GradeYear.getCurrentGrade());
        TermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //根据用户所选的spinner容器位置获取学年、学期
                String year= GradeYear.getCurrentXN(position);
                String term=GradeYear.getCurrentXQ(position);
                //获取数据库grade表数据并装填成绩列表容器

                int visit=InformationShared.getInt("exam"+position);
                if(visit==0)
                {
                    ExamList.removeAll(ExamList);
                    examCardViewAdapter=new ExamCardViewAdapter(ExamList,getActivity());
                    ExamCardView.setAdapter(examCardViewAdapter);
                    ExamCardView.setEmptyView(emptyView);
 //                   setListViewHeightBasedOnChildren(ExamListView);
//                    if(isNetworkAvailable(getActivity())){
//                        if(ping()){
//                            //pd=ProgressDialog.show(getActivity(),"正在加载","请稍后......");
//                            layout.setRefreshing(true);
//                            getExam(year,term);
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
//                                    @Override
//                                    public void onClick(DialogInterface arg0, int arg1) {
//                                        Intent intent = null;
//                                        // 先判断当前系统版本
//                                        if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
//                                            intent = new Intent(Settings.ACTION_SETTINGS);
//                                        }else{
//                                            intent = new Intent();
//                                            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
//                                        }
//                                        ExamFragment.this.startActivity(intent);
//                                    }
//                                }).show();
//
//
//                    }
//                    pd=ProgressDialog.show(getActivity(),"正在加载","请稍后......");
                    layout.setRefreshing(true);
                    getExam(year,term);
                }else{
                    setExam(year,term);
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

    //设置考试安排
    public void setExam(String year, String term){
        ExamList=dbexamManager.query(year,term);
        //XxList=dbexamManager.xxquery(year, term);
        final String xn=year;
        final String xq=term;
        examCardViewAdapter =new ExamCardViewAdapter(ExamList,getActivity());
        //OwnToast.Short("一共"+adapter.getCount()+"门成绩");
//        lastTv.setText("下拉刷新,点击可查看考场及座号详情");
        //lastTv.setGravity(Gravity.END);
        ExamCardView.setAdapter(examCardViewAdapter);
        ExamCardView.setEmptyView(emptyView);
//        ExamListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
//
//                Intent intent=new Intent(mContext, ExamDetailActivity.class);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("data", ""+xn+" "+xq+"\n"+XxList.get(i).kczwmc+"\n\n"
//                        +"考试地点："+XxList.get(i).jsmc+"\n"
//                        +"座位号："+XxList.get(i).zwh);
//                mContext.startActivity(intent);
//            }
//        });
//        setListViewHeightBasedOnChildren(ExamListView);
    }

    public String returnNull(String str){
        if(str.equals("null")){
            return "";
        }else{
            return str;
        }
    }
    //通过接口获取成绩存入数据库exam表
    public void getExam(final String year, final String term) {

        String getExamUrl = "https://api.cugapp.com/public_api/CugApp/exam_schedules_for_wechat_unionid?unionid=" + InformationShared.getString("unionid") + "&academic_year=" + year + "&term=" + term;
        JsonObjectRequest getExamJson = new JsonObjectRequest(getExamUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    dbexamManager.delete(year,term);
                    boolean status = response.getBoolean("status");
                    if (status) {
                        String data = response.getString("data");
                        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                        Matcher matcher = pattern.matcher(data);
                        while (matcher.find()) {
                            String json = "{" + matcher.group(1) + "}";
                            JSONObject d = new JSONObject(json);
                            String xkkh=d.getString("XKKH");
                            String kczwmc=d.getString("KCZWMC");
                            if(Exam.Isks(xkkh)&&!kczwmc.equals("null")){
                               // OwnToast.Short(returnNull(d.getString("JSMC")));
                                Exam exam=new Exam(Exam.getXN(xkkh),Exam.getXQ(xkkh),returnNull(d.getString("KCZWMC")),returnNull(d.getString("KSSJ")),returnNull(d.getString("JSMC")),returnNull( d.getString("ZWH")));
                                dbexamManager.add(exam);
                            }

                        }
                        InformationShared.setInt("exam"+TermSpinner.getSelectedItemPosition(),1);
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
                            .setMessage("本学期考试安排还没出哦,再等等吧")
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
                if(pd!=null)
                    pd.dismiss();
                layout.setRefreshing(false);
                String year=GradeYear.getCurrentXN(TermSpinner.getSelectedItemPosition());
                String term=GradeYear.getCurrentXQ(TermSpinner.getSelectedItemPosition());
                setExam(year,term);
            }
        };CugApplication.getInstance().addToRequestQueue(getExamJson);

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
//    public static final boolean ping() {
//
//        String result = null;
//        try {
//            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
//            Process p = Runtime.getRuntime().exec("ping -c 1 -w 5 " + ip);// ping网址3次
//            // ping的状态
//            int status = p.waitFor();
//            if (status == 0) {
//                result = "success";
//                return true;
//            } else {
//                result = "failed";
//            }
//        } catch (IOException e) {
//            result = "IOException";
//        } catch (InterruptedException e) {
//            result = "InterruptedException";
//        } finally {
//            Log.d("----result---", "result = " + result);
//        }
//        return false;
//    }

    public static ExamFragmentc getInstannce() {
        return examFragmentc;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbexamManager.closeDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("Exam"); //统计页面，"MainScreen"为页面名称，可自定义
        TCAgent.onPageStart(getActivity(), "Exam");
    }

    @Override
    public void onPause() {
        super.onPause();
        //  MobclickAgent.onPageStart("Exam"); //统计页面，"MainScreen"为页面名称，可自定义
        TCAgent.onPageEnd(getActivity(), "Exam");
    }

}