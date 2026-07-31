package org.masaha.rejalalhadith.ui.viewer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.papyrus.mehdok.rejalalhadith.R
import org.masaha.rejalalhadith.database.Bookmark
import org.masaha.rejalalhadith.database.DataRepositoryImpl
import org.masaha.rejalalhadith.database.RejalGhavaed
import org.masaha.rejalalhadith.database.RejalLink
import org.masaha.rejalalhadith.ui.main.StyleDialog
import org.masaha.rejalalhadith.utils.Constants
import org.masaha.rejalalhadith.utils.RejalHierarchy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_text_viewer.*
import kotlinx.android.synthetic.main.content_text_viewer.*
import org.masaha.rejalalhadith.utils.PrefManager


class TextViewer : AppCompatActivity(), StyleDialog.ClickListener {

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    public enum class ViewerType {
        Rejal, Ghavaed, Bookmark
    }

    private var itemBookmark: MenuItem? = null

    var type = ViewerType.Rejal

    var currentIndex = 0
    var rejalList: List<RejalLink>? = null
    var ghavaedList: List<RejalGhavaed>? = null
    var bookmarkList: List<Bookmark>? = null
    private var rejalHierarchy: RejalHierarchy? = null
    private var relatedRequestId = 0

    var currentFontSize: Int = PrefManager.initialFontSize
    val minTextSize = 10 //px
    val maxTextSize = 50 //px

    var prefManager: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefManager = PrefManager(this)
        currentFontSize = prefManager?.getFontSize() ?: PrefManager.initialFontSize

        nextItem.setOnClickListener {
            showItemIn(currentIndex + 1)
        }

        prevItem.setOnClickListener {
            showItemIn(currentIndex - 1)
        }

