package org.pointstone.cugapp.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;

import io.yunba.android.manager.YunBaManager;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2017/2/25.
 */

public class YunBaReceiver extends BroadcastReceiver {
    public static int id=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context) ;
            if(prefs.getBoolean("status",false) ){
                String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
                //创建一个NotificationManager的引用

              /*  NotificationManager mNotifyMgr =
                        (NotificationManager)context. getSystemService(NOTIFICATION_SERVICE);
                PendingIntent contentIntent = PendingIntent.getActivity(
                        context, 0, new Intent(context, DrawerActivity.class), 0);

                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("我的地质锤")
                        .setContentText(msg)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)
                        .build();// getNotification()

                mNotifyMgr.notify(id, notification);
                id++;*/
                PendingIntent contentIntent = PendingIntent.getActivity(
                        context, 0, new Intent(context, DrawerActivity.class), 0);
                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                Notification notification = builder
                        .setContentTitle("我的地质锤")
                        .setContentText(msg)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.alpha_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                context.getResources(), R.mipmap.ic_launcher))
                        .build();
                manager.notify(id, notification);
                id++;

            }

        }
     /*   if (MQMessageManager.ACTION_NEW_MESSAGE_RECEIVED.equals(action)) {
            // 从 intent 获取消息 id
            String msgId = intent.getStringExtra("msgId");
            // 从 MCMessageManager 获取消息对象
            MQMessageManager messageManager = MQMessageManager.getInstance(context);
            MQMessage message = messageManager.getMQMessage(msgId);
            // do something

        }

        // 客服正在输入
        else if (MQMessageManager.ACTION_AGENT_INPUTTING.equals(action)) {
            // do something
        }

        // 客服转接
        else if (MQMessageManager.ACTION_AGENT_CHANGE_EVENT.equals(action)) {
            // 获取转接后的客服

            // do something
        }*/
    }
}
