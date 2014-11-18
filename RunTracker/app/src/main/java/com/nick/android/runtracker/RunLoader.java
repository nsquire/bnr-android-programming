package com.nick.android.runtracker;


import android.content.Context;

public class RunLoader extends DataLoader<Run> {
    private long mRunId;

    public RunLoader(Context context, long runId) {
        super(context);
        mRunId = runId;
    }

    @Override
    public Run loadInBackground() {
        return RunManager.getInstance(getContext()).getRun(mRunId);
    }
}
