package com.amsspecialist.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amsspecialist.R;
import com.amsspecialist.classes.GetAvatarfromUser;
import com.amsspecialist.classes.GlobalUtilities;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login_Fragment extends Fragment {
    private static long Maxatems = 3;
    private long atem = 0;
    private String urlAvatar;
    @InjectView(R.id.user)
    EditText edituser;
    @InjectView(R.id.password)
    EditText editpass;
    @InjectView(R.id.login_progress)
    ProgressBar progress;
    @InjectView(R.id.sign_in_button)
    Button buttonSigIn;

    @OnClick(R.id.sign_in_button)
    public void submit() {
        //progress.setVisibility(View.VISIBLE);
        final String user = edituser.getText().toString();
        final String pass = editpass.getText().toString();

        if (GlobalUtilities.login(getActivity().getApplicationContext(), user, pass)) {

            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.dialog_ok).replace("#", edituser.getText())).setConfirmText("OK")
                    .setContentText(getString(R.string.dialog_ok_comment)).setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();

                            getActivity().finish();
                        }
                    })
                    .show();
            try {
                urlAvatar = new GetAvatarfromUser().execute(user).get();
            } catch (Exception ex) {

            }
            //Edit SharedPreferences
            GlobalUtilities.editSharePrefs(getActivity())
                    .putBoolean("login", true)
                    .putString("user", user)
                    .putString("avatar", urlAvatar.length() > 0 ? urlAvatar : "http://www.amsspecialist.com/images/unknown.jpg")
                    .putString("password", pass)
                    .commit();
        } else {
            if (atem >= Maxatems) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Error")
                        .setContentText("Llevas 3 intentos fallidos , deseas registrarte o recuperar tu contraseña?")
                        .setConfirmText("Ok")
                        .setCancelText("Cancel")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Toast.makeText(getActivity(), "Ir a la URL", Toast.LENGTH_LONG).show();
                                atem = 0;
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                progress.setVisibility(View.GONE);
                return;
            }
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.dialog_error)).setConfirmText("OK")
                    .setContentText(getString(R.string.dialog_error_comment))
                    .show();
            edituser.setText("");
            editpass.setText("");
            atem++;

        }
        progress.setVisibility(View.GONE);

    }

    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, v);

        editpass.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    buttonSigIn.performClick();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
