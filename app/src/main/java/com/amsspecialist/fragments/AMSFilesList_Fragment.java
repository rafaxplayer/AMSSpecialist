package com.amsspecialist.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.amsspecialist.R;
import com.amsspecialist.classes.AMSFile;
import com.amsspecialist.classes.AmsFilesAdapter;
import com.amsspecialist.classes.GlobalUtilities;
import com.amsspecialist.classes.ReadIni;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.TokenPair;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class AMSFilesList_Fragment extends Fragment implements SearchView.OnQueryTextListener{


    @InjectView(R.id.list_amsfiles)
    RecyclerView listamsfiles;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.msgbar)
    View acbar;
    @InjectView(R.id.textView)
    TextView msgtxt;
    @OnClick(R.id.msgbar)
    public void clickinfobar(){

        closebar("");
    }

    public AMSFilesList_Fragment() {
        // Required empty public constructor
    }

    public AMSFilesList_Fragment newInstance(String type){
        // Required empty public constructor
        AMSFilesList_Fragment frm = new AMSFilesList_Fragment();
        Bundle bun= new Bundle();
        bun.putString("type",type);
        frm.setArguments(bun);
        return frm;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_list_amsfiles, container, false);
        ButterKnife.inject(this,v);
        listamsfiles.setLayoutManager(new LinearLayoutManager(getActivity()));
        listamsfiles.setItemAnimator(new DefaultItemAnimator());
        fab.hide();
        if(getArguments()!=null){
           LoadData(getActivity(),this.getArguments().get("type").toString());
        }else{
            LoadData(getActivity(),"DataBases");
        }
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(getResources().getBoolean(R.bool.dual_pane)){
            menu.findItem(R.id.overflow).setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_amsfiles_list, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case 16908332:
                if(((AmsFilesAdapter) listamsfiles.getAdapter()).searchResultsok){
                    if(getArguments()!=null){
                        LoadData(getActivity(),this.getArguments().get("type").toString());
                    }else{
                        LoadData(getActivity(),"DataBases");
                    }
                    ((AmsFilesAdapter) listamsfiles.getAdapter()).setResultOk(false);
                }else{
                    getActivity().onBackPressed();}

                break;
            case R.id.overflow:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select type")
                        .setItems(R.array.string_menu_types, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String[] arraytypes = getResources().getStringArray(R.array.string_menu_types);

                                LoadData(getActivity(), arraytypes[which].toString());
                            }
                        });

                builder.create();
                builder.show();
                break;
            case R.id.search:

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void LoadData(Context con, String type) {

        ReadIni in = new ReadIni(con,getActivity(), type);
        ArrayList<AMSFile> list = null;
        try {


            list = in.execute(GlobalUtilities.URLDATAINI).get();
            listamsfiles.setAdapter(new AmsFilesAdapter(getActivity(),this,list));

            fab.attachToRecyclerView(listamsfiles, new ScrollDirectionListener() {
                @Override
                public void onScrollDown() {
                    fab.hide(true);
                }

                @Override
                public void onScrollUp() {
                    fab.show(true);
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listamsfiles.smoothScrollToPosition(0);
                }
            });

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        ((AmsFilesAdapter) listamsfiles.getAdapter()).getFilter().filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public void showbar(String msg) {
        Animation an;
        an = AnimationUtils.loadAnimation(getActivity(),
                R.anim.abc_slide_in_bottom);
        if (acbar.getVisibility() == View.GONE) {
            acbar.setVisibility(View.VISIBLE);

            acbar.startAnimation(an);
            delayShowbar(15000);
        }
        msgtxt.setText(msg);
    }

    public void delayShowbar(int miliseconds) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                closebar("");
            }
        }, miliseconds);
    }


    public void closebar(String msg) {

        Animation an;
        an = AnimationUtils.loadAnimation(getActivity(),
                R.anim.abc_slide_out_bottom);
        if (acbar.getVisibility() == View.VISIBLE) {
            acbar.setVisibility(View.GONE);

            acbar.startAnimation(an);
        }
        msgtxt.setText(msg);
    }
}
