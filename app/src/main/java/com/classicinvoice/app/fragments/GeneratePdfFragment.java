package com.classicinvoice.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.Navigator;
import com.classicinvoice.app.R;
import com.classicinvoice.app.adapters.GoodsItemListAdapter;
import com.classicinvoice.app.classes.FixControl;
import com.classicinvoice.app.classes.GlobalFunctions;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.classicinvoice.app.models.GoodsItem;
import com.classicinvoice.app.models.Invoice;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class GeneratePdfFragment extends Fragment {

    protected static final String TAG = GeneratePdfFragment.class.getSimpleName();

    static FragmentActivity act;

    static GeneratePdfFragment fragment;

    int[] XY;

    RelativeLayout mainLayout;

    SessionManager mSessionManager;

    LanguageSeassionManager languageSeassionManager;

    LinearLayoutManager mLayoutManager;

    RecyclerView.Adapter mAdapter;

    // declaring width and height
    // for our PDF file. (A4 size page pixel dimensions)
    private static final int pageHeight = 3508;
    private static final int pagewidth = 2480;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    String filename = "";

    GoodsItem goodsItem = null;

    ArrayList<GoodsItem> goodsItemArrayList = new ArrayList<>();

    public static TextView dialog_add_btn;

    Invoice invoice = null;


    @BindView(R.id.loading_progress)
    ProgressBar mloading;

    @BindView(R.id.tv_save)
    TextView tv_save;

    @BindView(R.id.tv_share)
    TextView tv_share;

    @BindView(R.id.tv_print)
    TextView tv_print;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rv_itemsRecycler)
    RecyclerView rv_itemsRecycler;

    @BindView(R.id.et_freight)
    EditText et_freight;

    @BindView(R.id.et_cgst)
    EditText et_cgst;

    @BindView(R.id.et_sgst)
    EditText et_sgst;

    @BindView(R.id.et_igst)
    EditText et_igst;


    public static GeneratePdfFragment newInstance(FragmentActivity act) {

        fragment = new GeneratePdfFragment();

        GeneratePdfFragment.act = act;

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

            Log.d("Date Check ", " Calendar.getInstance().getTimeInMillis()" + Calendar.getInstance().getTimeInMillis());
            Log.d("Date Check ", " Calendar.getInstance().getTime()" + Calendar.getInstance().getTime());

        } catch (Exception e) {

            Log.e(TAG + " " + " onCreate>>LineNumber: "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    e.getMessage());

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {

            mainLayout = (RelativeLayout) inflater.inflate(R.layout.gen_pdf, null);

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

            rv_itemsRecycler.setLayoutManager(mLayoutManager);

            MainActivity.setTextFonts(mainLayout);

            if (getArguments() != null) {

                if (getArguments().containsKey("invoice")) {

                    invoice = new Gson().fromJson(getArguments().getString("invoice"),
                            Invoice.class);
                }
            }

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

        tv_save.setTypeface(MainActivity.tf, Typeface.BOLD);
        tv_title.setTypeface(MainActivity.tf, Typeface.BOLD);
        tv_share.setTypeface(MainActivity.tf, Typeface.BOLD);
        tv_print.setTypeface(MainActivity.tf, Typeface.BOLD);

        // below code is used for
        // checking our permissions.

        //todo: remove when ui is complete and PDF generation is required
//        if (checkPermission()) {
//            Snackbar.make(mainLayout, "Permission Granted", Snackbar.LENGTH_SHORT).show();
//        } else {
//            requestPermission();
//        }

        dialog_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDialogDataValid()) {

                    goodsItem = new GoodsItem();

                    goodsItem.setDescription(MainActivity.et_description.getText().toString());
                    goodsItem.setHsnCode(MainActivity.et_hsnCode.getText().toString());
                    goodsItem.setQuantity(Integer.parseInt(MainActivity.et_qty.getText().toString()));
                    goodsItem.setRate(Double.parseDouble(MainActivity.et_rate.getText().toString()));
                    goodsItem.setDiscount(MainActivity.et_discount.getText().toString().isEmpty() ?
                            0.0 : Double.parseDouble(MainActivity.et_discount.getText().toString()));
                    goodsItem.setSrNo(goodsItemArrayList.size() + 1);
                    goodsItem.setTotalValue(goodsItem.getQuantity() * goodsItem.getRate());
                    goodsItem.setTaxableValue(goodsItem.getDiscount() > 0 ? goodsItem.getTotalValue()*(goodsItem.getDiscount()/100.0f) : goodsItem.getTotalValue());

                    goodsItemArrayList.add(goodsItem);

                    if (goodsItemArrayList.size() >= 0) {
                        Log.d("entered ", "setRecycler()");

                        setRecycler();
                    } else {
                        Log.d("entered ", "mAdapter.notifyDataSetChanged()");
                        mAdapter.notifyDataSetChanged();
                    }

                    MainActivity.relative_main_dialog.setVisibility(View.GONE);

                    FixControl.hideKeybord(tv_print, act);

                }
            }
        });

    }


    @OnClick(R.id.iv_addItem)
    public void iv_addItem() {

//        open popup to add *Description of Goods *HSN Code *Qty. *Rate *Discount

        MainActivity.relative_main_dialog.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.tv_save)
    public void tv_save() {

        GlobalFunctions.DisableLayout(mainLayout);
        mloading.setVisibility(View.VISIBLE);

        generatePDF();
    }

    @OnClick(R.id.tv_share)
    public void tv_share() {

        GlobalFunctions.DisableLayout(mainLayout);
        mloading.setVisibility(View.VISIBLE);

        generatePDF();
    }

    @OnClick(R.id.tv_print)
    public void tv_print() {

        GlobalFunctions.DisableLayout(mainLayout);
        mloading.setVisibility(View.VISIBLE);

        generatePDF();
    }


    public void setRecycler() {

        mAdapter = new GoodsItemListAdapter(act, goodsItemArrayList, new GoodsItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Snackbar.make(mainLayout, "open Edit row here", Snackbar.LENGTH_SHORT).show();
            }
        });

        rv_itemsRecycler.setAdapter(mAdapter);
    }


    private void generatePDF() {

        Log.d("=======>>>", " entered GeneratePDF()");

        //todo check condition here for goodsItemArrayList != null otherwise cgst sgst will give computational error
        prepareDummyInvoice();
//        prepareInvoice();

        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
//        Paint paint = new Paint();
//
//        Paint title = new Paint();

        setPdfBackground(canvas);

        drawZone1(canvas);
        drawZone2(canvas);
        drawZone3(canvas);
        drawGst(canvas);
        drawZone4(canvas);
        drawZone5(canvas);
        drawZone6(canvas);
        drawZone7(canvas);
        drawZone8(canvas);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        filename = "ClassicInvoice_" + Calendar.getInstance().getTimeInMillis() + ".pdf";

        Log.d("Status ", "check 1");
//        Log.d("File path ","==>>" + act.getExternalFilesDir(null).getAbsolutePath()
//                + File.separator
//                + filename);
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            Log.d("Status ", "check 2");

            // below line is used to set the name of
            // our PDF file and its path.
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            file = new File(file, filename);

            pdfDocument.writeTo(new FileOutputStream(file));
            Log.d("Status ", "check 3");

            GlobalFunctions.EnableLayout(mainLayout);
            mloading.setVisibility(View.GONE);

            // below line is to print toast message
            // on completion of PDF generation.
            Snackbar.make(mainLayout, "PDF file generated successfully.", Snackbar.LENGTH_SHORT).show();

            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

                    Uri uri = FileProvider.getUriForFile
                            (act, "com.classicinvoice.app.fileprovider", file);

                    Intent target;

                    boolean isShow = true;

                    if (isShow) {

                        target = new Intent(Intent.ACTION_VIEW);

                    } else {

                        target = new Intent(Intent.ACTION_SEND);

                        target.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                    }

                    target.setDataAndType(uri, "application/pdf");

                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Intent intent = Intent.createChooser(target, "Open File");

                    try {

                        act.startActivity(intent);

                    } catch (ActivityNotFoundException e) {


                    }

                }

            });

        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }

        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }


    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(act.getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(act.getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(act, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    public void prepareInvoice() {

        if (invoice != null) {

            invoice.setItem(goodsItemArrayList);
            invoice.setFreight(Double.parseDouble(et_freight.getText().toString()));
            invoice.setCgst(et_cgst.getText().toString().isEmpty() ? 0 : Double.parseDouble(et_cgst.getText().toString()));
            invoice.setSgst(et_sgst.getText().toString().isEmpty() ? 0 : Double.parseDouble(et_sgst.getText().toString()));
            invoice.setIgst(et_igst.getText().toString().isEmpty() ? 0 : Double.parseDouble(et_igst.getText().toString()));

        } else {

            Snackbar.make(mainLayout, "Some error has occured, please refill the form", Snackbar.LENGTH_SHORT).show();

            Navigator.loadFragment(act, CreateInvoiceFragment.newInstance(act), R.id.content_home, true, "generatePdf");

        }

    }


    public void prepareDummyInvoice() {

        if (invoice != null) {

            invoice.setItem(goodsItemArrayList);
            invoice.setFreight(100.00);
            invoice.setCgst(9.0);
            invoice.setSgst(9.0);
            invoice.setIgst(et_igst.getText().toString().isEmpty() ? 0 : Double.parseDouble(et_igst.getText().toString()));
            invoice.setInvoiceNumber("0023");

        } else {

            Snackbar.make(mainLayout, "Some error has occured, please refill the form", Snackbar.LENGTH_SHORT).show();

            Navigator.loadFragment(act, CreateInvoiceFragment.newInstance(act), R.id.content_home, true, "generatePdf");

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Snackbar.make(mainLayout, "Permission Granted. onRequest", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mainLayout, "Permission Denied. onRequest", Snackbar.LENGTH_SHORT).show();

                    act.finish();
                }
            }
        }

    }


    public boolean isDialogDataValid() {

        boolean result = false;

        if (!MainActivity.et_qty.getText().toString().isEmpty()
                && !MainActivity.et_rate.getText().toString().isEmpty()) {

            result = true;
        } else {

            Snackbar.make(mainLayout, "Quantity and Rate are compulsory fields", Snackbar.LENGTH_SHORT).show();
        }
        return result;
    }

    //complete
    public void setPdfBackground(Canvas canvas) {

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.invoice_background);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 2241, 2911, false);

        Paint paint = new Paint();
        // below line is used to draw our image on our PDF file.
        // the first parameter of our draw bitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 100, 140, paint);
    }

