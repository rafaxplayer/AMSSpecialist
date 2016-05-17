package com.amsspecialist.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.amsspecialist.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by exhowi on 16/01/2015.
 */
public class DownloadTask extends AsyncTask<String, String, Boolean> {

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private Context context;
    private ProgressDialog mProgressDialog;
    private String root;

    public DownloadTask(Context con) {
        this.context = con;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(this.context);
        mProgressDialog.setMessage("Downloading file..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... file) {
        int count;
        Boolean ret = false;
        root = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/AMSFIles/";
        File dir = new File(root);
        try {
            String fileUrl = file[0].replace(" ", "%20");

            URL url = new URL(fileUrl);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();


            if (dir.exists() == false) {
                dir.mkdirs();
            }

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(root + file[1]);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();

            ret = false;
        } finally {
            if (new File(root + file[1]).exists()) {
                ret = true;
            }
        }
        return ret;

    }

    protected void onProgressUpdate(String... progress) {

        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(Boolean resp) {

        mProgressDialog.dismiss();
        if (resp) {
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Archivo descargado!")
                    .setContentText(Environment.getExternalStorageDirectory().toString() + "/AMSFiles/")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();

                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error")
                    .setContentText("Ocurrio un error al descragar el archivo")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();

                        }
                    })
                    .show();
        }
    }
}

