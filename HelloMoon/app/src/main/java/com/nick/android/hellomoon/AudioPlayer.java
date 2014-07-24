package com.nick.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by nsquire on 7/22/14.
 */
public class AudioPlayer {

    private MediaPlayer mPlayer;

    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Context context) {
        // Check if paused
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            return;
        }

        stop();

        mPlayer = MediaPlayer.create(context, R.raw.one_small_step);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });

        mPlayer.start();
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

}