        getExtraData()
        loadRejalHierarchy()
    }

    fun getExtraData() {
        type = intent.extras!!.getSerializable(Constants.EXTRA_VIEWER_TYPE) as ViewerType

        when (type) {
            ViewerType.Rejal -> {
                val passedRejal: RejalLink = intent.extras!!.getParcelable(Constants.EXTRA_REJAL_LINK)!!
                val rejalFilter = intent.extras!!.getString(Constants.EXTRA_REJAL_FILTER)
                rejalFilter?.let { getAllRejalFromDB(passedRejal, it) }
            }
            ViewerType.Ghavaed -> {
                val passedGhavaed: RejalGhavaed? = intent.extras!!.getParcelable(Constants.EXTRA_GHAVAED_LINK)
                passedGhavaed?.let { getAllGhavaedFromDB(it) }
            }
            ViewerType.Bookmark -> {
                val passedBookmark: Bookmark? = intent.extras!!.getParcelable(Constants.EXTRA_BOOKMARK)
                passedBookmark?.let { getAllBookmarkFromDB(it) }
            }
        }
    }

    private fun showItemIn(index: Int, fontSize: Int = currentFontSize) {
        Log.d("showItemIn", "index: $index type: $type")
        when (type) {
            ViewerType.Rejal -> {
                itemBookmark?.isVisible = true

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
            ViewerType.Ghavaed -> {
                itemBookmark?.isVisible = false

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
            ViewerType.Bookmark -> {
                itemBookmark?.isVisible = false

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
        val dd = "معجم رجال الحديث " + rejal.joz + ": " + rejal.page
        name.text = dd
        toolbar.title = rejal.name
        content.loadDataWithBaseURL(null, getHTMLText(rejal.det, fontSize), "text/html", "UTF-8", null)
        showRelatedRejals(rejal.ID)
        checkRejalBookmark()
    }

    fun showGhavaed(item: RejalGhavaed, fontSize: Int) {
        relationCard.visibility = View.GONE
        val dd = "معجم رجال الحديث " + item.joz + ": " + item.page
        name.text = dd
        toolbar.title = item.title
        content.loadDataWithBaseURL(null, getHTMLText(item.text, fontSize), "text/html", "UTF-8", null)
    }

    fun showBookmark(item: Bookmark, fontSize: Int) {
        val dd = "معجم رجال الحديث " + item.joz + ": " + item.page
        name.text = dd
        toolbar.title = item.bookmarkTitle
        content.loadDataWithBaseURL(null, getHTMLText(item.bookmarkText, fontSize), "text/html", "UTF-8", null)
        showRelatedRejals(item.bookmarkId)
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
        itemBookmark = menu.findItem(R.id.action_bookmark)

        when (type) {
            ViewerType.Rejal -> itemBookmark?.isVisible = true
            ViewerType.Ghavaed -> itemBookmark?.isVisible = false
            ViewerType.Bookmark -> itemBookmark?.isVisible = false
        }
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

            R.id.action_bookmark -> {
                val rejal = rejalList?.get(currentIndex) ?: return super.onOptionsItemSelected(item)
                if (isBookmarked(rejal)) {
                    deBookmarkItem()
                } else {
                    bookmarkItem()
                }
            }

            R.id.action_share -> {
                shareCurrentText()
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
        val rejals: Observable<List<RejalLink>> = if (filter.isEmpty()) {
            DataRepositoryImpl
                    .getInstance()
                    .getRejals()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            DataRepositoryImpl
                    .getInstance()
                    .getRejals(filter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }

        subscriptions.add(
                Observable.zip(rejals, getBookmarkList(), BiFunction { t1: List<RejalLink>, t2: List<Bookmark> ->
                    rejalList = t1
                    currentIndex = rejalList!!.indexOf(rejal)
                    bookmarkList = t2
                    showItemIn(currentIndex)
                }).subscribe()
        )
    }

    private fun loadRejalHierarchy() {
        subscriptions.add(
                Observable.fromCallable { RejalHierarchy.get(applicationContext) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ hierarchy ->
                            rejalHierarchy = hierarchy
                            when (type) {
                                ViewerType.Rejal ->
                                    rejalList?.getOrNull(currentIndex)?.let { showRelatedRejals(it.ID) }
                                ViewerType.Bookmark ->
                                    bookmarkList?.getOrNull(currentIndex)?.let {
                                        showRelatedRejals(it.bookmarkId)
                                    }
                                ViewerType.Ghavaed -> relationCard.visibility = View.GONE
                            }
                        }, { error ->
                            Log.e("TextViewer", "Unable to load rejal relationships", error)
                            relationCard.visibility = View.GONE
                        })
        )
    }

    private fun showRelatedRejals(rejalId: Int) {
        relationCard.visibility = View.GONE
        relationItems.removeAllViews()

        val hierarchy = rejalHierarchy ?: return
        val childIds = hierarchy.childrenOf(rejalId)
        val relatedIds: List<Int>
        val titleRes: Int

        if (childIds.isNotEmpty()) {
            relatedIds = childIds
            titleRes = R.string.related_children
        } else {
            relatedIds = hierarchy.parentsOf(rejalId)
            titleRes = if (relatedIds.size > 1) R.string.related_parents else R.string.related_parent
        }

        if (relatedIds.isEmpty()) {
            return
        }

        relationTitle.setText(titleRes)
        val requestId = ++relatedRequestId
        subscriptions.add(
                DataRepositoryImpl.getInstance().getRejalsByIds(relatedIds)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ relatedRejals ->
                            if (requestId != relatedRequestId) {
                                return@subscribe
                            }

                            relationItems.removeAllViews()
                            val byId = relatedRejals.associateBy { it.ID }
                            val orderedRelated = relatedIds.mapNotNull { byId[it] }
                            if (orderedRelated.isEmpty()) {
                                relationCard.visibility = View.GONE
                                return@subscribe
                            }

                            orderedRelated.forEach { related ->
                                relationItems.addView(createRelatedRejalView(related))
                            }
                            relationCard.visibility = View.VISIBLE
                            // Views are attached after the scroll view was first laid out, so its
                            // RTL start offset has to be restored by hand.
                            relationScroll.post { relationScroll.fullScroll(View.FOCUS_RIGHT) }
                        }, { error ->
                            Log.e("TextViewer", "Unable to load related rejals", error)
                            if (requestId == relatedRequestId) {
                                relationCard.visibility = View.GONE
                            }
                        })
        )
    }

    private fun createRelatedRejalView(rejal: RejalLink): TextView {
        return TextView(this).apply {
            text = rejal.name
            gravity = Gravity.CENTER
            setTextColor(resources.getColor(R.color.colorPrimary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            val horizontalPadding = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.nav_header_vertical_spacing)
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
            isClickable = true
            isFocusable = true

            val selectableBackground = TypedValue()
            if (theme.resolveAttribute(
                            android.R.attr.selectableItemBackground,
                            selectableBackground,
                            true)) {
                setBackgroundResource(selectableBackground.resourceId)
            }

            setOnClickListener { openRelatedRejal(rejal) }
        }
    }

    private fun openRelatedRejal(rejal: RejalLink) {
        if (type == ViewerType.Rejal) {
            val index = rejalList?.indexOfFirst { it.ID == rejal.ID } ?: -1
            if (index >= 0) {
                showItemIn(index)
                return
            }
        }

        val bundle = Bundle()
        bundle.putParcelable(Constants.EXTRA_REJAL_LINK, rejal)
        bundle.putString(Constants.EXTRA_REJAL_FILTER, "")
        bundle.putSerializable(Constants.EXTRA_VIEWER_TYPE, ViewerType.Rejal)
        startActivity(Intent(this, TextViewer::class.java).putExtras(bundle))
    }

    private fun getAllBookmarkFromDB(bookmark: Bookmark) {
        subscriptions.add(
                getBookmarkList()
                        .subscribe({ bookmarks ->
                            bookmarkList = bookmarks
                            currentIndex = bookmarkList!!.indexOf(bookmark)
                            showItemIn(currentIndex)
                        }, { e ->
                            e.printStackTrace()
                            this.finish()
                        })
        )
    }

    private fun getAllGhavaedFromDB(ghavaed: RejalGhavaed) {
        subscriptions.add(
                DataRepositoryImpl.getInstance().getGhavaeds()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            ghavaedList = it
                            currentIndex = ghavaedList!!.indexOf(ghavaed)
                            showItemIn(currentIndex)
                        }, {
                            it.printStackTrace()
                            this.finish()
                        })
        )
    }

    private fun getBookmarkList(): Observable<List<Bookmark>> {
        return DataRepositoryImpl.getInstance().getBookmarkList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun showStyleDialog() {
        StyleDialog.newInstance(toolbar.height)
                .setClickListener(this)
                .show(supportFragmentManager, "TextViewerStyle")
    }

    override fun decreaseFontSize() {
        if ((currentFontSize - 5) > minTextSize) {
            currentFontSize -= 5
            prefManager?.saveFontSize(currentFontSize)
            showItemIn(currentIndex, currentFontSize)
        }
    }

    override fun increaseFontSize() {
        if ((currentFontSize + 5) < maxTextSize) {
            currentFontSize += 5
            prefManager?.saveFontSize(currentFontSize)
            showItemIn(currentIndex, currentFontSize)
        }
    }

    private fun getHTMLText(text: String, fontSize: Int): String {
        var result = "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_asset/Lotus.ttf\")\n" +
                "}\n" +
                "@font-face {\n" +
                "    font-family: MyFont2;\n" +
                "    src: url(\"file:///android_asset/Mosawi.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "   line-height: 200%;\n" +
                "    text-align: justify;\n" +
                "   direction: rtl; \n" +
                "   font-size: ${fontSize}px;\n" +
                "   color: #707070; \n"+
                "}\n" +
                ".alaem{\n" +
                "  color: red;\n" +
                "  font-family: MyFont2;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
        text.replace("(عليه السلام)",
                "<span class='alaem'> ×</span>").replace("(عليهم السلام)",
                "<span class='alaem'> ^</span>").replace("(عليهما السلام)",
                "<span class='alaem'> ’</span>").replace("(عليها السلام)",
                "<span class='alaem'> ÷</span>").replace("(ص)",
                "<span class='alaem'>|</span>").replace("(رضي الله عنه)",
                "<span class='alaem'> (رضي الله عنه)</span>").replace("(رحمه الله)",
                "<span class='alaem'> &</span>").replace("(قدس سره)",
                "<span class='alaem'> +</span>").replace("&اختلاف النسخ&",
                "</br><h4><font color='#1B5E20'>اختلاف النسخ</font></h4>").replace("&اختلاف الكتب&",
                "</br><h4><font color='#1B5E20'>اختلاف الكتب</font></h4>").replace("&طبقته في الحديث&",
                "</br><h4><font color='#1B5E20'>طبقته في الحديث</font></h4>").replace("&", "</br>") +
                "</body>\n" +
                "</html>"


        return result
    }

    private fun checkRejalBookmark() {
        if (bookmarkList == null || rejalList == null) {
            return
        }

        if (isBookmarked(rejalList!![currentIndex])) {
            itemBookmark?.setIcon(R.drawable.ic_star_white_36dp)
        } else {
            itemBookmark?.setIcon(R.drawable.ic_star_border_white_36dp)
        }
    }

    private fun isBookmarked(rejal: RejalLink): Boolean {
        if (bookmarkList == null) {
            return false
        }

        for (bookmark in bookmarkList!!) {
            if (rejal.ID == bookmark.bookmarkId) {
                return true
            }
        }

        return false
    }

    private fun refillBookmarkList() {
        subscriptions.add(
                getBookmarkList().subscribe({ list ->
                    bookmarkList = list
                    checkRejalBookmark()
                }, { e ->
                    e.printStackTrace()
                })
        )
    }

    private fun bookmarkItem() {
        val rejal = rejalList?.get(currentIndex) ?: return
        val bookmark = Bookmark(rejal.name, rejal.det, rejal.ID, rejal.joz, rejal.page, rejal.harf)

        subscriptions.add(
                DataRepositoryImpl.getInstance().addBookmark(bookmark)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            refillBookmarkList()
                        }, { e ->
                            e.printStackTrace()
                        })
        )
    }

    private fun deBookmarkItem() {
        val rejal = rejalList?.get(currentIndex) ?: return

        subscriptions.add(
                DataRepositoryImpl.getInstance().deleteBookmark(rejal.ID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            refillBookmarkList()
                        }, { e ->
                            e.printStackTrace()
                        })
        )
    }

    private fun shareCurrentText() {
        val appInfo = getText(R.string.share_text)
        val shareBody: String
        shareBody = when (type) {
            ViewerType.Rejal -> {
                val rejal = rejalList!![currentIndex]
                "${rejal.name} \n${rejal.det} \n $appInfo"
            }
            ViewerType.Ghavaed -> {
                val ghavaed = ghavaedList!![currentIndex]
                "${ghavaed.title} \n${ghavaed.text} \n$appInfo"
            }
            ViewerType.Bookmark -> {
                val bookmark = bookmarkList!![currentIndex]
                "${bookmark.bookmarkTitle} \n${bookmark.bookmarkText} \n$appInfo"
            }
        }
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    }

}
