package com.classicinvoice.app.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by shahbazshaikh on 16/04/16.
 */
public class FixControl {
    public static Boolean CheckNotEmptyOrNull(View view) {
        if (view instanceof TextView) {
            if (((TextView) view).getText() != null
                    && ((TextView) view).getText().toString().length() > 0)
                return true;// text has values
            else if (((TextView) view).getText() != null
                    && ((TextView) view).getText().toString().length() > 0)
                return true;// text has values
            else
                return false;// text has not values
        } else if (view instanceof EditText) {
            if (((EditText) view).getText() != null
                    && ((EditText) view).getText().toString().length() > 0)
                return true;// text has values
            else if (((EditText) view).getText() != null
                    && ((EditText) view).getText().toString().length() > 0)
                return true;// text has values
            else
                return false;// text has not values
        }
        return false;
    }

    public static Boolean CheckMobileNumberIs_8(View view) {
        if (view instanceof EditText) {
            if (((EditText) view).getText() != null
                    && ((EditText) view).getText().toString().length() == 8){
                return true;
            }

            else
                return false;
        } else
            return false;

    }
    /*public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }*/

    public final static  boolean isValidEmaillId(String email){
        if(email==null){
            return false;
        }
        else {
            return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
        }
    }

    public final static  boolean isValidPassword(String pw){
        if(pw==null){
            return false;
        }
        else {
            return Pattern.compile("^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$!&]).*$").matcher(pw).matches();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listItem.setLayoutParams(new android.widget.AbsListView.LayoutParams(0,0));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listItem.setLayoutParams(new android.widget.AbsListView.LayoutParams(0,0));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        //params.height = totalHeight
          //      + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.setLayoutParams(params);

        return totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String round2 (String price){
        //price = "5250,00";
        //price = Double.parseDouble(price)+"";
        price = price.replace(".",",");
        Log.e("price",""+price);
        String[] pArray = price.split(",");
        Log.e("pArray",""+pArray.length);
        Log.e("pArray 0",""+pArray[0]);
        Log.e("pArray [1]",""+pArray[1]);
        if(pArray[1].length()==1){
            pArray[1] = pArray[1]+"00";
            Log.e("pArray 1",""+pArray[1]);
        }
        else if(pArray[1].length()==2){
            pArray[1] = pArray[1]+"0";
            Log.e("pArray 2",""+pArray[1]);
        }
        else if(pArray[1].length()==3){
            pArray[1] = pArray[1];
            Log.e("pArray 3",""+pArray[1]);
        }
        else{
            pArray[1] = (pArray[1].charAt(0)+"" + pArray[1].charAt(1)+"" + pArray[1].charAt(2))+"";
            Log.e("pArray 3>",""+pArray[1]);
        }
        Log.e("pArray 1 final",""+pArray[1]);
        double d = Double.parseDouble(pArray[0]+"."+pArray[1]);
        Log.e("pArray d", "" + pArray[0]+"."+pArray[1]);
        return pArray[0]+"."+pArray[1]+"";
    }

    public static Drawable getDrawable(Context context, int resource) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(resource, context.getTheme());
        } else {
            return context.getResources().getDrawable(resource, null);
        }
    }


    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static int getImageHeight(Context context, int resId){

        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), resId, dimensions);
        return dimensions.outHeight;

    }

    public static int getImageWidth(Context context, int resId){

        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), resId, dimensions);
        return dimensions.outWidth;

    }

    public static void hideKeybord(View view, Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void showKeyboard(EditText editText, Activity activity){

//        editText.requestFocus();
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editText, InputMethodManager.HIDE_IMPLICIT_ONLY);

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    public static int numberOfDaysInMonth(int month, int year) {

        Calendar monthStart = new GregorianCalendar(year, month, 1);

        return monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);

    }


    public static String getNameOfImage(String path) {

        String s = path;
        int index = s.lastIndexOf('/');

        String name = s.substring(index + 1);

        return name;

    }

    public static int getAge(String dobString){

        int age = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date date1 = dateFormat.parse(dobString);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            now.setTime(new Date());
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age ;

//        Date date = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            date = sdf.parse(dobString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if(date == null) return 0;
//
//        Calendar dob = Calendar.getInstance();
//        Calendar today = Calendar.getInstance();
//
//        dob.setTime(date);
//
//        int year = dob.get(Calendar.YEAR);
//        int month = dob.get(Calendar.MONTH);
//        int day = dob.get(Calendar.DAY_OF_MONTH);
//
//        dob.set(year, month+1, day);
//
//        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
//        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
//            age--;
//        }
//
//
//
//        return age;
    }

}
