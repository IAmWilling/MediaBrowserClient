package com.mt.mediasessiondemo1;

public interface IMediaController {
    void play();
    void pause();
    void stop();
    void next();
    void prev();
    void seekTo(long pos);
}
