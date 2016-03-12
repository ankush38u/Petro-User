package com.paprbit.module.utility;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by ankush38u on 1/7/2016.
 */
public class UserIntraction {
    public static void makeSnack(ViewGroup parent,String msg){
        Snackbar.make(parent,msg, Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        }).show();
    }

    public static void makeToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
