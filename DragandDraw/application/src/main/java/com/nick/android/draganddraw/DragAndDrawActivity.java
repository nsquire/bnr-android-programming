package com.nick.android.draganddraw;

import android.support.v4.app.Fragment;

public class DragAndDrawActivity extends FragmentHostingActivity {
    @Override
    protected Fragment createFragment() {
        return new DragAndDrawFragment();
    }
}
