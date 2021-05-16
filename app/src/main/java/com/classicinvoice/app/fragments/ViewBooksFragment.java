package com.classicinvoice.app.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classicinvoice.app.ClassicConstant;
import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.Navigator;
import com.classicinvoice.app.R;
import com.classicinvoice.app.adapters.ViewBooksAdapter;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.classicinvoice.app.models.getBooks.GetBooks;
import com.classicinvoice.app.models.getBooks.Values;
import com.classicinvoice.app.networking.ClassicAPICall;
import com.google.android.material.snackbar.Snackbar;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBooksFragment extends Fragment {

    protected static final String TAG = ViewBooksFragment.class.getSimpleName();

    static FragmentActivity act;

    static ViewBooksFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;

    LinearLayoutManager mLayoutManager;

    RecyclerView.Adapter mAdapter;

    ArrayList<ArrayList<String>> values = new ArrayList<>();


    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;


    public static ViewBooksFragment newInstance(FragmentActivity act) {

        fragment = new ViewBooksFragment();

        ViewBooksFragment.act = act;

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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.view_books, null);

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

            mLayoutManager = new LinearLayoutManager(act);

            mLayoutManager.setOrientation(RecyclerView.VERTICAL);

            recycler_view.setLayoutManager(mLayoutManager);

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

        loadSheet();

    }


    public void loadSheet() {

//        Call<GetBooks> topicsCall = ClassicAPICall.getSheetsAPIInterface().getData(ClassicConstant.SheetId,
//                MainActivity.getLastRow(), ClassicConstant.ApiKey);

        Call<GetBooks> topicsCall = ClassicAPICall.getSheetsAPIInterface().getData(ClassicConstant.SheetId,
                "Sheet1!A2:C", ClassicConstant.ApiKey);

        topicsCall.enqueue(new Callback<GetBooks>() {
            @Override
            public void onResponse(Call<GetBooks> call, Response<GetBooks> response) {

                if (response.isSuccessful()) {

//                    Snackbar.make(mainLayout, "Data added successfully", Snackbar.LENGTH_SHORT).show();

                    Log.d("Retrofit Response ==> ", response.body().getValues().get(0).get(0));

                    values.clear();
                    values.addAll(response.body().getValues());

                    Log.d("total rows ==> ", values.size()+"");
                    Log.d("total columns ==> ", values.get(0).size()+"");

                    setData();

                }

            }

            @Override
            public void onFailure(Call<GetBooks> call, Throwable t) {

                Snackbar.make(mainLayout, "Server error: " + t, Snackbar.LENGTH_LONG).show();

                Log.d("RetrofitError: ", t+"");

            }

        });

    }


    public void setData() {

        if (values.size() > 0) {

            mAdapter = new ViewBooksAdapter(act, values, new ViewBooksAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    // do nothing
                }
            });

            recycler_view.setAdapter(mAdapter);
        }
    }

}