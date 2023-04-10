package com.laorencel.uilibrary.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.internal.ContextUtils;
import com.laorencel.uilibrary.util.ContextUtil;
import com.laorencel.uilibrary.util.EdgeToEdgeUtil;

public class UiWindowManager {
    private OnApplyWindowInsetsListener onApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
        @Override
        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
//            Log.d("onApplyWindowInsets", "onApplyWindowInsets: " + v.getResources().getConfiguration().orientation + insets.getInsets(WindowInsets.Type.systemBars()));
            if (v.getResources().getConfiguration().orientation
                    != Configuration.ORIENTATION_LANDSCAPE) {
                return insets;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                v.setPadding(
                        insets.getInsets(WindowInsets.Type.systemBars()).left,
                        0,
                        insets.getInsets(WindowInsets.Type.systemBars()).right,
                        insets.getInsets(WindowInsets.Type.systemBars()).bottom);
            } else {
                v.setPadding(
                        insets.getStableInsetLeft(),
                        0,
                        insets.getStableInsetRight(),
                        insets.getStableInsetBottom());
            }
            return insets;
        }
    };

    public void applyEdgeToEdge(Window window, boolean isEdgeToEdge) {
        // Lay out your app in full screen 设置屏幕EdgeToEdge，参考：https://zhuanlan.zhihu.com/p/277342333
        EdgeToEdgeUtil.applyEdgeToEdge(window, isEdgeToEdge);
        ViewCompat.setOnApplyWindowInsetsListener(
                window.getDecorView(), isEdgeToEdge ? onApplyWindowInsetsListener : null);
    }

    public void setDefaultNightMode(int mode) {
        //设置暗夜模式：MODE_NIGHT_FOLLOW_SYSTEM,MODE_NIGHT_NO,MODE_NIGHT_YES ...
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void recreateActivityIfPossible(Context context) {
        Activity activity = ContextUtil.getActivity(context);
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                activity.recreate();
            }
        }
    }
}
