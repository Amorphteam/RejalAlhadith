package org.masaha.rejalalhadith

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import org.masaha.rejalalhadith.ui.main.FontOverride
import org.masaha.rejalalhadith.utils.PrefManager
import org.masaha.rejalalhadith.utils.MapListSync
import com.raizlabs.android.dbflow.config.FlowManager

class RejalAlhadithApplication : Application() {
    override fun onCreate() {
        super.onCreate()
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
}