package com.classicinvoice.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.LocaleHelper;
import com.classicinvoice.app.classes.SessionManager;
import com.classicinvoice.app.fragments.LoginFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.classicinvoice.app.fragments.HomeFragment;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;
//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class MainActivity extends AppCompatActivity {

    public static boolean isEnglish = false;

    LanguageSeassionManager mLangSessionManager;

    SessionManager mSessionManager;

    @BindView(R.id.bottom_bar)
    public static LinearLayout bottom_bar;

    @BindView(R.id.nav_view)
    NavigationView nav_view;

    public DrawerLayout drawer;

    @BindView(R.id.content_home)
    static
    FrameLayout content_home;

    @BindView(R.id.tv_lang)
    TextView tv_lang;

    @BindView(R.id.title)
    public static TextView title;

    @BindView(R.id.list_btn)
    public static ImageView list_btn;

    @BindView(R.id.title_img)
    public static ImageView title_img;

    public static ImageView back;

    @BindView(R.id.home_btn)
    public static TextView home_btn;

    @BindView(R.id.general_info_btn)
    public static TextView general_info_btn;

    @BindView(R.id.meal_btn)
    public static TextView meal_btn;

    @BindView(R.id.my_account_btn)
    public static TextView my_account_btn;

    @BindView(R.id.tv_lineTop)
    public static TextView tv_lineTop;

    @BindView(R.id.subscription)
    LinearLayout subscription;

    @BindView(R.id.appbar)
    public static AppBarLayout appbar;

    public static Typeface tf, tfBold;

    public static Toolbar toolbar;

     static AppCompatActivity act;

    @BindView(R.id.relative_side_menu)
    RelativeLayout relative_side_menu;

    @BindView(R.id.linear_top_bar)
    LinearLayout linear_top_bar;

    public static int tabNumber = 1;

//    public static ImageLoader mImageLoader;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(LocaleHelper.onAttach(newBase));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = MainActivity.this;

        setContentView(R.layout.activity_main);

        ButterKnifeLite.bind(this);

        mLangSessionManager = new LanguageSeassionManager(this);

//        initImageLoader(this);

        mSessionManager = new SessionManager(this);

        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();

        StrictMode.setVmPolicy(newbuilder.build());

        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

