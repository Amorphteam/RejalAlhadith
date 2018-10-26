package org.masaha.rejalalhadith.ui.main

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.papyrus.mehdok.rejalalhadith.R
import kotlinx.android.synthetic.main.activity_about.*

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        textView2


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView2.text = Html.fromHtml(getString(R.string.about_us), Html.FROM_HTML_MODE_COMPACT);
        } else {
            textView2.text = Html.fromHtml(getString(R.string.about_us))
        }

    }
}
