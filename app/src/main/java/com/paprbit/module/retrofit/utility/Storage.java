package com.paprbit.module.retrofit.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.paprbit.module.R;

import java.util.ArrayList;


/**
 * Created by ankush38u on 12/26/2015.
 */
public class Storage {
    public static ArrayList<String> pumpList = new ArrayList<>();

    static {
        pumpList = new ArrayList<String>();
        pumpList.add(0, "Sitapura Area Pump");
        pumpList.add(1, "Durgapura area Pump");
        pumpList.add(2, "Gopalpura Area Pump");
        pumpList.add(3, "PoloVictory area Pump");
        pumpList.add(4, "M.I Area Pump");
        pumpList.add(5, "GandhiNagar area Pump");
    }

    public static void saveStringToPrefs(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromPrefs(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }
}
