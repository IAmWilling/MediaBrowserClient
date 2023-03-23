package com.mt.mediasessiondemo1;

import android.media.browse.MediaBrowser;
import android.support.v4.media.MediaBrowserCompat;

import java.util.ArrayList;
import java.util.List;

public class MusicCache {
    private static MusicCache INSTANCE;

    public static MusicCache getInstance() {
        synchronized (MediaControllerUtil.class) {
            if (INSTANCE == null) {
                INSTANCE = new MusicCache();
            }
        }
        return INSTANCE;
    }

    private List<MediaBrowserCompat.MediaItem> musicList = new ArrayList<>();
    private int currentIdx = 0;

    public List<MediaBrowserCompat.MediaItem> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MediaBrowserCompat.MediaItem> musicList) {
        this.musicList = musicList;
    }

    public int getCurrentIdx() {
        return currentIdx;
    }

    public void setCurrentIdx(int currentIdx) {
        this.currentIdx = currentIdx;
    }

    public boolean autoAddIdx() {
        if (currentIdx + 1 >= musicList.size()) {
            return false;
        }
        currentIdx += 1;
        return true;
    }

    public boolean autoSubtractIdx() {
        if (currentIdx - 1 < 0) {
            return false;
        }
        currentIdx -= 1;
        return true;
    }
}