//        mImageLoader = ImageLoader.getInstance();

        if (mLangSessionManager.getLang() != null && !mLangSessionManager.getLang().equals("")) {

            if (mLangSessionManager.getLang().equals("en")) {

                updateViews("en");

                isEnglish = true;

                tfBold = Typeface.createFromAsset(act.getAssets(), ClassicConstant.English_Bold_Font);

                tf = Typeface.createFromAsset(act.getAssets(), ClassicConstant.English_Regular_Font);

            } else if (mLangSessionManager.getLang().equals("ar")) {

                updateViews("ar");

                isEnglish = false;

                tfBold = Typeface.createFromAsset(act.getAssets(), ClassicConstant.Arabic_Bold_Font);

                tf = Typeface.createFromAsset(act.getAssets(), ClassicConstant.Arabic_Regular_Font);

            }

        }else {

            updateViews("en");

            isEnglish = true;

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        back = (ImageView) findViewById(R.id.back);

        setSupportActionBar(toolbar);

        setTextFonts(relative_side_menu);

        setTextFonts(linear_top_bar);

        setTextFonts(bottom_bar);

        tabNumber = 1;

        setTabs();

        if (getIntent().hasExtra("fragType")) {

            int fragType = getIntent().getIntExtra("fragType", 0);

            if(fragType == 1){

                Navigator.loadFragment(MainActivity.this, HomeFragment.newInstance(this), R.id.content_home, false, "home");

            }

        } else {

            Navigator.loadFragment(MainActivity.this, HomeFragment.newInstance(this), R.id.content_home, false, "home");

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerSlide(View drawerView, float slideOffset) {

                super.onDrawerSlide(drawerView, slideOffset);

                float moveFactor = (nav_view.getWidth() * slideOffset);

                content_home.setTranslationX(-moveFactor);

                toolbar.setTranslationX(-moveFactor);

                bottom_bar.setTranslationX(-moveFactor);

                Log.d("onDrawerSlide", "onDrawerSlide");

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                Log.d("onDrawerOpened", "onDrawerOpened");

                setTextFonts(relative_side_menu);

                if(mLangSessionManager.getLang().equalsIgnoreCase("en")){

                    tv_lang.setText(act.getText(R.string.ArabicLabel));

                    tv_lang.setTypeface(Typeface.createFromAsset(act.getAssets(), ClassicConstant.Arabic_Regular_Font));

                }
                else{

                    tv_lang.setText(act.getText(R.string.EnglishLabel));

                    tv_lang.setTypeface(Typeface.createFromAsset(act.getAssets(), ClassicConstant.English_Regular_Font));

                }

            }

        };

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        toolbar.setNavigationIcon(null);

//        if (mSessionManager.isLoggedin()) {
//
//            subscription.setVisibility(View.VISIBLE);
//
//        } else {
//
//            subscription.setVisibility(View.GONE);
//
//        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        gotoDetails(getIntent());

//        mSessionManager.LoginSeassion();
//
//        mSessionManager.setUserCode("5659");

    }


    public static void setTextFonts(ViewGroup root) {

//        MainActivity.setTextGravity(root);

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                setTextFonts((ViewGroup) v);
            }
        }
    }


    public static void setTextGravity(ViewGroup root) {

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            } else if (v instanceof Button) {
                ((Button) v).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            } else if (v instanceof EditText) {
                ((EditText) v).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            }
//            else if (v instanceof LinearLayout) {
//                ((LinearLayout) v).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//                ((LinearLayout) v).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            } else if (v instanceof RelativeLayout) {
//                ((RelativeLayout) v).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//                ((RelativeLayout) v).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            }
            else if (v instanceof ViewGroup) {
                setTextGravity((ViewGroup) v);
            }
        }

    }


    @OnClick(R.id.general_info_btn)
    void general_info_btn() {

        tabNumber = 3;

        setTabs();

    }


    @OnClick(R.id.home_btn)
    void setHome_btn() {

        Navigator.loadFragment(act, HomeFragment.newInstance(act), R.id.content_home, true, "bottomBar");

        tabNumber = 1;

        setTabs();

    }


    @OnClick(R.id.my_account_btn)
    void my_account_btn() {

        Navigator.loadFragment(act, LoginFragment.newInstance(act), R.id.content_home, true, "bottomBar");

        tabNumber = 4;

        setTabs();

    }


    @OnClick(R.id.subscription)
    void subscription() {

        drawer.closeDrawers();

    }


    @OnClick(R.id.lang)
    void lang() {

        if (MainActivity.isEnglish) {

            mLangSessionManager.setLang("ar");

            updateViews("ar");

            MainActivity.isEnglish = false;

        } else {

            mLangSessionManager.setLang("en");

            updateViews("en");

            MainActivity.isEnglish = true;

        }

        startActivity(new Intent(this,
                MainActivity.class).putExtra("fragType", mSessionManager.getFragType()));

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        finish();

    }


    private void updateViews(String languageCode) {

        LocaleHelper.setLocale(this, languageCode);

    }

