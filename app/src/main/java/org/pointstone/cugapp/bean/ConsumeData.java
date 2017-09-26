package org.pointstone.cugapp.bean;

/**
 * Created by keyboy on 2016/12/21.
 */

public class ConsumeData {
    private int imgId;
    private String content_time;
    private String content_money;
    private String content_use;

    public ConsumeData() {}

    public ConsumeData(int imgId, String content_time, String content_money, String content_use) {
        this.imgId = imgId;
        this.content_time = content_time;
        this.content_money = content_money;
        this.content_use = content_use;
    }

    public int getImgId() {
        return imgId;
    }

    public String getContentTime() {
        return content_time;
    }
    public String getContentMoney() {
        return content_money;
    }
    public String getContentUse() {
        return content_use;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setContentTime(String content_time) {
        this.content_time = content_time;
    }
    public void setContentMoney(String content_money) {
        this.content_money = content_money;
    }
    public void setContentUse(String content_use) {
        this.content_use = content_use;
    }
}