package com.classicinvoice.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.classicinvoice.app.classes.AlertDialogManager;
import com.classicinvoice.app.classes.ConnectionDetector;
import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;
import com.classicinvoice.app.R;

import java.util.concurrent.atomic.AtomicInteger;


public class SplashActivity extends Activity {
    private final int splashDuration = 2000;
    String SENDER_ID = "611914818036";
    LanguageSeassionManager mLangSessionManager;
    static SessionManager sessionManager;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String deviceId;
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Demo";
    private boolean isBackButtonPressed = false;

    Context context;

    String regid;
    AtomicInteger msgId = new AtomicInteger();
    // Internet detector
    ConnectionDetector cd;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    String android_id;
    SharedPreferences shared_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Removes notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLangSessionManager = new LanguageSeassionManager(this);
        sessionManager = new SessionManager(this);

        if (mLangSessionManager.getLang().equals("ar")) {

            MainActivity.isEnglish=false;

        } else {

            MainActivity.isEnglish=true;

        }

        shared_pref = getSharedPreferences("gcm", 0);
        // pending
        cd = new ConnectionDetector(getApplicationContext());

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        regid = task.getResult().getToken();
//
//                        Log.e("registerationid Splash ", "regid -> "+regid);
//
//                        registerInBackground();
//
//
//                    }
//                });

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isBackButtonPressed) {
                    if(sessionManager.isFirstTime()){

                        startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("fragType", 1));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();

                    }
                    else{

                        startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("fragType", sessionManager.getFragType()));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();

                    }
                }
            }
        }, 3000);

        /*android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();


        if (!cd.isConnectingToInternet()) {

            alert.showAlertDialog(this, "Error Network Connection", "Error Network Connection",
                    Boolean.valueOf(false));

        } else {
            context = getApplicationContext();
            // Check device for Play Services APK. If check succeeds,
            // proceed with
            // GCM registration.
            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                regid = getRegistrationId(context);
                if (regid.isEmpty()) {
                    registerInBackground();
                }
            } else {
                Log.i(TAG, "No valid Google Play Services APK found.");
            }

        }*/

//        // comment this and un-comment above for working with push notification
//        if (!isBackButtonPressed) {
//            startActivity(new Intent(SplashActivity.this, LanguageActivity.class));
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            finish();
//        }

    }


    @Override
    public void onBackPressed() {
        isBackButtonPressed = true;
        super.onBackPressed();
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the caloriecontrol versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {

//        Call<ResponseBody> insertTokenCall = CalorieAPICall.getCalorieAPIInterface().RegisterDevice(regid, "2", getIMEI(), sessionManager.getUserCode());
//
//        insertTokenCall.enqueue(new retrofit2.Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//
//                if(response.isSuccessful()){
//
//                    mLangSessionManager.setRegId(regid);
//
//                }
//                //token expire
//                else if(response.code() == 401){
//
//                }
//                else{
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

    }


    /**
     * Gets the current registration ID for application on GCM service, if there
     * is one.
     * <p/>
     * If result is empty, the caloriecontrol needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        // Check if caloriecontrol was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // caloriecontrol version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }

    }


    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample caloriecontrol persists the registration ID in shared preferences,
        // but
        // how you store the regID in your caloriecontrol is up to you.
        return getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(getApplicationContext());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//
//            } else {
//
//            }
//            return false;
//        }
        return true;
    }


    //function to get the device id
    public String getIMEI() {

        return Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID) + "" + getApplicationContext().getPackageName();

    }

}
