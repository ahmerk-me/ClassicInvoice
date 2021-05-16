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

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.Navigator;
import com.classicinvoice.app.R;
import com.classicinvoice.app.adapters.DummyAdapter;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.classicinvoice.app.models.DummyClass;
import com.google.android.material.snackbar.Snackbar;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;

import java.util.ArrayList;

public class PrinterListFragment extends Fragment {

    protected static final String TAG = PrinterListFragment.class.getSimpleName();

    static FragmentActivity act;

    static PrinterListFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;

    LinearLayoutManager mLayoutManager;

    RecyclerView.Adapter mAdapter;


    @BindView(R.id.loading_progress)
    ProgressBar mloading;

    @BindView(R.id.tv_selectPrinterLabel)
    TextView tv_selectPrinterLabel;

    @BindView(R.id.noData)
    TextView noData;

    @BindView(R.id.rv_printers)
    RecyclerView rv_printers;


    public static PrinterListFragment newInstance(FragmentActivity act) {

        fragment = new PrinterListFragment();

        PrinterListFragment.act = act;

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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.printer_list, null);

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

            mLayoutManager = new LinearLayoutManager(getActivity());

            mLayoutManager.setOrientation(RecyclerView.VERTICAL);

            rv_printers.setLayoutManager(mLayoutManager);

            MainActivity.setTextFonts(mainLayout);

        }

    }


    @Override
    public void onStart() {

        super.onStart();

        MainActivity.setupDefaultSettings();

        MainActivity.tabNumber = 1;

        MainActivity.setTabs();

        MainActivity.title.setText(act.getString(R.string.PrintersLabel));

        tv_selectPrinterLabel.setTypeface(MainActivity.tf, Typeface.BOLD);

        setRecycler();

    }


    public void setRecycler() {

        ArrayList<DummyClass> dummyArrayList = new ArrayList<>();

        DummyClass dummyClass = new DummyClass();
        dummyClass.setTitle("Test Printer 1");
        dummyClass.setDescription("This is description for Test Printer 1");
        dummyArrayList.add(dummyClass);

        dummyClass = new DummyClass();
        dummyClass.setTitle("Test Printer 2");
        dummyClass.setDescription("This is description for Test Printer 2");
        dummyArrayList.add(dummyClass);

        dummyClass = new DummyClass();
        dummyClass.setTitle("Test Printer 3");
        dummyClass.setDescription("This is description for Test Printer 3");
        dummyArrayList.add(dummyClass);

        dummyClass = new DummyClass();
        dummyClass.setTitle("Test Printer 4");
        dummyClass.setDescription("This is description for Test Printer 4");
        dummyArrayList.add(dummyClass);

        dummyClass = new DummyClass();
        dummyClass.setTitle("Test Printer 5");
        dummyClass.setDescription("This is description for Test Printer 5");
        dummyArrayList.add(dummyClass);

        dummyClass = new DummyClass();
        dummyClass.setTitle("Test Printer 6");
        dummyClass.setDescription("This is description for Test Printer 6");
        dummyArrayList.add(dummyClass);

        mAdapter = new DummyAdapter(act, dummyArrayList, new DummyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                Snackbar.make(mainLayout, "You selected " + dummyArrayList.get(position).getTitle(), Snackbar.LENGTH_SHORT).show();

//                Bundle bundle = new Bundle();
//
//                Fragment fragment = dummyArrayList.get(position).getFragment();
//
//                if (dummyArrayList.get(position).getTitle().contains("Terms")) {
//                    bundle.putString("page_id", "3");
//                    fragment.setArguments(bundle);
//                } else if (dummyArrayList.get(position).getTitle().contains("About")) {
//                    bundle.putString("page_id", "1");
//                    fragment.setArguments(bundle);
//                } else if (dummyArrayList.get(position).getTitle().contains("Favourites")) {
//                    bundle.putBoolean("isWishList", true);
//                    fragment.setArguments(bundle);
//                }
//
//                Navigator.loadFragment(act, fragment, R.id.content_home, true, "more");

            }

        });
        rv_printers.setAdapter(mAdapter);

    }

}