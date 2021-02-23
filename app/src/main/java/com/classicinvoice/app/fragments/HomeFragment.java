package com.classicinvoice.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;

public class HomeFragment extends Fragment {

    protected static final String TAG = HomeFragment.class.getSimpleName();

    static FragmentActivity act;

    static HomeFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;


    @BindView(R.id.loading_progress)
    ProgressBar mloading;


    public static HomeFragment newInstance(FragmentActivity act) {

        fragment = new HomeFragment();

        HomeFragment.act = act;

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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.home,null);

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

        MainActivity.title.setText(act.getString(R.string.HomeLabel));

    }

}
