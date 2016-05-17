package com.amsspecialist.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amsspecialist.R;
import com.amsspecialist.classes.GlobalUtilities;
import com.amsspecialist.classes.rssItem;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link News_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class News_Fragment extends Fragment {
    @InjectView(R.id.listnews)
    RecyclerView listnews;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    // TODO: Rename and change types and number of parameters
    public static News_Fragment newInstance() {
        News_Fragment fragment = new News_Fragment();

        return fragment;
    }

    public News_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this,v);

        listnews.setHasFixedSize(true);
        listnews.setLayoutManager(new LinearLayoutManager(getActivity()));
        listnews.setItemAnimator(new DefaultItemAnimator());
        fab.hide();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadRss(getActivity()).execute(GlobalUtilities.RSSURL);

    }


    private class loadRss extends AsyncTask<String, Void, String> {

        SweetAlertDialog pDialog;
        private Context context;
        private ArrayList<rssItem> ListGroup;


        public loadRss(Context con) {
            this.context = con;
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        }

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor(getString(R.color.colorPrimary)));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            StringBuffer buf = new StringBuffer("");
            try {
                // Read all the text returned by the server
                URL xmlUrl = new URL(params[0]);
                InputStreamReader reader = new InputStreamReader(
                        xmlUrl.openStream(), "UTF-8");

                BufferedReader in = new BufferedReader(reader);

                String str;
                while ((str = in.readLine()) != null) {

                    buf.append(str + "\r\t");
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();

            }

            return buf.toString();

        }

        @Override
        protected void onPostExecute(String xml) {
            try {
                ListGroup = new ArrayList<rssItem>();

                int rssentryes = Integer.parseInt(GlobalUtilities.getPrefs(getActivity()).getString("rssentryes", "20"));
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                Document doc = db.parse(is);
                NodeList posts = doc.getElementsByTagName("entry");
                for (int i = 0; i < posts.getLength(); i++) {
                    rssItem rss = new rssItem();
                    Element Post = (Element) posts.item(i);
                    Node title = Post.getElementsByTagName("title").item(0);
                    // Log.d("title",title.getTextContent());
                    rss.setTitle(title.getTextContent());
                    Node link = Post.getElementsByTagName("id").item(0);
                    // Log.d("link",link.getTextContent());
                    rss.setLink(link.getTextContent());
                    Node author = Post.getElementsByTagName("name").item(0);
                    // Log.d("Author",author.getTextContent());
                    rss.setAuthor(author.getTextContent());
                    Node content = Post.getElementsByTagName("content").item(0);
                    // Log.d("content",content.getTextContent());
                    rss.setContent(content.getTextContent());
                    ListGroup.add(rss);

                    //ListChilds.add(c);
                    if (ListGroup.size() >= rssentryes) {
                        break;
                    }

                }

                listnews.setAdapter(new RSSAdapter(context, ListGroup));
                fab.attachToRecyclerView(listnews, new ScrollDirectionListener() {
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
                        listnews.smoothScrollToPosition(0);
                    }
                });

            }

            catch (Exception ex) {
                ex.printStackTrace();
            }

            pDialog.dismissWithAnimation();
            //Toast.makeText(getActivity(),String.valueOf(ListGroup.size()),Toast.LENGTH_LONG).show();
        }

    }
    public class RSSAdapter extends RecyclerView.Adapter<RSSAdapter.ViewHolder> {

        private ArrayList<rssItem> mDataset;
        private Context sContext;

        // Adapter's Constructor
        public RSSAdapter(Context context, ArrayList<rssItem> myDataset) {
            mDataset = myDataset;
            sContext = context;
        }

        // Create new views. This is invoked by the layout manager.
        @Override
        public RSSAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            // Create a new view by inflating the row item xml.
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rss, parent, false);

            // Set the view to the ViewHolder
            ViewHolder holder = new ViewHolder(v);


            return holder;
        }

        // Replace the contents of a view. This is invoked by the layout manager.
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            rssItem it =((rssItem)mDataset.get(position));
            holder.textTitleRss.setText(it.getTitle());
            holder.textAuthor.setText(it.getAuthor());
            holder.textRssComment.setText(Html.fromHtml(it.getContent()));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }




        // Create the ViewHolder class to keep references to your views
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            @InjectView(R.id.texttitlerss)
            TextView textTitleRss;
            @InjectView(R.id.textauthor)
            TextView textAuthor;
            @InjectView(R.id.textrsscoment)
            TextView textRssComment;

            public ViewHolder(View v) {
                super(v);

                ButterKnife.inject(this,v);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(textTitleRss.getText().toString())
                        .setContentText("Deseas ver la entrada?")
                        .setCancelText("Cancelar")
                        .setConfirmText("Aceptar")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(((rssItem)mDataset.get(ViewHolder.this.getPosition())).getLink())));

                            }
                        })
                        .show();

            }
        }
    }


}
