package com.nick.android.runtracker;


import android.content.Context;
import android.database.Cursor;

public class LocationsListCursorLoader extends SQLiteCursorLoader {
    private long mRunId;

    public LocationsListCursorLoader(Context context, long runId) {
        super(context);
        mRunId = runId;
    }

    @Override
    protected Cursor loadCursor() {
        return RunManager.getInstance(getContext()).queryLocationsForRun(mRunId);
    }
}
