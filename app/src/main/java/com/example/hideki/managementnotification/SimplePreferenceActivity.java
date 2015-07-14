package com.example.hideki.managementnotification;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Mercury on 2015/07/14.
 */
public class SimplePreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SimplePreferenceFragment()).commit();
    }
}
