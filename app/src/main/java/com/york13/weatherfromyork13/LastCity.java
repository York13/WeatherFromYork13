package com.york13.weatherfromyork13;

import android.app.Activity;
import android.content.SharedPreferences;

public class LastCity {

    SharedPreferences prefs;

    public LastCity(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getCity(){
        return prefs.getString("city", "Ulyanovsk, RU");
    }

    void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
