package com.nick.android.nerdLauncher;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RunningTasksFragment extends Fragment implements AbsListView.OnItemClickListener {
    private static final String TAG = "RunningTasksFragment";

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAppsArrayAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RunningTasksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);

        Log.i(TAG, "I've found " + runningTasks.size() + " tasks");

        Collections.sort(runningTasks, new Comparator<ActivityManager.RunningTaskInfo>() {
            @Override
            public int compare(ActivityManager.RunningTaskInfo lhs, ActivityManager.RunningTaskInfo rhs) {
                return String.CASE_INSENSITIVE_ORDER.compare(
                        lhs.baseActivity.getClassName(),
                        rhs.baseActivity.getClassName());
            }
        });

        // Change Adapter to display your content
        mAppsArrayAdapter = new ArrayAdapter<ActivityManager.RunningTaskInfo>(getActivity(), R.layout.list_app_item, runningTasks) {
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = convertView;
                if (null == v) {
                    v = View.inflate(getActivity(), R.layout.list_app_item, null);
                }

                ActivityManager.RunningTaskInfo runningTask = getItem(position);

                // Set the task name
                TextView textView = (TextView) v.findViewById(R.id.appName);
                textView.setText(runningTask.baseActivity.getClassName());

                return v;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAppsArrayAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActivityManager.RunningTaskInfo runningTask = (ActivityManager.RunningTaskInfo) parent.getAdapter().getItem(position);

        if (null == runningTask) {
            return;
        }

        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(runningTask.id, ActivityManager.MOVE_TASK_WITH_HOME);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
}
