package org.pointstone.cugapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.fragments.SettingFragment;

/**
 * Created by Administrator on 2017/1/5.
 */

public class SettingActivity extends   SwipeBackAppCompatActivity {


    private  static SettingActivity settingActivity;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingActivity=this;
        mToolbar= (Toolbar) findViewById(R.id.toolbar_setting);
        initToolbar();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingFragment())
                .commit();//初始界面
    }


    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar.setTitle("设置");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.setting_back);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    public static SettingActivity getInstance()
    {
        return settingActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
    //    MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}