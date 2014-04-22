package com.nick.android.criminalIntent;

import java.util.UUID;

/**
 * Created by nsquire on 4/18/14.
 */
public class Crime {

    private UUID mId;
    private String mTitle;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public UUID getmId() {
        return mId;
    }

    public Crime () {
        // Generate unique identifier
        mId = UUID.randomUUID();
    }
}
