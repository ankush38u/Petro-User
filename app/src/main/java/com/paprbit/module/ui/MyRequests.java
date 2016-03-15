package com.paprbit.module.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.paprbit.module.R;
import com.paprbit.module.retrofit.adapter.AllRequestsAdapter;
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


public class MyRequests extends AppCompatActivity {
    @Bind(R.id.recView)
    RecyclerView recView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.parent_layout)
    LinearLayout parentLayout;
    private String TAG = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        ButterKnife.bind(this);
        TAG = getLocalClassName();
        setupActionBar();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading .. ");
        pd.setCancelable(false);
        recView.setVisibility(View.INVISIBLE);
        recView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(llm);
        setRecyclerView();
        setTitle("Request Queue");
    }

    private void setRecyclerView() {
        Call<List<RequestData>> call = ServiceGenerator.getService(getApplicationContext()).getRequestByUid(Storage.getStringFromPrefs(getApplicationContext(), getString(R.string.uid)));
        call.enqueue(new Callback<List<RequestData>>() {
            @Override
            public void onResponse(Response<List<RequestData>> response, Retrofit retrofit) {
                pd.hide();
                recView.setVisibility(View.VISIBLE);
                if (response.isSuccess()) {

                    if (response.body() != null && response.body().size() > 0) {
                        AllRequestsAdapter allRequestsAdapter = new AllRequestsAdapter(response.body(), MyRequests.this);
                        recView.setAdapter(allRequestsAdapter);
                    }

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

    private void setupActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //   getSupportActionBar().setDisplayShowTitleEnabled(false);
            //   getSupportActionBar().setIcon(R.drawable.gs);
        }

    }

}
