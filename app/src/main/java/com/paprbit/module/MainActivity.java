package com.paprbit.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.paprbit.module.retrofit.gson_pojo.Message;
import com.paprbit.module.ui.FillPetrol;
import com.paprbit.module.ui.MessageAdmin;
import com.paprbit.module.ui.MonthlyExpenses;
import com.paprbit.module.ui.MyRequests;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ResideMenu resideMenu;
    Context mContext;
    ResideMenuItem pumpitem;
    ResideMenuItem fillitem;
    ResideMenuItem requestsitem;
    ResideMenuItem expenseItem;
    ResideMenuItem complaintitem;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        setSupportActionBar(toolbar);
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.image_bg);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = {"NearBy Pumps", "Fill Petrol", "My Requests", "Monthly Expenses", "Complaint Admin"};
        int icon[] = {R.drawable.petrolpump, R.drawable.fill_petrol, R.drawable.old_requests, R.drawable.expenses, R.drawable.complaint};

        pumpitem = new ResideMenuItem(this, icon[0], titles[0]);
        pumpitem.setOnClickListener(this);
        resideMenu.addMenuItem(pumpitem, ResideMenu.DIRECTION_LEFT);

        fillitem = new ResideMenuItem(this, icon[1], titles[1]);
        fillitem.setOnClickListener(this);
        resideMenu.addMenuItem(fillitem, ResideMenu.DIRECTION_LEFT);

        requestsitem = new ResideMenuItem(this, icon[2], titles[2]);
        requestsitem.setOnClickListener(this);
        resideMenu.addMenuItem(requestsitem, ResideMenu.DIRECTION_LEFT);

        expenseItem = new ResideMenuItem(this, icon[3], titles[3]);
        expenseItem.setOnClickListener(this);
        resideMenu.addMenuItem(expenseItem, ResideMenu.DIRECTION_LEFT);

        complaintitem = new ResideMenuItem(this, icon[4], titles[4]);
        complaintitem.setOnClickListener(this);
        resideMenu.addMenuItem(complaintitem, ResideMenu.DIRECTION_LEFT);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        if (v == pumpitem) {
            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
        }
        if (v == fillitem) {
            startActivity(new Intent(MainActivity.this, FillPetrol.class));
        }
        if (v == requestsitem) {
            startActivity(new Intent(MainActivity.this, MyRequests.class));
        }
        if (v == expenseItem) {
            startActivity(new Intent(MainActivity.this, MonthlyExpenses.class));
        }
        if (v == complaintitem) {
            startActivity(new Intent(MainActivity.this, MessageAdmin.class));
        }
    }
}
