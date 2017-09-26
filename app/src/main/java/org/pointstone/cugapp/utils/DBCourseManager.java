package org.pointstone.cugapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class DBCourseManager {
    private DBCourseHelper helper;
    private SQLiteDatabase db;

    public DBCourseManager(Context context) {
        helper = new DBCourseHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     * @param courses
     */
    public void add(List<Course> courses) {
        db.beginTransaction();  //开始事务
        try {
            for (Course course : courses) {
                db.execSQL("INSERT INTO course VALUES(null, ?, ?, ?)", new Object[]{course.xn,course.xq,course.xqj,course.djj,course.qsz,course.jsz,course.kcb,course.dsz,course.skcd});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void add(Course course)
    {
        String xsql="insert into course  values (\'"+course.xn+"\',\'"+course.xq+"\',\'"+course.xqj+"\',\'"+course.djj+"\',\'"+course.qsz+"\',\'"+course.jsz+"\',\'"+course.kcb+"\',\'"+course.dsz+"\',\'"+course.skcd+"\')";
        db.execSQL(xsql);
    }

    public void delete(String XN,String XQ)
    {
        String xsql="delete from course where xn=\'"+XN+"\' AND xq=\'"+XQ+"\'";
        db.execSQL(xsql);
    }
    /**
     * query all persons, return list
     * @return List<Person>
     */
    public List<Course> query(String xn,String xq) {
        ArrayList<Course> persons = new ArrayList<Course>();
        Cursor c = queryTheCursor(xn,xq);
        while (c.moveToNext()) {
            Course course = new Course();
            course.djj = c.getString(c.getColumnIndex("djj"));
            course.xn = c.getString(c.getColumnIndex("xn"));
            course.xq = c.getString(c.getColumnIndex("xq"));
            course.xqj = c.getString(c.getColumnIndex("xqj"));
            course.qsz = c.getString(c.getColumnIndex("qsz"));
            course.jsz = c.getString(c.getColumnIndex("jsz"));
            course.kcb = c.getString(c.getColumnIndex("kcb"));
            course.dsz = c.getString(c.getColumnIndex("dsz"));
            course.skcd = c.getString(c.getColumnIndex("skcd"));
            persons.add(course);
        }
        c.close();
        return persons;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor(String xn,String xq) {
        Cursor c = db.rawQuery("SELECT * FROM course where xn=\'"+xn+"\' AND xq=\'"+xq+"\'", null);
 //       Cursor c = db.rawQuery("SELECT * FROM course", null);
        return c;
    }
    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    public void deleteCourse(String xn,String xq,String kcb)
    {
        String xsql="delete from course where xn=\'"+xn+"\' AND xq=\'"+xq+"\' AND kcb=\'"+kcb+"\'";
        db.execSQL(xsql);
    }
}
