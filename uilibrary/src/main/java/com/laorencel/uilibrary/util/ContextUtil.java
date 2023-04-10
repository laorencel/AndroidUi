package com.laorencel.uilibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.annotation.Nullable;

public class ContextUtil {

    /**
     * Returns the {@link Activity} given a {@link Context} or null if there is no {@link Activity},
     * taking into account the potential hierarchy of {@link ContextWrapper ContextWrappers}.
     */
    @Nullable
    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
