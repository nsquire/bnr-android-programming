package com.nick.android.runtracker;

import android.support.v4.app.Fragment;


public class RunActivity extends FragmentHostingActivity {

    @Override
    protected Fragment createFragment() {
        return new RunFragment();
    }

}
