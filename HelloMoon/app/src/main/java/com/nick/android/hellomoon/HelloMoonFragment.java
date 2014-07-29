package com.nick.android.hellomoon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelloMoonFragment extends Fragment {


    private VideoPlayer mPlayer = new VideoPlayer();
    private Button mPlayButton;
    private Button mPauseButton;
    private Button mStopButton;
    private SurfaceView mVideoSurfaceView;
    private SurfaceHolder mVideoSurfaceHolder;


    public HelloMoonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hello_moon, container, false);

        mVideoSurfaceView = (SurfaceView) v.findViewById(R.id.helloMoon_videoSurfaceView);
        mVideoSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d("HelloMoonFragment", "surfaceCreated()");
                mVideoSurfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("HelloMoonFragment", "surfaceChanged()");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("HelloMoonFragment", "surfaceDestroyed()");
            }
        });

        mPlayButton = (Button) v.findViewById(R.id.helloMoon_playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HelloMoonFragment", "Created holder:" + mVideoSurfaceHolder.toString());
                mPlayer.play(getActivity(), mVideoSurfaceHolder);
            }
        });

        mPauseButton = (Button) v.findViewById(R.id.helloMoon_pauseButton);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
            }
        });

        mStopButton = (Button) v.findViewById(R.id.helloMoon_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
            }
        });

        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }


}
