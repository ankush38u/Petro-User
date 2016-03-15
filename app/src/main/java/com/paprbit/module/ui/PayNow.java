package com.paprbit.module.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paprbit.module.R;
import com.paprbit.module.retrofit.utility.Storage;
import com.razorpay.Checkout;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayNow extends AppCompatActivity {
    Activity activity;
    int pid = 0;
    private String PUBLIC_KEY = "rzp_test_gUg6mKgfHlXEyR";
    @Bind(R.id.btn_pay)
    Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);
        ButterKnife.bind(this);
        activity = this;
        if (getIntent() != null) {
            pid = Integer.parseInt(getIntent().getExtras().getString("pid"));
        }
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }


    public void startPayment() {


        Checkout razorpayCheckout = new Checkout();
        razorpayCheckout.setPublicKey(PUBLIC_KEY);

        /**
         * Reference to current activity
         */


        try {
            JSONObject options = new JSONObject("{" +
                    "description: 'Pay For Petrol'," +
                    "image: 'http://goyalsales.com/test/app-services/appicon.png'," + // can also be base64, if you don't want it to load from network
                    "currency: 'INR'}"
            );

            options.put("amount", "100");
            options.put("name", Storage.pumpList.get(pid));
            options.put("prefill", new JSONObject("{email: 'ankush38u@gmail.com', contact: '9660721831', name: 'Ankush Chugh'}"));

            razorpayCheckout.open(activity, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("com.merchant", e.getMessage(), e);
        }
    }
}
