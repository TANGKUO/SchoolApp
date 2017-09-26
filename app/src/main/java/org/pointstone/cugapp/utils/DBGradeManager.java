package org.pointstone.cugapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luckysonge on 2016/12/6.
 */

public class DBGradeManager {
    private DBCourseHelper helper;
    private SQLiteDatabase db;

    public DBGradeManager(Context context) {
        helper = new DBCourseHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }



    public void add(Grade grade)
    {
        String xsql="insert or ignore into grade  values (\'"+grade.xn+"\',\'"+grade.xq+"\',\'"+grade.kcmc+"\',\'"+grade.kcxz+"\',\'"+grade.xf+"\',\'"+grade.jd+"\',\'"+grade.cj+"\',\'"+grade.pscj+"\',\'"+grade.qmcj+"\',\'"+grade.sycj+"\')";
        db.execSQL(xsql);
    }

    public void delete(String XN,String XQ)
    {
        String xsql="delete from grade where xn=\'"+XN+"\' AND xq=\'"+XQ+"\'";
        db.execSQL(xsql);
    }
    public void delete(Grade grade)
    {
        String xsql="delete from grade where xn=\'"+grade.xn+"\' AND xq=\'"+grade.xq+"\' AND kcmc=\'"+grade.kcmc+"\' AND kcxz=\'"+grade.kcxz+"\'";
        db.execSQL(xsql);
    }
    /**
     * query all persons, return list
     * @return List<Person>
     */
    public List<Grade> query(String xn,String xq) {
        ArrayList<Grade> persons = new ArrayList<Grade>();
        Cursor c = queryTheCursor(xn,xq);
        while (c.moveToNext()) {
            Grade grade=new Grade();
            grade.xq=c.getString(c.getColumnIndex("xq"));
            grade.xn=c.getString(c.getColumnIndex("xn"));
            grade.kcmc=c.getString(c.getColumnIndex("kcmc"));
            grade.kcxz=c.getString(c.getColumnIndex("kcxz"));
            grade.xf=c.getString(c.getColumnIndex("xf"));
            grade.jd=c.getString(c.getColumnIndex("jd"));
            grade.cj=c.getString(c.getColumnIndex("cj"));
            grade.pscj=c.getString(c.getColumnIndex("pscj"));
            grade.qmcj=c.getString(c.getColumnIndex("qmcj"));
            grade.sycj=c.getString(c.getColumnIndex("sycj"));
            persons.add(grade);
        }
        c.close();
        return persons;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor(String xn,String xq) {
        Cursor c = db.rawQuery("SELECT * FROM grade where xn=\'"+xn+"\' AND xq=\'"+xq+"\'", null);
 //       Cursor c = db.rawQuery("SELECT * FROM course", null);
        return c;
    }
    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
