package com.example.hideki.managementnotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

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
        if(usernamePref.getText() != null)usernamePref.setSummary(usernamePref.getText());

        EditTextPreference passwordPref = (EditTextPreference) findPreference(PASSWORD_KEY);
        if(passwordPref.getText() != null)passwordPref.setSummary(passwordPref.getText().replaceAll(".", "*"));

        EditTextPreference childnamePref = (EditTextPreference) findPreference(CHILD_NAME_KEY);
        if(childnamePref.getText() != null)childnamePref.setSummary(childnamePref.getText());


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

               new AsyncTask<Void, Void, Integer>() {

                    MobileServiceClient mClient;
                    ChildAdapter mAdapter;

                    @Override
                    protected Integer doInBackground(Void... params) {

                        int accountId = -1;
                        Log.d("SPF", "doInBackGround: AccountMobile");

                        try {

                            boolean isChildTable = false; //false = ない

                            mClient = new MobileServiceClient("https://mnmobile.azure-mobile.net/",
                                    "FzelBAxIDNvLBsVazacMeokCyNybYI94",
                                    getActivity());
                            mAdapter = new ChildAdapter(getActivity(), 0);

                            MobileServiceTable<AccountMobile> acTable = mClient.getTable("AccountMobile", AccountMobile.class);
                            MobileServiceList<AccountMobile> result = acTable.execute().get();


                            //usernameとpassが一致する行を探す
                            for (AccountMobile item : result) {
                                if (item.getUsername().equals(username) & item.getPassword().equals(pass)) {
                                    accountId = item.getAccountid();
                                    break;
                                }
                            }

                            SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            if(accountId == -1)
                            {
                                Log.d("SPF", "username or passが違う");
                                return -1;//error
                            }


                            //子機名を更新
                            MobileServiceTable<ChildMobile> cct = mClient.getTable("childMobile", ChildMobile.class);
                            MobileServiceList<ChildMobile> ccl = cct.execute().get();

                            for(final ChildMobile ch : ccl){
                                if(Build.SERIAL.equals(ch.getSerialid())){

                                    if(ch.getChildname().equals(child)){
                                        //正常終了
                                        return 2;
                                    }

                                    ch.setChildname(child);

                                    cct.update(ch).get();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.remove(ch);
                                        }
                                    });
                                    Log.d("SPF", "子機名更新");
                                    isChildTable = true;
                                    return 1;//正常終了
                                }
                            }

                            //更新できない　＝　データが入ってない
                            if(!isChildTable){

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

                            }

                        } catch (Exception e) {
                            Log.d("SPF", e.getMessage());
                            return -2; //error
                        }
                        return 2;//正常終了
                    }

                   @Override
                   protected void onPostExecute(Integer i) {
                       switch (i){
                           case -1:
                               Toast.makeText(getActivity(), "ユーザ名または,パスワードが違います", Toast.LENGTH_SHORT).show();
                               break;
                           case 1:
                               Toast.makeText(getActivity(), "子機名を更新しました", Toast.LENGTH_SHORT).show();
                               break;
                           case 2:
                               Toast.makeText(getActivity(), "接続しました", Toast.LENGTH_SHORT).show();
                               break;
                           case -2:
                               Toast.makeText(getActivity(), "エラーが発生しました", Toast.LENGTH_SHORT).show();
                               break;
                       }
                   }

               }.execute();

        }else {
            Log.d("SPF", "全て入っていない");
        }



    }

}
