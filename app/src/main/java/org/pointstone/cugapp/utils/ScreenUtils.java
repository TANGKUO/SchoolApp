package org.pointstone.cugapp.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by keyboy on 2016/12/25.
 */

public class ScreenUtils {

    @SuppressWarnings("deprecation")
    public static int[] getScreenDispaly(Context context){
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width=wm.getDefaultDisplay().getWidth();//手机屏幕的宽度
        int height=wm.getDefaultDisplay().getHeight();//手机屏幕的高度
        int result[] = {width,height};
        return result;
    }
}
