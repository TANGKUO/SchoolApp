package org.pointstone.cugapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/12/27.
 */

public class ScreenShot {
    private static Bitmap getBitmapByView(ScrollView scrollView,LinearLayout time,LinearLayout week) {
        int h = 0;
        Bitmap bitmap_scroll = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }

        bitmap_scroll= Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap_scroll);
        scrollView.draw(canvas);

        Bitmap bitmap_time = null;
        bitmap_time = Bitmap.createBitmap(time.getWidth(), time.getHeight(),
                Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap_time);
        time.draw(canvas);

        Bitmap bitmap_week = null;
        bitmap_week= Bitmap.createBitmap(week.getWidth(), week.getHeight(),
                Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap_week);
        week.draw(canvas);


        Bitmap newbmp = Bitmap.createBitmap(time.getWidth(),time.getHeight()+week.getHeight()+h, Bitmap.Config.ARGB_4444);
        final Canvas canvas2 = new Canvas(newbmp);
        canvas2.drawBitmap(bitmap_time,0,0,null);
        canvas2.drawBitmap(bitmap_week,0,time.getHeight(),null);
        canvas2.drawBitmap(bitmap_scroll,0,time.getHeight()+week.getHeight(),null);

        return newbmp;
    }
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
    // 保存到sdcard
    private static boolean savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return  true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
        return  false;
    }

    // 程序入口
    public static String shoot(ScrollView a, LinearLayout time,LinearLayout week) {
        Calendar c = Calendar.getInstance();
        int current_hour = c.get(Calendar.HOUR_OF_DAY);
        int current_minute = c.get(Calendar.MINUTE) ;
        int current_second = c.get(Calendar.SECOND);
        String fileName="sdcard/"+current_hour+"_"+current_minute+"_"+current_second+".png";
        if(ScreenShot.savePic(compressImage(getBitmapByView(a,time,week)), fileName))
            return  fileName;
        else
            return "";
    }
}
