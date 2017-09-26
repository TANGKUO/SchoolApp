package org.pointstone.cugapp.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/28.
 */

public class CourseDay implements Serializable {
    private String tname;
    private String place;
    private String time;
    private String cname;
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }



    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CourseDay (String time, String place,String cname,String tname) {
        this.cname = cname;
        this.tname = tname;
        this.time= time;
        this.place = place;
    }


}
