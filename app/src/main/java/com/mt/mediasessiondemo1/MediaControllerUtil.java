package com.mt.mediasessiondemo1;

import android.media.session.MediaController;
import android.support.v4.media.session.MediaControllerCompat;

public class MediaControllerUtil {
    private static MediaControllerUtil INSTANCE;
    public static MediaControllerUtil getInstance(){
        synchronized (MediaControllerUtil.class) {
            if(INSTANCE == null) {
                INSTANCE = new MediaControllerUtil();
            }
        }
        return INSTANCE;
    }

    private MediaControllerCompat controller;

    public MediaControllerCompat getController() {
        return controller;
    }

    public void setController(MediaControllerCompat controller) {
        this.controller = controller;
    }
}
