package com.nick.android.hellomoon;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelloMoonFragment extends Fragment {


    private VideoView mVideoView;
    private MediaController mMediaController;


    public HelloMoonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hello_moon, container, false);

        mVideoView = (VideoView) v.findViewById(R.id.helloMoon_videoView);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getApplicationContext().getPackageName() + "/" + R.raw.sample_mpeg4));

        mMediaController = new MediaController(getActivity());
        mMediaController.setAnchorView(mVideoView);

        mVideoView.setMediaController(mMediaController);
        mVideoView.start();

        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }


}
