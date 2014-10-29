package com.nick.android.nerdLauncher;

import android.support.v4.app.Fragment;


public class NerdLauncherActivity extends FragmentHostingActivity {

    @Override
    protected Fragment createFragment() {
        return new RunningTasksFragment();
    }
}
