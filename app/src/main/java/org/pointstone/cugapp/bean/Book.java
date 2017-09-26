package org.pointstone.cugapp.bean;

/**
 * Created by keyboy on 2017/2/28.
 */

public class Book {

    public String book_name;
    public String book_id;
    public String lend_time;
    public String return_time;

    public String getName() {
        return book_name;
    }
    public void setName(String name) {
        this.book_name = name;
    }
    public String getId() {
        return book_id;
    }
    public void setId(String id) {
        this.book_id = id;
    }

    public String getLend_time() {
        return lend_time;
    }

    public void setLend_time(String lend_time) {
        this.lend_time = lend_time;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }


}
