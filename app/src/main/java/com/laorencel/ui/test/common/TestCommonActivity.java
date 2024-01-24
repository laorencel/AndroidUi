package com.laorencel.ui.test.common;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityTestCommonBinding;
import com.laorencel.uilibrary.ui.BaseCommonActivity;

public class TestCommonActivity extends BaseCommonActivity<ActivityTestCommonBinding, TestCommonViewModel> {
    @Override
    protected int layoutID() {
        return R.layout.activity_test_common;
    }

    @Override
    protected int footerLayoutID() {
        return R.layout.activity_test_common_footer;
    }
}
