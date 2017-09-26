package org.pointstone.cugapp.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.adapter.MessageGroupFragmentAdapter;
import org.pointstone.cugapp.utils.InformationShared;

import java.util.ArrayList;

/**
 * Created by keyboy on 2017/1/2.
 */

public class LibraryFragment extends Fragment {
    private ViewPager vPager;
    private ArrayList<android.support.v4.app.Fragment> list = new ArrayList<android.support.v4.app.Fragment>();
    private MessageGroupFragmentAdapter adapter;

    private ImageView ivShapeCircle;
    private TextView tvNow,tvHistory;

    private int offset=0;//偏移量216  我这边只是举例说明,不同手机值不一样
    private int currentIndex=0;

    private  ImageView headRechargeIv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_library, null);

        headRechargeIv= (ImageView)rootView.findViewById(R.id.library_head_iv);
        String tp = InformationShared.getString("headimage");
        if (!tp.equals("0")) {
            byte[] bytes = Base64.decode(tp, Base64.DEFAULT);
            Bitmap dBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            headRechargeIv.setImageBitmap(dBitmap);
        }
        headRechargeIv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DrawerActivity.getInstannce().openDrawer();
            }
        });
        /**
         * 初始化两个Fragment  并且填充到ViewPager
         */
        vPager = (ViewPager) rootView.findViewById(R.id.viewpager_home);
        LibraryHistoryFragment historyFragment = new LibraryHistoryFragment();
        LibraryNowFragment nowFragment = new LibraryNowFragment();
        list.add(nowFragment);
        list.add(historyFragment);

        adapter = new MessageGroupFragmentAdapter(DrawerActivity.getInstannce().getSupportFragmentManager(), list);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(1);
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(pageChangeListener);


        ivShapeCircle = (ImageView) rootView.findViewById(R.id.iv_shape_circle);

        tvNow=(TextView) rootView.findViewById(R.id.tv_now);
        tvHistory=(TextView) rootView.findViewById(R.id.tv_history);
        tvNow.setSelected(true);//推荐默认选中


        /**
         * 标题栏两个按钮设置点击效果
         */
        tvNow.setOnClickListener(clickListener);
        tvHistory.setOnClickListener(clickListener);


        initCursorPosition();
        return rootView;
    }

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_now:
                    //当我们设置setCurrentItem的时候就会触发viewpager的OnPageChangeListener借口,
                    //所以我们不需要去改变标题栏字体啥的
                    vPager.setCurrentItem(0);
                    break;
                case R.id.tv_history:
                    vPager.setCurrentItem(1);
                    break;

            }
        }
    };

    private void initCursorPosition() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        Matrix matrix = new Matrix();

        //标题栏我用weight设置权重  分成5份
        //(width / 4) * 2  这里表示标题栏两个控件的宽度
        //(width / 8)  标题栏一个控件的2分之一
        //7  约等于原点宽度的一半
        matrix.postTranslate((width / 4)  + (width / 8)-7,0);//图片平移
        ivShapeCircle.setImageMatrix(matrix);

        //一个控件的宽度  我的手机宽度是1080/5=216 不同的手机宽度会不一样哦
        offset=(width / 4);
    }

    /**
     * ViewPager滑动监听,用位移动画实现指示器效果
     *
     * TranslateAnimation 强调一个地方,无论你移动了多少次,现在停留在哪里,你的起始位置从未变化过.
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            changeTextColor(index);
            translateAnimation(index);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * 改变标题栏字体颜色
     * @param index
     */
    private void changeTextColor(int index){
        tvNow.setSelected(false);
        tvHistory.setSelected(false);

        switch (index) {
            case 0:
                tvNow.setSelected(true);
                break;
            case 1:
                tvHistory.setSelected(true);
                break;
        }
    }

    /**
     * 移动标题栏点点点...
     * @param index
     */
    private void translateAnimation(int index){
        TranslateAnimation animation = null;
        switch(index) {
            case 0:
                animation = new TranslateAnimation(offset, 0, 0, 0);
                break;
            case 1:
                animation = new TranslateAnimation(0, offset, 0, 0);
                break;
        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivShapeCircle.startAnimation(animation);

        currentIndex=index;
    }
}
