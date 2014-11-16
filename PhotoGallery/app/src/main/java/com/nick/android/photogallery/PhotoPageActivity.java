package com.nick.android.photogallery;

import android.support.v4.app.Fragment;

public class PhotoPageActivity extends FragmentHostingActivity {
    @Override
    protected Fragment createFragment() {
        return new PhotoPageFragment();
    }
}
