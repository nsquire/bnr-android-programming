package com.nick.android.criminalIntent;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_ID = "com.nick.android.criminalIntent.crime_id";
    private static final String TAG = "CrimeFragment";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final String DIALOG_IMAGE = "image";

    private Crime mCrime;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private EditText mTitleEditText;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public CrimeFragment() {
        // Required empty public constructor
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);

        return crimeFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo photo = mCrime.getPhoto();
                if (photo == null) {
                    return;
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path, photo.getRotation()).show(fragmentManager, DIALOG_IMAGE);
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // Use floating context menus on Froyo and Gingerbread
            registerForContextMenu(mPhotoView);
        } else {
            mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mCrime.getPhoto() == null) {
                        return false;
                    }

                    getActivity().startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.crime_list_item_context, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            // Required, but not used in this implementation
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_item_delete_crime:
                                    deleteCurrentPhoto();
                                    mPhotoView.setImageDrawable(null);
                                    mode.finish();
                                    return true;
                            }

                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            // Required, but not used in this implementation
                        }
                    });

                    return true;
                }
            });
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });

        // If camera is not available, disable camera functionality
        PackageManager packageManager = getActivity().getPackageManager();
        boolean hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD &&
                        Camera.getNumberOfCameras() > 0);

        if (!hasCamera) {
            mPhotoButton.setEnabled(false);
        }

        mTitleEditText = (EditText) v.findViewById(R.id.crimeTitle);
        mTitleEditText.setText(mCrime.getTitle());
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crimeDate);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fragmentManager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crimeSolved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            // Create a new Photo object and attach it to the crime
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);

            // Determine device orientation, have to do this here since camera activity is locked in landscape
            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();

            if (filename != null) {
                // Delete previous photo file if there is one
                deleteCurrentPhoto();
                Photo photo = new Photo(filename);
                photo.setRotation(rotation);
                mCrime.setPhoto(photo);
                showPhoto();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (mCrime.getPhoto() == null) {
            return;
        }

        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                deleteCurrentPhoto();
                mPhotoView.setImageDrawable(null);
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private void showPhoto() {
        // (Re)set the image button's image based on our photo
        Photo photo = mCrime.getPhoto();
        BitmapDrawable bitmapDrawable = null;
        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);

            // Rotate picture to same orientation it was taken
            if (Build.VERSION.SDK_INT < 11) {
                RotateAnimation rotateAnimation = null;
                switch (photo.getRotation()) {
                    case Surface.ROTATION_0:
                        rotateAnimation = new RotateAnimation(0, 90);
                        break;
                    case Surface.ROTATION_90:
                        // Do nothing, keep at 0
                        break;
                    case Surface.ROTATION_180:
                        rotateAnimation = new RotateAnimation(0, 90); // never hits here for Nexus 5
                        break;
                    case Surface.ROTATION_270:
                        rotateAnimation = new RotateAnimation(0, 180);
                        break;
                }

                if (rotateAnimation != null) {
                    rotateAnimation.setDuration(100);
                    rotateAnimation.setFillAfter(true);
                    mPhotoView.startAnimation(rotateAnimation);
                }
            } else {
                switch (photo.getRotation()) {
                    case Surface.ROTATION_0:
                        mPhotoView.setRotation(90);
                        break;
                    case Surface.ROTATION_90:
                        mPhotoView.setRotation(0);
                        break;
                    case Surface.ROTATION_180:
                        mPhotoView.setRotation(90); // never hits here for Nexus 5
                        break;
                    case Surface.ROTATION_270:
                        mPhotoView.setRotation(180);
                        break;
                }
            }
        }

        mPhotoView.setImageDrawable(bitmapDrawable);
    }

    private void deleteCurrentPhoto() {
        if (mCrime.getPhoto() != null && !mCrime.getPhoto().getFilename().equals("")) {
            String previousFilename = mCrime.getPhoto().getFilename();
            Log.d(TAG, "File to delete: " + getActivity().getFilesDir() + "/" + previousFilename);
            if (getActivity().deleteFile(previousFilename)) {
                Log.d(TAG, "Delete success");
            }
        }

        // Set current photo for crime to null since we just deleted the file
        mCrime.setPhoto(null);
    }
}
