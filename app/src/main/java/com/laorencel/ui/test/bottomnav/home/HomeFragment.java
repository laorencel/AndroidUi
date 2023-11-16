package com.laorencel.ui.test.bottomnav.home;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.FragmentHomeBinding;
import com.laorencel.uilibrary.ui.BaseUiFragment;

public class HomeFragment extends BaseUiFragment<FragmentHomeBinding, HomeViewModel> {
    @Override
    protected int layoutID() {
        return R.layout.fragment_home;
    }

    public HomeFragment() {
    }
}
