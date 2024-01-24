package com.laorencel.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityMainBinding;
import com.laorencel.ui.login.LoginActivity;
import com.laorencel.ui.test.bottomnav.BottomNavActivity;
import com.laorencel.ui.test.common.TestCommonActivity;
import com.laorencel.ui.test.m3.button.TestButtonActivity;
import com.laorencel.uilibrary.ui.BaseUiActivity;


public class MainActivity extends BaseUiActivity<ActivityMainBinding, MainVM> {

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void toTestM3Button(View view) {
        startActivity(new Intent(MainActivity.this, TestButtonActivity.class));
    }

    public void toLogin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void toBottomNav(View view) {
        startActivity(new Intent(MainActivity.this, BottomNavActivity.class));
    }

    public void toTestCommon(View view) {
        startActivity(new Intent(MainActivity.this, TestCommonActivity.class));
    }

    protected void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }
}
