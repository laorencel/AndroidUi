package com.laorencel.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityLoginBinding;
import com.laorencel.uilibrary.ui.BaseActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginVM> {

    @Override
    protected int layoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EdgeToEdgeUtils.applyEdgeToEdge(window, isEdgeToEdgeEnabled());
//        ViewCompat.setOnApplyWindowInsetsListener(
//                getWindow().getDecorView(), isEdgeToEdgeEnabled() ? listener : null);

        setToolbar(contentBinding.toolbar);
    }
}
