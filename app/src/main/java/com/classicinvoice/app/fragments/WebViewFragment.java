package com.classicinvoice.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;

public class WebViewFragment extends Fragment  {

    protected static final String TAG = WebViewFragment.class.getSimpleName();

    static FragmentActivity act;

    static WebViewFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;

    @BindView(R.id.loading_progress)
    ProgressBar mloading;

    @BindView(R.id.webView)
    WebView webView;

    String URL;

    public static WebViewFragment newInstance(FragmentActivity act) {

        fragment = new WebViewFragment();

        WebViewFragment.act = act;

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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.web_view, null);

            ButterKnifeLite.bind(this, mainLayout);

            initViews(mainLayout);

        } catch (Exception e) {

            Log.e(TAG + " " + " onCreate>>LineNumber: "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    e.getMessage());

        }

        return mainLayout;

    }

    void initViews(RelativeLayout mainLayout) {

        if (mainLayout != null) {

            MainActivity.setTextFonts(mainLayout);

        }

    }

    @Override
    public void onStart() {

        super.onStart();

        MainActivity.setupDefaultSettings();



        if (getArguments() != null) {

            if (getArguments().containsKey("title")) {

//                MainActivity.title.setText(getArguments().getString("title"));

                URL = getArguments().getString("url");

                loadURL();
            }
        }

    }


    public void loadURL() {

        Log.d("URL_Load", URL);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(URL);

        webView.setWebViewClient(new WebViewClient() {



        });
    }

}
