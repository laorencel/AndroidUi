package com.laorencel.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityMainBinding;
import com.laorencel.uilibrary.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //下面三种写法都可以
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//写法一
//        mainBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);//写法二（主要用于Fragment和Adapter)
//        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());//写法三
//        setContentView(mainBinding.getRoot());//写法二和写法三需要setContentView
//        setSupportActionBar(mainBinding.toolbar);
    }
}
