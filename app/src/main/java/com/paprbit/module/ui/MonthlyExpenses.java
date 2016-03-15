package com.paprbit.module.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paprbit.module.R;
import com.paprbit.module.retrofit.gson_pojo.RequestData;
import com.paprbit.module.retrofit.utility.ServiceGenerator;
import com.paprbit.module.retrofit.utility.Storage;
import com.paprbit.module.utility.UserIntraction;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MonthlyExpenses extends AppCompatActivity {
    @Bind(R.id.parent_layout)
    LinearLayout parentLayout;
    ProgressDialog pd;
    public static int expence = 0;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.amount_tv)
    TextView amountTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_expenses);
        ButterKnife.bind(this);
        setupActionBar();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.. ");
        pd.setCancelable(false);
        pd.show();
        calculateMonthlyExpense();
        setTitle("Monthly Expenses");
    }

    private void setupActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setDisplayShowTitleEnabled(false);
            //  getSupportActionBar().setIcon(R.drawable.gs);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        expence = 0;
    }

    private void calculateMonthlyExpense() {
        Call<List<RequestData>> call = ServiceGenerator.getService(getApplicationContext()).getRequestByUid(Storage.getStringFromPrefs(getApplicationContext(), getString(R.string.uid)));
        call.enqueue(new Callback<List<RequestData>>() {
            @Override
            public void onResponse(Response<List<RequestData>> response, Retrofit retrofit) {
                pd.hide();
                if (response.isSuccess()) {

                    if (response.body() != null && response.body().size() > 0) {
                        for (int i = 0; i < response.body().size(); i++) {
                            expence += response.body().get(i).getQuantity() * 60;
                        }
                    }
                    amountTv.setText(String.valueOf(expence));

                } else {
                    //handle all error like 404,500 etc server errors based on request
                    UserIntraction.makeSnack(parentLayout, "Error Code: " + response.raw().code() + "  " + response.raw().message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                pd.hide();
                //  Log.d(TAG, t.getMessage());
                UserIntraction.makeSnack(parentLayout, getString(R.string.internet_error));
            }
        });
    }
}
