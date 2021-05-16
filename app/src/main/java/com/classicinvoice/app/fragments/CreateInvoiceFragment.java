package com.classicinvoice.app.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.classicinvoice.app.models.Invoice;
import com.google.gson.Gson;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

public class CreateInvoiceFragment extends Fragment {

    protected static final String TAG = CreateInvoiceFragment.class.getSimpleName();

    static FragmentActivity act;

    static CreateInvoiceFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;

    Invoice invoice = new Invoice();

    boolean isSameAsRecipient = false;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.et_supplierAddress)
    EditText et_supplierAddress;

    @BindView(R.id.et_supplierGST)
    EditText et_supplierGST;

    @BindView(R.id.et_recipientAddress)
    EditText et_recipientAddress;

    @BindView(R.id.et_recipientGST)
    EditText et_recipientGST;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;

    @BindView(R.id.radio_sameAsBefore)
    RadioButton radio_sameAsBefore;

    @BindView(R.id.et_consigneeAddress)
    EditText et_consigneeAddress;

    @BindView(R.id.et_ewayNumber)
    EditText et_ewayNumber;

    @BindView(R.id.et_vehicleNumber)
    EditText et_vehicleNumber;

    @BindView(R.id.et_date)
    EditText et_date;

    public static CreateInvoiceFragment newInstance(FragmentActivity act) {

        fragment = new CreateInvoiceFragment();

        CreateInvoiceFragment.act = act;

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

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.gen_invoice, null);

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

        tv_title.setTypeface(MainActivity.tf, Typeface.BOLD);
        tv_next.setTypeface(MainActivity.tf, Typeface.BOLD);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (radio_sameAsBefore.isChecked()) {

                    isSameAsRecipient = true;
                } else {

                    isSameAsRecipient = false;
                }
            }
        });

        radio_sameAsBefore.setChecked(true);

    }


    @OnClick(R.id.tv_next)
    void tv_next() {

        Bundle bundle = new Bundle();

        bundle.putString("invoice", new Gson().toJson(prepareDummyInvoice()));
//        bundle.putString("invoice", new Gson().toJson(prepareInvoice()));

        Fragment fragment = GeneratePdfFragment.newInstance(act);

        fragment.setArguments(bundle);

        Navigator.loadFragment(act, fragment, R.id.content_home, true, "createInvoice");

    }


    public Invoice prepareInvoice() {

        Invoice inv = new Invoice();
//todo: commented GST here for default value
        inv.setSupplierAddress(et_supplierAddress.getText().toString());
//        inv.setSupplierGst(et_supplierGST.getText().toString());
        inv.setRecipientAddress(et_recipientAddress.getText().toString());
//        inv.setRecipientGst(et_recipientGST.getText().toString());
        inv.setConsigneeAddress(isSameAsRecipient ? et_recipientAddress.getText().toString() : et_consigneeAddress.getText().toString());
        inv.setEwayNumber(et_ewayNumber.getText().toString());
        inv.setVehicleNumber(et_vehicleNumber.getText().toString());
        inv.setDate(et_date.getText().toString());

        return inv;
    }


    public Invoice prepareDummyInvoice() {

        Invoice inv = new Invoice();
//todo: commented GST here for default value
        inv.setSupplierAddress("Classic Tube Industries, B/806 Potia Apts");
//        inv.setSupplierGst(et_supplierGST.getText().toString());
        inv.setRecipientAddress("Arcadia Travels Pvt Ltd, B/806 Potia Apts");
//        inv.setRecipientGst(et_recipientGST.getText().toString());
        et_consigneeAddress.setText("Just Another Firm, B/806 Potia Apts");
        inv.setConsigneeAddress(isSameAsRecipient ? inv.getRecipientAddress() : et_consigneeAddress.getText().toString());
        inv.setEwayNumber("123456");
        inv.setVehicleNumber("MH01AL2478");
        inv.setDate("16/05/2021");

        return inv;
    }

}