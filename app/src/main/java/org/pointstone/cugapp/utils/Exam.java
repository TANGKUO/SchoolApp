package org.pointstone.cugapp.utils;

/**
 * Created by Luckysonge on 2016/12/25.
 */

public class Exam {
    public String xkkh;
    public String xn;
    public String xq;
    public String kczwmc;
    public String kssj;
    public String jsmc;
    public String zwh;

    public String getXkkh() {
        return xkkh;
    }

    public void setXkkh(String xkkh) {
        this.xkkh = xkkh;
    }


    public String getKczwmc() {
        return kczwmc;
    }

    public void setKczwmc(String kczwmc) {
        this.kczwmc = kczwmc;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJsmc() {
        return jsmc;
    }

    public void setJsmc(String jsmc) {
        this.jsmc = jsmc;
    }

    public String getZwh() {
        return zwh;
    }

    public void setZwh(String zwh) {
        this.zwh = zwh;
    }

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public Exam(){};
    public Exam(String xn,String xq,String kczwmc,String kssj,String jsmc,String zwh){
        this.xn=xn;
        this.xq=xq;
        this.kczwmc=kczwmc;
        this.kssj=kssj;
        this.jsmc=jsmc;
        this.zwh=zwh;
    }
    public static String getXN(String xkkh){
        return xkkh.substring(1,10);
    }
    public static String getXQ(String xkkh){
        return xkkh.substring(11,12);
    }
    public static boolean Isks(String xkkh){
        if(xkkh.charAt(xkkh.length()-1)=='A'){
            return false;
        }else {
            return true;
        }
    }
}
