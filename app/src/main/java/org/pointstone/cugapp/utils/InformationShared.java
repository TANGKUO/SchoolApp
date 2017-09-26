package org.pointstone.cugapp.utils;

import android.content.SharedPreferences;

import org.pointstone.cugapp.CugApplication;

/**
 * Created by Administrator on 2016/12/1.
 */

public class InformationShared {

    //若没有值则返回0
    public static String getString(String key)
    {
        SharedPreferences information= CugApplication.getInstance().getSharedPreferences("information", 0);
        String str=information.getString(key,"0");
        return  str;
    }

    public  static void setString(String key,String value)
    {
        SharedPreferences information=CugApplication.getInstance().getSharedPreferences("information", 0);
        SharedPreferences.Editor editor = information.edit();
        editor.putString(key,value);
        editor.commit();
    }

    //若没有值则返回0
    public static int getInt(String key)
    {
        SharedPreferences information= CugApplication.getInstance().getSharedPreferences("information", 0);
        int str=information.getInt(key,0);
        return  str;
    }

    public static boolean getBoolean(String key)
    {
        SharedPreferences information= CugApplication.getInstance().getSharedPreferences("information", 0);
        boolean str=information.getBoolean(key,false);
        return  str;
    }
    //若没有值则返回defaultvalue
    public static int getInt(String key,int defaultvalue)
    {
        SharedPreferences information= CugApplication.getInstance().getSharedPreferences("information", 0);
        int str=information.getInt(key,defaultvalue);
        return  str;
    }
    public  static void setInt(String key,int value)
    {
        SharedPreferences information=CugApplication.getInstance().getSharedPreferences("information", 0);
        SharedPreferences.Editor editor = information.edit();
        editor.putInt(key,value);
        editor.commit();
    }
}
