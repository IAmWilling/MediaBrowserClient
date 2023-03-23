package com.mt.mediasessiondemo1;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import java.io.IOException;
import java.util.List;

public class MediaService extends MediaBrowserServiceCompat implements IMediaController {
    private final static String ROOT_ID = "meida_service_id";
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat playbackState;
    private MediaPlayer mediaPlayer;
    private final MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            pause();
        }
    };
    private final MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
            MediaSessionUtil.getInstance().getMediaSession().setMetadata(new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, getMediaItem().getMediaId())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration())
                    .build());
            setPlayState(PlaybackStateCompat.STATE_PLAYING);
            NotificationUtil.getInstance().notifyNotification(MediaService.this, getMediaItem(), playbackState.getState());
        }
    };
    private final MediaSessionCompat.Callback mediaSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            super.onPlay();
            play();
        }

        @Override
        public void onPause() {
            super.onPause();
            pause();
        }

        @Override
        public void onStop() {
            super.onStop();
            stop();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            next();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            prev();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            seekTo(pos);
        }
    };

    @Override
    public void next() {
        //自增索引
        if (MusicCache.getInstance().autoAddIdx()) {
            setPlayState(PlaybackStateCompat.STATE_STOPPED);
            play();
        }

    }

    @Override
    public void prev() {
        //自减索引
        if (MusicCache.getInstance().autoSubtractIdx()) {
            setPlayState(PlaybackStateCompat.STATE_STOPPED);
            play();
        }
    }

    @Override
    public void seekTo(long pos) {
        setPlayState(PlaybackStateCompat.STATE_PAUSED);
        mediaPlayer.seekTo(pos, MediaPlayer.SEEK_PREVIOUS_SYNC);
        play();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(MediaService.this, ROOT_ID);
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        playbackState = new PlaybackStateCompat.Builder()
                //这里指定可以接收的来自锁屏页面的按键信息
                .setActions(PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SEEK_TO | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_STOP | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build();
        mediaSession.setPlaybackState(playbackState);
        mediaSession.setCallback(mediaSessionCallback);
        setSessionToken(mediaSession.getSessionToken());
        MediaSessionUtil.getInstance().setMediaSession(mediaSession);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, new Intent(this, MusicIntentReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        MediaSessionUtil.getInstance().getMediaSession().setMediaButtonReceiver(pendingIntent);
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setOnCompletionListener(completionListener);
        mediaPlayer.setOnPreparedListener(preparedListener);
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new BrowserRoot(ROOT_ID, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        //模拟....从服务端拿到音乐
        result.sendResult(CreateTestDataUtils.createTestData());
    }

    @Override
    public void play() {
        MediaBrowserCompat.MediaItem mediaItem = getMediaItem();
        if (playbackState.getState() != PlaybackStateCompat.STATE_PAUSED) {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(mediaItem.getDescription().getMediaId());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.start();
            setPlayState(PlaybackStateCompat.STATE_PLAYING);
            NotificationUtil.getInstance().notifyNotification(this, mediaItem, playbackState.getState());
        }


    }

    @Override
    public void pause() {
        mediaPlayer.pause();
        setPlayState(PlaybackStateCompat.STATE_PAUSED);
        NotificationUtil.getInstance().notifyNotification(this, getMediaItem(), playbackState.getState());
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
        setPlayState(PlaybackStateCompat.STATE_STOPPED);
        NotificationUtil.setIsCreate(false);
        stopForeground(true);
    }

    private void setPlayState(@PlaybackStateCompat.State int state) {
        playbackState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SEEK_TO | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_STOP | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(state, mediaPlayer.getCurrentPosition(), 1.0f)
                .build();
        MediaSessionUtil.getInstance()
                .getMediaSession()
                .setPlaybackState(playbackState);
    }

    private MediaBrowserCompat.MediaItem getMediaItem() {
        int idx = MusicCache.getInstance().getCurrentIdx();
        MediaBrowserCompat.MediaItem mediaItem = MusicCache.getInstance().getMusicList().get(idx);
        return mediaItem;
    }
}