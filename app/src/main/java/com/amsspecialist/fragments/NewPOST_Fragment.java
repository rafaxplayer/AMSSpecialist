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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amsspecialist.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class NewPOST_Fragment extends Fragment {
    @InjectView(R.id.listnew_post)
    RecyclerView listNewPost;
    private String url;
    private static String TAG = NewPOST_Fragment.class.getSimpleName();
    public NewPOST_Fragment() {
        // Required empty public constructor
    }

    public static NewPOST_Fragment newInstance(String url) {
        NewPOST_Fragment fragment = new NewPOST_Fragment();
        Bundle bun = new Bundle();
        bun.putString("Url", url);
        Log.e(TAG,url);
        fragment.setArguments(bun);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_post_, container, false);
        ButterKnife.inject(this, v);
        listNewPost.setHasFixedSize(true);
        listNewPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        listNewPost.setItemAnimator(new DefaultItemAnimator());
        this.url = this.getArguments().getString("Url");

        refreshData(this.url);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_newpost, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_reload){
            refreshData(this.url);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public void refreshData(String url) {

        if (url.length() > 0 && url != null) {
            new loadNewPost().execute(url);
            this.url=url;
        }

    }


    public class loadNewPost extends AsyncTask<String, Integer, String> {

        private ArrayList<HashMap<String, String>> list;
        SweetAlertDialog pDialog;

        public loadNewPost() {
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        }

        protected void onPreExecute() {

            super.onPreExecute();
            pDialog.getProgressHelper().setBarColor(Color.parseColor(getString(R.color.colorPrimary)));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            list = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected String doInBackground(String... params) {

            String xml = getXmlFromUrl(params[0]); // getting XML
Log.e(TAG,xml);
            return xml;
        }

        protected void onPostExecute(String xml) {
            Document doc = getDomElement(xml);

            NodeList nl = doc.getElementsByTagName("post");

            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                HashMap<String, String> newpost = new HashMap<String, String>();
                String title = getValue(e, "title"); // name child value
                String author = getValue(e, "lastpostedname"); // cost child value
                String numberpost = getValue(e, "numero"); // description child value
                String date = getValue(e, "lastposteddate"); // description child value
                String link = getValue(e, "link");
                String forumId = getValue(e, "forumid"); // description child value
                newpost.put("title", title);
                newpost.put("author", author);
                newpost.put("numberpost", numberpost);
                newpost.put("date", date);
                newpost.put("link", link);
                newpost.put("forum", forumId);
                list.add(newpost);

            }
            if (list.size() > 0) {
                listNewPost.setAdapter(new NewPostAdapter(getActivity(), list));
            }
            pDialog.dismissWithAnimation();
        }


    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    //if (child.getNodeType() == Node.TEXT_NODE) {
                    return child.getNodeValue();
                    //}
                }
            }
        }
        return "";
    }

    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }


    public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.ViewHolder> {

        private ArrayList<HashMap<String, String>> mDataset;
        private Context sContext;

        // Adapter's Constructor
        public NewPostAdapter(Context context, ArrayList<HashMap<String, String>> myDataset) {
            mDataset = myDataset;
            sContext = context;
        }

        // Create new views. This is invoked by the layout manager.
        @Override
        public NewPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
            // Create a new view by inflating the row item xml.
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_newpost, parent, false);

            // Set the view to the ViewHolder
            ViewHolder holder = new ViewHolder(v);


            return holder;
        }

        @Override
        public void onBindViewHolder(NewPostAdapter.ViewHolder holder, int position) {
            holder.Title.setText(mDataset.get(position).get("title"));
            holder.Author.setText(mDataset.get(position).get("author"));
            holder.Date.setText(mDataset.get(position).get("date"));
            holder.NewpostNumber.setText(mDataset.get(position).get("numberpost"));
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


        // Create the ViewHolder class to keep references to your views
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @InjectView(R.id.texttitlenewpost)
            TextView Title;
            @InjectView(R.id.textauthornewpost)
            TextView Author;
            @InjectView(R.id.textdatanewpost)
            TextView Date;
            @InjectView(R.id.textnumbernewpost)
            TextView NewpostNumber;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.inject(this, v);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("AMSSpecialist")
                        .setContentText(getString(R.string.visit_post))
                        .setCancelText(getString(R.string.Cancel))
                        .setConfirmText(getString(R.string.ok))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismissWithAnimation();
                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(mDataset.get(ViewHolder.this.getPosition()).get("link"))));


                            }
                        })
                        .show();

                //Toast.makeText(view.getContext(),mDataset.get(ViewHolder.this.getPosition()).get("link"),Toast.LENGTH_LONG).show();
            }
        }
    }
}

