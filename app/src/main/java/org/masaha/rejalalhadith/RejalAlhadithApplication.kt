package org.masaha.rejalalhadith

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.support.v7.app.AppCompatDelegate
import org.masaha.rejalalhadith.ui.main.FontOverride
import org.masaha.rejalalhadith.utils.LocaleHelper
import org.masaha.rejalalhadith.utils.PrefManager
import org.masaha.rejalalhadith.utils.MapListSync
import com.raizlabs.android.dbflow.config.FlowManager

class RejalAlhadithApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.wrap(base))
    }

    override fun onCreate() {
        super.onCreate()
        LocaleHelper.apply(this)
        AppCompatDelegate.setDefaultNightMode(PrefManager(this).getThemeMode().toNightMode())

        FontOverride.setDefaultFont(this, "DEFAULT", "Rubik-VariableFont_wght.ttf", 500);
        FontOverride.setDefaultFont(this, "MONOSPACE", "Rubik-VariableFont_wght.ttf", 500);
        FontOverride.setDefaultFont(this, "SERIF", "Rubik-VariableFont_wght.ttf", 500);
        FontOverride.setDefaultFont(this, "SANS_SERIF", "Rubik-VariableFont_wght.ttf", 500);

        FlowManager.init(this)
        Thread {
            MapListSync.sync(this)
        }.start()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.apply(this)
    }
}