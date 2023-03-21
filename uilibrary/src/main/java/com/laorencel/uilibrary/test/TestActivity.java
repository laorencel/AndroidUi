package com.laorencel.uilibrary.test;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.databinding.ActivityTestBinding;
import com.laorencel.uilibrary.ui.BaseActivity;

public class TestActivity extends BaseActivity<ActivityTestBinding,TestVM> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_test;
    }
}
