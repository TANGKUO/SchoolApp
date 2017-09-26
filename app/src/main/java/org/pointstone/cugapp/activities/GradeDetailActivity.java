package org.pointstone.cugapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.adapter.CardItem;
import org.pointstone.cugapp.adapter.GradePagerAdapter;
import org.pointstone.cugapp.view.ShadowTransformer;

/**
 * Created by luckysonge on 2016/12/21.
 */

public class GradeDetailActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private GradePagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private View BlackView1;
    private View BlackView2;
    private String time;
    private static GradeDetailActivity gradeDetailActivity; //Activity实例


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_grade);
        mViewPager = (ViewPager) findViewById(R.id.GradeViewPager);
        gradeDetailActivity=this;
        //通过Activity.getIntent()获取当前页面接收到的Intent。
        Intent intent =getIntent();
        mCardAdapter = new GradePagerAdapter(this);
        String data=intent.getStringExtra("data");
        if(data.charAt(0)!='\n') {
                mCardAdapter.addCardItem(new CardItem(data));
        }else{
                data=data.replace("\n","");
                mCardAdapter.addCardItem(new CardItem("\n\n"+data+"\n\n\n成绩详情"));
        }


        mViewPager.setAdapter(mCardAdapter);
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

    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public String getTime()
    {
        return time;
    }


    public static GradeDetailActivity getInstannce() {
        return gradeDetailActivity ;
    }



}
