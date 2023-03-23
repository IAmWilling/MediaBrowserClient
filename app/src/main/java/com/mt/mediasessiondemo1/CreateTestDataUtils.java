package com.mt.mediasessiondemo1;

import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.List;

public class CreateTestDataUtils {
    public static List<MediaBrowserCompat.MediaItem> createTestData(){
        List<MediaBrowserCompat.MediaItem> list= new ArrayList<>();
        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE,"测试标题")
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,"测试专辑")
                .putBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON, BitmapFactory.decodeResource(App.getContext().getResources(),R.drawable.qccc))
                .putString(MediaMetadata.METADATA_KEY_MEDIA_ID,"http://music.163.com/song/media/outer/url?id=447925558.mp3")
                .putLong(MediaMetadata.METADATA_KEY_DURATION,22222)
                .build();
        MediaMetadataCompat mediaMetadata2 = new MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE,"那些花儿")
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,"专辑 - 夏天消息")
                .putBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON, BitmapFactory.decodeResource(App.getContext().getResources(),R.drawable.qqq))
                .putString(MediaMetadata.METADATA_KEY_MEDIA_ID,"http://music.163.com/song/media/outer/url?id=447925558.mp3")
                .putLong(MediaMetadata.METADATA_KEY_DURATION,22222)
                .build();
        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaMetadata.getDescription(),MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        MediaBrowserCompat.MediaItem mediaItem2 = new MediaBrowserCompat.MediaItem(mediaMetadata2.getDescription(),MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        list.add(mediaItem);
        list.add(mediaItem2);
        return list;
    }
}
