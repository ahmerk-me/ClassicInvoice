package com.classicinvoice.app.fragments;

import android.graphics.Typeface;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.Navigator;
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

public class LoginFragment extends Fragment {

    protected static final String TAG = LoginFragment.class.getSimpleName();

    static FragmentActivity act;

    static LoginFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;


    @BindView(R.id.loading_progress)
    ProgressBar mloading;

    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.tv_login)
    TextView tv_login;

    @BindView(R.id.tv_register)
    TextView tv_register;


    public static LoginFragment newInstance(FragmentActivity act) {

        fragment = new LoginFragment();

        LoginFragment.act = act;

        return fragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (act == null) {

            act = getActivity();

        }

        try {

            XY = GlobalFunctions.getScreenWidthAndHeight(act);

            mSessionManager = new SessionManager(act);

            languageSeassionManager = new LanguageSeassionManager(act);

        } catch (Exception e) {

            Log.e(TAG + " " + " onCreate>>LineNumber: "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    e.getMessage());

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.login,null);

            ButterKnifeLite.bind(this, mainLayout);

            initViews(mainLayout);

        } catch (Exception e) {

            Log.e(TAG + " " + " onCreate>>LineNumber: "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    e.getMessage());

        }

        return mainLayout;

    }


    void initViews(final RelativeLayout mainLayout){

        if(mainLayout != null) {

            MainActivity.setTextFonts(mainLayout);

        }

    }


    @Override
    public void onStart() {

        super.onStart();

        MainActivity.setupDefaultSettings();

        MainActivity.title.setText(act.getString(R.string.LoginLabel));

        tv_login.setTypeface(MainActivity.tf, Typeface.BOLD);
        tv_register.setTypeface(MainActivity.tf, Typeface.BOLD);

    }


    @OnClick(R.id.tv_register)
    void tv_register(){

        Navigator.loadFragment(act, RegisterFragment.newInstance(act), R.id.content_home, true, "login");
    }

}
