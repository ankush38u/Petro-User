package com.paprbit.module.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.paprbit.module.R;
import com.paprbit.module.retrofit.utility.Storage;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {
    @Bind(R.id.parent_layout)
    LinearLayout parentLayout;
    private String TAG = "";
    static final Handler handler = new Handler();
    Runnable r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        TAG = getLocalClassName();
        //implement logic neeeded at startup like login chk in sharedprefs update chk\
        //chk server session available or not
        //  startActivity(new Intent(this,LoginRegister.class));
        ShimmerTextView myShimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        Shimmer shimmer = new Shimmer();
        shimmer.start(myShimmerTextView);

        chkSession();

    }


    private void chkSession() {
        if (Storage.getStringFromPrefs(getApplicationContext(), getString(R.string.uid)) != null) {
            r = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }
            };
            if (handler != null) {
                handler.postDelayed(r, 3000);

            }

        } else {
            // close this activity
            //if null send delayed message through handler to open loginregister
            r = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, LoginRegisterActivity.class));
                    finish();
                }
            };
            if (handler != null) {
                handler.postDelayed(r, 3000);

            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //remove handler work
        if (r != null)
            handler.removeCallbacks(r);
    }
}
