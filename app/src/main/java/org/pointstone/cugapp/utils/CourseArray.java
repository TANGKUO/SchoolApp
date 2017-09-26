package org.pointstone.cugapp.utils;

/**
 * Created by Administrator on 2016/12/12.
 */

public class CourseArray {
    private int size=0;
    private int maxsize=10;
    private String [] course=new String[10];
    public int getSize()
    {
        return size;
    }
    public  void add(String c)
    {
        if(size==maxsize)
        {
            maxsize*=2;
            String [] course2=new String[maxsize];
            for (int i=0;i<size;i++)
            {
                course2[i]=course[i];
            }
            course=course2;
        }
        course[size]=c;
        size++;
    }

    public  String get(int index)
    {
        return course[index];
    }
}
