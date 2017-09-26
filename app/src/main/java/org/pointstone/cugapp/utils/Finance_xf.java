package org.pointstone.cugapp.utils;

/**
 * Created by Luckysonge on 2017/2/3.
 */

public class Finance_xf {
    private String jfxm;
    private String jfn;
    private String yjje;
    private String sjje;
    private String qf;

    public String getJfxm() {
        return jfxm;
    }

    public void setJfxm(String jfxm) {
        this.jfxm = jfxm;
    }

    public String getJfn() {
        return jfn;
    }

    public void setJfn(String jfn) {
        this.jfn = jfn;
    }

    public String getYjje() {
        return yjje;
    }

    public void setYjje(String yjje) {
        this.yjje = yjje;
    }

    public String getSjje() {
        return sjje;
    }

    public void setSjje(String sjje) {
        this.sjje = sjje;
    }

    public String getQf() {
        return qf;
    }

    public void setQf(String qf) {
        this.qf = qf;
    }
    public Finance_xf(){};
    public Finance_xf(String jfxm,String jfn,String yjje,String sjje,String qf){
        this.jfxm=jfxm;
        this.jfn= jfn;
        this.yjje=yjje;
        this.sjje=sjje;
        this.qf=qf;
    }
}
