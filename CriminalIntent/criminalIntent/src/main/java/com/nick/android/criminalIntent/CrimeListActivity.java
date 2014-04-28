package com.nick.android.criminalIntent;

import android.support.v4.app.Fragment;

/**
 * Created by nsquire on 4/27/14.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
