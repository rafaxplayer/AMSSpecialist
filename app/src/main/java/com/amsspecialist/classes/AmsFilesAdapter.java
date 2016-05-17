package com.amsspecialist.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amsspecialist.MainActivity;
import com.amsspecialist.R;
import com.amsspecialist.fragments.AMSFilesList_Fragment;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AmsFilesAdapter extends RecyclerView.Adapter<AmsFilesAdapter.ViewHolder> implements Filterable {

    public ArrayList<AMSFile> data;
    public ArrayList<AMSFile> dataorigin;
    public Context con;
    public Activity act;
    private FilesFilter filefilter;
    public Boolean searchResultsok;
    private Fragment frm;

    public AmsFilesAdapter(Activity context, Fragment frm, ArrayList<AMSFile> data) {
        this.data = data;
        this.con = context;
        this.dataorigin = data;
        this.searchResultsok = false;
        this.act = context;
        this.frm = frm;
    }

    @Override
    public AmsFilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_item_amsfile, parent, false);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtfile.setText(data.get(position).getFilename());
        holder.txttype.setText(data.get(position).getFiletype());
        holder.txtsize.setText(String.format("File Size :%s", data.get(position).getFilesize()));
        if(data.get(position).getFiletype().equals("Tutorials")){
            holder.imgType.setImageResource(R.drawable.pdf);
        }

    }


    public AMSFile getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtfile;
        public TextView txttype;
        public TextView txtsize;
        public ImageButton infobtn;
        public ImageView imgType;
        public IconizedMenu popup;
        public long pos;


        public ViewHolder(View v) {
            super(v);
            final View vie = v;
            txtfile = (TextView) v.findViewById(R.id.textname);
            txttype = (TextView) v.findViewById(R.id.textType);
            txtsize = (TextView) v.findViewById(R.id.textSize);
            imgType = (ImageView) v.findViewById(R.id.imgtype);

            infobtn = (ImageButton) v.findViewById(R.id.imageInfo);
            infobtn.setOnClickListener(this);

            v.setOnClickListener(this);
            popup = new IconizedMenu(con, infobtn);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu_amsfile_options, popup.getMenu());

            popup.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.Share:
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, data.get(ViewHolder.this.getPosition()).getUrl().replace(" ","%20"));
                            sendIntent.setType("text/plain");
                            con.startActivity(Intent.createChooser(sendIntent, con.getString(R.string.send_to)));
                            break;
                        case R.id.Comment:

                            ((AMSFilesList_Fragment) AmsFilesAdapter.this.frm)
                                    .showbar(data.get(ViewHolder.this.getPosition()).getComment());
                            break;
                        case R.id.download:
                            new SweetAlertDialog(vie.getContext(), SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText("Descargar Archivo?").setCancelText(vie.getContext().getString(R.string.Cancel))
                                    .setContentText("Se guardara en:" + Environment.getExternalStorageDirectory().toString() + "/AMSFiles/")
                                    .setConfirmText(vie.getContext().getString(R.string.ok)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            new DownloadTask(vie.getContext())
                                                    .execute(data.get(ViewHolder.this.getPosition()).getUrl(), data.get(ViewHolder.this.getPosition()).getFilename());
                                            sDialog.dismissWithAnimation();

                                        }
                                    })
                                    .show();

                            break;
                        default:
                            break;

                    }
                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {
            final View vie = v;
            switch (vie.getId()) {
                case R.id.imageInfo:

                    popup.show(); //showing popup menu

                    break;
                case R.id.card_view:

                    break;
                default:

                    break;

            }
        }


    }

    @Override
    public Filter getFilter() {

        if (filefilter == null) {
            filefilter = new FilesFilter();
        }
        return filefilter;

    }

    public void setResultOk(Boolean bol) {
        this.searchResultsok = bol;
    }

    private class FilesFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = data;
                results.count = data.size();
            } else {

                List<AMSFile> nFilesList = new ArrayList<AMSFile>();

                for (AMSFile p : data) {
                    if (p.getFilename().toLowerCase().contains(constraint.toString().toLowerCase()))
                        nFilesList.add(p);
                }

                results.values = nFilesList;
                results.count = nFilesList.size();

            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                data = dataorigin;
                Toast.makeText(con, "No results", Toast.LENGTH_LONG).show();
                searchResultsok = false;
            } else {
                data = (ArrayList<AMSFile>) results.values;
                searchResultsok = true;
            }
            notifyDataSetChanged();
        }
    }

}

