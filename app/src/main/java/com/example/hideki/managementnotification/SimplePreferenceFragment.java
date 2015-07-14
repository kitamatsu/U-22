package com.example.hideki.managementnotification;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Mercury on 2015/07/14.
 */
public class SimplePreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
