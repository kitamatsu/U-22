package com.example.hideki.managementnotification;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

/**
 * Created by Mercury on 2015/07/14.
 */
public  class SimplePreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String USER_NAME_KEY = "editText_userName";
    private static final String PASSWORD_KEY = "editText_password";
    private static final String CHILD_NAME_KEY = "editText_childName";

    private static final String USER_SUMMARY = "ユーザー名を入力してください";
    private static final String PASS_SUMMARY = "パスワードを入力してください";
    private static final String CHILD_SUMMARY = "子機名を入力してください";

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
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, String key) {

        Log.d("SPF", "onSharedPreferenceChanged");

        Preference pre = findPreference(key);
        switch (key)
        {
            case USER_NAME_KEY:
                pre.setSummary(sharedPreferences.getString(key, USER_SUMMARY));
                break;

            case PASSWORD_KEY:
                pre.setSummary(sharedPreferences.getString(key, PASS_SUMMARY).replaceAll(".", "*"));
                break;

            case CHILD_NAME_KEY:
                pre.setSummary(sharedPreferences.getString(key, CHILD_SUMMARY));
                break;

            default:
                break;
        }

        //account参照、挿入処理
        final String username = sharedPreferences.getString(USER_NAME_KEY, USER_SUMMARY);
        final String pass = sharedPreferences.getString(PASSWORD_KEY, PASS_SUMMARY);
        final String child = sharedPreferences.getString(CHILD_NAME_KEY, CHILD_SUMMARY);

        //usernameとpassとchildが入力されているか
        if(!username.equals(USER_SUMMARY)
                &  !pass.equals(PASS_SUMMARY) &  !child.equals(CHILD_SUMMARY) ){
            Log.d("SPF", username);
            Log.d("SPF", pass);
            Log.d("SPF", child);

            //accountが入っているかどうか
            if(sharedPreferences.getInt("isAccount", -1) ==  -1 | key.equals(USER_NAME_KEY) | key.equals(PASSWORD_KEY)) {
                Log.d("SPF", String.valueOf(sharedPreferences.getInt("isAccount", -1)));
                Log.d("SPF", "アカウント参照");


                //参照
                new AsyncTask<Void, Void, MobileServiceTable<AccountMobile>>() {

                    MobileServiceClient mClient;
                    ChildAdapter mAdapter;

                    @Override
                    protected MobileServiceTable<AccountMobile> doInBackground(Void... params) {

                        Log.d("SPF", "doInBackGround: AccountMobile");

                        try {

                            mClient = new MobileServiceClient("https://mnmobile.azure-mobile.net/",
                                    "FzelBAxIDNvLBsVazacMeokCyNybYI94",
                                    getActivity());

                            //全部
                            MobileServiceTable<AccountMobile> acTable = mClient.getTable("AccountMobile", AccountMobile.class);
                            MobileServiceList<AccountMobile> result = acTable.execute().get();

                            //usernameとpassが一致する行を探す
                            for (AccountMobile item : result) {
                                if (item.getUsername().equals(username) & item.getPassword().equals(pass)) {
                                    sharedPreferences.edit().putInt("isAccount", item.getAccountid()).commit();
                                    Log.d("SPF", String.valueOf(sharedPreferences.getInt("isAccount", -1)));
                                    break;
                                }

                            }

                            //挿入
                            Log.d("SPF", "データ挿入");
                            Log.d("SPF", String.valueOf(sharedPreferences.getInt("isAccount", -1)));

                            //挿入するデータの作成
                            final ChildMobile cm = new ChildMobile();
                            cm.setChildname(child);
                            cm.setComplete(false);
                            cm.setAccountid(sharedPreferences.getInt("isAccount", -1));

                            mAdapter = new ChildAdapter(getActivity(), 0);
                            MobileServiceTable<ChildMobile> childTable = mClient.getTable("childMobile", ChildMobile.class);

                            childTable.insert(cm).get();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.add(cm);
                                    }
                                });

                        } catch (Exception e) {
                            Log.d("SPF", e.getMessage());
                        }
                        return null;
                    }
                }.execute();


            }else if(key.equals(CHILD_NAME_KEY)){
                Log.d("SPF", "更新");
                Log.d("SPF", String.valueOf(sharedPreferences.getInt("isAccount", -1)));
            }


        }else{
            Log.d("SPF", "全て入っていない");
        }



    }
}
