package com.classicinvoice.app.fragments;

import android.graphics.Typeface;
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
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;

public class RegisterFragment extends Fragment {

    protected static final String TAG = RegisterFragment.class.getSimpleName();

    static FragmentActivity act;

    static RegisterFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;


    @BindView(R.id.loading_progress)
    ProgressBar mloading;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.tv_register)
    TextView tv_register;


    public static RegisterFragment newInstance(FragmentActivity act) {

        fragment = new RegisterFragment();

        RegisterFragment.act = act;

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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.register,null);

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

        MainActivity.tabNumber = 4;

        MainActivity.setTabs();

        MainActivity.title_img.setVisibility(View.GONE);
        MainActivity.title.setVisibility(View.VISIBLE);

        MainActivity.title.setText(act.getString(R.string.RegisterLabel));

        tv_register.setTypeface(MainActivity.tf, Typeface.BOLD);

    }

}
