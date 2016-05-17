package com.amsspecialist.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amsspecialist.LoginActivity;
import com.amsspecialist.R;
import com.amsspecialist.classes.GlobalUtilities;
import com.amsspecialist.classes.RoundedTransformation;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notifications_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notifications_Fragment extends Fragment {

    @InjectView(R.id.card_new_post)
    CardView card_newpost;
    @InjectView(R.id.card_new_mps)
    CardView card_newmps;
    @InjectView(R.id.textBadge_newpost)
    TextView badge_New_Post;
    @InjectView(R.id.textBadge_newmps)
    TextView badge_New_Mps;
    @InjectView(R.id.textUsername)
    TextView textUsername;
    @InjectView(R.id.imageUser)
    ImageView imgUser;
    @InjectView(R.id.buttonLogin)
    Button buttonLogin;

    @OnClick(R.id.buttonLogin)
    public void submit() {

        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
    @OnClick(R.id.card_new_post)
    public void submitPost() {
        String username = GlobalUtilities.getPrefs(getActivity()).getString("user","");
        if(!username.isEmpty()){
            NewPOST_Fragment frm = NewPOST_Fragment.newInstance(GlobalUtilities.URLNEWPOST+username);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.notifications_fragment,frm,"newpost");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
            setHasOptionsMenu(false);


        }


    }

    @OnClick(R.id.card_new_mps)
    public void submitMps() {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.amsspecialist.com")));

    }
    private Boolean Login;
    private int nPost;
    private int nMps;

    // TODO: Rename and change types and number of parameters
    public static Notifications_Fragment newInstance() {
        Notifications_Fragment fragment = new Notifications_Fragment();

        return fragment;
    }

    public Notifications_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notifications, container, false);

        ButterKnife.inject(this, v);

        return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        updateInfo();

    }

    public void updateInfo() {

        Login = GlobalUtilities.getPrefs(getActivity()).getBoolean("login", false);
        if (Login) {
            buttonLogin.setVisibility(View.GONE);

        } else {
            buttonLogin.setVisibility(View.VISIBLE);
        }
        nPost = GlobalUtilities.getPrefs(getActivity()).getInt("newpost", 0);
        nMps = GlobalUtilities.getPrefs(getActivity()).getInt("newmps", 0);
        Picasso.with(getActivity())
                .load(GlobalUtilities.getPrefs(getActivity()).getString("avatar", "http://www.amsspecialist.com/images/unknown.jpg"))
                .transform(new RoundedTransformation(80, 4))
                .into(this.imgUser);

        textUsername.setText(GlobalUtilities.getPrefs(getActivity()).getString("user", getActivity().getString(R.string.no_user_login)));
        card_newmps.setEnabled((Login && nMps > 0));
        card_newpost.setEnabled((Login && nPost > 0));
        badge_New_Post.setText(String.valueOf(nPost));
        badge_New_Mps.setText(String.valueOf(nMps));
    }
}
