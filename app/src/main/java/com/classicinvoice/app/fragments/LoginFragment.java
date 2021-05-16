package com.classicinvoice.app.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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

import com.classicinvoice.app.ClassicConstant;
import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.Navigator;
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.BasicAuthInterceptor;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    protected static final String TAG = LoginFragment.class.getSimpleName();

    static FragmentActivity act;

    static LoginFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;

//    String password = "bZFWwsClPYkStAI2zGvxPIcD5EqCVm2XsqVDcaKaIvCf5jXYDFTmqXNIlL6GOsbSgvkwgHWzFUkScdLaXFnr1w1faiGUS00Rr4RbLssDvgkBOl8zyoWsFblvcDiJTYUY";
    String grant_type = "authorization_code";
    String redirect_uri = "https://test-printer-api.herokuapp.com/callback";
//    String requestData = "grant_type=" + grant_type + "&client_id=" + username + "&password=" + password;
    String requestData = "response_type=code" + "&client_id=" + ClassicConstant.ClientId + "&redirect_uri=" + redirect_uri;

    public OkHttpClient client;
    public static final MediaType CONTENT_TYPE = MediaType.get("application/x-www-form-urlencoded");
    String apUrl = ClassicConstant.BASE_URL+"authorize";       // replace host url through your oauth2 server.



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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.login, null);

            ButterKnifeLite.bind(this, mainLayout);

            initViews(mainLayout);

        } catch (Exception e) {

            Log.e(TAG + " " + " onCreate>>LineNumber: "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    e.getMessage());

        }

        return mainLayout;

    }


    void initViews(final RelativeLayout mainLayout) {

        if (mainLayout != null) {

            MainActivity.setTextFonts(mainLayout);

        }

    }


    @Override
    public void onStart() {

        super.onStart();

        MainActivity.setupDefaultSettings();

        MainActivity.tabNumber = 4;

        MainActivity.setTabs();

        MainActivity.title.setText(act.getString(R.string.LoginLabel));

        tv_login.setTypeface(MainActivity.tf, Typeface.BOLD);
        tv_register.setTypeface(MainActivity.tf, Typeface.BOLD);

        client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("USER_CLIENT_APP", "password"))
                .build();

    }


    @OnClick(R.id.tv_login)
    void tv_login() {

//        mloading.setVisibility(View.VISIBLE);
//
//        GlobalFunctions.DisableLayout(mainLayout);
//
//        Call<ResponseBody> topicsCall = ClassicAPICall.getClassicAPIInterface().authorize();
//
//        topicsCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if (response.isSuccessful()) {
//
//                    Bundle bundle = new Bundle();
//Log.d(" ","response.raw().request().url().toString() ==>> " + response.raw().request().url().toString());
////                    bundle.putString("url", ClassicConstant.AuthoriseURL);
//                    bundle.putString("url", response.raw().request().url().toString());
//
//                    Fragment fragment = PayFragment.newInstance(act);
//
//                    fragment.setArguments(bundle);
//
//                    Navigator.loadFragment(act, fragment, R.id.content_home, true, "login");
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
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                mloading.setVisibility(View.GONE);
//
//                Log.d("RetrofitError", "failure");
//
//            }
//
//        });

//        try {
//            loginRequest(apUrl, requestData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Intent myIntent = new Intent(getActivity(), AuthorizeActivity.class);
//        myIntent.putExtra("key", "OK"); //Optional parameters
//        getActivity().startActivity(myIntent);

//        Intent myIntent = new Intent(getActivity(), AppAuthActivity.class);
//        myIntent.putExtra("key", "OK"); //Optional parameters
//        getActivity().startActivity(myIntent);

    }


    @OnClick(R.id.tv_register)
    void tv_register() {

//        Navigator.loadFragment(act, RegisterFragment.newInstance(act), R.id.content_home, true, "login");
        Navigator.loadFragment(act, CreateInvoiceFragment.newInstance(act), R.id.content_home, true, "login");

    }


    @OnClick(R.id.tv_addData)
    void tv_addData() {

//        Navigator.loadFragment(act, RegisterFragment.newInstance(act), R.id.content_home, true, "login");
        Navigator.loadFragment(act, AddBooksFragment.newInstance(act), R.id.content_home, true, "login");

    }


    @OnClick(R.id.tv_viewData)
    void tv_viewData() {

//        Navigator.loadFragment(act, RegisterFragment.newInstance(act), R.id.content_home, true, "login");
        Navigator.loadFragment(act, ViewBooksFragment.newInstance(act), R.id.content_home, true, "login");

    }


    @OnClick(R.id.tv_editData)
    void tv_editData() {

        Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/1OG4LzT6r4m7wz49K9L8T7Vq77FkQI40-cRhzviJRqCQ/edit?usp=sharing#Intent;scheme=https;action=android.intent.action.VIEW;category=android.intent.category.DEFAULT;category=android.intent.category.BROWSABLE;end");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }


    public void loginRequest(String apUrl, String requestData) throws IOException {

        RequestBody body = RequestBody.create(CONTENT_TYPE, requestData);
        Request request = new Request.Builder()
                .url(apUrl)
                .addHeader("Content-Type", " application/x-www-form-urlencoded")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("CallBackResponse", "onFailure() Request was: " + call.request());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("CallBackResponse ", "onResponse(): " + response.body().string());
            }

        });
    }

}
