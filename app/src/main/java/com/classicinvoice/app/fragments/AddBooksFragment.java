package com.classicinvoice.app.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.classicinvoice.app.networking.ClassicAPICall;
import com.google.android.material.snackbar.Snackbar;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBooksFragment extends Fragment {

    protected static final String TAG = AddBooksFragment.class.getSimpleName();

    static FragmentActivity act;

    static AddBooksFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.et_bookName)
    EditText et_bookName;

    @BindView(R.id.et_availability)
    EditText et_availability;

    public static AddBooksFragment newInstance(FragmentActivity act) {

        fragment = new AddBooksFragment();

        AddBooksFragment.act = act;

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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.add_books, null);

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

        MainActivity.tabNumber = 1;

        MainActivity.setTabs();

        MainActivity.title_img.setVisibility(View.VISIBLE);

        MainActivity.title.setVisibility(View.GONE);

        tv_next.setTypeface(MainActivity.tf, Typeface.BOLD);
        tv_title.setTypeface(MainActivity.tf, Typeface.BOLD);

        tv_title.setText("Book No. : ");

    }


    @OnClick(R.id.tv_next)
    public void tv_next() {

        if (isAllDataValid()) {

            Call<Void> topicsCall = ClassicAPICall.getClassicAPIInterface().postData(et_bookName.getText().toString(), et_availability.getText().toString());

            topicsCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.isSuccessful()) {

                        Snackbar.make(mainLayout, "Data added successfully", Snackbar.LENGTH_SHORT).show();

                        et_availability.setText("");
                        et_bookName.setText("");

                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                    Snackbar.make(mainLayout, "Server error: " + t, Snackbar.LENGTH_LONG).show();

                    Log.d("RetrofitError", "failure");

                }

            });

        }

    }

    public boolean isAllDataValid() {

        boolean result = false;

        if (!et_bookName.getText().toString().isEmpty() || !et_availability.getText().toString().isEmpty()) {

            result = true;

        } else {

            result = false;

            Snackbar.make(mainLayout, "Please fill all fields", Snackbar.LENGTH_SHORT).show();

        }

        return result;
    }
}