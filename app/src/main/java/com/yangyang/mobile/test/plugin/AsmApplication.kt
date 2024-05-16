package com.yangyang.mobile.test.plugin

import android.app.Application
import com.yangyang.mobile.test.asmsdk.SensorsDataAPI


/**
 * Create by Yang Yang on 2024/5/16
 */
class AsmApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        SensorsDataAPI.init(this)
    }
}