package org.pointstone.cugapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.Version;

/**
 * Created by Administrator on 2017/2/16.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否是第一次开启应用
        int isFirstOpen = InformationShared.getInt("welcome"+Integer.parseInt(Version.getVersionCode(this)));
        // 如果是第一次启动，则先进入功能引导页
      /*  if (isFirstOpen==0) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }*/

        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);
        finish();
    }
}