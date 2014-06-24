package com.nick.android.criminalIntent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class ChooserDialogFragment extends DialogFragment {
    public static final String EXTRA_DATE_TIME = "com.nick.android.criminalIntent.dateTime";
    private static final int REQUEST_DATE = -1;
    private static final int REQUEST_TIME = -2;
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";

    private Button mDateButton;
    private Button mTimeButton;
    private Date mDateTime;

    public static ChooserDialogFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE_TIME, date);

        ChooserDialogFragment chooserDialogFragment = new ChooserDialogFragment();
        chooserDialogFragment.setArguments(args);

        return chooserDialogFragment;
    }

    ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDateTime = (Date) getArguments().getSerializable(EXTRA_DATE_TIME);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_chooser, null);

        mDateButton = (Button) v.findViewById(R.id.dateButton);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mDateTime);
                datePickerFragment.setTargetFragment(ChooserDialogFragment.this, REQUEST_DATE);
                datePickerFragment.show(fragmentManager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button) v.findViewById(R.id.timeButton);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mDateTime);
                timePickerFragment.setTargetFragment(ChooserDialogFragment.this, REQUEST_TIME);
                timePickerFragment.show(fragmentManager, DIALOG_TIME);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.crime_date_time_chooser_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            mDateTime = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        }

        if (requestCode == REQUEST_TIME) {
            mDateTime = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
        }
    }

    public void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_TIME, mDateTime);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
