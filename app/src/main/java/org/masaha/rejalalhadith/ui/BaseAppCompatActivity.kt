package org.masaha.rejalalhadith.ui

import android.content.Context
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import org.masaha.rejalalhadith.utils.LocaleHelper

open class BaseAppCompatActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.apply(this)
    }
}
