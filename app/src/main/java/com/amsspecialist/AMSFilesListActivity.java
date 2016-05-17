package com.amsspecialist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amsspecialist.classes.BaseActivity;
import com.amsspecialist.fragments.AMSFilesList_Fragment;


public class AMSFilesListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String type = getIntent().getStringExtra("type");
        ((AMSFilesList_Fragment) getSupportFragmentManager().findFragmentById(R.id.fragmentamsfileslist))
                .LoadData(getApplicationContext(), type);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_amsfiles_list;
    }


}
