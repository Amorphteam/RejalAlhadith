package com.papyrus.mehdok.rejalalhadith

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager

class RejalAlhadithApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FlowManager.init(this)
    }
}