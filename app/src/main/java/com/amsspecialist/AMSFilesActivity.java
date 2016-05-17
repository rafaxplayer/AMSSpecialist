package com.amsspecialist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amsspecialist.classes.BaseActivity;
import com.amsspecialist.classes.GlobalUtilities;
import com.amsspecialist.fragments.AMSFILesMenu_Fragment;
import com.amsspecialist.fragments.AMSFilesList_Fragment;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.TokenPair;


public class AMSFilesActivity extends BaseActivity implements AMSFILesMenu_Fragment.OnSelectedListener {
    public DropboxAPI<AndroidAuthSession> dropbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair("rn2qhqcptrj66p2","t2xlsftg8krazw7");
        String key=GlobalUtilities.getPrefs(getApplicationContext()).getString("dropkey", null);
        String secret=GlobalUtilities.getPrefs(getApplicationContext()).getString("dropsecret",null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair,token);
        } else {
            session = new AndroidAuthSession(pair);
        }
            //AccessTokenPair token = new AccessTokenPair(key, secret);
        session.setOAuth2AccessToken("M6pfh3byjrIAAAAAAAC88JbMP96jINxwKlY2_8qO4bLvXw_iZdMQyWKsUf2KgJ2Q");
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        Log.i("DbAuthLog",dropbox.toString());
        //dropbox.getSession().startAuthentication(AMSFilesActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = dropbox.getSession();
        if (session.authenticationSuccessful()) {
            try {

                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                GlobalUtilities.editSharePrefs(getApplicationContext()).putString("dropkey",tokens.key)
                .putString("dropsecret",tokens.secret).commit();
                Log.i("DbAuthLog", "Authenticating OK");

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }else{
            Log.i("DbAuthLog", "No authenticating");
        }
    }

    @Override
    protected int getLayoutResourceId() {

        return R.layout.activity_amsfiles;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_amsfiles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == 16908332) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onitemSelected(String type) {

        // Boolean haylista =(getSupportFragmentManager().findFragmentById(R.id.listamsfiles)!= null);
        AMSFilesList_Fragment fragment = (AMSFilesList_Fragment) getSupportFragmentManager().findFragmentById(R.id.listamsfiles);
        if (fragment == null || !fragment.isInLayout()) {

            Intent in = new Intent(this, AMSFilesListActivity.class);
            in.putExtra("type", type);
            startActivity(in);
        } else {
            ((AMSFilesList_Fragment) getSupportFragmentManager()
                    .findFragmentById(R.id.listamsfiles))
                    .LoadData(getApplicationContext(), type);

        }

    }


}
