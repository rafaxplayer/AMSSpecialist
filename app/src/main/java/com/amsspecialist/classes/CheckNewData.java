package com.amsspecialist.classes;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by exhowi on 02/01/2015.
 */
public class CheckNewData extends AsyncTask<String, Integer, ContentValues> {
    ContentValues con;

    public CheckNewData(Context context) {


        con = new ContentValues();

    }

    @Override
    protected ContentValues doInBackground(String... args) {
        StringBuffer localStringBuffer1 = new StringBuffer("");
        StringBuffer localStringBuffer2 = new StringBuffer("");

        try {
            BufferedReader localBufferedReader1 = new BufferedReader(
                    new InputStreamReader(new URL(args[0]).openStream(),
                            "ISO-8859-1"));

            String line;

            while ((line = localBufferedReader1.readLine()) != null) {
                localStringBuffer1.append(line.toString());
            }

            localBufferedReader1.close();

            BufferedReader localBufferedReader2 = new BufferedReader(
                    new InputStreamReader(new URL(args[1]).openStream(),
                            "ISO-8859-1"));

            String line2;

            while ((line2 = localBufferedReader2.readLine()) != null) {
                localStringBuffer2.append(line2.toString());
            }

            localBufferedReader2.close();
            con.put(GlobalUtilities.NEWPOST, localStringBuffer1.toString());
            con.put(GlobalUtilities.NEWMPS, localStringBuffer2.toString());
        } catch (IOException localIOException) {

            localIOException.printStackTrace();
        }
        return con;

    }

    @Override
    protected void onPostExecute(ContentValues result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

    }
}
