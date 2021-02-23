package com.classicinvoice.app;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.classicinvoice.app.R;

public class Navigator {
    public static void loadFragment(FragmentActivity activity, Fragment baseFragment, int containerId, boolean isStacked, String s) {
        if (!isStacked) {
            activity.getSupportFragmentManager().beginTransaction().replace(containerId, baseFragment).commitAllowingStateLoss();
        } else {
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                    .replace(containerId, baseFragment)
                    .addToBackStack(s).commit();
//
        }
    }

    public static void loadFragment2(FragmentActivity activity, Fragment baseFragment, int containerId, boolean isStacked, String s) {
        if (!isStacked) {
            activity.getSupportFragmentManager().beginTransaction().add(containerId, baseFragment).commitAllowingStateLoss();
        } else {
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                    .add(containerId, baseFragment)
                    .addToBackStack(s).commit();
//
        }
    }

//    public static void goToAuthorizationActivity(Activity activity) {
//
//        activity.startActivity(new Intent(activity, AuthorizationActivity.class));
//        activity.finish();
//
//    }

    public static void goToHomeActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
}
