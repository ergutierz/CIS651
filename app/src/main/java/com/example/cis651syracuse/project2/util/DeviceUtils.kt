package com.example.cis651syracuse.project2.util

import android.content.Context
import android.content.res.Configuration
import android.telephony.TelephonyManager


object DeviceUtils {
    fun isTablet(context: Context): Boolean {
        val configuration = context.resources.configuration
        val screenLayout = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE
    }
}

