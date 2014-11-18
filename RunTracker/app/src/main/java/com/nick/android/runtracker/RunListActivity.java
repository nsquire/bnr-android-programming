package com.nick.android.runtracker;

import android.support.v4.app.Fragment;


public class RunListActivity extends FragmentHostingActivity {

    @Override
    protected Fragment createFragment() {
        return new RunListFragment();
    }
}
