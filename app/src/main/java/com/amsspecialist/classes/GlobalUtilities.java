package com.amsspecialist.classes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.amsspecialist.R;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class GlobalUtilities {
    static final String SERVER_URL = "http://www.amsspecialist.com/gcm_androidapp";
    //public static final String PREFS_NAME = "AmsSpecialist_Preferences";
    public static final String URLLOGIN = "http://www.amsspecialist.com/login.php";
    public static final String URLAVATAR = "http://www.amsspecialist.com/android_avatar.php?id=";
    public static final String URL_FORUM = "url_forum";
    public static final String SENDER_ID = "307800336459";
    public static final String[] _mensages = {"",
            "Usuario No Valido , intentalo de nuevo", "Registro finalizado"};
    public static final String URLNEWPOST = "http://www.amsspecialist.com/Unread_post_android.php?id=";
    static final String URLPOST = "http://www.amsspecialist.com/unread_count.php?id=";
    static final String URLMPS = "http://www.amsspecialist.com/newmps.php?id=";
    public static final String RSSURL = "http://www.amsspecialist.com/feed.php?";
    public static final String TAG = "AMSSpecialist CGM";
    public static final String DISPLAY_MESSAGE_ACTION = "com.amsspecialist.DISPLAY_MESSAGE";
    public static String URLAMSFILEBASE = "https://dl.dropbox.com/u/27007640/";
    static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    private static AsyncTask<Void, Void, Void> mRegisterTask;
    static ConnectivityManager connectivityManager;
    static NetworkInfo wifiInfo, mobileInfo;
    public static final String NEWPOST = "newpost";
    public static final String NEWMPS = "newmps";
    public static String URLDATAINI = "http://www.amsspecialist.com/AMSFILES/dropfiles.dat";
    //   Global preferences..

    public static SharedPreferences.Editor editSharePrefs(Context con) {

        SharedPreferences.Editor editor = getPrefs(con).edit();

        return editor;
    }

    public static SharedPreferences getPrefs(Context con) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(con);

        return settings;
    }

    public static void displayMessage(Context context, Bundle bund) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtras(bund);
        context.sendBroadcast(intent);
    }

    //TestConnection

    public static Boolean TestConnection(Context con) {
        try {
            connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }


//login :

    public static Boolean login(Context con, String username, String password) {
        Login lg = new Login(con);

        try {
            JSONObject obj = lg.execute(username, password).get();
            // Toast.makeText(getApplicationContext(),
            // obj.getString("login"),Toast.LENGTH_SHORT).show();
            if (obj != null) {
                // Log.d("Login bool", "no null");

                if (obj.getString("login").equals("LOGIN_SUCCESS")) {
                    Log.d("Login bool", obj.getString("login").toString());
                    return true;
                }
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static void unlogin(Context con) {
        editSharePrefs(con)
                .putBoolean("login", false)
                .putString("user", con.getString(R.string.no_user_login))
                .putString("avatar", "http://www.amsspecialist.com/images/unknown.jpg")
                .putString("password", "")
                .putInt("newmps", 0)
                .putInt("newpost", 0)
                .commit();
        GCMRegistrar.unregister(con);

    }

    public static ContentValues checkNewdata(Context con) {

        ContentValues cn = null;
        String username = getPrefs(con).getString("user", "");
        CheckNewData ch = new CheckNewData(con);
        try {
            cn = ch.execute(URLPOST + username, URLMPS + username).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cn;

    }


    //Server And GCM utilities:

    public static void checkRegisterGCM(Context con) {
        String regID = "";
        String user = getPrefs(con).getString("username", "");
        GCMRegistrar.checkDevice(con);
        GCMRegistrar.checkManifest(con);

        regID = GCMRegistrar.getRegistrationId(con);

        if (regID == "") {

            GCMRegistrar.register(con, GlobalUtilities.SENDER_ID);

        } else {
            if (GCMRegistrar.isRegisteredOnServer(con)) {
                // Skips registration.
                //Log.d(TAG, "Already registered with GCM");

            } else {

                final Context context = con;

                final String userName = user;
                final String id = regID;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        Serverregister(context, userName, id);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }

        }
    }

    public static void Serverregister(final Context context, String name, final String regId) {
        Log.i(GlobalUtilities.TAG, "registering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/register.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("name", name);


        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            //Log.d(CommonUtilities.TAG, "Attempt #" + i + " to register");
            try {
                //Log.i(CommonUtilities.TAG,"Registrando...");
                post(serverUrl, params);

                GCMRegistrar.setRegisteredOnServer(context, true);
                GlobalUtilities.editSharePrefs(context).putString("regid", regId);
                //Log.i(CommonUtilities.TAG, "Registered in server");
                return;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(GlobalUtilities.TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    //Log.e(CommonUtilities.TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    //Log.i(CommonUtilities.TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        GCMRegistrar.setRegisteredOnServer(context, false);
        //Log.e(GlobalUtilities.TAG, "Error in registered");
    }


    public static void Serverunregister(final Context context, final String regId) {
        //Log.i(CommonUtilities.TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = GlobalUtilities.SERVER_URL + "/unregister.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);

            Log.d(GlobalUtilities.TAG, "Unregister");
        } catch (IOException e) {


            Log.e(GlobalUtilities.TAG, "Error : " + e.getMessage());
        }
    }


    private static void post(String endpoint, Map<String, String> params)
            throws IOException {

        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(GlobalUtilities.TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    public static int getTypeicon(String type) {

        /*if (type.equals("DataBases")) {
            return R.drawable.ic_db;
        } else if (type.equals("DLLs")) {
            return R.drawable.ic_dll;
        } else if (type.equals("Functions")) {
            return R.drawable.ic_script;
        } else if (type.equals("Objects")) {
            return R.drawable.ic_object;
        } else if (type.equals("Others")) {
            return R.drawable.ic_others;
        } else if (type.equals("Plugins")) {
            return R.drawable.ic_plugins;
        } else if (type.equals("Tutorials")) {
            return R.drawable.ic_pdf;
        } else if (type.equals("Visual")) {
            return R.drawable.ic_visual;

        }
        return R.drawable.ic_others;*/
        return 0;
    }
}
