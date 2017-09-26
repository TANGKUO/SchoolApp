package org.pointstone.cugapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.Version;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2017/2/28.
 */

public class GuideActivity extends Activity {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        setListener();
        processLogic();
    }

    private void initView() {
        setContentView(R.layout.activity_guide_new);
        mBackgroundBanner = (BGABanner) findViewById(R.id.banner_guide_background);
        mForegroundBanner = (BGABanner) findViewById(R.id.banner_guide_foreground);
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
               InformationShared.setInt("welcome"+Integer.parseInt(Version.getVersionCode(GuideActivity.this)), 1);
                startActivity(new Intent(GuideActivity.this,SplashActivity.class));
                finish();
            }
        });
    }

    private void processLogic() {
        // 设置数据源
        mBackgroundBanner.setData(R.drawable.guideimg1, R.drawable.guideimg2, R.drawable.guideimg3,R.drawable.guideimg4);

        mForegroundBanner.setData(R.drawable.foreground, R.drawable.foreground, R.drawable.foreground, R.drawable.foreground);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }

    @Override
    protected void onPause() {
        super.onPause();
        InformationShared.setInt("welcome"+Integer.parseInt(Version.getVersionCode(this)), 1);
    }
}
