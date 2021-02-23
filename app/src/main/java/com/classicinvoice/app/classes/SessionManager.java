package com.classicinvoice.app.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by webuser1 on 6/14/2015.
 */
public class SessionManager {
    private static final String PREF_NAME = "com.camouflage.pref";
    private static final String IS_LOGGED = "isLogged";
    private static final String UserCode = "UserCode";
    private static final String UserAddress = "UserAddress";
    private static final String UserEmail = "UserEmail";
    private static final String UserName = "UserName";
    private static final String UserPassword = "UserPassword";
    private static final String UserMobile = "UserMobile";
    private static final String UserCountryId = "UserCountryId";
    private static final String IsFirstTime = "IsFirstTime";
    private static final String UserCurrencyCode = "UserCurrencyCode";
    private static final String GuestUserId = "GuestUserId";
    private static String Token = "Token";
    private static String IsVendor = "IsVendor";
    private static String RecentlyViewArrayList = "RecentlyViewArrayList";
    private static String IsNotificationOn = "IsNotificationOn";
    private static String FragType = "FragType";
    private static String VersionName = "VersionName";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void LoginSeassion(){
        editor.putBoolean(IS_LOGGED,true);
        editor.commit();
    }
    public boolean isLoggedin(){
        return  pref.getBoolean(IS_LOGGED,false);
    }

    public void setFirstTime(boolean isFirstTime){
        editor.putBoolean(IsFirstTime,isFirstTime);
        editor.commit();
    }
    public boolean isFirstTime(){
        return  pref.getBoolean(IsFirstTime,true);
    }

    public void logout(){
        editor.putBoolean(IS_LOGGED,false);
        editor.commit();
    }

    public void setUserCode(String code){
        editor.putString(UserCode,code);
        editor.commit();
    }
    public String getUserCode(){
        return  pref.getString(UserCode,"0");
    }

    public void setFragType(int code){
        editor.putInt(FragType,code);
        editor.commit();
    }
    public int getFragType(){
        return  pref.getInt(FragType,1);
    }

    public void setUserAddress(String address){
        editor.putString(UserAddress,address);
        editor.commit();
    }
    public String getUserAddress(){
        return  pref.getString(UserAddress,"");
    }


    public void setUserName(String name){
        editor.putString(UserName,name);
        editor.commit();
    }

    public String getUserName(){
        return  pref.getString(UserName,"");
    }

    public String getUserPassword(){
        return  pref.getString(UserPassword,"");
    }


    public void setUserPassword(String password){
        editor.putString(UserPassword,password);
        editor.commit();
    }



    public String getUserMobile(){
        return  pref.getString(UserMobile,"");
    }


    public void setUserMobile(String mob){
        editor.putString(UserMobile,mob);
        editor.commit();
    }



    public void setUserEmail(String email){
        editor.putString(UserEmail,email);
        editor.commit();
    }
    public String getUserEmail(){
        return  pref.getString(UserEmail,"");
    }

    public void setUserCountryId(String id){
        editor.putString(UserCountryId,id);
        editor.commit();
    }
    public String getUserCountryId(){
        return  pref.getString(UserCountryId,"");
    }

    public String getUserCurrencyCode(){
        return  pref.getString(UserCurrencyCode,"");
    }


    public void setUserCurrencyCode(String userCurrencyId){
        editor.putString(UserCurrencyCode,userCurrencyId);
        editor.commit();
    }

    public void setGuestUserId(String code){
        editor.putString(GuestUserId,code);
        editor.commit();
    }
    public String getGuestUserId(){
        return  pref.getString(GuestUserId,"0");
    }

    public void setToken(String code){
        editor.putString(Token,code);
        editor.commit();
    }
    public String getToken(){
        return  pref.getString(Token,"");
    }



    public void vendor(){
        editor.putBoolean(IsVendor,true);
        editor.commit();
    }

    public void customer(){
        editor.putBoolean(IsVendor,false);
        editor.commit();
    }

    public boolean isVendor(){
        return  pref.getBoolean(IsVendor,false);
    }

    public void setRecentlyViewArrayList(String token){
        editor.putString(RecentlyViewArrayList, token);
        editor.commit();
    }
    public String getRecentlyViewArrayList(){
        return  pref.getString(RecentlyViewArrayList,"");
    }

    public void setVersionName(String version){
        editor.putString(VersionName, version);
        editor.commit();
    }
    public String getVersionName(){
        return  pref.getString(VersionName,"0");
    }

    public void changeNotification(boolean status){
        editor.putBoolean(IsNotificationOn,status);
        editor.commit();
    }
    public boolean isNotificationOn(){
        return  pref.getBoolean(IsNotificationOn,true);
    }


}
