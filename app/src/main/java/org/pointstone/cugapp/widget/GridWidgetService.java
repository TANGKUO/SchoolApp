package org.pointstone.cugapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Administrator on 2017/2/12.
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this, intent);
    }
}
