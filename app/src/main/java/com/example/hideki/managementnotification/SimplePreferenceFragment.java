package com.example.hideki.managementnotification;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
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


                new AsyncTask<Void, Void, Void>() {

                    MobileServiceClient mClient;
                    ChildAdapter mAdapter;

                    @Override
                    protected Void doInBackground(Void... params) {

                        Log.d("SPF", "doInBackGround: AccountMobile");

                        try {

                            boolean isChildTable = false; //false = ない

                            mClient = new MobileServiceClient("https://mnmobile.azure-mobile.net/",
                                    "FzelBAxIDNvLBsVazacMeokCyNybYI94",
                                    getActivity());
                            mAdapter = new ChildAdapter(getActivity(), 0);

                            MobileServiceTable<ChildMobile> cct = mClient.getTable("childMobile", ChildMobile.class);
                            MobileServiceList<ChildMobile> ccl = cct.execute().get();

                            //子機名を更新
                            for(final ChildMobile ch : ccl){
                                if(Build.SERIAL.equals(ch.getSerialid())){
                                    ch.setChildname(child);

                                    cct.update(ch).get();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.remove(ch);
                                            //refreshItemsFromTable();
                                        }
                                    });

                                    isChildTable = true;
                                    break;
                                }
                            }

                            if(!isChildTable){
                                //------------参照と挿入-----------------
                                MobileServiceTable<AccountMobile> acTable = mClient.getTable("AccountMobile", AccountMobile.class);
                                MobileServiceList<AccountMobile> result = acTable.execute().get();
                                int accountId = -1;

                                //usernameとpassが一致する行を探す
                                for (AccountMobile item : result) {
                                    if (item.getUsername().equals(username) & item.getPassword().equals(pass)) {
                                        accountId = item.getAccountid();
                                        break;
                                    }
                                }

                                if(accountId == -1)
                                {
                                    Log.d("SPF", "return");
                                    return null;
                                }

                                Log.d("SPF", "データ挿入");

                                //挿入するデータの作成
                                final ChildMobile cm = new ChildMobile();
                                cm.setChildname(child);
                                cm.setComplete(false);
                                cm.setAccountid(accountId);

                                MobileServiceTable<ChildMobile> childTable = mClient.getTable("childMobile", ChildMobile.class);

                                childTable.insert(cm).get();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.add(cm);
                                    }
                                });
                                //--------------------------------------
                            }

                        } catch (Exception e) {
                            Log.d("SPF", e.getMessage());
                        }
                        return null;
                    }
                }.execute();

        }else{
            Log.d("SPF", "全て入っていない");
        }



    }

}
