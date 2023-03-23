package com.mt.mediasessiondemo1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;

import com.mt.mediasessiondemo1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private MediaBrowserCompat mediaBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(this,MediaService.class);
        startService(intent);
        mediaBrowser = new MediaBrowserCompat(this,new ComponentName(this,MediaService.class),connectionCallback,null);
        binding.bf.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == binding.bf.getId()) {
            MediaControllerUtil.getInstance()
                    .getController()
                    .getTransportControls()
                    .play();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mediaBrowser.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaBrowser.disconnect();
    }

    private final MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback(){
        @Override
        public void onConnected() {
            super.onConnected();
            //表示已经连接
            final String mediaId = mediaBrowser.getRoot();
            mediaBrowser.unsubscribe(mediaId);
            //订阅消息
            mediaBrowser.subscribe(mediaId,subscriptionCallback);
            MediaControllerCompat mediaController = null;
            try {
                mediaController = new MediaControllerCompat(App.getContext(),mediaBrowser.getSessionToken());
                MediaControllerCompat.setMediaController(MainActivity.this,mediaController);
                //保存mediaController
                MediaControllerUtil.getInstance().setController(mediaController);
                mediaController.registerCallback(mediaControllerCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

    };


    private final MediaBrowserCompat.SubscriptionCallback subscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);
            //从MediaService发送回来列表的数据
            MusicCache.getInstance().setMusicList(children);
        }
    };

    private MediaControllerCompat.Callback mediaControllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);

        }
    };


}