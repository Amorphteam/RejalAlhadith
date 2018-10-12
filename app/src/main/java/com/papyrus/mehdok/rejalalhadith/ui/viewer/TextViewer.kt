package com.papyrus.mehdok.rejalalhadith.ui.viewer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.Bookmark
import com.papyrus.mehdok.rejalalhadith.database.DataRepositoryImpl
import com.papyrus.mehdok.rejalalhadith.database.RejalGhavaed
import com.papyrus.mehdok.rejalalhadith.database.RejalLink
import com.papyrus.mehdok.rejalalhadith.ui.main.StyleDialog
import com.papyrus.mehdok.rejalalhadith.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_text_viewer.*
import kotlinx.android.synthetic.main.content_text_viewer.*

class TextViewer : AppCompatActivity(), StyleDialog.ClickListener {

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    public enum class ViewerType {
        Rejal, Ghavaed, Bookmark
    }

    var type = ViewerType.Rejal

    var currentIndex = 0
    var rejalList: List<RejalLink>? = null
    var ghavaedList: List<RejalGhavaed>? = null
    var bookmarkList: List<Bookmark>? = null

    var currentFontSize: Int = 20 // px
    val minTextSize = 10 //px
    val maxTextSize = 50 //px

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nextItem.setOnClickListener {
            showItemIn(currentIndex + 1)
        }

        prevItem.setOnClickListener {
            showItemIn(currentIndex - 1)
        }

        getExtraData()
    }

    fun getExtraData() {
        type = intent.extras.getSerializable(Constants.EXTRA_VIEWER_TYPE) as ViewerType

        when (type) {
            ViewerType.Rejal -> {
                val passedRejal: RejalLink = intent.extras.getParcelable(Constants.EXTRA_REJAL_LINK)
                val rejalFilter = intent.extras.getString(Constants.EXTRA_REJAL_FILTER)
                getAllRejalFromDB(passedRejal, rejalFilter)
            }
            ViewerType.Ghavaed -> {
                val passedGhavaed: RejalGhavaed = intent.extras.getParcelable(Constants.EXTRA_GHAVAED_LINK)
            }
            ViewerType.Bookmark -> {
                val passedBookmark: Bookmark = intent.extras.getParcelable(Constants.EXTRA_BOOKMARK)
            }
        }
    }

    private fun showItemIn(index: Int, fontSize: Int = currentFontSize) {
        when (type) {
            TextViewer.ViewerType.Rejal -> {
                if (index < 0) {
                    showFirstPageMsg()
                    return
                }
                if (index >= rejalList!!.count()) {
                    showLastPageMsg()
                    return
                }

                currentIndex = index
                showRejal(rejalList!![index], fontSize)
            }
            TextViewer.ViewerType.Ghavaed -> {
                if (index < 0) {
                    showFirstPageMsg()
                    return
                }
                if (index >= ghavaedList!!.count()) {
                    showLastPageMsg()
                    return
                }

                currentIndex = index
                showGhavaed(ghavaedList!![index], fontSize)
            }
            TextViewer.ViewerType.Bookmark -> {
                if (index < 0) {
                    showFirstPageMsg()
                    return
                }
                if (index >= bookmarkList!!.count()) {
                    showLastPageMsg()
                    return
                }

                currentIndex = index
                showBookmark(bookmarkList!![index], fontSize)
            }
        }
    }

    fun showRejal(rejal: RejalLink, fontSize: Int) {
        name.text = rejal.name
        content.loadDataWithBaseURL(null, getHTMLText(rejal.det, fontSize), "text/html", "UTF-8", null)
    }

    fun showGhavaed(item: RejalGhavaed, fontSize: Int) {

    }

    fun showBookmark(item: Bookmark, fontSize: Int) {

    }

    fun showFirstPageMsg() {
        Toast.makeText(this, R.string.first_item, Toast.LENGTH_SHORT).show()
    }

    fun showLastPageMsg() {
        Toast.makeText(this, R.string.last_item, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_text_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }

            R.id.action_style -> {
                showStyleDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        subscriptions.clear()
        subscriptions.dispose()
    }

    private fun getAllRejalFromDB(rejal: RejalLink, filter: String) {
        var rejals: Observable<List<RejalLink>>
        if (filter.isEmpty()) {
            rejals = DataRepositoryImpl
                    .getInstance()
                    .getRejals()
        } else {
            rejals = DataRepositoryImpl
                    .getInstance()
                    .getRejals(filter)
        }

        subscriptions.add(
                rejals.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ list ->
                            rejalList = list
                            currentIndex = rejalList!!.indexOf(rejal)
                            showItemIn(currentIndex)
                        }, { e ->
                            e.printStackTrace()
                            this.finish()
                        })
        )
    }

    private fun showStyleDialog() {
        StyleDialog.newInstance(toolbar.height)
                .setClickListener(this)
                .show(supportFragmentManager, "TextViewerStyle")
    }

    override fun decreaseFontSize() {
        if ((currentFontSize - 5) > minTextSize) {
            currentFontSize -= 5
            showItemIn(currentIndex, currentFontSize)
        }
    }

    override fun increaseFontSize() {
        if ((currentFontSize + 5) < maxTextSize) {
            currentFontSize += 5
            showItemIn(currentIndex, currentFontSize)
        }
    }

    private fun getHTMLText(text: String, fontSize: Int): String {
        var result = "<!DOCTYPE HTML>\n" +
                "<html>" +
                "<body text=\"red\" dir=\"rtl\" style=\"font-size:${fontSize}px\">" +
                text +
                "</body>\n" +
                "</html>"

        return result
    }

}
