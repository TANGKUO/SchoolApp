package org.pointstone.cugapp.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/9.
 */

public class CourseDayW implements Serializable{
    public String tname;
    public String place;
    public String time;
    public String cname;
    public CourseDayW(String time, String place, String cname, String tname) {
        this.cname = cname;
        this.tname = tname;
        this.time= time;
        this.place = place;
    }
}
