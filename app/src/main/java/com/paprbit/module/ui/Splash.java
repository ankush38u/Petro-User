package com.paprbit.module.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.paprbit.module.MainActivity;
import com.paprbit.module.R;
import com.paprbit.module.retrofit.gson_pojo.Message;
import com.paprbit.module.retrofit.utility.ServiceGenerator;
import com.paprbit.module.retrofit.utility.Storage;
import com.paprbit.module.utility.UserIntraction;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
        ShimmerTextView myShimmerTextView=( ShimmerTextView)findViewById(R.id.shimmer_tv);
        Shimmer shimmer = new Shimmer();
        shimmer.start(myShimmerTextView);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this,MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 2000);

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
