package com.amsspecialist.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import org.ini4j.Ini;
import org.ini4j.Wini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by exhowi on 12/01/2015.
 */

public class ReadIni extends AsyncTask<String, Integer, ArrayList<AMSFile>> {

    private ArrayList<AMSFile> list;
    private String type;
    Context con;
    Activity act;
    SweetAlertDialog pDialog;
    protected void onPreExecute() {
        super.onPreExecute();


    }

    public ReadIni(Context con,Activity act,String type) {

        this.list = new ArrayList<AMSFile>();
        this.type = type;
        this.con = con;
        this.act=act;
        pDialog = new SweetAlertDialog(act, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#296eb3"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected ArrayList<AMSFile> doInBackground(String... args) {
        AMSFile am = null;
        try {

            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(new URL(args[0]).openStream(),
                            "UTF-8"));
            Wini ini = new Wini(localBufferedReader);
            Set<String> sectionNames = ini.keySet();
            //Log.d("Count sections :", String.valueOf(sectionNames.size()));
            for (String section : sectionNames) {
                Ini.Section sec = ini.get(section);
                String url=GlobalUtilities.URLAMSFILEBASE+sec.get("Type")+"/"+section.toString();
                if (sec.get("Type").equals(type)) {
                    String comment = sec.get("comments");
                    if (sec.get("comments").equals("")) {
                        comment = "No Comments";
                    }
                    am = new AMSFile(section.toString(), sec.get("Type"),
                            sec.get("Size"), comment,url);
                    list.add(am);
                }

            }

            localBufferedReader.close();

        } catch (IOException localIOException) {

            localIOException.printStackTrace();
        }
        return list;
    }

    protected void onPostExecute(ArrayList<AMSFile> list) {
        super.onPostExecute(list);
        if (pDialog.isShowing()||pDialog!=null)
                pDialog.dismissWithAnimation();


    }


}
