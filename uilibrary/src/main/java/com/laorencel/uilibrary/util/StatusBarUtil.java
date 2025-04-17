package com.laorencel.uilibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

public class StatusBarUtil {


    public static int getStatusBarHeight(Context context) {
        //获取顶部statusBar高度
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
//        Log.v("StatusBarUtil", "Status height:" + height);
        return height;
    }

    public static int getNavigationBarHeight(Activity context) {
        //获取底部navigationBar高度
        //导航栏即某些手机底部有返回键的虚拟键那一栏，叫navigationBar
        //没显示导航栏的，方法二还是会返回导航栏的值，而不是返回0，所以要结合方法checkDeviceHasNavigationBar使用
        boolean hasNavigationBar = checkDeviceHasNavigationBar(context);
//        Log.d("StatusBarUtil", "hasNavigationBar:" + hasNavigationBar);
        if (hasNavigationBar) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
//            Log.d("StatusBarUtil", "getNavigationBarHeight:" + height);
            return height;
        } else {
            return 0;
        }
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Activity context) {
        //如果是沉浸式透明的导航栏，如全面屏手机，三星S8，方法三返回true，即存在导航栏，
        // 所以如果有些布局是以距离底部多高为标注，就要注意了！透明导航栏实际上是不影响布局的。
//        boolean hasNavigationBar = false;
//        Resources rs = context.getResources();
//        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
//        if (id > 0) {
//            hasNavigationBar = rs.getBoolean(id);
//        }
//        Log.d("StatusBarUtil", "hasNavigationBar:" + hasNavigationBar);
//        try {
//            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
//            Method m = systemPropertiesClass.getMethod("get", String.class);
//            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
//            Log.d("StatusBarUtil", "navBarOverride:" + navBarOverride);
//            if ("1".equals(navBarOverride)) {
//                hasNavigationBar = false;
//            } else if ("0".equals(navBarOverride)) {
//                hasNavigationBar = true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return hasNavigationBar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * 隐藏/显示系统状态栏和导航栏
     *
     * @param activity
     * @param hide
     */
    public static void hideSystemUi(Activity activity, boolean hide) {
//        View.SYSTEM_UI_FLAG_LAYOUT_STABLE：全屏显示时保证尺寸不变。
//        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，状态栏显示在Activity页面上面。
//        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏导航栏
//        View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
//        View.SYSTEM_UI_FLAG_VISIBLE：Activity非全屏显示，显示状态栏和导航栏。
//        View.INVISIBLE：Activity伸展全屏显示，隐藏状态栏。
//        View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY：必须配合View.SYSTEM_UI_FLAG_FULLSCREEN和View.SYSTEM_UI_FLAG_HIDE_NAVIGATION组合使用，达到的效果是拉出状态栏和导航栏后显示一会儿消失。

        if (null != activity) {
            if (hide) {
                // 全屏展示
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // 全屏显示，隐藏状态栏和导航栏，拉出状态栏和导航栏显示一会儿后消失。
                        activity.getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    } else {
                        // 全屏显示，隐藏状态栏
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                    }
                }
            } else {
                // 非全屏显示，显示状态栏和导航栏
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }
}
