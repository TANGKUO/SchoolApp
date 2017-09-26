package org.pointstone.cugapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/6.
 */
public class DBCourseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "course.db";
    private static final int DATABASE_VERSION = 4;

    public DBCourseHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS course" +
                "(xn VARCHAR,xq  VARCHAR,xqj VARCHAR,djj VARCHAR,qsz VARCHAR,jsz VARCHAR,kcb VARCHAR,dsz VARCHAR,skcd VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS grade"+
                "(xn VARCHAR,xq VARCHAR,kcmc VARCHAR,kcxz VARCHAR,xf VARCHAR,jd VARCHAR,cj VARCHAR,pscj VARCHAR, qmcj VARCHAR, sycj VARCHAR,PRIMARY KEY(xn,xq,kcmc,kcxz))");
        db.execSQL("CREATE TABLE IF NOT EXISTS coursenew" +
                "(xn VARCHAR,xq  VARCHAR,xqj VARCHAR,djj VARCHAR,qsz VARCHAR,jsz VARCHAR,kcb VARCHAR,dsz VARCHAR,skcd VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS exam"+
                "(xn VARCHAR,xq  VARCHAR,kczwmc VARCHAR,kssj VARCHAR,jsmc VARCHAR, zwh VARCHAR)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  db.execSQL("ALTER TABLE course ADD COLUMN other STRING");
        db.execSQL("CREATE TABLE IF NOT EXISTS course" +
                "(xn VARCHAR,xq  VARCHAR,xqj VARCHAR,djj VARCHAR,qsz VARCHAR,jsz VARCHAR,kcb VARCHAR,dsz VARCHAR,skcd VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS grade"+
                "(xn VARCHAR,xq VARCHAR,kcmc VARCHAR,kcxz VARCHAR,xf VARCHAR,jd VARCHAR,cj VARCHAR,pscj VARCHAR, qmcj VARCHAR, sycj VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS coursenew" +
                "(xn VARCHAR,xq  VARCHAR,xqj VARCHAR,djj VARCHAR,qsz VARCHAR,jsz VARCHAR,kcb VARCHAR,dsz VARCHAR,skcd VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS exam"+
                "(xn VARCHAR,xq  VARCHAR,kczwmc VARCHAR,kssj VARCHAR,jsmc VARCHAR, zwh VARCHAR)");
    }
}
