package org.masaha.rejalalhadith

import android.app.Application
import org.masaha.rejalalhadith.ui.main.FontOverride
import com.raizlabs.android.dbflow.config.FlowManager

class RejalAlhadithApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FontOverride.setDefaultFont(this, "DEFAULT", "droidkufi_regular.ttf");
        FontOverride.setDefaultFont(this, "MONOSPACE", "droidkufi_regular.ttf");
        FontOverride.setDefaultFont(this, "SERIF", "droidkufi_regular.ttf");
        FontOverride.setDefaultFont(this, "SANS_SERIF", "droidkufi_regular.ttf");

        FlowManager.init(this)
    }
}