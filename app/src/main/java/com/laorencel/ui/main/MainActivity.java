package com.laorencel.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityMainBinding;
import com.laorencel.ui.databinding.ActivityMainFooterBinding;
import com.laorencel.uilibrary.ui.BaseActivity;
import com.laorencel.uilibrary.ui.BaseUiActivity;
import com.laorencel.uilibrary.widget.State;

import java.util.Random;

public class MainActivity extends BaseUiActivity<ActivityMainBinding, MainVM> {

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected int headerLayoutID() {
        return super.headerLayoutID();
    }

    @Override
    protected int footerLayoutID() {
        return R.layout.activity_main_footer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ((ActivityMainFooterBinding) footerBinding).setActivity(this);
    }

}
