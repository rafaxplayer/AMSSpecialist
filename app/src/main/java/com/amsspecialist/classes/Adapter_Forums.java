package com.amsspecialist.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amsspecialist.R;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class Adapter_Forums extends RecyclerView.Adapter<Adapter_Forums.ViewHolder> {
    private Context con;
    private Activity act;
    private FragmentManager frm;
    public static int[] FORUMS = {R.string.general, R.string.dudas,
            R.string.noticias, R.string.programas, R.string.creaciones,
            R.string.tutoriales, R.string.ejemplos, R.string.desarrollo,
            R.string.plugins, R.string.utilidades, R.string.extras,
            R.string.factory, R.string.ingles, R.string.programacion};

    public static int[] COMMENTS = {R.string.comment_general,
            R.string.comment_dudas, R.string.comment_noticias,
            R.string.comment_programas, R.string.comment_creaciones,
            R.string.comment_tutoriales, R.string.comment_ejemplos,
            R.string.comment_desarrollo, R.string.comment_plugins,
            R.string.comment_utilidades, R.string.comment_extras,
            R.string.comment_factory, R.string.comment_ingles,
            R.string.comment_programacion};

    public Adapter_Forums(Activity act, FragmentManager frm) {

        this.act = act;
        this.frm = frm;
    }

    @Override
    public Adapter_Forums.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forums, parent, false);


        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextDrawable drawable = TextDrawable.builder()
                /*.beginConfig()
                .withBorder(4) *//* thickness in px *//*
                .endConfig()*/
                .buildRoundRect(String.valueOf(act.getString(FORUMS[position]).charAt(0)), Color.parseColor("#296eb3"), 5);

        holder.imgTitle.setImageDrawable(drawable);
        holder.txtForum.setText(act.getString(FORUMS[position]));
        // holder.txtCom.setText(String.valueOf(color1));
        holder.txtCom.setText(act.getString(COMMENTS[position]));
    }

    @Override
    public int getItemCount() {
        return FORUMS.length;
    }

    private String getForum(int position) {

        int sItem = this.FORUMS[position];
        int nforum;
        switch (sItem) {
            case R.string.general:
                nforum = 2;
                break;
            case R.string.dudas:
                nforum = 3;
                break;
            case R.string.noticias:
                nforum = 4;
                break;
            case R.string.programas:
                nforum = 19;
                break;
            case R.string.creaciones:
                nforum = 18;
                break;
            case R.string.tutoriales:
                nforum = 9;
                break;
            case R.string.ejemplos:
                nforum = 12;
                break;
            case R.string.desarrollo:
                nforum = 20;
                break;
            case R.string.plugins:
                nforum = 15;
                break;
            case R.string.utilidades:
                nforum = 16;
                break;
            case R.string.extras:
                nforum = 17;
                break;
            case R.string.factory:
                nforum = 21;
                break;
            case R.string.ingles:
                nforum = 26;
                break;
            case R.string.programacion:
                nforum = 27;
                break;
            default:
                nforum = 2;
                break;
        }

        return "http://www.amsspecialist.com/viewforum.php?f="
                + nforum;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.textForums)
        TextView txtForum;
        @InjectView(R.id.textComment)
        TextView txtCom;
        @InjectView(R.id.imageTitle)
        ImageView imgTitle;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.inject(this, v);

        }

        @Override
        public void onClick(View v) {
            final View vie = v;
            switch (vie.getId()) {
                case R.id.card_view:
                    new SweetAlertDialog(act, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText(this.txtForum.getText().toString())
                            .setContentText("Ir a ala web?")
                            .setCancelText("Cancelar")
                            .setConfirmText("Aceptar")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    act.startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(getForum(ViewHolder.this.getPosition()))));

                                }
                            })
                            .show();
                    break;
                default:

                    break;
            }

        }
    }
}
