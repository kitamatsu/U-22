package com.example.hideki.managementnotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by Mercury on 2015/07/14.
 */
public  class SimplePreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String USER_NAME_KEY = "editText_userName";
    private static final String PASSWORD_KEY = "editText_password";
    private static final String CHILD_NAME_KEY = "editText_childName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        EditTextPreference usernamePref = (EditTextPreference) findPreference(USER_NAME_KEY);
        usernamePref.setSummary(usernamePref.getText());

        EditTextPreference passwordPref = (EditTextPreference) findPreference(PASSWORD_KEY);
        passwordPref.setSummary(passwordPref.getText().replaceAll(".", "*"));

        EditTextPreference childnamePref = (EditTextPreference) findPreference(CHILD_NAME_KEY);
        childnamePref.setSummary(childnamePref.getText());

    }

    @Override
    public void onResume() {
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference pre = findPreference(key);
        switch (key)
        {
            case USER_NAME_KEY:
                pre.setSummary(sharedPreferences.getString(key, "ユーザー名を入力してください"));
                break;

            case PASSWORD_KEY:
                pre.setSummary(sharedPreferences.getString(key, "パスワードを入力してください").replaceAll(".","*"));
                break;

            case CHILD_NAME_KEY:
                pre.setSummary(sharedPreferences.getString(key, "子機名を入力してください"));
                break;

            default:
                break;
        }

    }
}
