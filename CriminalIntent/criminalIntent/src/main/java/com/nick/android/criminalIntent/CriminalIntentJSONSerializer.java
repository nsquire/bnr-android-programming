package com.nick.android.criminalIntent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by nsquire on 9/19/14.
 */
public class CriminalIntentJSONSerializer {

    private static final String TAG = "CriminalIntentJSONSerializer";
    private static final String mDataDirectoryName = "criminalIntentData";
    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context context, String filename) {
        mContext = context;
        mFilename = filename;
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();

        if (isExternalStorageWritable()) {
            BufferedReader reader = null;

            try {
                // Open and read the file into a StringBuilder
                FileInputStream inputStream = new FileInputStream(getStoredCrimesFilePath());
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    // Line breaks are omitted and irrelevant
                    jsonString.append(line);
                }

                // Parse the JSON using JSONTokener
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

                // Build the array of crimes from JSONObjects
                for (int i = 0; i < array.length(); i++) {
                    crimes.add(new Crime(array.getJSONObject(i)));
                }
            } catch (FileNotFoundException e) {
                // Ignore this one; it happens when starting fresh
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
        if (isExternalStorageWritable()) {
            // Build an array in JSON
            JSONArray array = new JSONArray();
            for (Crime crime : crimes) {
                array.put(crime.toJSON());
            }

            // Write the file to disk
            Writer writer = null;
            try {
                FileOutputStream out = new FileOutputStream(getStoredCrimesFilePath());
                writer = new OutputStreamWriter(out);
                writer.write(array.toString());
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }

    private String getStoredCrimesFilePath() {
        // Get the directory for the private external storage
        File sdCard = mContext.getExternalFilesDir(null);
        File dataDirectory = new File(sdCard.getAbsolutePath() + "/" + mDataDirectoryName);

        if (!dataDirectory.mkdirs()) {
            Log.d(TAG, "Directory not created");
        }

        File dataFile = new File(dataDirectory, mFilename);
        return String.valueOf(dataFile);
    }
}
