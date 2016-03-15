package com.paprbit.module.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.paprbit.module.R;
import com.paprbit.module.retrofit.gson_pojo.Message;
import com.paprbit.module.retrofit.utility.ServiceGenerator;
import com.paprbit.module.utility.UserIntraction;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MessageAdmin extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.message)
    EditText msgEt;
    @Bind(R.id.btn_submit)
    Button buttonSb;
    @Bind(R.id.parent)
    LinearLayout parentLayout;
    private String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_admin);
        ButterKnife.bind(this);
        TAG = getLocalClassName();
        setupActionBar();
        buttonSb.setOnClickListener(this);
        hideKeyboard();
    }

    private void setupActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setDisplayShowTitleEnabled(false);
            //  getSupportActionBar().setIcon(R.drawable.gs);
        }

    }

    private void hideKeyboard() {
        msgEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(MessageAdmin.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSb) {
            if (msgEt.getText().toString().length() == 0) {
                msgEt.setError("Please Enter Message.");
            } else {
                parentLayout.requestFocus();
                Call<Message> call = ServiceGenerator.getService(getApplicationContext()).msgAdmin(msgEt.getText().toString());
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Response<Message> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            if (response.body().isType()) {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(MessageAdmin.this, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Status");
                                builder.setMessage(response.body().getMessage());
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(MessageAdmin.this, MainActivity.class));
                                        finish();
                                    }
                                });
                                builder.show();

                            } else {
                                UserIntraction.makeSnack(parentLayout, response.body().getMessage());
                            }
                        } else {
                            //handle all error like 404,500 etc server errors based on request
                            UserIntraction.makeSnack(parentLayout, "Error Code: " + response.raw().code() + "  " + response.raw().message());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        //Log.d(TAG, t.getMessage());
                        UserIntraction.makeSnack(parentLayout, getString(R.string.internet_error));
                    }
                });
            }
        }
    }
}
