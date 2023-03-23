package com.mt.mediasessiondemo1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.media.session.PlaybackState;
import android.os.Build;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.media.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

public class NotificationUtil {
    public static final String CHANNEL_ID = "MUSIC_NOTIFICATION_ID";
    public static final String CHANNEL_NAME = "音乐播放";
    public static final int NOTIFICATION_ID = 10086;
    private static NotificationUtil INSTANCE;
    private static boolean isCreate = false;

    public static NotificationUtil getInstance() {
        synchronized (MediaControllerUtil.class) {
            if (INSTANCE == null) {
                INSTANCE = new NotificationUtil();
            }
        }
        return INSTANCE;
    }

    private Notification notification;


    public void notifyNotification(Service service, MediaBrowserCompat.MediaItem mediaItem, int state) {
        MediaDescriptionCompat description = mediaItem.getDescription();
        Context context = App.getContext();
        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(description.getIconBitmap())

                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_STOP))
                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(new androidx.core.app.NotificationCompat.Action.Builder(
                        R.mipmap.prev, "prev",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)).build());
        if (state == PlaybackState.STATE_PLAYING) {
            builder.addAction(new androidx.core.app.NotificationCompat.Action.Builder(
                    R.mipmap.pause, "pause",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                            PlaybackStateCompat.ACTION_PAUSE)).build());
        } else{
            builder.addAction(new androidx.core.app.NotificationCompat.Action.Builder(
                    R.mipmap.play, "play",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                            PlaybackStateCompat.ACTION_PLAY)).build());
        }
        builder.addAction(new androidx.core.app.NotificationCompat.Action.Builder(
                        R.mipmap.next, "next",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT)).build())
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(MediaSessionUtil.getInstance().getMediaSession().getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setSmallIcon(IconCompat.createWithBitmap(description.getIconBitmap()));
        }
        notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_HIGH_PRIORITY;
        if (isCreate) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, notification);
        } else {
            service.startForeground(NOTIFICATION_ID, notification);
            isCreate = true;
        }

    }

    public static boolean isIsCreate() {
        return isCreate;
    }

    public static void setIsCreate(boolean isCreate) {
        NotificationUtil.isCreate = isCreate;
    }
}
