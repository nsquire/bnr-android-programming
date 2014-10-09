package com.nick.android.criminalIntent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by nsquire on 4/26/14.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private static CrimeLab sCrimeLab;
    private CriminalIntentJSONSerializer mCriminalIntentJSONSerializer;
    private ArrayList<Crime> mCrimes;
    private Context mAppContext;

    private CrimeLab(Context context) {
        mAppContext = context;
        mCriminalIntentJSONSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);

        try {
            mCrimes = mCriminalIntentJSONSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.d(TAG, "Error loading crimes: ", e);
        }

        // To create a set of random crimes for testing
//        for (int i = 0; i < 100; i++) {
//            Crime c = new Crime();
//            c.setTitle("Crime #" + i);
//            c.setSolved(i % 2 == 0);
//            mCrimes.add(c);
//        }
    }

    public static CrimeLab getInstance(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }

        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime) {
        mCrimes.remove(crime);
    }

    public boolean saveCrimes() {
        try {
            mCriminalIntentJSONSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "Crimes saved to a file");
            return true;
        } catch (Exception exception) {
            Log.e(TAG, "Error saving crimes: ", exception);
            return false;
        }
    }
}