//remove for drawer
//    @OnClick(R.id.list_btn)
//    void list_btn() {
//
//        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
//
//            drawer.closeDrawers();
//
//        } else {
//
//            drawer.openDrawer(Gravity.RIGHT);
//
//        }
//
//        if(mLangSessionManager.getLang().equalsIgnoreCase("en")){
//
//            tv_lang.setText(act.getText(R.string.ArabicLabel));
//
//            tv_lang.setTypeface(Typeface.createFromAsset(act.getAssets(), ClassicConstant.Arabic_Regular_Font));
//
//        }
//        else{
//
//            tv_lang.setText(act.getText(R.string.EnglishLabel));
//
//            tv_lang.setTypeface(Typeface.createFromAsset(act.getAssets(), ClassicConstant.English_Regular_Font));
//
//        }
//
//    }


    @OnClick(R.id.back)
    void back() {

        onBackPressed();

    }


    @OnClick(R.id.contact_us)
    void contact_us() {

        drawer.closeDrawers();

    }


    @OnClick(R.id.aboutus)
    void aboutus() {

        drawer.closeDrawers();

    }


    @OnClick(R.id.offers)
    void offers() {

        drawer.closeDrawers();

        Navigator.loadFragment(MainActivity.this, HomeFragment.newInstance(this), R.id.content_home, true, "home");

    }


    @OnClick(R.id.request)
    void request() {

        drawer.closeDrawers();

    }


    @OnClick(R.id.meal_btn)
    void meal_btn() {

        tabNumber = 2;

        setTabs();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public static void setupDefaultSettings() {

        MainActivity.back.setImageResource(R.drawable.arrow_en);

        appbar.setBackgroundColor(Color.WHITE);

        title.setTextColor(act.getResources().getColor(R.color.textColor));

        list_btn.setImageResource(R.drawable.menu_en);

        list_btn.setVisibility(View.INVISIBLE);

        bottom_bar.setVisibility(View.VISIBLE);

        appbar.setVisibility(View.VISIBLE);

        back.setVisibility(View.VISIBLE);

        tv_lineTop.setVisibility(View.VISIBLE);

        title.setVisibility(View.VISIBLE);

        title.setVisibility(View.GONE);

        title.setTypeface(MainActivity.tfBold, Typeface.BOLD);

        if(act.getSupportFragmentManager().getBackStackEntryCount()>0){

            back.setVisibility(View.VISIBLE);

        }

        setTabs();

    }


    private static void setTabs(){

        home_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_unselected, 0, 0);

        home_btn.setTextColor(Color.parseColor("#B9B9B9"));

        meal_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.meal_icon_unsel, 0, 0);

        meal_btn.setTextColor(Color.parseColor("#B9B9B9"));

        general_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.gi_icon_unsel, 0, 0);

        general_info_btn.setTextColor(Color.parseColor("#B9B9B9"));

        my_account_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_icon_unsel, 0, 0);

        my_account_btn.setTextColor(Color.parseColor("#B9B9B9"));

        switch (tabNumber){

            case 1:

                home_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_icon_sel, 0, 0);

                home_btn.setTextColor(act.getResources().getColor(R.color.colorPrimary));

                break;

            case 2:

                meal_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.meal_icon_sel, 0, 0);

                meal_btn.setTextColor(act.getResources().getColor(R.color.colorPrimary));

                break;

            case 3:

                general_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.gi_icon_sel, 0, 0);

                general_info_btn.setTextColor(act.getResources().getColor(R.color.colorPrimary));

                break;

            case 4:

                my_account_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_icon_sel, 0, 0);

                my_account_btn.setTextColor(act.getResources().getColor(R.color.colorPrimary));

                break;

        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        gotoDetails(intent);

    }


    private void gotoDetails(Intent intent) {

        if (intent.hasExtra("Id")) {

            Log.d("gotoDetails", "1 -> "+intent.getStringExtra("type"));

            Log.d("gotoDetails", "2 -> " + intent.getStringExtra("Id"));


        }

    }


//    public static void initImageLoader(Context context) {
//        // This configuration tuning is custom. You can tune every option, you
//        // may tune some of them,
//        // or you can create default configuration by
//        // ImageLoaderConfiguration.createDefault(this);
//        // method.
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 3)
//                .denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
////                .writeDebugLogs() // Remove for release caloriecontrol
//                .build();
//        // Initialize ImageLoader with configuration.
//        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
//    }

}
