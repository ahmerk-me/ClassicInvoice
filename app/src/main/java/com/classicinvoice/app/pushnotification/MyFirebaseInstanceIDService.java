package com.classicinvoice.app.pushnotification;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.JsonObject;

import com.classicinvoice.app.classes.LanguageSeassionManager;
import com.classicinvoice.app.classes.SessionManager;

/**
 * Created by DELL on 17-Nov-16.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    SessionManager sessionManager;

    LanguageSeassionManager languageSessionManager;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("Refreshed", "Refreshed token: " + s);

        sessionManager = new SessionManager(this);

        languageSessionManager = new LanguageSeassionManager(this);

        // TODO: Implement this method to send any registration to your app's servers.
        UpdateDeviceToken(s);

    }


    private void UpdateDeviceToken(final String regid){

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("DeviceType", "2");

        jsonObject.addProperty("DeviceId", getIMEI());

        jsonObject.addProperty("Token", regid);

//        Call<ResponseBody> insertTokenCall = ClassicAPICall.getCalorieAPIInterface().RegisterDevice(regid, "2", getIMEI(), sessionManager.getUserCode());
//
//        insertTokenCall.enqueue(new retrofit2.Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//
//                if(response.isSuccessful()){
//
//                    languageSessionManager.setRegId(regid);
//
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

    }

    //function to get the device id
    public String getIMEI() {

        return Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID) + "" + getApplicationContext().getPackageName();

    }

}
