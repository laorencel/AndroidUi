package com.laorencel.uilibrary.util.kt

import android.app.Activity
import java.lang.ref.WeakReference

object ActivityManager {
   private var currentActivityWeakRef: WeakReference<Activity>? = null

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        if (currentActivityWeakRef != null && currentActivityWeakRef!!.get() != null) {
            currentActivity = currentActivityWeakRef!!.get()
        }
        return currentActivity
    }

    fun setCurrentActivity(activity: Activity) {
        currentActivityWeakRef = WeakReference(activity)
    }

}