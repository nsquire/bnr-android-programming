package com.nick.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by nsquire on 7/22/14.
 */
public class VideoPlayer {

    private MediaPlayer mPlayer;

    public void play(Context context, SurfaceHolder s) {
        // Check if paused
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            return;
        }

        stop();

        mPlayer = new MediaPlayer();
        mPlayer.setDisplay(s);
        try {
            mPlayer.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sample_mpeg4));
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("AudioPlayer", "In onError....");
                return false;
            }
        });

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("AudioPlayer", "In onPrepared....");
                if (mPlayer != null) {
                    mPlayer.start();
                }
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("AudioPlayer", "In onCompletion....");
                stop();
            }
        });
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
