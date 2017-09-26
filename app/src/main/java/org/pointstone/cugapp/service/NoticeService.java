package org.pointstone.cugapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NoticeService extends Service {
    public NoticeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
