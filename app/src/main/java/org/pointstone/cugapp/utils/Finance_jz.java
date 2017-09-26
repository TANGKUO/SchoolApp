package org.pointstone.cugapp.utils;

/**
 * Created by Luckysonge on 2017/2/3.
 */

public class Finance_jz {
    private String jesm;
    private String sjff;
    private String yff;
    private String jelx;

    public String getJesm() {
        return jesm;
    }

    public void setJesm(String jesm) {
        this.jesm = jesm;
    }

    public String getSjff() {
        return sjff;
    }

    public void setSjff(String sjff) {
        this.sjff = sjff;
    }

    public String getYff() {
        return yff;
    }

    public void setYff(String yff) {
        this.yff = yff;
    }

    public String getJelx() {
        return jelx;
    }

    public void setJelx(String jelx) {
        this.jelx = jelx;
    }
    public Finance_jz(){};
    public Finance_jz(String jelx,String yff,String sjff,String jesm){
        this.jesm=jesm;
        this.sjff=sjff;
        this.yff=yff;
        this.jelx=jelx;
    }
}
