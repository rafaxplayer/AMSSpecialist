package com.amsspecialist.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.amsspecialist.R;
import com.amsspecialist.classes.GlobalUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings_Fragment extends PreferenceFragment {


    public Settings_Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Settings_Fragment newInstance() {
        Settings_Fragment fragment = new Settings_Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.fragment_settings);

        //EditText editText = ((EditTextPreference)findPreference("rssentry")).getEditText();
        //editText.setFilters(new InputFilter[]{ new EdittextFilterMaxMin(1, 50)});

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean notify = GlobalUtilities.getPrefs(getActivity()).getBoolean("notifishow", false);
        preferenceScreen.findPreference("notifisound").setEnabled(notify);
        preferenceScreen.findPreference("notifyvibrate").setEnabled(notify);


        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}
