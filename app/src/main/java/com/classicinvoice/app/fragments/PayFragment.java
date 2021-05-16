package com.classicinvoice.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.HtmlJSInterface;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


import java.io.IOException;
import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;


public class PayFragment extends Fragment implements Observer {
    protected static final String TAG = PayFragment.class.getSimpleName();
    static FragmentActivity act;
    static PayFragment fragment;
    int[] XY;
    LinearLayout mainLayout;
    SessionManager mSessionManager;
    LanguageSeassionManager languageSeassionManager;
    ProgressBar mloading;
    String url;
    String id;
    WebView webView;
    boolean isDataLoadedBefore = false;


    public static PayFragment newInstance(FragmentActivity act) {
        fragment = new PayFragment();
        PayFragment.act = act;
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
            if (getArguments() != null) {
                url = getArguments().getString("url");
                id = getArguments().getString("id");
            }

        } catch (Exception e) {
            Log.e(TAG + " " + " onCreate>>LineNumber: "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    e.getMessage() + "");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mainLayout = (LinearLayout) inflater.inflate(R.layout.pay_payment, null);
            initViews(mainLayout);
        } catch (Exception e) {
            Log.e(TAG + " " + " onCreate>>LineNumber: "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    e.getMessage() + "");
        }
        return mainLayout;
    }


    void initViews(final LinearLayout mainLayout) {

        if (mainLayout != null) {

            mloading = (ProgressBar) mainLayout.findViewById(R.id.loading);

            webView = (WebView) mainLayout.findViewById(R.id.webView);

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d("URL_Load", url);

        MainActivity.setupDefaultSettings();


//        MainActivity.bottom_bar.setVisibility(View.GONE);
//
//        MainActivity.tv_lineBottom.setVisibility(View.GONE);

        if (!isDataLoadedBefore) {

            isDataLoadedBefore = true;

            webView.setWebViewClient(new WebViewClient());

            webView.getSettings().setJavaScriptEnabled(true);

            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            webView.getSettings().setPluginState(WebSettings.PluginState.ON);

            webView.setWebChromeClient(new WebChromeClient());

            webView.getSettings().setUserAgentString(System.getProperty("http.agent"));

            // Enable Cookies
            CookieManager.getInstance().setAcceptCookie(true);
            if (android.os.Build.VERSION.SDK_INT >= 21)
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

            webView.loadUrl(url);

//            HtmlJSInterface htmlJSInterface = new HtmlJSInterface();
//            webView.addJavascriptInterface(htmlJSInterface, "HTMLOUT");
//            htmlJSInterface.addObserver(this);

            webView.setWebViewClient(new WebViewClient() {

//                boolean showDialog = false;

                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    Log.d("url_resp", url.toLowerCase());

                    if (url.toLowerCase().contains("?code=")) {

                        Log.d("url_resp", "contains code");

                        webView.evaluateJavascript("(function() {return document.getElementsByTagName('html')[0].outerHTML;})();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(final String value) {
                                JsonReader reader = new JsonReader(new StringReader(value));
                                reader.setLenient(true);
                                try {
                                    if (reader.peek() == JsonToken.STRING) {
                                        String domStr = reader.nextString();
                                        if (domStr != null) {
                                            onHtmlChanged(domStr);
                                        }
                                    }
                                } catch (IOException e) {
                                    // handle exception
                                    Log.e(" CI error", " ===>> " + e);
                                } finally {
//                                    IOUtils.close(reader);
                                }
                            }
                        });


//
//                        Document document = Jsoup.parse(url);
//                        if (document != null) {
//
//                            Log.d(" "," document" + document);
//                            Element element = document.selectFirst("pre");
//                            String json = element.text();
//
//                            Log.d("============> "," element.text() == " + json);
//
//                            GetToken result = new Gson().fromJson(json, GetToken.class);
//
//                            Log.d("============> "," response.body().getAccessToken() == " + result.getAccessToken());
//                            Log.d("============> "," response.body().getExpiresAt() == " + result.getExpiresAt());
//
////                        mSessionManager.setToken(result.getAccessToken());
//
//                        }
//
//
////                        getToken();
//
////                        String code = "";
//
////                        int index = url.indexOf('=');
//
////                        for (int i = index+1; i<url.length(); i++) {
////
////                            code = code + url.charAt(i);
////                        }
////
////                        Log.d("code "," code = " + code);
////
////                        getToken(code);
//
////                        if (!showDialog) {
////
////                            showDialog = true;
//
////                            act.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
////                            Navigator.loadFragment(act, HomeFragment.newInstance(act), R.id.content_home, true, "home");
//
////                            new AlertDialog.Builder(act)
////                                    .setMessage("Authorization successful")
////                                    .setPositiveButton(act.getString(R.string.OkLabel), new DialogInterface.OnClickListener() {
////                                        public void onClick(DialogInterface dialog, int which) {
////
////                                            dialog.dismiss();
////
////                                        }
////
////                                    })
////                                    .setIcon(R.drawable.icon_512)
////                                    .show();
//
////                        }
//
//                        return false;
//
//                    }
//
//                    if (url.toLowerCase().contains("error=access_denied")) {
//
//                        Log.d("url_resp", "contains error");
//
//                        Snackbar.make(mainLayout, "Error: please try authenticating again.", Snackbar.LENGTH_SHORT).show();
////todo: check if backstack is working.. it should remove the payment fragment!!
//                        act.getSupportFragmentManager().popBackStack();
//
//                        Navigator.loadFragment(act, HomeFragment.newInstance(act), R.id.content_home, true, "pay");
//
////                        if (!showDialog) {
////
////                            showDialog = true;
////
////                            new AlertDialog.Builder(act)
////                                    .setTitle(act.getString(R.string.MessageLabel))
////                                    .setIcon(R.drawable.icon_512)
////                                    .setMessage(act.getString(R.string.OperationFailed))
////                                    .setPositiveButton(act.getString(R.string.OkLabel), new DialogInterface.OnClickListener() {
////                                        public void onClick(DialogInterface dialog, int which) {
////
////                                            dialog.dismiss();
////
////                                            act.getSupportFragmentManager().popBackStack();
////
////                                        }
////
////                                    })
////                                    .setIcon(R.drawable.icon_512)
////                                    .show();
////
////                        }
//
//                        return false;
//
//                    }

                        Log.d("url_resp", "contains nothing");

                    }

                    webView.loadUrl(url);

                    return true;

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // When each page is finished we're going to inject our custom
                    // JavaScript which allows us to
                    // communicate to the JS Interfaces. Responsible for sending full
                    // HTML over to the
                    // HtmlJSInterface...
//                    isStarted = false;
//                    isLoaded = true;
//                    timeoutTimer.cancel();
                    view.loadUrl("javascript:(function() { "
                            + "window.HTMLOUT.setHtml('<html>'+"
                            + "document.getElementsByTagName('html')[0].innerHTML+'</html>');})();");
                }

            });

        }

    }


    public void getToken() {

//        Call<GetToken> topicsCall = ClassicAPICall.getClassicAPIInterface().getToken();
//
//        topicsCall.enqueue(new Callback<GetToken>() {
//            @Override
//            public void onResponse(Call<GetToken> call, Response<GetToken> response) {
//
//                if (response.isSuccessful()) {
//
//                    if (response.body() != null) {
//
//                        Log.d("============", " response.body().getAccessToken() == " + response.body().getAccessToken());
//                        Log.d("============", " response.body().getExpiresAt() == " + response.body().getExpiresAt());
//
//                        mSessionManager.setToken(response.body().getAccessToken());
//                    }
//
//                } else {
//
//                    GlobalFunctions.showErrorMessage(response.errorBody(), mainLayout);
//
//                }
//
//                mloading.setVisibility(View.GONE);
//
//                GlobalFunctions.EnableLayout(mainLayout);
//            }
//
//            @Override
//            public void onFailure(Call<GetToken> call, Throwable t) {
//
//                mloading.setVisibility(View.GONE);
//
//                Log.d("RetrofitError", "failure");
//
//            }
//
//        });
    }

    @Override
    public void update(Observable observable, Object observation) {

        // Got full page source.
        if (observable instanceof HtmlJSInterface) {

            onHtmlChanged((String) observation);
        }
    }

    private void onHtmlChanged(String html) {
        // Do stuff here...
        Log.d("", " <pre> tag content ====> " + html);
    }

}
