package com.emobi.convoy.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by sunil on 19-01-2017.
 */

public class Pref {

    private static final String PrefDB="convey.txt";

    public static final String PROFILE_IMAGE="profile_image";
    public static final String PROFILE_NAME="profile_name";
    public static final String FCM_REGISTRATION_TOKEN="fcm_registration_token";

    public static void SetStringPref(Context context,String KEY,String Value){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(KEY,Value);
        editor.commit();
    }

    public static String GetStringPref(Context context,String KEY,String defValue){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        return sp.getString(KEY,defValue);
    }

    public static void SetBooleanPref(Context context,String KEY,boolean Value){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(KEY,Value);
        editor.commit();
    }

    public static boolean GetBooleanPref(Context context,String KEY,boolean defValue){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        return sp.getBoolean(KEY,defValue);
    }

    public static void clearSharedPreference(Context context){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void clearALLSharedPreferences(Context context){
        File sharedPreferenceFile = new File("/data/data/"+ context.getPackageName()+ "/shared_prefs/");
        File[] listFiles = sharedPreferenceFile.listFiles();
        for (File file : listFiles) {
            file.delete();
        }
    }

    public static void SetDeviceToken(Context context,String Value){
        SharedPreferences sp=context.getSharedPreferences("devicecraton.txt",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(FCM_REGISTRATION_TOKEN,Value);
        editor.commit();
    }

    public static String GetDeviceToken(Context context,String defValue){
        SharedPreferences sp=context.getSharedPreferences("devicecraton.txt",Context.MODE_PRIVATE);
        return sp.getString(FCM_REGISTRATION_TOKEN,defValue);
    }
}
