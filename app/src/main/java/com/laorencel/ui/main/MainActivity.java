package com.laorencel.ui.main;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityMainBinding;
import com.laorencel.uilibrary.ui.BaseActivity;
import com.laorencel.uilibrary.ui.BaseUiActivity;

public class MainActivity extends BaseUiActivity<ActivityMainBinding,MainVM> {

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
}
