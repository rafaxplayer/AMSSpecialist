package com.amsspecialist.classes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.amsspecialist.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by exhowi on 14/01/2015.
 */
public abstract class BaseActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.titlebar)
    TextView titleBar;
    @InjectView(R.id.titlebar2)
    TextView titleBar2;
    private Typeface font;
    protected abstract int getLayoutResourceId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.inject(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            font = Typeface.createFromAsset(getAssets(), "neuroplitical.ttf");

            titleBar.setTypeface(font);
            titleBar2.setTypeface(font);
        }

    }
}
