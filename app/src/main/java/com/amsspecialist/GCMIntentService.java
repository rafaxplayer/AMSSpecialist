package com.amsspecialist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.amsspecialist.classes.GlobalUtilities;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "AMSSpecialist::Service";
    private Notification notification;


    public GCMIntentService() {
        super(GlobalUtilities.SENDER_ID);
        // TODO Auto-generated constructor stub

    }


    @Override
    protected void onError(Context arg0, String intent) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onError: errorId=" + intent);
    }


    @Override
    protected void onMessage(Context context, Intent intent) {
        String newpost;
        String user;
        String notifynew;
        String notify;
        int nMsg;
        String nMps;

        newpost = intent.getStringExtra("new");
        user = intent.getStringExtra("user");
        notify = intent.getStringExtra("notify");
        notifynew = intent.getStringExtra("newnotify");
        nMsg = Integer.parseInt(intent.getStringExtra("msg"));
        nMps = intent.getStringExtra("newmps");

        if (newpost != null)
            GlobalUtilities.editSharePrefs(context).putInt("newpost", Integer.parseInt(newpost)).commit();
        //CommonUtilities.setBadge(context, Integer.parseInt(newpost));
        if (nMps != null)
            GlobalUtilities.editSharePrefs(context).putInt("newmps", Integer.parseInt(nMps)).commit();

        //Log.i(TAG, notify);
        if (Boolean.parseBoolean(notify)) {
            //Log.i(TAG, "notification true");
            NotifyShow(context, 1, R.drawable.ic_launcher, getString(R.string.app_name), user + " " + getString(R.string.notification).replace("###", notifynew));

        }


        Bundle bun = new Bundle();
        bun.putString("newpost", newpost);
        bun.putString("newMps", nMps);
        bun.putInt("mensage", nMsg);

        GlobalUtilities.displayMessage(context, bun);

    }


    @Override
    protected void onRegistered(Context arg0, String intent) {
        // TODO Auto-generated method stub

        Log.i(TAG, "onRegistered: registrationId=" + intent);

        GlobalUtilities.Serverregister(arg0, GlobalUtilities.getPrefs(arg0).getString("user", getString(R.string.no_user_login)), intent);
        GlobalUtilities.editSharePrefs(arg0).putString("regid", intent.toString()).commit();
    }

    @Override
    protected void onUnregistered(Context arg0, String intent) {

        Log.i(TAG, "onUnregistered: registrationId=" + intent);
        GlobalUtilities.Serverunregister(arg0, intent);

    }

    public void NotifyShow(Context context, int id, int icon, String title, String msg) {


        Uri UriSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bm = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_launcher),
                context.getResources().getDimensionPixelSize(
                        android.R.dimen.notification_large_icon_width),
                context.getResources().getDimensionPixelSize(
                        android.R.dimen.notification_large_icon_height), true);
        long[] vibrate = new long[]{100, 250, 100, 500};

        if (!GlobalUtilities.getPrefs(context).getBoolean("notifisound", false)) {
            UriSound = null;
        }
        if (!GlobalUtilities.getPrefs(context).getBoolean("notifyvibrate", false)) {
            vibrate = null;
        }
        Intent resultIntent = new Intent(context,
                MainActivity.class);
        resultIntent.putExtra("notification",true);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);
        mBuilder.setSmallIcon(icon).setLargeIcon(bm)
                .setContentTitle(title).setContentText(msg)
                .setVibrate(vibrate)
                .setSound(UriSound)
                .setAutoCancel(true);

        TaskStackBuilder StackBuilder = TaskStackBuilder.create(this);
        StackBuilder.addParentStack(MainActivity.class);

        StackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = StackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (GlobalUtilities.getPrefs(context).getBoolean("notifishow", true)) {
            mNotifyMgr.notify(id, mBuilder.build());
        } else {
            mNotifyMgr.cancelAll();
        }
    }


}

