package com.papyrus.mehdok.rejalalhadith.ui.viewer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.Bookmark
import com.papyrus.mehdok.rejalalhadith.database.RejalGhavaed
import com.papyrus.mehdok.rejalalhadith.database.RejalLink
import com.papyrus.mehdok.rejalalhadith.utils.Constants
import kotlinx.android.synthetic.main.activity_text_viewer.*
import kotlinx.android.synthetic.main.content_text_viewer.*

class TextViewer : AppCompatActivity() {

    public enum class ViewerType {
        Rejal, Ghavaed, Bookmark
    }

    var type = ViewerType.Rejal
    var passedRejal: RejalLink? = null
    var passedGhavaed: RejalGhavaed? = null
    var passedBookmark: Bookmark? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getExtraData()
    }

    fun getExtraData() {
        type = intent.extras.getSerializable(Constants.EXTRA_VIEWER_TYPE) as ViewerType

        when (type) {
            ViewerType.Rejal -> {
                passedRejal = intent.extras.getParcelable(Constants.EXTRA_REJAL_LINK)
                showRejal(passedRejal)
            }
            ViewerType.Ghavaed -> {
                passedGhavaed = intent.extras.getParcelable(Constants.EXTRA_GHAVAED_LINK)
            }
            ViewerType.Bookmark -> {
                passedBookmark = intent.extras.getParcelable(Constants.EXTRA_BOOKMARK)
            }
        }
    }

    fun showRejal(rejal: RejalLink?) {
        name.text = rejal?.name
        webView.loadDataWithBaseURL(null, rejal?.det, "text/html", "UTF-8", null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_text_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

}
