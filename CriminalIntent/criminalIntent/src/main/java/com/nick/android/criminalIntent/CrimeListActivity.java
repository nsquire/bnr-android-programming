package com.nick.android.criminalIntent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by nsquire on 4/27/14.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detailFragmentContainer) == null) {
            // Start an instance of CrimePagerActivity
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(intent);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment oldDetail = fragmentManager.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            if (oldDetail != null) {
                fragmentTransaction.remove(oldDetail);
            }

            fragmentTransaction.add(R.id.detailFragmentContainer, newDetail);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CrimeListFragment crimeListFragment = (CrimeListFragment)
                fragmentManager.findFragmentById(R.id.fragmentContainer);
        crimeListFragment.updateUI();
    }
}
