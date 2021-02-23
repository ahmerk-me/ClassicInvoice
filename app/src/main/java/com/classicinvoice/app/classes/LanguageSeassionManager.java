package com.classicinvoice.app.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by webuser1 on 6/14/2015.
 */
public class LanguageSeassionManager {
    private static final String PREF_NAME = "com.ais.pref.lang";
    public static final String KEY_Lang = "KEY_Lang";
    public static final String KEY_RegID = "regId";
    public static final String KEY_NotificationStatus = "NotificationStatus";
    private static final String IS_Registered = "IsRegistered";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    @SuppressLint("CommitPrefEdits")
    public LanguageSeassionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public String getLang() {
        return pref.getString(KEY_Lang, "en");
    }

    public void setLang(String lang) {
        editor.putString(KEY_Lang, lang);
        editor.commit();
    }

    public String getRegId() {
        return pref.getString(KEY_RegID, "");
    }

    public void setRegId(String id) {
        editor.putString(KEY_RegID, id);
        editor.commit();
    }

    public boolean isRegIdToken() {
        return pref.getBoolean(IS_Registered, false);
    }


    public void setNotificationStatus(Boolean status) {
        // Storing notification value as TRUE
        editor.putBoolean(KEY_NotificationStatus, status);
        editor.commit();
    }

    public boolean getNotificationStatus() {
        // Get Notification Status
        return pref.getBoolean(KEY_NotificationStatus, false);
    }
}
