package com.nick.android.remoteControl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;


public class RemoteControlActivity extends FragmentHostingActivity {

    @Override
    protected Fragment createFragment() {
        return new RemoteControlFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }
}
