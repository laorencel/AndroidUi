package com.laorencel.ui.test.m3.button;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityTestM3ButtonBinding;
import com.laorencel.ui.databinding.ActivityTestM3ButtonFooterBinding;
import com.laorencel.uilibrary.ui.BaseUiActivity;

public class TestButtonActivity extends BaseUiActivity<ActivityTestM3ButtonBinding, TestButtonVM> {
    @Override
    protected int layoutID() {
        return R.layout.activity_test_m3_button;
    }

    @Override
    protected int headerLayoutID() {
        return R.layout.activity_test_m3_button_header;
    }

    @Override
    protected int footerLayoutID() {
        return R.layout.activity_test_m3_button_footer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ActivityTestM3ButtonFooterBinding) footerBinding).setActivity(TestButtonActivity.this);
    }
}
