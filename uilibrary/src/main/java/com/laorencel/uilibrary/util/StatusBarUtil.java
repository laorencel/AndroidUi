package com.laorencel.uilibrary.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Method;

public class StatusBarUtil {


    public static int getStatusBarHeight(Context context) {
        //获取顶部statusBar高度
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("StatusBarUtil", "Status height:" + height);
        return height;
    }

    public static int getNavigationBarHeight(Context context) {
        //获取底部navigationBar高度
        //导航栏即某些手机底部有返回键的虚拟键那一栏，叫navigationBar
        //没显示导航栏的，方法二还是会返回导航栏的值，而不是返回0，所以要结合方法checkDeviceHasNavigationBar使用
        boolean hasNavigationBar = checkDeviceHasNavigationBar(context);
        if (hasNavigationBar) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            Log.d("StatusBarUtil", "getNavigationBarHeight:" + height);
            return height;
        } else {
            return 0;
        }
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        //如果是沉浸式透明的导航栏，如全面屏手机，三星S8，方法三返回true，即存在导航栏，
        // 所以如果有些布局是以距离底部多高为标注，就要注意了！透明导航栏实际上是不影响布局的。
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }
}
