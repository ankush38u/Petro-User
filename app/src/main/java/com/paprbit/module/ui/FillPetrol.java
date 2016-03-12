package com.paprbit.module.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.paprbit.module.MainActivity;
import com.paprbit.module.R;
import com.paprbit.module.retrofit.gson_pojo.Message;
import com.paprbit.module.retrofit.utility.ServiceGenerator;
import com.paprbit.module.retrofit.utility.Storage;
import com.paprbit.module.utility.UserIntraction;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FillPetrol extends AppCompatActivity {
    ProgressDialog pd;
    ArrayList<String> pumpList;
    HashMap<Integer,String> pumpmap;
    @Bind(R.id.petrol_id_spinner)
    Spinner pidSpinner;
    @Bind(R.id.qty)
    EditText etQty;
    @Bind(R.id.btn_request)
    Button btnRequest;
    @Bind(R.id.parent_layout)
    LinearLayout parentLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_petrol);
        ButterKnife.bind(this);
        setUpToolBar();
        pd= new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        //load data for spinner
        setPetrolPumps();
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRequest();
            }
        });

            setTitle("Fuel Request");
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void newRequest() {

        Call<Message> call = ServiceGenerator.getService(getApplicationContext()).request(Storage.getStringFromPrefs(getApplicationContext(),getString(R.string.uid)),String.valueOf(pidSpinner.getSelectedItemPosition()),etQty.getText().toString());
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Response<Message> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    if(response.body().isType()){
                        //request accepted and send to pid
                        UserIntraction.makeSnack(parentLayout,response.body().getMessage());
                        startActivity(new Intent(FillPetrol.this, PayNow.class).putExtra("pid",String.valueOf(pidSpinner.getSelectedItemPosition())));
                    }else{
                        UserIntraction.makeSnack(parentLayout,response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                UserIntraction.makeSnack(parentLayout,t.getMessage());
            }
        });
    }

    private void setPetrolPumps() {
        pumpList = Storage.pumpList;
        ArrayAdapter<String> adapterState = new ArrayAdapter<>(this, R.layout.row_spn,pumpList);
        adapterState.setDropDownViewResource(R.layout.row_spn_dropdown);
        pidSpinner.setAdapter(adapterState);
    }
}
