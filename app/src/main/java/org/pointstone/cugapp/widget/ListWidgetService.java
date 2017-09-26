package org.pointstone.cugapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ListWidgetService extends RemoteViewsService {



    //实现RemoteViewService时要实现的抽象方法，获得一个RemoteViewsFactory实例
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new ListRemoteViewFactory(this.getApplicationContext(), intent));
    }
    /**
     * 内部类，实现RemoteViewsFatory接口
     * 这个RemoteViewsFactory就相当于BaseAdatper
     */



}