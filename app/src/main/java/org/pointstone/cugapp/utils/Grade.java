package org.pointstone.cugapp.utils;

/**
 * Created by Luckysonge on 2016/12/19.
 */

public class Grade {
    public String xn;
    public String xq;
    public String kcmc;
    public String kcxz;
    public String xf;
    public String jd;
    public String cj;
    public String pscj;
    public String sycj;
    public String qmcj;
    public String getKcmc() {
        return kcmc;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getKcxz() {
        return kcxz;
    }

    public void setKcxz(String kcxz) {
        this.kcxz = kcxz;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }

    public String getPscj() {
        return pscj;
    }

    public void setPscj(String pscj) {
        this.pscj = pscj;
    }

    public String getSycj() {
        return sycj;
    }

    public void setSycj(String sycj) {
        this.sycj = sycj;
    }

    public String getQmcj() {
        return qmcj;
    }

    public void setQmcj(String qmcj) {
        this.qmcj = qmcj;
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

    public Grade(){}

    public Grade(String xn, String xq, String kcmc, String kcxz, String xf, String jd, String cj, String pscj, String qmcj, String sycj){
        this.xn=xn;
        this.xq=xq;
        this.kcmc=kcmc;
        this.kcxz=kcxz;
        this.xf=xf;
        this.jd=jd;
        this.cj=cj;
        this.pscj=pscj;
        this.qmcj=qmcj;
        this.sycj=sycj;
    }

    public static String getJD(String cj) {
        if (!Character.isDigit(cj.charAt(0))){
            switch (cj){
                case "优秀":
                    return Double.toString(4.5);
                case "良好":
                    return Double.toString(3.5);
                case "中等":
                    return Double.toString(2.5);
                case "及格":
                    return Double.toString(1.5);
                case "不及格":
                    return Double.toString(0);
                default:
                    return Double.toString(-1);
            }
        }else{
            double digit_cj=Double.parseDouble(cj);
            if(digit_cj==100){
                return Double.toString(5.0);
            }else if(digit_cj>=95){
                return Double.toString(4.5);
            }else if(digit_cj>=90){
                return Double.toString(4.0);
            }else if(digit_cj>=85){
                return Double.toString(3.5);
            }else if(digit_cj>=80){
                return Double.toString(3.0);
            }else if(digit_cj>=75){
                return Double.toString(2.5);
            }else if(digit_cj>=70){
                return Double.toString(2.0);
            }else if(digit_cj>=65){
                return Double.toString(1.5);
            }else if(digit_cj>=60){
                return Double.toString(1.0);
            }else if(digit_cj>=0){
                return Double.toString(0);
            }else{
                return Double.toString(-1);
            }
        }
    }
}
