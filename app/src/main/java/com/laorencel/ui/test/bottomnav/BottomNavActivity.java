package com.laorencel.ui.test.bottomnav;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityBottomNavBinding;
import com.laorencel.ui.main.MainVM;
import com.laorencel.ui.test.bottomnav.home.HomeFragment;
import com.laorencel.ui.test.bottomnav.mine.MineFragment;
import com.laorencel.uilibrary.ui.BaseActivity;
import com.laorencel.uilibrary.ui.BaseUiActivity;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding, MainVM> {
    @Override
    protected int layoutID() {
        return R.layout.activity_bottom_nav;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentBinding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFragment(FRAGMENT_TAG_HOME);
                        break;
                    case R.id.navigation_mine:
                        switchFragment(FRAGMENT_TAG_MINE);
                        break;

                }
                return true;
            }
        });
        contentBinding.bottomNavView.setSelectedItemId(R.id.navigation_home);
//        contentBinding.bottomNavView.setSelectedItemId(R.id.navigation_customer);
    }

    private static final String FRAGMENT_TAG_HOME = "home";
    private static final String FRAGMENT_TAG_MINE = "mine";

    private Fragment currentFragment;

    private void switchFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            fragment = getFragment(tag);
        }
        if (null != fragment) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (null != currentFragment) {
                fragmentTransaction.hide(currentFragment);
            }
            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.add(R.id.fragment_container, fragment, tag);
            }
            currentFragment = fragment;
//            fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.fragment, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    private Fragment getFragment(String tag) {
        Fragment fragment = null;
        switch (tag) {
            case FRAGMENT_TAG_HOME:
                fragment = new HomeFragment();
                break;
            case FRAGMENT_TAG_MINE:
                fragment = new MineFragment();
                break;
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        if (null != currentFragment && !(currentFragment instanceof HomeFragment)) {
            contentBinding.bottomNavView.setSelectedItemId(R.id.navigation_home);
            return;
        }
        super.onBackPressed();
    }
}
