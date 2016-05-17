package com.amsspecialist.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amsspecialist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by exhowi on 13/01/2015.
 */
public  class AMSFILesMenu_Fragment extends Fragment {
    @InjectView(R.id.listmenu_amsfiles)
    RecyclerView listamsfiles;

    OnSelectedListener mCallback;

    public AMSFILesMenu_Fragment() {
    }

    public interface OnSelectedListener {
        public void onitemSelected(String type);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnSelectedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_amsfiles, container, false);
        ButterKnife.inject(this, rootView);
        listamsfiles.setHasFixedSize(true);
        listamsfiles.setLayoutManager(new LinearLayoutManager(getActivity()));
        listamsfiles.setItemAnimator(new DefaultItemAnimator());
        listamsfiles.setAdapter(new Adapter_Menu_AmsFiles(GetData()));
        return rootView;
    }
    private ArrayList<Map<String, String>> GetData() {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();

        map.put("name", "Bases de datos");
        map.put("description", "Ejemplos prácticos con bases de datos, utilizando plugins como Sqlite ,sqlite3, Mysql, etc..");
        map.put("datatype", "DataBases");
        list.add(map);
        map = new HashMap();
        map.put("name", "DLLs");
        map.put("description", "Librerías y activeX externos nos ayudan en nuestros proyectos , aquí es su sitio.");
        map.put("datatype", "DLLs");
        list.add(map);
        map = new HashMap();
        map.put("name", "Funciones");
        map.put("description", "Ejemplos con funciones prácticas (no incluidas en AMS)");
        map.put("datatype", "Functions");
        list.add(map);
        map = new HashMap();
        map.put("name", "Plugins");
        map.put("description", "Actión y Object plugins , imprescindibles para trabajos avanzados.");
        map.put("datatype", "Plugins");
        list.add(map);
        map = new HashMap();
        map.put("name", "Objetos");
        map.put("description", "Ejemplos utilizando objetos incluidos en Autoplay ( no plugins).");
        map.put("datatype", "Objects");
        list.add(map);
        map = new HashMap();
        map.put("name", "Visual");
        map.put("description", "En este apartado caben los proyectos de tipo visual , como textos en movimiento , transparencias ,efectos,.etc...");
        map.put("datatype", "Visual");
        list.add(map);
        map = new HashMap();
        map.put("name", "PDF");
        map.put("description", "Archivos pdf de ayuda en Autoplay, la mejor manera de aprender uso y manejo de este programa así como de sus plugins y complementos.");
        map.put("datatype", "Tutorials");
        list.add(map);

        map = new HashMap();
        map.put("name", "Otros");
        map.put("description", "Todos los que no tengan cabida en las categorías anteriores , se incluirán aquí.");
        map.put("datatype", "Others");
        list.add(map);

        return list;
    }

    public class Adapter_Menu_AmsFiles extends RecyclerView.Adapter<Adapter_Menu_AmsFiles.ViewHolder> {
        private Context con;

        ArrayList<Map<String, String>> list;


        public Adapter_Menu_AmsFiles(ArrayList<Map<String, String>> lst) {


            this.list=lst;
        }

        @Override
        public Adapter_Menu_AmsFiles.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_amsfiles, parent, false);


            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {


            holder.txtTitle.setText(list.get(position).get("name"));
            // holder.txtCom.setTe
            holder.txtComment.setText(list.get(position).get("description"));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @InjectView(R.id.texttitle_amsfiles)
            TextView txtTitle;
            @InjectView(R.id.textcomment_amsfiles)
            TextView txtComment;


            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                ButterKnife.inject(this, v);

            }

            @Override
            public void onClick(View v) {
                final View vie = v;
                switch (vie.getId()) {
                    case R.id.card_viewmenu_amsfiles:

                        mCallback.onitemSelected(list.get(ViewHolder.this.getPosition()).get("datatype"));
                        break;
                    default:

                        break;
                }

            }
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}
