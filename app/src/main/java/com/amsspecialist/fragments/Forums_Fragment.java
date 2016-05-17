package com.amsspecialist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amsspecialist.R;
import com.amsspecialist.classes.Adapter_Forums;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Forums_Fragment extends Fragment {


    @InjectView(R.id.listViewForums)
    RecyclerView lst;

    public static Forums_Fragment newInstance() {
        Forums_Fragment fragment = new Forums_Fragment();

        return fragment;
    }

    public Forums_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forums, container, false);
        ButterKnife.inject(this, v);
        lst.setHasFixedSize(true);
        lst.setLayoutManager(new LinearLayoutManager(getActivity()));
        lst.setItemAnimator(new DefaultItemAnimator());
        lst.setAdapter(new Adapter_Forums(getActivity(),getActivity().getSupportFragmentManager()));
        return v;
    }


}
