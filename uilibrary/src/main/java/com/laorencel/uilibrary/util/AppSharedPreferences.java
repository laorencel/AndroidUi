package com.laorencel.uilibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;


/**
 * 封装SharedPreferences，在Application中初始化init方法
 */
public class AppSharedPreferences {
    private AppSharedPreferences() {
        gson = new Gson();
    }

    private static class Builder {
        private static final AppSharedPreferences INSTANCE = new AppSharedPreferences();
    }

    public static AppSharedPreferences get() {
        return AppSharedPreferences.Builder.INSTANCE;
    }

    private Gson gson;
    private SharedPreferences sharedPreferences;

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, String name) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(!EmptyUtil.isEmpty(name) ? name : "AppSharedPreferences", Context.MODE_PRIVATE);
    }

    public AppSharedPreferences put(String key, Object obj) {
        // key 不为null时再存入，否则不存储
        if (!EmptyUtil.isEmpty(key)) {
//            if (null == sharedPreferences) {
//                throw new Exception("you must init AppSharedPreferences!");
//            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (obj instanceof Integer) {
                editor.putInt(key, (Integer) obj);
            } else if (obj instanceof Long) {
                editor.putLong(key, (Long) obj);
            } else if (obj instanceof Boolean) {
                editor.putBoolean(key, (Boolean) obj);
            } else if (obj instanceof Float) {
                editor.putFloat(key, (Float) obj);
            } else if (obj instanceof String) {
                editor.putString(key, String.valueOf(obj));
            } else {
                if (EmptyUtil.isEmpty(obj)) {
                    editor.putString(key, "");
                } else {
                    try {
                        //对象类型，gson转换为String存储
                        String jsonOb = gson.toJson(obj);
                        editor.putString(key, jsonOb);
                    } catch (Exception e) {
                        e.printStackTrace();
                        editor.putString(key, "");
                    }
                }
            }
            //apply() 会立即更改内存中的 SharedPreferences 对象，但会将更新异步写入磁盘。
            // 或者，您也可以使用 commit() 将数据同步写入磁盘。但是，由于 commit() 是同步的，您应避免从主线程调用它，因为它可能会暂停您的界面呈现。
            editor.apply();
        }
        return this;
    }

    public <T> T getObject(String key, Class<T> tClass) {
        String value = sharedPreferences.getString(key, "");
        if (!EmptyUtil.isEmpty(value)) {
            try {
                return gson.fromJson(value, tClass);
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

}
