package org.pointstone.cugapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.adapter.CardItem;
import org.pointstone.cugapp.adapter.ExamPagerAdapter;
import org.pointstone.cugapp.utils.DBCourseManager;
import org.pointstone.cugapp.utils.DBCourseNewManager;
import org.pointstone.cugapp.view.ShadowTransformer;

/**
 * Created by luckysonge on 2016/12/21.
 */

public class ExamDetailActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ExamPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private View BlackView1;
    private View BlackView2;
    private static ExamDetailActivity examDetailActivity; //Activity实例
    private DBCourseManager mgr;
    private DBCourseNewManager mgr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_exam);
        mViewPager = (ViewPager) findViewById(R.id.ExamViewPager);
        examDetailActivity=this;
        //通过Activity.getIntent()获取当前页面接收到的Intent。
        Intent intent =getIntent();
        mCardAdapter = new ExamPagerAdapter(this);
        String data=intent.getStringExtra("data");
        if(data.charAt(0)!='\n') {
                mCardAdapter.addCardItem(new CardItem(data));
        }else{

        }


        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setOffscreenPageLimit(4);

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




    public static ExamDetailActivity getInstannce() {
        return examDetailActivity ;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
        mgr2.closeDB();
    }

}
