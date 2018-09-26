package com.papyrus.mehdok.rejalalhadith.ui.viewer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.papyrus.mehdok.rejalalhadith.R
import kotlinx.android.synthetic.main.activity_text_viewer.*

class TextViewer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)
        setSupportActionBar(toolbar)

    }

}
