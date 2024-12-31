package com.laorencel.uilibrary.util;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {
    public static <T> Class<T> getViewModel(Object obj, Class<?> viewModelClass) {
        Class<?> currentClass = obj.getClass();
        Class<T> tClass = getGenericClass(currentClass, viewModelClass);
        if (tClass == null || tClass == viewModelClass) {
            return null;
        }
        return tClass;
    }

    public static <T> Class<T> getViewModel(Object obj) {
        return getViewModel(obj, ViewModel.class);
    }

    public static <T> Class<T> getGenericClass(Class<?> klass, Class<?> filterClass) {
        Type type = klass.getGenericSuperclass();
        if (type == null || !(type instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        for (Type t : types) {
            Class<T> tClass = (Class<T>) t;
            if (filterClass.isAssignableFrom(tClass)) {
                return tClass;
            }
        }
        return null;
    }
}
