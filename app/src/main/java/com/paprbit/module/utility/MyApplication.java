package com.paprbit.module.utility;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by ankush38u on 2/27/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
