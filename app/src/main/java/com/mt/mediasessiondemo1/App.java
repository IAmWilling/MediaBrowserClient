package com.mt.mediasessiondemo1;

import static com.mt.mediasessiondemo1.NotificationUtil.CHANNEL_ID;
import static com.mt.mediasessiondemo1.NotificationUtil.CHANNEL_NAME;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class App extends Application {

    private static Context M_CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        M_CONTEXT = this;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("适用于音乐播放媒体");
            manager.createNotificationChannel(channel);
        }

    }

    public static Context getContext(){
        return M_CONTEXT;
    }
}
