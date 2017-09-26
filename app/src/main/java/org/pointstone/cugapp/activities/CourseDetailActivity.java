package org.pointstone.cugapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.adapter.CardItem;
import org.pointstone.cugapp.adapter.CardPagerAdapter;
import org.pointstone.cugapp.utils.DBCourseManager;
import org.pointstone.cugapp.utils.DBCourseNewManager;
import org.pointstone.cugapp.view.ShadowTransformer;

/**
 * Created by Administrator on 2016/12/21.
 */

public class CourseDetailActivity  extends AppCompatActivity {
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private View BlackView1;
    private View BlackView2;
    private String time;
    private static CourseDetailActivity courseDetailActivity; //Activity实例
    private DBCourseManager mgr;
    private DBCourseNewManager mgr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        courseDetailActivity=this;
        //通过Activity.getIntent()获取当前页面接收到的Intent。
        Intent intent =getIntent();
        mCardAdapter = new CardPagerAdapter(this);
        String data=intent.getStringExtra("data");
        time=intent.getStringExtra("time");
        if(data.charAt(0)!='\n')
        {
            String course[]=data.split("<br>");


            for(int i=0;i<course.length;i++)
            {
                String c="";
                if(course[i].charAt(1)==':')
                    c=course[i].substring(2);
                else
                    c=course[i].substring(3);
                c=c.replace("\n\n","\n");
                mCardAdapter.addCardItem(new CardItem(c));
            }
        }else{
            data=data.replace("\n","");
            mCardAdapter.addCardItem(new CardItem("\n\n"+data+"\n\n\n点击添加课程"));
        }

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mCardShadowTransformer.enableScaling(true);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);


        BlackView1=findViewById(R.id.black_view1);
        BlackView2=findViewById(R.id.black_view2);
        BlackView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        BlackView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mgr = new DBCourseManager(this);
        mgr2 = new DBCourseNewManager(this);
    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public String getTime()
    {
        return time;
    }


    public static CourseDetailActivity getInstannce() {
        return courseDetailActivity ;
    }

    public void DeleteCourse(String XN,String XQ,String KCB)
    {
        mgr.deleteCourse(XN,XQ,KCB);
        mgr2.deleteCourse(XN,XQ,KCB);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
        mgr2.closeDB();
    }

}