//next line logic and limit chars per line logic left
    public void drawZone1(Canvas canvas) {

        Paint title = new Paint();

        Log.d("=======>>>", " entered zone1()");

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(MainActivity.tf);

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(50);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(act, R.color.textRed));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText(invoice.getSupplierAddress(), 209, 280, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        title.setColor(ContextCompat.getColor(act, R.color.colorPrimary));
//        title.setTextSize(15);
//
//        // below line is used for setting
//        // our text to center of PDF.
//        title.setTextAlign(Paint.Align.CENTER);
//        canvas.drawText("This is sample document which we have created.", 396, 560, title);

//        drawZone2(myPage);

        Log.d("=======>>>", " entered zone1() return");

    }

//next line logic and limit chars per line logic left
    public void drawZone2(Canvas canvas) {

        Log.d("=======>>>", " entered zone2()");

        Paint text = new Paint();

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        text.setTypeface(MainActivity.tf);

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        text.setTextSize(30);

        // below line is sued for setting color
        // of our text inside our PDF file.
        text.setColor(ContextCompat.getColor(act, R.color.textRed));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText(invoice.getRecipientAddress(), 209, 790, text);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

//        drawZone3(myPage);
    }

//next line logic and limit chars per line logic left
    public void drawZone3(Canvas canvas) {

        Log.d("=======>>>", " entered zone3()");

        Paint text = new Paint();

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        text.setTypeface(MainActivity.tf);

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        text.setTextSize(30);

        // below line is sued for setting color
        // of our text inside our PDF file.
        text.setColor(ContextCompat.getColor(act, R.color.textRed));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText(invoice.getConsigneeAddress(), 1300, 502, text); //+2y //+30y

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

//        drawZone4(myPage);
    }

