package com.mt.mediasessiondemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class MusicIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {

            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(
                    Intent.EXTRA_KEY_EVENT);
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    MediaControllerUtil.getInstance().getController()
                            .getTransportControls()
                            .pause();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    MediaControllerUtil.getInstance().getController()
                            .getTransportControls()
                            .play();
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    MediaControllerUtil.getInstance().getController()
                            .getTransportControls()
                            .skipToNext();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    MediaControllerUtil.getInstance().getController()
                            .getTransportControls()
                            .skipToPrevious();
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    MediaControllerUtil.getInstance().getController()
                            .getTransportControls()
                            .stop();
                    break;
                default:
                    break;
            }
        }
    }
}
