package org.masaha.rejalalhadith.ui.main

import android.os.Build
import android.os.Bundle
import android.text.Html
import com.papyrus.mehdok.rejalalhadith.R
import kotlinx.android.synthetic.main.activity_about.*
import org.masaha.rejalalhadith.ui.BaseAppCompatActivity

class About : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView2.text = Html.fromHtml(getString(R.string.about_us), Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView2.text = Html.fromHtml(getString(R.string.about_us))
        }

    }
}
