package org.pointstone.cugapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luckysonge on 2016/12/6.
 */

public class DBExamManager {
    private DBCourseHelper helper;
    private SQLiteDatabase db;

    public DBExamManager(Context context) {
        helper = new DBCourseHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }



    public void add(Exam exam)
    {
        String xsql="insert into exam  values (\'"+exam.xn+"\',\'"+exam.xq+"\',\'"+exam.kczwmc+"\',\'"+exam.kssj+"\',\'"+exam.jsmc+"\',\'"+exam.zwh+"\')";
        db.execSQL(xsql);
    }

    public void delete(String xn,String xq)
    {
        String xsql="delete from exam where xn=\'"+xn+"\' AND xq=\'"+xq+"\'" ;
        db.execSQL(xsql);
    }
    public void update(String xn,String xq,String kcmc,String kssj,String jsmc,String zwh){
        String xsql="update exam set kssj=\'"+kssj+"\'," +
                                "jsmc=\'"+jsmc+"\'," +
                                "zwh=\'"+zwh+"\'where xn=\'"+xn+"\' AND xq=\'"+xq+"\' AND kczwmc=\'"+kcmc+"\'";
        db.execSQL(xsql);
    }
    /**
     * query all persons, return list
     * @return List<Person>
     */
    public List<Exam> query(String xn,String xq) {
        ArrayList<Exam> persons = new ArrayList<Exam>();
        Cursor c = queryTheCursor(xn,xq);
        while (c.moveToNext()) {
            Exam exam=new Exam();
            exam.xn=c.getString(c.getColumnIndex("xn"));
            exam.xq=c.getString(c.getColumnIndex("xq"));
            exam.kczwmc=c.getString(c.getColumnIndex("kczwmc"));
            exam.kssj=c.getString(c.getColumnIndex("kssj"));
            exam.jsmc=c.getString(c.getColumnIndex("jsmc"));
            exam.zwh=c.getString(c.getColumnIndex("zwh"));
            persons.add(exam);
        }
        c.close();
        return persons;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor(String xn,String xq) {
        Cursor c = db.rawQuery("SELECT * FROM exam where xn=\'"+xn+"\' AND xq=\'"+xq+"\'", null);
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
