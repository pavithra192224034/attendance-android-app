package com.example.attendance.Service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;


public interface SF {

    String LOGIN_USER_LOGGED_IN = "LOGIN_USER_LOGGED_IN";
    String LOGIN_USER_NAME      = "LOGIN_USER_NAME";
    String LOGIN_PROFILE        = "LOGIN_PROFILE";
    String LOGIN_USER_TYPE      = "LOGIN_USER_TYPE";
    String LOGIN_USER_ID        = "LOGIN_USER_ID";
    String LOGIN_USER_EMAIL     = "LOGIN_USER_EMAIL";
    String LOGIN_USER_REG_NO     = "LOGIN_USER_REG_NO";

    String COURSE_CODE_FOR_QR_SCANNER = "COURSE_CODE_FOR_QR_SCANNER";

    static SharedPreferences getLoginSF(@NonNull FragmentActivity activity) {
        String LOGIN_SHARED_PREFS = "LOGIN_SHARED_PREFS";
        return activity.getSharedPreferences(LOGIN_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    static SharedPreferences.Editor getLoginSFEditor(FragmentActivity activity) {
        return getLoginSF(activity).edit();
    }

    static void setLoginSFValue(FragmentActivity activity, String key, String value) {
        getLoginSFEditor(activity).putString(key, value).apply();
    }

    static void setLoginSFValue(FragmentActivity activity, String key, int value) {
        getLoginSFEditor(activity).putInt(key, value).apply();
    }

    static void setLoginSFValue(FragmentActivity activity, String key, Boolean value) {
        getLoginSFEditor(activity).putBoolean(key, value).apply();
    }

    static void clearLoginSF(FragmentActivity activity) {
        getLoginSFEditor(activity).clear().apply();
    }
}