//logic to separate each char in ArrayList and print separately in a loop
    public void drawGst(Canvas canvas) {

        Paint title = new Paint();

        title.setTypeface(MainActivity.tf);
        title.setTextSize(30);
        title.setColor(ContextCompat.getColor(act, R.color.textRed));

        canvas.drawText(invoice.getSupplierGst(),300, 700, title);

        canvas.drawText(invoice.getRecipientGst(),1470, 700, title); //+10

    }

    //complete
    public void drawZone4(Canvas canvas) {

        Log.d("=======>>>", " entered zone4()");

        Paint text = new Paint();

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        text.setTypeface(MainActivity.tf);

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        text.setTextSize(30);

        // below line is sued for setting color
        // of our text inside our PDF file.
        text.setColor(ContextCompat.getColor(act, R.color.textRed));

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        // printing e-way

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText(invoice.getEwayNumber(), 1542, 746, text);

        // printing vehicle number
        canvas.drawText(invoice.getVehicleNumber(), 1542, 790, text); //+10y

        // printing e-way date
        canvas.drawText(invoice.getDate(), 2000, 746, text); //+100x

        // printing invoice number
        canvas.drawText(invoice.getInvoiceNumber(), 1542, 890, text); //-10y

        // printing invoice date
        canvas.drawText(invoice.getDate(), 1970, 890, text); //-10y +70x

//        drawZone5(myPage);
    }

    //complete
    public void drawZone5(Canvas canvas) {

        Log.d("=======>>>", " entered zone5()");

        if (goodsItemArrayList != null && goodsItemArrayList.size() > 0) {

            Paint text = new Paint();

            // below line is used for adding typeface for
            // our text which we will be adding in our PDF file.
            text.setTypeface(MainActivity.tf);

            // below line is used for setting text size
            // which we will be displaying in our PDF file.
            text.setTextSize(30);

            // below line is sued for setting color
            // of our text inside our PDF file.
            text.setColor(ContextCompat.getColor(act, R.color.textRed));

            // similarly we are creating another text and in this
            // we are aligning this text to center of our PDF file.
            text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            int y = 1220; // -80y

            for (int i = 0; i < goodsItemArrayList.size(); i++) {

                canvas.drawText(invoice.getItem().get(i).getSrNo()+"", 200, y, text);
                canvas.drawText(invoice.getItem().get(i).getDescription(), 300, y, text); //-10x
                canvas.drawText(invoice.getItem().get(i).getHsnCode(), 1050, y, text); //+50x
                canvas.drawText(invoice.getItem().get(i).getQuantity()+"", 1365, y, text); //+15x //+150x
                canvas.drawText(invoice.getItem().get(i).getRate()+"", 1560, y, text); //+60x //+100x
                canvas.drawText(invoice.getItem().get(i).getTotalValue()+"", 1720, y, text); //+120x
                canvas.drawText(invoice.getItem().get(i).getDiscount()+"", 1960, y, text); //+10x //+250x
                canvas.drawText(invoice.getItem().get(i).getTaxableValue()+"", 2130, y, text); //-20x //+250x

                y = y + 50;

            }
        }

//        drawZone6(myPage);
    }

    //complete
    public void drawZone6(Canvas canvas) {

        Log.d("=======>>>", " entered zone6()");

        Paint text = new Paint();
        Paint gst = new Paint();

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        text.setTypeface(MainActivity.tf);
        gst.setTypeface(MainActivity.tf);

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        text.setTextSize(30);
        gst.setTextSize(27);

        // below line is sued for setting color
        // of our text inside our PDF file.
        text.setColor(ContextCompat.getColor(act, R.color.textRed));
        gst.setColor(ContextCompat.getColor(act, R.color.textRed));

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        gst.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        int x = 2100; //+250

        canvas.drawText(invoice.getTotalTaxableValue()+"", x, 2100, text); //-399y
        canvas.drawText(invoice.getFreight()+"", x, 2160, text); //-20y //-425y

        double finalTaxableValue = invoice.getTotalTaxableValue();

        if (invoice.getFreight() > 0) {

            finalTaxableValue = finalTaxableValue+invoice.getFreight();
        }

        canvas.drawText(finalTaxableValue+"", x, 2240, text); //-20y //-449y

        double cgst = invoice.getCgst() > 0 ? finalTaxableValue*(invoice.getCgst()/100.0f) : 0;
        double sgst = invoice.getSgst() > 0 ? finalTaxableValue*(invoice.getSgst()/100.0f) : 0;
        double igst = invoice.getIgst() > 0 ? finalTaxableValue*(invoice.getIgst()/100.0f) : 0;

        canvas.drawText(invoice.getCgst() > 0 ? invoice.getCgst()+"" : "", 1910, 2312, gst); //+2y //+10x //-30x //-20x -50y//-962x -450y
        canvas.drawText(invoice.getCgst() > 0 ?  cgst+"" : "", x, 2310, text);
        canvas.drawText(invoice.getSgst() > 0 ? invoice.getSgst()+"" : "", 1910, 2372, gst); //+2y //+10x //-30x //-50y //-20x -40y //-455y
        canvas.drawText(invoice.getSgst() > 0 ?  sgst+"" : "", x, 2370, text);
        canvas.drawText(invoice.getIgst() > 0 ? invoice.getIgst()+"" : "", 1910, 2432, gst);
        canvas.drawText(invoice.getIgst() > 0 ?  igst+"" : "", x, 2430, text);
        canvas.drawText(finalTaxableValue + cgst + sgst + igst +"", x, 2550, text);

//        drawZone7(myPage);
    }

    //complete
    public void drawZone7(Canvas canvas) {

        Log.d("=======>>>", " entered zone7()");

        Paint text = new Paint();

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        text.setTypeface(MainActivity.tfBold);

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        text.setTextSize(36);

        // below line is sued for setting color
        // of our text inside our PDF file.
        text.setColor(ContextCompat.getColor(act, R.color.textRed));

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        canvas.drawText("One Lac Twenty Five Thousand And Thirty Eight Only", 160, 2540, text); //+40y +20x //+50y //+350y


//        drawZone8(myPage);
    }

    //complete
    public void drawZone8(Canvas canvas) {

        Log.d("=======>>>", " entered zone8()");

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.title_logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 418, 159, false);

        Paint paint = new Paint();
        // below line is used to draw our image on our PDF file.
        // the first parameter of our draw bitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 1700, 2720, paint); //-80y

    }

}