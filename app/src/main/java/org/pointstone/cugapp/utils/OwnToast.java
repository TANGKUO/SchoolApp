package org.pointstone.cugapp.utils;

import android.widget.Toast;

import org.pointstone.cugapp.CugApplication;

/**
 * Created by Administrator on 2016/12/1.
 */

public class OwnToast {
    public static void Long(String str)
    {
        Toast.makeText(CugApplication.getInstance(), str, Toast.LENGTH_LONG).show();
    }

    public static void Short(String str){
        Toast.makeText(CugApplication.getInstance(), str, Toast.LENGTH_SHORT).show();
    }
}
