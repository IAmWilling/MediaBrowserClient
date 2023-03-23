package com.mt.mediasessiondemo1;

import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompat;

public class MediaSessionUtil {
    private static MediaSessionUtil INSTANCE;
    public static MediaSessionUtil getInstance(){
        synchronized (MediaSessionUtil.class) {
            if(INSTANCE == null) {
                INSTANCE = new MediaSessionUtil();
            }
        }
        return INSTANCE;
    }

    private MediaSessionCompat mediaSession;

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    public void setMediaSession(MediaSessionCompat mediaSession) {
        this.mediaSession = mediaSession;
    }
}
