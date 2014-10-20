package com.nick.android.criminalIntent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.nick.android.criminalIntent.image_path";
    public static final String EXTRA_IMAGE_ROTATION = "com.nick.android.criminalIntent.image_orientation";

    private ImageView mImageView;

    public static ImageFragment newInstance(String imagePath, int orientation) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        args.putInt(EXTRA_IMAGE_ROTATION, orientation);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);
        mImageView.setImageDrawable(bitmapDrawable);

        // Rotate picture to same orientation it was taken
        int rotation = getArguments().getInt(EXTRA_IMAGE_ROTATION, Surface.ROTATION_0);
        if (Build.VERSION.SDK_INT < 11) {
            RotateAnimation rotateAnimation = null;
            switch (rotation) {
                case Surface.ROTATION_0:
                    rotateAnimation = new RotateAnimation(0, 90);
                    break;
                case Surface.ROTATION_90:
                    // Do nothing, keep at 0
                    break;
                case Surface.ROTATION_180:
                    rotateAnimation = new RotateAnimation(0, 90); // never hits here for Nexus 5
                    break;
                case Surface.ROTATION_270:
                    rotateAnimation = new RotateAnimation(0, 180);
                    break;
            }

            if (rotateAnimation != null) {
                rotateAnimation.setDuration(100);
                rotateAnimation.setFillAfter(true);
                mImageView.startAnimation(rotateAnimation);
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    mImageView.setRotation(90);
                    break;
                case Surface.ROTATION_90:
                    mImageView.setRotation(0);
                    break;
                case Surface.ROTATION_180:
                    mImageView.setRotation(90); // never hits here for Nexus 5
                    break;
                case Surface.ROTATION_270:
                    mImageView.setRotation(180);
                    break;
            }
        }

        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }
}
