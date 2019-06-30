package com.mel.minitwitter.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    private static final String APP_SETTINGS_FILE="APP_SETTINGS";

    private SharedPreferenceManager(){}

    private static SharedPreferences getSharedPreference(){
        return MyApp.getContext().getSharedPreferences(APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    public static void setSomeStringValue(String dataLabel,String dataValue){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putString(dataLabel,dataValue);
        editor.commit();
    }

    public static void setSomeBooleanValue(String dataLabel,boolean dataValue){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putBoolean(dataLabel,dataValue);
        editor.commit();
    }

    public static String getSomeStringValue(String dataLabel){
        return getSharedPreference().getString(dataLabel,null);
    }

    public static boolean getSomeBooleanValue(String dataLabel){
        return getSharedPreference().getBoolean(dataLabel,false);
    }

}
