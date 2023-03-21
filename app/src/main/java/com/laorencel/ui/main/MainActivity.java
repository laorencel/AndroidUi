package com.laorencel.ui.main;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityMainBinding;
import com.laorencel.uilibrary.ui.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainVM> {

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }
}
